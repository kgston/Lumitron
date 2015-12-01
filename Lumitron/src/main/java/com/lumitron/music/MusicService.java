package com.lumitron.music;

import java.util.HashMap;

import com.lumitron.network.LumitronService;
import com.lumitron.network.RequestHandler;


public class MusicService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;
    
    public void load() {
        HashMap<String, Object> response = new HashMap<>();
        Long totalPlaybackInMicro =  MusicHandler.load(params.get("file"));
        response.put("totalPlaybackTime", convertTime(totalPlaybackInMicro));
        response.put("totalPlaybackInMicro", totalPlaybackInMicro.toString());
        RequestHandler.send(serviceRoute.get("uuid"), response);
    }
    
    public void play() {
        try {
            if(!MusicHandler.isPaused()) {
                MusicHandler.play(); //Start playback
                
                //Create a shared response object
                HashMap<String, Object> response = new HashMap<>();
                while(!MusicHandler.isStopped()) { //While it is playing
                    if(!MusicHandler.isPaused()) { //And it is not paused
                        //Get the playback time and push into the hashmap
                        Long currentPlaybackInMicro = MusicHandler.getCurrentPlaybackTime();
                        response.put("currentPlaybackTime", convertTime(currentPlaybackInMicro));
                        response.put("currentPlaybackInMicro", currentPlaybackInMicro.toString());
                        RequestHandler.stream(serviceRoute.get("uuid"), response); //Stream it back to the UI
                    }
                    try {
                        Thread.sleep(200); //After that, take a short nap
                    } catch (InterruptedException e) {
                        //Give whoever who disturbs your sleep the finger
                    } finally {
                        //Once you awaken from your slumber and find the music has stopped
                        if(MusicHandler.isStopped()) {
                            //Tell the front end the damn music has stopped!
                            response.put("hasCompleted", true);
                            response.put("currentPlaybackTime", convertTime(0L));
                            response.put("currentPlaybackInMicro", 0);
                            RequestHandler.send(serviceRoute.get("uuid"), response); //Don't stream, but send a final response
                        }
                    }
                }
            } else {
                MusicHandler.play(); //Unpause playback
            }
        } catch(MusicException e) {
            RequestHandler.sendError(
                serviceRoute.get("uuid"), 
                e.getOriginClass(),
                e.getErrorCode(),
                e.getMessage());
        }
    }
    
    public void pause() {
        try {
            MusicHandler.pause();
        } catch(MusicException e) {
            RequestHandler.sendError(
                serviceRoute.get("uuid"), 
                e.getOriginClass(),
                e.getErrorCode(),
                e.getMessage());
        }
    }
    
    public void stop() {
        try {
            MusicHandler.stop();
        } catch(MusicException e) {
            RequestHandler.sendError(
                serviceRoute.get("uuid"), 
                e.getOriginClass(),
                e.getErrorCode(),
                e.getMessage());
        }
    }
    
    private String convertTime(Long microSeconds) {
        Double timeInSeconds = microSeconds.doubleValue() / 1000000 / 60;
        int minutes = timeInSeconds.intValue();
        String seconds = (new Double((timeInSeconds - minutes) * 60)).intValue() + "";
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }
    
}
