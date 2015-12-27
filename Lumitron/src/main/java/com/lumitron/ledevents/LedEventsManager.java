package com.lumitron.ledevents;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lumitron.music.MusicHandler;
import com.lumitron.util.AppSystem;

public class LedEventsManager implements Runnable {
    private static final Long SLEEP_UNTIL_START_MILLISECONDS = new Long(10);
    private static final Long SLEEP_OFFSET_MILLISECONDS = new Long(-50);
    private static final Long PLAYBACK_JITTER = new Long(100);

    private static LedEventsManager executionRunnable;
    private volatile static Thread executionThread;
    private static String originalEvents = null;
    private static List<LedEvent> ledEvents = new ArrayList<>();

    private boolean isRunning = false;
    private volatile static Long seekTo = null;

    /**
     * Load new events to be executed
     * @param ledEvents
     */
    public static void load(String jsonLedEvents) {
        originalEvents = jsonLedEvents;
        ledEvents = deserializeLedEvents(jsonLedEvents);
        // Sort and register the events
        Collections.sort(ledEvents);
        LedEventsManager.ledEvents = (List<LedEvent>) Collections.synchronizedList(ledEvents);
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
        AppSystem.log(LedEventsManager.class, "Starting events playback thread");
        executionThread.start();
    }
    
    public static void seek(Long targetTimeInMicro, Long currentPlaybackTime) {
        if(targetTimeInMicro > currentPlaybackTime) {
            synchronized (ledEvents) {
                int counter = 0;
                while(ledEvents.size() > 0 && ledEvents.get(0).getExecutionTime() * 1000 <= targetTimeInMicro) {
                    ledEvents.remove(0);
                    counter++;
                }
                AppSystem.log(LedEventsManager.class, "Removed " + counter + " events, from " + currentPlaybackTime / 1000 + " ms to " + targetTimeInMicro / 1000 + " ms");
            }
        } else {
            AppSystem.log(LedEventsManager.class, "Reloading events list from start");
            load(originalEvents);
            seek(targetTimeInMicro, 0L);
        }
        seekTo = targetTimeInMicro;
        if(isStopped()) {
            play();
        } else {
            executionThread.interrupt();
        }
    }

    /**
     * Stop the execution of the events
     */
    public static void stop() {
        if (executionRunnable != null) {
            AppSystem.log(LedEventsManager.class, "Events playback is stopping as stop command is received");
            executionRunnable.terminate();
            executionRunnable = null;
        }

        if (LedEventsManager.executionThread == null) {
            return;
        }

        try {
            executionThread.interrupt();
            executionThread.join();
            AppSystem.log(LedEventsManager.class, "Events playback thread has stopped");
        } catch (InterruptedException e) {
            try {
                //Try again
                executionThread.join();
            } catch (InterruptedException e1) {}
        } finally {
            executionThread = null;
        }
    }
    
    public static boolean isStopped() {
        if(executionThread == null) {
            AppSystem.log(LedEventsManager.class, "Events playback is stopped");
            return true;
        } else {
            AppSystem.log(LedEventsManager.class, "Events playback is running");
            return false;
        }
    }

    @Override
    public void run() {
        Long playbackTime = 0L;
        while (isRunning) {
            // Stop the execution if the array of events is empty
            if (ledEvents.isEmpty()) {
                AppSystem.log(this.getClass(), "Events playback is stopping as playback is finished");
                executionRunnable.terminate();
                break;
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
            
            AppSystem.log(this.getClass(), "Getting next event");
            LedEvent upcomingLedEvent = getNextEvent();
            if(upcomingLedEvent != null) {
                Long timeToNextExecution = null;
                do {
                    AppSystem.log(this.getClass(), "Next event at " + (upcomingLedEvent.getExecutionTime()) + " ms");
                    timeToNextExecution = (upcomingLedEvent.getExecutionTime() * 1000) - playbackTime;
                    timeToNextExecution /= 1000;
                    if (timeToNextExecution > 0) {
                        timeToNextExecution += (timeToNextExecution + SLEEP_OFFSET_MILLISECONDS > 0) ? SLEEP_OFFSET_MILLISECONDS : 0;
                        AppSystem.log(this.getClass(), "Sleeping for " + timeToNextExecution + "ms");
                        sleep(timeToNextExecution);
                    }
                    
                    //Seeking realignment code
                    if(seekTo != null) {
                        AppSystem.log(this.getClass(), "Events has been seeked! Refreshing");
                        AppSystem.log(this.getClass(), "Waiting for music to be seeked");
                        //Wait till timecode realigns back to event timecode
                        while(playbackTime < seekTo - 1000 * 1000 || playbackTime > seekTo + 1000 * 1000) {
                            playbackTime = MusicHandler.getCurrentPlaybackTime();
                            AppSystem.log(this.getClass(), "Current playback time while waiting for seek: " + playbackTime / 1000 + " ms");
                            sleep(30);
                        }
                        AppSystem.log(this.getClass(), "Music seeked! Continuing playback");
                        seekTo = null;
                        break;
                    }
                    
                    playbackTime = MusicHandler.getCurrentPlaybackTime();
                    timeToNextExecution = (upcomingLedEvent.getExecutionTime() * 1000) - playbackTime;
                    timeToNextExecution /= 1000;
                    if(timeToNextExecution > PLAYBACK_JITTER) {
                        AppSystem.log(this.getClass(), "There is still more time till the next event, could be on pause");
                        continue;
                    }
                    
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
                } while(timeToNextExecution > PLAYBACK_JITTER && upcomingLedEvent != null && isRunning);
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
        executionRunnable = null;
        executionThread = null;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            AppSystem.log(this.getClass(), "Something happened while sleeping, waking up... yawnzzz");
        }
    }

    private static ArrayList<LedEvent> deserializeLedEvents(String ledEventsJson) {
        Type eventListType = new TypeToken<ArrayList<LedEvent>>(){}.getType();
        ArrayList<LedEvent> ledEvents = new Gson().fromJson(ledEventsJson, eventListType);
        return ledEvents;
    }
}
