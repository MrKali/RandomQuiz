package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.TimeAdapter;
import com.example.myapplication.models.TimeModel;
import com.example.myapplication.utils.DesignUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewScore, textViewTime;
    private RecyclerView recyclerViewTimeDetails;
    private Button buttonAgain;

    private TimeAdapter timeAdapter;
    private ArrayList<TimeModel> timeModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        DesignUtils.setStatusBarGradiant(this);

        findVisualElements();
        onEventsFunctions();
        prepareRecyclerView();
        showScore();
        showTotalTime();
        showIndividualTimesOfQuestions();

    }

    /* Function to identify the elements on XML */
    private void findVisualElements(){
        textViewScore = findViewById(R.id.textViewScore);
        textViewTime = findViewById(R.id.textViewTime);
        recyclerViewTimeDetails = findViewById(R.id.recyclerViewTimeDetails);
        buttonAgain = findViewById(R.id.buttonAgain);
    }

    /* Function to catch the events of buttons, listeners, etc */
    private void onEventsFunctions(){
        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(QuizActivity.class);
                finish();
            }
        });
    }

    /* Function to open a new activity */
    private void openNewActivity(final Class<? extends Activity> ActivityToOpen){
        Intent intent = new Intent(ScoreActivity.this, ActivityToOpen);
        startActivity(intent);
    }

    /* Prepare recycler view of time details */
    private void prepareRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewTimeDetails.setLayoutManager(layoutManager);

        timeAdapter = new TimeAdapter(this, timeModels);
        recyclerViewTimeDetails.setAdapter(timeAdapter);
    }

    /* Function to show score */
    private void showScore(){
        String score = "Score: " + Objects.requireNonNull(
                Objects.requireNonNull(getIntent().getExtras()).get("score")).toString();
        textViewScore.setText(score);
    }

    /* Function to show total time */
    private void showTotalTime(){
        try {
            ArrayList<Long> time_array = (ArrayList<Long>) getIntent().getSerializableExtra("time");
            assert time_array != null;
            long total_time = time_array.get(time_array.size() - 1);

            String strToShow = "Duration: " + formatTime(total_time);
            textViewTime.setText(strToShow);
        }catch (Exception ignored){}
    }

    /* Function to show individual times of questions on recycler view */
    private void showIndividualTimesOfQuestions(){
        try {
            ArrayList<Long> time_array = (ArrayList<Long>) getIntent().getSerializableExtra("time");
            long question_time = 0;
            for (int i = 0; i <= time_array.size(); i++){
                if (i != 0){
                    question_time = time_array.get(i) - time_array.get(i - 1);
                }else {
                    question_time = time_array.get(i);
                }

                TimeModel timeModelToSet = new TimeModel(String.valueOf(i + 1),
                        "≈" + formatTime(question_time));
                timeModels.add(timeModelToSet);

                if (i == time_array.size() - 1){
                    timeAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception ignored){}
    }

    /* Function to format time*/
    @SuppressLint("DefaultLocale")
    private String formatTime(long timeToFormat){
        return String.format("%d sec",
                TimeUnit.MILLISECONDS.toSeconds(timeToFormat) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeToFormat)));
    }
}
