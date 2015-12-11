package com.lumitron.network;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lumitron.util.AppSystem;
import com.lumitron.util.LumitronException;

/**
 * The request handler manages the WebSocket connection and intercepts all incoming requests
 * and routes the request to the right class and method
 * @author Kingston Chan
 *
 */
@ServerEndpoint("/ws/request")
public class RequestHandler {
    //Response cache - When client disconnects in between processing
    //Request UUID - requests
    private static HashMap<String, HashMap<String, Object>> cache = new HashMap<>();
    //Map of which request-response goes to which session
    //Request UUID - Session
    private static HashMap<String, Session> deliveryMap = new HashMap<>();
    //Service Mapping of the domain-service name to the class and method
    //Domain name -> Services Map :: Service name -> Service Details Map :: className || methodName -> String
    private static HashMap<String, HashMap<String, HashMap<String, String>>> serviceMap = new HashMap<>();
    //Service Mapping Properties Filepath
    private static final String SERVICE_MAPPING_PROPERTIES_FILE = "serviceMapping.json";
    
    //This blocks runs only once when the class is first loaded
    static {
        //Parse service mapping file
        //Connect to the file
        InputStream iStream = RequestHandler.class.getClassLoader().getResourceAsStream(SERVICE_MAPPING_PROPERTIES_FILE);
        Scanner sc = new Scanner(iStream);
        String serviceMappingJson = sc.useDelimiter("\\A").next(); //Read the file
        serviceMap = parseServiceMapping(serviceMappingJson); //Parse and commit to memory
        
        AppSystem.log(RequestHandler.class, "The following services have been configured:");
        for(String domain: serviceMap.keySet()) {
            HashMap<String, HashMap<String, String>> services = serviceMap.get(domain);
            AppSystem.log(RequestHandler.class, "\t" + domain + ":");
            for(String serviceName: services.keySet()) {
                HashMap<String, String> service = services.get(serviceName);
                AppSystem.log(RequestHandler.class, "\t\t" + serviceName + ":");
                AppSystem.log(RequestHandler.class, "\t\t\t" + service.get("serviceClass"));
                AppSystem.log(RequestHandler.class, "\t\t\t" + service.get("method"));
            }
        }
        sc.close(); //Close the scanner
        try {
            if(iStream != null) {
                iStream.close();
            }
        } catch (IOException e) {
        }
    }
    
    /**
     * Websockets implementation when a connection is opened from the client
     * @param session The incoming client session
     */
    @OnOpen
    public void openConnection(Session session) {
        //Register this connection in the queue
        try {
            session.getBasicRemote().sendText(newResponse("Open connection", "receipt", null, true));
            AppSystem.log(this.getClass(), "Connection opened");
        } catch(IOException ioe) {
            AppSystem.log(this.getClass(), "Unable to send receipt for new connection");
        }
    }
    
    /**
     * Websockets implementation when a connection is closed from the client
     * @param session The closing client session
     */
    @OnClose
    public void closedConnection(Session session) {
        AppSystem.log(this.getClass(), "Connection closed");
    }
    
    /**
     * Websockets implementation when an error is received from a client
     * @param session The error generating client session
     * @param t The error object
     */
    @OnError
    public void error(Session session, Throwable t) {
        AppSystem.log(this.getClass(), "Connection error: " + t.getMessage());
        t.printStackTrace();
    }
    
    /**
     * Websockets implementation when a message is received from a client
     * @param message The incoming message
     * @param session The incoming client session
     */
    @OnMessage
    public void onMessage(final String message, final Session session) {
        (new Thread() {
            public void run() {
                //Convert the JSON message to a native format
                HashMap<String, HashMap<String, String>> request = parseRequest(message);
                HashMap<String, String> serviceRoute = request.get("serviceRoute");
                HashMap<String, String> params = request.get("params");
                
                //Check for a uuid in request
                String uuid = null;
                if(!serviceRoute.containsKey("uuid")) {
                    uuid = UUID.randomUUID().toString(); //Create one if not found
                    serviceRoute.put("uuid", uuid); //Put the uuid into the request
                } else {
                    uuid = serviceRoute.get("uuid"); //Otherwise use the provided one
                }
                //Store the session for later delivery
                deliveryMap.put(uuid, session);
                
                //Return a receipt to the client
                sendReceipt(uuid);
                
                //Invoke the service requested
                HashMap<String, String> serviceInfo = serviceMap.get(serviceRoute.get("domain")).get(serviceRoute.get("service"));
                AppSystem.log(RequestHandler.class, "Receiving request for: " + serviceInfo);
                AppSystem.log(RequestHandler.class, "With the following params: " + params);
                
                String serviceClassName = serviceMap.get(serviceRoute.get("domain")).get(serviceRoute.get("service")).get("serviceClass");
                Class<?> serviceClass = null;
                LumitronService service = null;
                try {
                    //Dynamically instantiate the class
                    serviceClass = Class.forName(serviceClassName);
                    Constructor<?> serviceClassConstructor = serviceClass.getConstructor();
                    service = (LumitronService) serviceClassConstructor.newInstance();
                } catch (ClassNotFoundException classEx) {
                    AppSystem.log(RequestHandler.class, "Domain name mismatch");
                    classEx.printStackTrace();
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0001", "Requested service not found");
                    return;
                } catch (NoSuchMethodException methodEx) {
                    AppSystem.log(RequestHandler.class, "Service name mismatch");
                    methodEx.printStackTrace();
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0001", "Requested service not found");
                    return;
                } catch (SecurityException securityEx) {
                    AppSystem.log(RequestHandler.class, "Security voilation");
                    securityEx.printStackTrace();
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0003", "Security voilation");
                    return;
                } catch (InstantiationException instantiateEx) {
                    AppSystem.log(RequestHandler.class, "Unable to instantiate " + serviceClassName);
                    instantiateEx.printStackTrace();
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0002", "Instantiation failure");
                    return;
                } catch (IllegalAccessException accessEx) {
                    AppSystem.log(RequestHandler.class, "Security voilation");
                    accessEx.printStackTrace();
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0003", "Security voilation");
                } catch (IllegalArgumentException arguementEx) {
                    AppSystem.log(RequestHandler.class, "Incorrect constructor auguement for " + serviceClassName);
                    arguementEx.printStackTrace();
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0002", "Instantiation failure");
                } catch (InvocationTargetException e) {
                    LumitronException cause = (LumitronException) e.getCause();
                    AppSystem.log(RequestHandler.class, 
                            "Error creating service [" + cause.getOriginClass() + "]: " + cause.getErrorCode() + " " + cause.getMessage());
                    cause.printStackTrace();
                    sendError(uuid, cause.getOriginClass(), cause.getErrorCode(), cause.getMessage());
                }
                
                //Inject the request data into the service object
                service.setRequestData(request);
                
                //Dynamically invoke the method
                try {
                    Method method = service.getClass().getMethod(serviceInfo.get("method"));
                    method.invoke(service);
                } catch (NoSuchMethodException e) {
                    AppSystem.log(RequestHandler.class, "Error in service mapping, method not found");
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0004", "Service configuration error");
                } catch (SecurityException e) {
                    AppSystem.log(RequestHandler.class, "Security voilation");
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0003", "Security voilation");
                } catch (IllegalAccessException e) {
                    AppSystem.log(RequestHandler.class, "Error accessing method in service");
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0004", "Service configuration error");
                } catch (IllegalArgumentException e) {
                    AppSystem.log(RequestHandler.class, "Invalid parameters");
                    sendError(uuid, RequestHandler.class.getSimpleName(), "0004", "Service configuration error");
                } catch (InvocationTargetException e) {
                    if(e.getCause() instanceof LumitronException) {
                        LumitronException cause = (LumitronException) e.getCause();
                        AppSystem.log(RequestHandler.class, 
                                "Error executing service [" + cause.getOriginClass() + "]: " + cause.getErrorCode() + " " + cause.getMessage());
                        cause.printStackTrace();
                        sendError(uuid, cause.getOriginClass(), cause.getErrorCode(), cause.getMessage());
                    } else {
                        Throwable cause = (Throwable) e.getCause();
                        AppSystem.log(RequestHandler.class, 
                                "Error executing service: " + cause.getMessage());
                        cause.printStackTrace();
                        sendError(uuid, RequestHandler.class.getSimpleName(), "0000", "System error");
                    }
                }
            }
        }).start();
    }
    
    @SuppressWarnings("unchecked")
    public static void resend(String oldUUID, String newUUID) {
        HashMap<String, Object> responseInfo = cache.get(oldUUID);
        if(responseInfo != null) {
            String type = (String) responseInfo.get("type");
            boolean isComplete = (boolean) responseInfo.get("isComplete");
            HashMap<String, Object> response = (HashMap<String, Object>) responseInfo.get("response");
            send(newUUID, type, response, isComplete);
        } else {
            sendError(newUUID, RequestHandler.class.getSimpleName(), "0005", "Requested UUID not in cache");
        }
    }
    
    /**
     * Sends a single response to the client and closes this request
     * @param uuid The request UUID
     * @param response The response object
     */
    public static void send(String uuid, HashMap<String, Object> response) {
        send(uuid, "response", response, true);
    }
    
    /**
     * Sends a single response to the client and keeps the request alive for future use
     * @param uuid The request UUID
     * @param response The response object
     */
    public static void stream(String uuid, HashMap<String, Object> response) {
        send(uuid, "response", response, false);
    }
    
    /**
     * Sends a simple receipt to the client
     * @param uuid The request UUID
     */
    public static void sendReceipt(String uuid) {
        send(uuid, "receipt", null, false);
    }
    
    /**
     * Sends an error to the client
     * @param uuid The request UUID
     * @param originator The class that is sending this error
     * @param errorCode The error code (unique to each class)
     * @param errorMessage A UI presentable error message
     */
    public static void sendError(String uuid, String originator, String errorCode, String errorMessage) {
        HashMap<String, Object> error = new HashMap<>();
        error.put("originator", originator);
        error.put("errorCode", errorCode);
        error.put("errorMessage", errorMessage);
        send(uuid, "error", error, true);
    }
    
    /**
     * A generic method to send a response to the client based on the request UUID
     * @param uuid The request UUID
     * @param type The response type
     * @param response The response object
     * @param isComplete Is the request completed? If so, forget the request after it is done.
     */
    private static void send(String uuid, String type, HashMap<String, Object> response, boolean isComplete) {
        String jsonMsg = newResponse(uuid, type, response, isComplete);
        try {
            // Send response to session
            AppSystem.log(RequestHandler.class, "Returning response for <" + uuid + ">\n" + jsonMsg);
            //Get the session based on the UUID
            Session client = deliveryMap.get(uuid);
            //Set the message on fire
            if(client.isOpen()) {
                client.getBasicRemote().sendText(jsonMsg);
                //Remove the request from the map
                if(isComplete) {
                    deliveryMap.remove(uuid);
                }
            } else {
                //Save the response to cache if the client connection is dead
                saveToCache(uuid, type, response, isComplete);
            }
        }
        catch (IOException e) {
            saveToCache(uuid, type, response, isComplete);
        }
    }
    
    private static void saveToCache(String uuid, String type, HashMap<String, Object> response, boolean isComplete) {
        AppSystem.log(RequestHandler.class, "Error sending response for request <" + uuid + ">. Saving to cache");
        HashMap<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("type", type);
        responseInfo.put("response", response);
        responseInfo.put("isComplete", isComplete);
        cache.put(uuid, responseInfo);
    }
    
    private static HashMap<String, HashMap<String, HashMap<String, String>>> parseServiceMapping(String requestString) {
        return new Gson().fromJson(requestString, new TypeToken<HashMap<String, HashMap<String, HashMap<String, String>>>>() {}.getType());
    }
    
    /**
     * Parses incoming JSON requests into the native request format
     * @param requestString JSON request
     * @return Request object in native format
     */
    private static HashMap<String, HashMap<String, String>> parseRequest(String requestString) {
        return new Gson().fromJson(requestString, new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType());
    }
    
    /**
     * Converts any generic map or list into a JSON string
     * @param nativeObject
     * @return Json String
     */
    private static String toJson(Object nativeObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(nativeObject);
    }
    
    
    /**
     * Creates a new JSON response string
     * @param uuid The request UUID
     * @param type Type of response. Currently only "receipt", "response" and "error" is supported
     * @param responseDetails The details of the response or error. If there are not response, just pass null. Receipts do not send responses.
     * @param isComplete Is the request completed? If so, forget the request after it is done.
     * @return The response formatted as JSON
     */
    private static String newResponse(String uuid, String type, HashMap<String, Object> responseDetails, boolean isComplete) {
        HashMap<String, Object> response = new HashMap<>();
        
        //Populate data based on response type
        switch(type) {
            case "receipt":
                response.put("uuid", uuid);
                response.put("type", "receipt");
                response.put("success", true);
                break;
            
            case "response":
                response.put("uuid", uuid);
                response.put("type", "response");
                response.put("success", true);
                if(!isComplete) {
                    response.put("keepAlive", true);
                }
                if(responseDetails != null) {
                    response.put("response", responseDetails);
                }
                break;
            
            case "error":
                response.put("uuid", uuid);
                response.put("type", "error");
                response.put("success", false);
                if(responseDetails != null) {
                    response.put("error", responseDetails);
                }
                break;
        }
        return toJson(response);
    }
}
