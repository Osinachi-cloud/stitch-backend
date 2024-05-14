package com.stitch.user.util;

import com.stitch.commons.exception.StitchException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryUtils {
    public static String getCountryCodeFromEndpoint(String endPoint, String key) {
        try {
            URL url = new URL(endPoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            conn.disconnect();

            return jsonResponse.getString(key);
        }catch (Exception e){
            throw new StitchException(e.getMessage());
        }

    }

    public static String getCountryCodeFromEndpoint() {
        try {
            URL url = new URL("https://ipinfo.io/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            conn.disconnect();

            return jsonResponse.getString("country");
        }catch (Exception e){
            throw new StitchException(e.getMessage());
        }

    }

    public static String getCountryNameFromEndpoint(String countryCode) {

        try {
            URL url = new URL("https://restcountries.com/v2/alpha/" + countryCode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            conn.disconnect();

            return jsonResponse.getString("name");
        }catch (Exception e){
            throw new StitchException(e.getMessage());
        }
    }
}
