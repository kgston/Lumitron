package com.lumitron.sheet;

import java.util.HashMap;

public class SheetHandler {
    public static HashMap<String, String> save(HashMap<String, String> params) {
        // Get the parameters
        String file = params.get("file");
        String data = params.get("data");

        // Save the sheet
        SheetManager.save(file, data);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("file", file);

        return response;
    }

    public static HashMap<String, String> load(HashMap<String, String> params) {
        // Get the parameters
        String file = params.get("file");

        // Load the file content
        String content = SheetManager.load(file);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("file", file);
        response.put("sheet", content);

        return response;
    }

    public static HashMap<String, String> read(HashMap<String, String> params) {
        // Get the parameters
        String file = params.get("file");
        String device = params.get("device");

        // Read the file
        SheetManager.read(file, device);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("file", file);
        response.put("device", device);

        return response;
    }
}
