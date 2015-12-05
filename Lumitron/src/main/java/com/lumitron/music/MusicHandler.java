package com.lumitron.music;

import java.io.File;

public class MusicHandler {
    
    private static MusicController musicPlayer;
    private static volatile boolean paused = false;
    
    public static Long load(String musicFilepath) throws MusicException {
        //If there is a existing musicPlayer, stop and unload it first
        if(musicPlayer != null) {
            musicPlayer.stopPlayback();
            musicPlayer = null;
        }
        //Load the music file
        File musicFile = new File(musicFilepath);
        musicPlayer = MusicController.load(musicFile);
        //Return the total length of the file
        return getTotalPlaybackTime();
    }
    
    public static void play() throws MusicException {
        if(musicPlayer != null) {
            if(!paused) {
                musicPlayer.startPlayback();
            } else {
                musicPlayer.pausePlayback();
            }
            paused = false;
        } else {
            throw new MusicException(MusicHandler.class.getSimpleName(), "0001", "Music file has yet to be loaded");
        }
    }
    
    public static void pause() throws MusicException {
        if(isLoaded()) {
            musicPlayer.pausePlayback();
            paused = true;
        } else {
            throw new MusicException(MusicHandler.class.getSimpleName(), "0001", "Music file has yet to be loaded");
        }
    }
    
    public static void stop() throws MusicException {
        if(isLoaded()) {
            musicPlayer.stopPlayback();
            musicPlayer = null;
            paused = false;
        } else {
            throw new MusicException(MusicHandler.class.getSimpleName(), "0001", "Music file has yet to be loaded");
        }
    }
    
    public static boolean isLoaded() {
        return (musicPlayer != null)? true: false;
    }
    
    public static boolean isPaused() {
        return paused;
    }
    
    public static boolean isStopped() {
        if(isLoaded()) {
            return musicPlayer.isStopped();
        } else {
            return true;
        }
    }
    
    public static Long getCurrentPlaybackTime() {
        return musicPlayer.getCurrentPlaybackTime();
    }
    
    public static Long getTotalPlaybackTime() {
        return musicPlayer.getTotalPlaybackTime();
    }
    
}
