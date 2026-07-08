package com.rvpa.autofix;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIFixer {

    private Context context;
    private static final String GROQ_API_KEY = "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";
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

    public AIFixer(Context context) {
        this.context = context;
    }

    public String fix(String error) {
        try {
            String prompt = "You are an expert Android developer. Fix this build error: " + error + 
                           ". Give only the fix, no explanation. Format: 'Fix: '";
            
            String response = callGroqAPI(prompt);
            
            if (response != null && !response.isEmpty()) {
                return response;
            }
            return "Manual fix required. Check build logs.";
            
        } catch (Exception e) {
            return "AI Fix failed: " + e.getMessage();
        }
    }

    private String callGroqAPI(String prompt) {
        try {
            URL url = new URL(GROQ_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + GROQ_API_KEY);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            JSONObject payload = new JSONObject();
            payload.put("model", "mixtral-8x7b-32768");
            payload.put("messages", new JSONObject[]{
                new JSONObject().put("role", "system").put("content", "You are an expert Android developer."),
                new JSONObject().put("role", "user").put("content", prompt)
            });
            payload.put("temperature", 0.3);
            payload.put("max_tokens", 500);

            OutputStream os = conn.getOutputStream();
            os.write(payload.toString().getBytes());
            os.flush();
            os.close();

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                JSONObject json = new JSONObject(response.toString());
                return json.getJSONArray("choices")
                           .getJSONObject(0)
                           .getJSONObject("message")
                           .getString("content")
                           .replace("Fix: ", "")
                           .trim();
            } else {
                return "API Error: " + conn.getResponseCode();
            }

        } catch (Exception e) {
            return "API Exception: " + e.getMessage();
        }
    }
}
