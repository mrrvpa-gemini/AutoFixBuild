package com.rvpa.autofix;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class GitManager {

    private Context context;

    public GitManager(Context context) {
        this.context = context;
    }

    public void cloneRepo(String url, String branch, String dest) {
        new GitTask().execute("clone", url, branch, dest);
    }

    public void commit(String message) {
        new GitTask().execute("commit", message);
    }

    public void push() {
        new GitTask().execute("push");
    }

    private class GitTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String cmd = params[0];
                if (cmd.equals("clone")) {
                    String url = params[1];
                    String branch = params[2];
                    String dest = params[3];
                    Process process = Runtime.getRuntime().exec(new String[]{
                        "git", "clone", "-b", branch, url, dest
                    });
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    process.waitFor();
                    return output.toString();
                } else if (cmd.equals("commit")) {
                    String message = params[1];
                    Process process = Runtime.getRuntime().exec(new String[]{
                        "git", "commit", "-am", message
                    });
                    process.waitFor();
                    return "Commit: " + message;
                } else if (cmd.equals("push")) {
                    Process process = Runtime.getRuntime().exec(new String[]{
                        "git", "push"
                    });
                    process.waitFor();
                    return "Push success";
                }
                return "Unknown command";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }
}
