package com.lumitron.sheet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lumitron.led.LedHandler;
import com.lumitron.util.LumitronException;

public class SheetManager {
    /**
     * Save the data in the file given in parameter
     * @param file The filepath of the sheet. The parent directories are created if they does not exist
     * @param data The sheet in the stringified JSON format
     * @throws LumitronException
     */
    public static void save(String file, String data) throws LumitronException {
        try {
            Path path = Paths.get(file);
            Files.createDirectories(path.getParent());
            Files.write(path, data.getBytes());
        } catch (IOException e) {
            throw new LumitronException(SheetManager.class.getSimpleName(), "0001", "Could not save the file " + file);
        }
    }

    /**
     * Load and return the content of the file given in parameter
     * @param file The filepath of the sheet
     * @return A String, being the content of the file
     * @throws LumitronException
     */
    public static String load(String file) throws LumitronException {
        String content = null;

        try {
            content = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            throw new LumitronException(SheetManager.class.getSimpleName(), "0002", "Could not load the file " + file);
        }

        return content;
    }

    /**
     * Read the sheet and schedule the execution of the commands to the device given in parameter
     * @param file The filepath of the sheet
     * @param device The target device of the commands
     * @throws LumitronException
     */
    public static void read(String file, final String device) throws LumitronException {
        // Load the sheet file
        String content = SheetManager.load(file);
        Type eventType = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> sheet = new Gson().fromJson(content, eventType);

        // Schedule the execution of each event of the sheet
        for (Map<String, String> event : sheet) {
            // Parse the parameters
            // Note: the keyword 'final' has to be used for being able to use the variables inside the TimerTask
            final String ledMethod = event.get("function");
            final ArrayList<String> ledMethodParameters = new ArrayList<>();

            switch(ledMethod) {
                case "setColour":
                    ledMethodParameters.add(event.get("colour"));
                    break;
                case "setBrightness":
                    ledMethodParameters.add(event.get("brightness"));
                    break;
                case "transitionToColour":
                    ledMethodParameters.add(event.get("pauseInterval"));
                    ledMethodParameters.add(event.get("incrementInterval"));
                    ledMethodParameters.add(event.get("colour"));
                    break;
            }

            final String[] ledMethodParametersArray = new String[ledMethodParameters.size()];

            // Create the timer
            Timer timer = new Timer();
            int delay = Integer.parseInt(event.get("time"));

            timer.schedule(
                new TimerTask() {
                    public void run() {
                        LedHandler.sendCommand(device, ledMethod, ledMethodParameters.toArray(ledMethodParametersArray));
                    }
                },
                delay
            );
        }
    }
}
