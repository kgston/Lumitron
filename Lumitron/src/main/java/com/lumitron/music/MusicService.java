package com.lumitron.music;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    
    public void play() throws MusicException {
        if(!MusicHandler.isPaused()) {
            MusicHandler.play(); //Start playback
            
            //While till the music starts
            while(MusicHandler.isStopped()) {}
            
            //Create a shared response object
            HashMap<String, Object> response = new HashMap<>();
            while(!MusicHandler.isStopped() || MusicHandler.isSeeking()) { //While it is playing
                if(!MusicHandler.isPaused() && !MusicHandler.isSeeking()) { //And it is not paused
                    //Get the playback time and push into the hashmap
                    Long currentPlaybackInMicro = MusicHandler.getCurrentPlaybackTime();
                    response.put("currentPlaybackTime", convertTime(currentPlaybackInMicro));
                    response.put("currentPlaybackInMicro", currentPlaybackInMicro.toString());
                    response.put("verbose", false);
                    RequestHandler.stream(serviceRoute.get("uuid"), response); //Stream it back to the UI
                }
                try {
                    Thread.sleep(100); //After that, take a short nap
                } catch (InterruptedException e) {
                    //Give whoever who disturbs your sleep the finger
                } finally {
                    //Once you awaken from your slumber and find the music has stopped
                    if(MusicHandler.isStopped() && !MusicHandler.isSeeking()) {
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
    }
    
    public void pause() throws MusicException {
        MusicHandler.pause();
        HashMap<String, Object> response = new HashMap<>();
        response.put("state", "paused");
        RequestHandler.send(serviceRoute.get("uuid"), response);
    }
    
    public void stop() throws MusicException {
        MusicHandler.stop();
        HashMap<String, Object> response = new HashMap<>();
        response.put("state", "stopped");
        RequestHandler.send(serviceRoute.get("uuid"), response);
    }
    
    public void seek() throws MusicException {
        MusicHandler.seek(Long.parseLong(params.get("seekTo")));
        HashMap<String, Object> response = new HashMap<>();
        response.put("state", "seeked");
        RequestHandler.send(serviceRoute.get("uuid"), response);
    }
    
    private String convertTime(Long microSeconds) {
        Long timeInMilliseconds = TimeUnit.MICROSECONDS.toMillis(microSeconds);
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) - TimeUnit.MINUTES.toSeconds(minutes);
        Long milliseconds = timeInMilliseconds - TimeUnit.SECONDS.toMillis(seconds) - TimeUnit.MINUTES.toMillis(minutes);
        String secondsString = seconds.toString();
        String millisecondsString =  milliseconds.toString();
        if(secondsString.length() == 1) {
            secondsString = "0" + secondsString;
        }
        while(millisecondsString.length() < 3) {
            millisecondsString = "0" + millisecondsString;
        }
        return minutes + ":" + secondsString + ":" + millisecondsString;
    }
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }
    
}
