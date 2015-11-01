package com.lumitron.network;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    
    private static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    
    static {
        //Parse service mapping file
        InputStream iStream = RequestHandler.class.getClassLoader().getResourceAsStream(SERVICE_MAPPING_PROPERTIES_FILE);
        Scanner sc = new Scanner(iStream);
        String serviceMappingJson = sc.useDelimiter("\\A").next();
        serviceMap = parseServiceMapping(serviceMappingJson);
        for(String domain: serviceMap.keySet()) {
            HashMap<String, HashMap<String, String>> services = serviceMap.get(domain);
            System.out.println(domain);
            for(String serviceName: services.keySet()) {
                HashMap<String, String> service = services.get(serviceName);
                System.out.println(serviceName);
                System.out.println(service.get("serviceClass"));
                System.out.println(service.get("method"));
            }
        }
        sc.close();
    }
    
    @OnOpen
    public void openConnection(Session session) {
        /* Register this connection in the queue */
        try {
            queue.add(session);
            session.getBasicRemote().sendText(newResponse("receipt", null));
            AppSystem.log(this.getClass(), "Connection opened");
        } catch(IOException ioe) {
            AppSystem.log(this.getClass(), "Unable to send receipt for new connection");
        }
    }
    
    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        queue.remove(session);
        AppSystem.log(this.getClass(), "Connection closed");
    }
    
    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        AppSystem.log(this.getClass(), "Connection error: " + t.getMessage());
        t.printStackTrace();
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        //Convert the JSON message to a native format
        HashMap<String, HashMap<String, String>> request = parseRequest(message);
        HashMap<String, String> serviceRoute = request.get("serviceRoute");
        HashMap<String, String> params = request.get("params");
        //Store the session for later delivery
        String uuid = UUID.randomUUID().toString();
        deliveryMap.put(uuid, session);
        //Put the uuid into the request
        serviceRoute.put("uuid", uuid);
        //Return a receipt to the client
        sendReceipt(uuid);
        
        //Invoke the service requested
        HashMap<String, String> serviceInfo = serviceMap.get(serviceRoute.get("domain")).get(serviceRoute.get("service"));
        AppSystem.log(this.getClass(), "Receiving request for: " + serviceInfo);
        AppSystem.log(this.getClass(), "With the following params: " + params);
        
        Class<?> serviceClass = null;
        RequestData service = null;
        try {
            //Dynamically instantiate the class
            serviceClass = Class.forName(serviceMap.get(serviceRoute.get("domain")).get(serviceRoute.get("service")).get("serviceClass"));
            Constructor<?> serviceClassConstructor = serviceClass.getConstructor();
            service = (RequestData) serviceClassConstructor.newInstance();
        } catch (Exception e) {
            AppSystem.log(RequestHandler.class, "Domain and/or service name mismatch");
            AppSystem.log(RequestHandler.class, e.getMessage());
            e.printStackTrace();
            sendError(uuid, this.getClass().getSimpleName(), "0001", "Requested service not found");
            return;
        }
        
        //Inject the request data into the service object
        service.setRequestData(request);
        
        //Dynamically invoke the method
        try {
            Method method = service.getClass().getMethod(serviceInfo.get("method"));
            method.invoke(service);
        } catch (NoSuchMethodException e) {
            AppSystem.log(RequestHandler.class, "Error in service mapping, method not found");
            sendError(uuid, this.getClass().getSimpleName(), "0002", "Service configuration error");
        } catch (SecurityException e) {
            AppSystem.log(RequestHandler.class, "Security voilation");
            sendError(uuid, this.getClass().getSimpleName(), "0003", "Security voilation");
        } catch (IllegalAccessException e) {
            AppSystem.log(RequestHandler.class, "Error accessing method in service");
            sendError(uuid, this.getClass().getSimpleName(), "0002", "Service configuration error");
        } catch (IllegalArgumentException e) {
            AppSystem.log(RequestHandler.class, "Invalid parameters");
            sendError(uuid, this.getClass().getSimpleName(), "0002", "Service configuration error");
        } catch (InvocationTargetException e) {
            AppSystem.log(RequestHandler.class, "Error executing service: " + e.getCause().getMessage());
            e.getCause().printStackTrace();
            sendError(uuid, this.getClass().getSimpleName(), "0004", "Error executing service");
        }
    }
    
    public static void send(String uuid, Object response) {
        send(uuid, "response", response, true);
    }
    
    public static void stream(String uuid, Object response) {
        send(uuid, "response", response, false);
    }
    
    public static void sendReceipt(String uuid) {
        send(uuid, "receipt", null, false);
    }
    
    public static void sendError(String uuid, String orignClass, String errorCode, String errorMessage) {
        HashMap<String, String> error = new HashMap<>();
        error.put("errorCode", errorCode);
        error.put("errorMessage", errorMessage);
        send(uuid, "error", error, true);
    }
    
    public static void send(String uuid, String type, Object response, boolean isComplete) {
        String jsonMsg = newResponse(type, response);
        try {
            // Send response to session
            AppSystem.log(RequestHandler.class, "Returning response for <" + uuid + ">\n" + jsonMsg);
            //Get the session based on the UUID
            Session client = deliveryMap.get(uuid);
            //Set the message on fire
            client.getBasicRemote().sendText(jsonMsg);
            //Remove the request from the map
            if(isComplete) {
                deliveryMap.remove(uuid);
            }
        }
        catch (IOException e) {
            AppSystem.log(RequestHandler.class, "Error sending response for request <" + uuid + ">. Saving to cache");
            HashMap<String, Object> responseInfo = new HashMap<>();
            responseInfo.put("type", type);
            responseInfo.put("response", response);
            responseInfo.put("isComplete", isComplete);
            cache.put(uuid, responseInfo);
        }
    }
    
    private static HashMap<String, HashMap<String, HashMap<String, String>>> parseServiceMapping(String requestString) {
        return new Gson().fromJson(requestString, new TypeToken<HashMap<String, HashMap<String, HashMap<String, String>>>>() {}.getType());
    }
    
    private static HashMap<String, HashMap<String, String>> parseRequest(String requestString) {
        return new Gson().fromJson(requestString, new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType());
    }
    
    private static String toJson(Object nativeObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(nativeObject);
    }
    
    private static String newResponse(String type, Object responseDetails) {
        HashMap<String, Object> response = new HashMap<>();
        
        //Populate data based on response type
        switch(type) {
            case "receipt":
                response.put("type", "receipt");
                response.put("success", true);
                break;
            
            case "response":
                response.put("type", "response");
                response.put("success", true);
                response.put("response", responseDetails);
                break;
            
            case "error":
                response.put("type", "error");
                response.put("success", false);
                response.put("error", responseDetails);
                break;
        }
        
        return toJson(response);
    }
}
