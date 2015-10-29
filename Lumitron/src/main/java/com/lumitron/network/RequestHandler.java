package com.lumitron.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lumitron.util.AppSystem;

@ServerEndpoint("/ws/request")
public class RequestHandler {
    //Response cache - When client disconnects in between processing
    //Request UUID - requests
    private static HashMap<String, ArrayList<Object>> cache = new HashMap<>();
    //Map of which request-response goes to which session
    //Request UUID - Session
    private static HashMap<String, Session> deliveryMap = new HashMap<>();
    //Service Mapping of the domain-service name to the class and method
    //Domain name -> Services Map :: Service name -> Service Details Map :: className || methodName -> String
    private static HashMap<String, HashMap<String, String>> serviceMap = new HashMap<>();
    
    private static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    
    static {
        //Parse service mapping file
        
    }
    
    @OnOpen
    public void openConnection(Session session) {
        /* Register this connection in the queue */
        try {
            queue.add(session);
            session.getBasicRemote().sendText(newReceipt("open", true));
            AppSystem.log(this.getClass(), "Connection opened");
        } catch(IOException ioe) {
            AppSystem.log(this.getClass(), "Unable to send receipt for new connection");
        }
    }
    
    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        try {
            queue.remove(session);
            session.getBasicRemote().sendText(newReceipt("close", true));
            AppSystem.log(this.getClass(), "Connection closed");
        } catch(IOException ioe) {
            AppSystem.log(this.getClass(), "Unable to send receipt for new connection");
        }
    }
    
    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        AppSystem.log(this.getClass(), "Connection error");
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        HashMap<String, Object> request = parseRequest(message);
        
        
        if (queue.contains(session)) {
            AppSystem.log(this.getClass(), "Incoming message: " + message);
            try {
                session.getBasicRemote().sendText("Received from client: " + message);
                send("[" + session.getId() + "]: " + message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static void send(String msg) {
        try {
            // Send updates to all open WebSocket sessions
            AppSystem.log(RequestHandler.class, "Current number of sessions: " + queue.size());
            for (Session session : queue) {
                session.getBasicRemote().sendText(msg);
                AppSystem.log(RequestHandler.class, "Sent: " + msg);
            }
        }
        catch (IOException e) {
            AppSystem.log(RequestHandler.class, e.toString());
        }
    }
    
    public static void stream(String msg) {
        
    }
    
    private void startDemo() {

            (new Thread() {
                public void run() {
                    
                }
            }).start();
    }
    
    private static HashMap<String, Object> parseRequest(String requestString) {
        return new Gson().fromJson(requestString, HashMap.class);
    }
    
    private static String newReceipt(String uuid, boolean success) {
        HashMap<String, Object> receipt = new HashMap<>();
        receipt.put("uuid", uuid);
        receipt.put("type", "receipt");
        receipt.put("success", success);
        Gson gson = new GsonBuilder().create();
        String receiptString = gson.toJson(receipt);
        return receiptString;
    }
}
