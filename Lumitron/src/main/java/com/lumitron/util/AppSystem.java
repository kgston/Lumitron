package com.lumitron.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lumitron.music.MusicHandler;

/**
 * Standard app context class listener to listen to the web server's calls and manages logs
 * @author kingston.chan
 *
 */
public class AppSystem implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log(this.getClass(), "Lumitron powering down");
        if(!MusicHandler.isStopped()) {
            log(this.getClass(), "Stopping music playback");
            MusicHandler.stop();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        log(this.getClass(), "Lumitron powering up");
    }
    
    /**
     * Writes log to the console. Use this method for logs so we can adjust it as necessary
     * @param <T>
     * @param message The message to report
     */
    public static <T> void log(Class<T> classObj, String message) {
        System.out.println("<" + classObj.getSimpleName() + ">  " + message);
    }

}