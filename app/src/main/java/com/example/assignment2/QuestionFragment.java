package com.example.assignment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuestionFragment extends Fragment {
    private JSONObject item;
    private Context context;
    private boolean showDivider;
    QuestionFragment(Context c, JSONObject json, boolean show) {
        context = c;
        item = json;
        showDivider = show;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        TextView textViewQuestion = view.findViewById(R.id.textViewQuestion);
        View v = view.findViewById(R.id.viewDivider);
        v.setVisibility(showDivider ? View.VISIBLE : View.INVISIBLE);

        final String questionKey = "question", typeKey = "type", optionsKey = "options";
        try {
            if(!(item.has(questionKey) && item.has(typeKey) && item.has(optionsKey)))
                return view;

            textViewQuestion.setText(item.getString("question"));

            String type = item.getString("type");
            JSONArray optionJSONArray = item.getJSONArray("options");
            ArrayList<String> options = new ArrayList<>();
            for(int i=0; i<optionJSONArray.length(); ++i)
                options.add(optionJSONArray.getString(i));

            if(type.equals("Radio Button")) {
                RadioGroup radioGroupOptions = view.findViewById(R.id.radioGroupOptions);
                radioGroupOptions.setVisibility(View.VISIBLE);
                for(int i=0; i<options.size(); ++i) {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setText(options.get(i));
                    radioButton.setTextSize(20);
                    radioGroupOptions.addView(radioButton);
                }
            } else if(type.equals("Check Box")) {
                LinearLayout checkBoxOptions = view.findViewById(R.id.checkBoxOptions);
                checkBoxOptions.setVisibility(View.VISIBLE);
                for(int i=0; i<options.size(); ++i) {
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(options.get(i));
                    checkBox.setTextSize(20);
                    checkBoxOptions.addView(checkBox);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
