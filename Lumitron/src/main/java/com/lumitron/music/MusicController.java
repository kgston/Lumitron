package com.lumitron.music;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.mp3transform.Decoder;

import com.lumitron.util.AppSystem;

public class MusicController extends Thread {
    
    private Decoder decoder = new Decoder();
    private File musicFile;
    private BufferedInputStream musicInput;
    private Thread musicThread;
    
    public static MusicController load(File file) throws MusicException {
        if (!file.getName().endsWith(".mp3")) {
            throw new MusicException(MusicController.class.getSimpleName(), "0003", "Unsupported file format");
        }
        MusicController MusicController = new MusicController();
        MusicController.musicFile = file;
        try {
            MusicController.musicInput = new BufferedInputStream(new FileInputStream(file), 128 * 1024);
        } catch (FileNotFoundException e) {
            throw new MusicException(MusicController.class.getSimpleName(), "0002", "Unable to open file: " + e.getMessage());
        }
        MusicController.musicThread = new Thread(MusicController);
        MusicController.musicThread.setName(MusicController.getClass().getName());
        MusicController.musicThread.setPriority(Thread.MAX_PRIORITY);
        return MusicController;
    }
    
    public Long getTotalPlaybackTime() throws MusicException {
        AudioFileFormat baseFileFormat;
        try {
            baseFileFormat = AudioSystem.getAudioFileFormat(musicFile);
            Map<String, Object> properties = baseFileFormat.properties();
            return (Long) properties.get("duration");
        } catch (UnsupportedAudioFileException e) {
            throw new MusicException(this.getClass().getSimpleName(), "0003", "Unsupported file format: " + e.getMessage());
        } catch (IOException e) {
            throw new MusicException(this.getClass().getSimpleName(), "0002", "Unable to open file: " + e.getMessage());
        }
    }
    
    public void startPlayback() {
        if (musicFile == null) {
            throw new MusicException(MusicController.class.getSimpleName(), "0004", "Music file not initilized");
        } else {
            musicThread.start();
        }
    }

    public void stopPlayback() {
        decoder.stop();
        try {
            musicThread.join();
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public boolean pausePlayback() {
        return decoder.pause();
    }
    
    public boolean isStopped() {
        return decoder.isStopped();
    }
    
    public Long getCurrentPlaybackTime() {
        return decoder.getCurrentPlaybackTime();
    }
    
    public void run() {
        try {
            if (musicFile == null) {
                throw new MusicException(MusicController.class.getSimpleName(), "0004", "Music file not initilized");
            }
            play(musicFile);
        } catch (IOException e) {
            throw new MusicException(this.getClass().getSimpleName(), "0001", "Error while playing back music: " + e.getMessage());
        }
    }
    
    private void play(File file) throws IOException {
        AppSystem.log(this.getClass(), "Playing back file: " + file);
        decoder.play(file.getName(), musicInput);
        musicFile = null;
    }
    
}
