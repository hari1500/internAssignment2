package com.example.assignment2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomViewHolder> {
    private ArrayList<JSONObject> items;

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;
        RadioGroup radioGroupOptions;
        LinearLayout checkBoxOptions;
        View viewDivider;
        Context context;

        CustomViewHolder(View v) {
            super(v);

            this.textViewQuestion   = v.findViewById(R.id.textViewQuestion);
            this.radioGroupOptions  = v.findViewById(R.id.radioGroupOptions);
            this.checkBoxOptions    = v.findViewById(R.id.checkBoxOptions);
            this.viewDivider        = v.findViewById(R.id.viewDivider);
            this.context            = v.getContext();
        }
    }

    CustomListAdapter(ArrayList<JSONObject> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return (new CustomViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Log.i("onBindViewHolder", "" + position);

        final JSONObject item = items.get(position);
        final String questionKey = "question", typeKey = "type", optionsKey = "options";
        if(!(item.has(questionKey) && item.has(typeKey) && item.has(optionsKey))) {
            return;
        }

        try {
            holder.textViewQuestion.setText(item.getString(questionKey));
            holder.viewDivider.setVisibility((position == getItemCount()-1) ? View.INVISIBLE : View.VISIBLE);

            final String type = item.getString(typeKey);
            final JSONArray optionJSONArray = item.getJSONArray(optionsKey);
            ArrayList<String> options = new ArrayList<>();
            for(int i = 0; i < optionJSONArray.length(); ++i) {
                options.add(optionJSONArray.getString(i));
            }

            if(type.equals("Radio Button") && (holder.radioGroupOptions.getChildCount() == 0)) {
                holder.radioGroupOptions.setVisibility(View.VISIBLE);
                for(int i=0; i<options.size(); ++i) {
                    RadioButton radioButton = new RadioButton(holder.context);
                    radioButton.setText(options.get(i));
                    radioButton.setTextSize(20);
                    holder.radioGroupOptions.addView(radioButton);
                }
            } else if(type.equals("Check Box") && (holder.radioGroupOptions.getChildCount() == 0)) {
                holder.checkBoxOptions.setVisibility(View.VISIBLE);
                for(int i=0; i<options.size(); ++i) {
                    CheckBox checkBox = new CheckBox(holder.context);
                    checkBox.setText(options.get(i));
                    checkBox.setTextSize(20);
                    holder.checkBoxOptions.addView(checkBox);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
