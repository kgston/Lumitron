package com.lumitron.ledevents;

import java.util.ArrayList;
import java.util.Collections;

import com.lumitron.music.MusicHandler;
import com.lumitron.util.AppSystem;

public class LedEventsManager implements Runnable {
    private static final Long SLEEP_UNTIL_START_MILLISECONDS = new Long(10);
    private static final Long SLEEP_OFFSET_MILLISECONDS = new Long(-50);

    private static LedEventsManager executionRunnable;
    private static Thread executionThread;
    private static ArrayList<LedEvent> ledEvents = new ArrayList<>();

    private boolean isRunning = false;

    /**
     * Load new events to be executed
     * @param ledEvents
     */
    public static void load(ArrayList<LedEvent> ledEvents) {
        // Sort and register the events
        Collections.sort(ledEvents);
        LedEventsManager.ledEvents = ledEvents;
    }

    /**
     * Start executing the event according to the music playback time
     */
    public static void play() {
        stop();
        executionRunnable = new LedEventsManager();
        executionRunnable.isRunning = true;
        executionThread = new Thread(executionRunnable);
        executionThread.setPriority(Thread.MAX_PRIORITY);
        executionThread.start();
    }

    /**
     * Stop the execution of the events
     */
    public static void stop() {
        if (executionRunnable != null) {
            executionRunnable.terminate();
            executionRunnable = null;
        }

        if (LedEventsManager.executionThread == null) {
            return;
        }

        try {
            executionThread.join();
            executionThread = null;
        } catch (InterruptedException e) {
            // Ignore, this is the normal behaviour
        }
    }

    @Override
    public void run() {
        Long playbackTime = 0L;
        while (isRunning) {
            // Stop the execution if the array of events is empty
            if (ledEvents.isEmpty()) {
                executionRunnable.terminate();
                continue;
            }

            // Retrieve the current playback time
            playbackTime = MusicHandler.getCurrentPlaybackTime();

            // If the music has not started yet, reschedule the start
            if (playbackTime == 0) {
                sleep(SLEEP_UNTIL_START_MILLISECONDS);
                continue;
            }

//            while (!ledEvents.isEmpty()) {
//                // Get the next event to be executed
//                LedEvent ledEvent = ledEvents.get(0);
//
//                // Report the execution if the playback time has not been reached yet
//                Long pause = (ledEvent.getExecutionTime() * 1000) - playbackTime;
//
//                if (pause > 0) {
//                    pause /= 1000;
//                    //pause += (pause + SLEEP_OFFSET_MILLISECONDS > 0) ? SLEEP_OFFSET_MILLISECONDS : 0;
//                    pause = 5L;
//                    AppSystem.log(this.getClass(), "Current: " + playbackTime/1000 + "ms Sleeping for: " + pause + "ms'");
//                    sleep(pause);
//                    break;
//                }
//
//                // Execute the event and remove it from the list
//                //System.out.println("<LedEventManager> [" + playbackTime + "ms] Executing event " + ledEvent.toString());
//                AppSystem.log(this.getClass(), "Executing event @ " + (playbackTime/1000) + "ms delay: " + ((playbackTime - ledEvent.getExecutionTime() * 1000) / 1000) + "ms\n" + ledEvent.toString());
//                ledEvent.execute();
//                ledEvents.remove(ledEvent);
//            }
            
            LedEvent upcomingLedEvent = getNextEvent();
            if(upcomingLedEvent != null) {
                Long pause = (upcomingLedEvent.getExecutionTime() * 1000) - playbackTime;
                if (pause > 0) {
                    pause /= 1000;
                    pause += (pause + SLEEP_OFFSET_MILLISECONDS > 0) ? SLEEP_OFFSET_MILLISECONDS : 0;
                    AppSystem.log(this.getClass(), "Sleeping for " + pause + "ms");
                    sleep(pause);
                }
                playbackTime = MusicHandler.getCurrentPlaybackTime();
                AppSystem.log(this.getClass(), "Executing event @ " + (playbackTime/1000) + "ms delay: " + ((playbackTime - upcomingLedEvent.getExecutionTime() * 1000) / 1000) + "ms\n" + upcomingLedEvent.toString());
                upcomingLedEvent.execute();
                ledEvents.remove(upcomingLedEvent);
                    
                LedEvent followingLedEvent = getNextEvent();
                while(followingLedEvent != null && (followingLedEvent.getExecutionTime() * 1000) - playbackTime <= 0) {
                    AppSystem.log(this.getClass(), "Executing event @" + playbackTime + followingLedEvent.toString());
                    followingLedEvent.execute();
                    ledEvents.remove(followingLedEvent);
                    followingLedEvent = getNextEvent();
                }
            }
        }
    }
    
    private LedEvent getNextEvent() {
        if(ledEvents.size() == 0) {
            return null;
        } else {
            return ledEvents.get(0);
        }
    }

    private void terminate() {
        isRunning = false;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
