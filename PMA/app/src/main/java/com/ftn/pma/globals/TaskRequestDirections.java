package com.ftn.pma.globals;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.ftn.pma.view.AboutActivity;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;

public class TaskRequestDirections extends AsyncTask<String, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private GoogleMap maps;

    public TaskRequestDirections(GoogleMap googleMap) {
        maps = googleMap;
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseString = "";
        try
        {
            responseString = AboutActivity.requestDirection(strings[0]);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //parsiranje JSON-a
        TaskParser taskParser = new TaskParser(maps);
        taskParser.execute(s);
    }
}
