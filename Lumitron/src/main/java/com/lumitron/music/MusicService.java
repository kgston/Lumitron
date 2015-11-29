package com.lumitron.music;

import java.io.File;
import java.util.HashMap;

import com.lumitron.network.LumitronService;
import com.lumitron.network.RequestHandler;


public class MusicService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;
    
    private static MusicHandler musicPlayer;
    private static volatile boolean paused = false;
    
    public void load() {
        File musicFile = new File(params.get("file"));
        musicPlayer = MusicHandler.load(musicFile);
        Long totalPlaybackInMicro = musicPlayer.getTotalPlaybackTime();
        String totalPlaybackTime = convertTime(totalPlaybackInMicro);
        
        HashMap<String, Object> response = new HashMap<>();
        response.put("totalPlaybackTime", totalPlaybackTime);
        response.put("totalPlaybackInMicro", totalPlaybackInMicro.toString());
        RequestHandler.send(serviceRoute.get("uuid"), response);
    }
    
    public void play() {
        if(musicPlayer != null) {
            if(!paused) {
                musicPlayer.startPlayback();
                
                HashMap<String, Object> response = new HashMap<>();
                while(!musicPlayer.isStopped()) {
                    if(!paused) {
                        Long currentPlaybackInMicro = musicPlayer.getCurrentPlaybackTime();
                        response.put("currentPlaybackTime", convertTime(currentPlaybackInMicro));
                        response.put("currentPlaybackInMicro", currentPlaybackInMicro.toString());
                        RequestHandler.stream(serviceRoute.get("uuid"), response);
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        //Ignore
                    } finally {
                        if(musicPlayer.isStopped()) {
                            response.put("hasCompleted", true);
                            response.put("currentPlaybackTime", convertTime(0L));
                            response.put("currentPlaybackInMicro", 0);
                            RequestHandler.send(serviceRoute.get("uuid"), response);
                        }
                    }
                }
            } else {
                musicPlayer.pausePlayback();
                paused = false;
            }
        } else {
            RequestHandler.sendError(
                            serviceRoute.get("uuid"), 
                            this.getClass().getSimpleName(),
                            "0001",
                            "Music file has yet to be loaded");
        }
        
    }
    
    public void pause() {
        if(musicPlayer != null) {
            paused = true;
            musicPlayer.pausePlayback();
        } else {
            RequestHandler.sendError(
                            serviceRoute.get("uuid"), 
                            this.getClass().getSimpleName(),
                            "0001",
                            "Music file has yet to be loaded");
        }
    }
    
    public void stop() {
        if(musicPlayer != null) {
            musicPlayer.stopPlayback();
            musicPlayer = null;
        } else {
            RequestHandler.sendError(
                            serviceRoute.get("uuid"), 
                            this.getClass().getSimpleName(),
                            "0001",
                            "Music file has yet to be loaded");
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
