package com.example.assignment2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomViewHolder> {
    private ArrayList<String> questions;
    private ArrayList<String> optionsType;
    private ArrayList<ArrayList<String>> allOptions;
    private ArrayList<Set<Integer>> markedOptions;
    private final static String[] optionsTypes = { "Radio Button", "Check Box" };

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;
        RadioGroup radioGroupOptions;
        LinearLayout checkBoxOptions;
        View viewDivider;
        View currentView;

        CustomViewHolder(View v) {
            super(v);

            this.textViewQuestion   = v.findViewById(R.id.textViewQuestion);
            this.radioGroupOptions  = v.findViewById(R.id.radioGroupOptions);
            this.checkBoxOptions    = v.findViewById(R.id.checkBoxOptions);
            this.viewDivider        = v.findViewById(R.id.viewDivider);
            this.currentView        = v;
        }
    }

    CustomListAdapter(ArrayList<String> questions,
                      ArrayList<String> optionsType,
                      ArrayList<ArrayList<String>> allOptions,
                      ArrayList<Set<Integer>> marked
    ) {
        this.questions          = questions;
        this.optionsType        = optionsType;
        this.allOptions         = allOptions;
        this.markedOptions      = marked;
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return (new CustomViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Log.v("onBindViewHolder", ""+position+" "+allOptions.get(position).size());
        String type = optionsType.get(position);
        ArrayList<String> options = allOptions.get(position);

        holder.textViewQuestion.setText(questions.get(position));
        holder.viewDivider.setVisibility((position != getItemCount()-1) ? View.VISIBLE : View.INVISIBLE);
        holder.checkBoxOptions.setVisibility(type.equals(optionsTypes[1]) ? View.VISIBLE : View.INVISIBLE);
        holder.radioGroupOptions.setVisibility(type.equals(optionsTypes[0]) ? View.VISIBLE : View.INVISIBLE);

        holder.checkBoxOptions.removeAllViews();
        holder.radioGroupOptions.removeAllViews();

        if(type.equals(optionsTypes[0])) {
            for(int i=0; i<options.size(); ++i) {
                addRadioButton(holder, options.get(i), position, i);
            }
            if (markedOptions.get(position).size() > 0) {
                ArrayList<Integer> markedOption = new ArrayList<Integer>(markedOptions.get(position));
                ((RadioButton)holder.radioGroupOptions.getChildAt(markedOption.get(0))).setChecked(true);
            }
        } else if(type.equals(optionsTypes[1])) {
            for(int i=0; i<options.size(); ++i) {
                addCheckBox(holder, options.get(i), position, i);
            }
        }
    }

    private void addRadioButton(@NonNull final CustomViewHolder holder, String optionText, final int parentIndex, final int optionIndex) {
        RadioButton radioButton = new RadioButton(holder.currentView.getContext());
        radioButton.setText(optionText);
        radioButton.setTextSize(20);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("onBindViewHolder: radioButton", ""+parentIndex+" "+optionIndex+" "+markedOptions.get(parentIndex));
                if(((CompoundButton)v).isChecked()) {
                    markedOptions.get(parentIndex).clear();
                    markedOptions.get(parentIndex).add(optionIndex);
                } else {
                    markedOptions.get(parentIndex).remove(optionIndex);
                }
            }
        });
        holder.radioGroupOptions.addView(radioButton);
    }

    private void addCheckBox(@NonNull final CustomViewHolder holder, String optionText, final int parentIndex, final int optionIndex) {
        CheckBox checkBox = new CheckBox(holder.currentView.getContext());
        checkBox.setText(optionText);
        checkBox.setTextSize(20);
        checkBox.setChecked(markedOptions.get(parentIndex).contains(optionIndex));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CompoundButton)v).isChecked()) {
                    markedOptions.get(parentIndex).add(optionIndex);
                } else {
                    markedOptions.get(parentIndex).remove(optionIndex);
                }
            }
        });

        holder.checkBoxOptions.addView(checkBox);
    }
}
