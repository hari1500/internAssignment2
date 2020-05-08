package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MainActivity extends AppCompatActivity {
    TextView textViewTitle;
    ArrayList<JSONObject> questionsArrayList = new ArrayList<>();
    final String surveyFileName = "survey.json", titleKey = "title", questionsKey = "questions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTitle = findViewById(R.id.textViewTitle);
        try {
            String json = getJSON(surveyFileName);
            if(json == null || json.equals(""))
                return;

            JSONArray surveyJSONArray = new JSONArray(json);
            if(surveyJSONArray.length() <= 0)
                return;

            JSONObject surveyJSON = surveyJSONArray.getJSONObject(0);
            if(!(surveyJSON.has(titleKey) && surveyJSON.has(questionsKey)))
                return;

            textViewTitle.setText(surveyJSON.getString("title"));
            JSONArray questionsJSONArray = surveyJSON.getJSONArray("questions");
            for(int i=0; i<questionsJSONArray.length(); ++i)
                questionsArrayList.add(questionsJSONArray.getJSONObject(i));

            addQuestions();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJSON(String fileName) {
        String json = null;

        try {
            InputStream is =getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void addQuestions() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for(int i=0; i<questionsArrayList.size(); ++i) {
            QuestionFragment fragment = new QuestionFragment(this, questionsArrayList.get(i), (i != questionsArrayList.size()-1));
            fragmentTransaction.add(R.id.linearLayoutQuestions, fragment);
        }
        fragmentTransaction.commit();
    }
}