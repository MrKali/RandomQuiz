package com.example.myapplication.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.QuestionsModel;
import com.example.myapplication.utils.DesignUtils;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private ArrayList<QuestionsModel> questionsModelArrayList = new ArrayList<>();
    private ArrayList<Long> timeArrayList = new ArrayList<>();

    private Button option_a, option_b, option_c, option_d, option_e;
    private TextView textViewQuestion, textViewPoints;
    private Chronometer chronometer;
    private ProgressBar progressBar;

    private static int lastQuestion = 0;
    private static int score = 0;

    private Boolean timerRunning = false;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        DesignUtils.setStatusBarGradiant(this);

        findVisualElements();
        onEventsFunctions();
        createArrayWithQuestions();
        showQuestion();

    }

    @Override
    public void finish() {
        lastQuestion = 0;
        score = 0;
        super.finish();
    }

    /* Function to identify the elements on XML */
    private void findVisualElements(){
        option_a = findViewById(R.id.option_a);
        option_b = findViewById(R.id.option_b);
        option_c = findViewById(R.id.option_c);
        option_d = findViewById(R.id.option_d);
        option_e = findViewById(R.id.option_e);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewPoints = findViewById(R.id.textViewPoints);
        chronometer = findViewById(R.id.chronometer);
        progressBar = findViewById(R.id.progressBar);
    }

    /* Animation for progressbar */
    private void animateProgressBar(){
        ValueAnimator animator = ValueAnimator.ofInt(0, progressBar.getMax());
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                progressBar.setProgress((Integer)animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    /* Function to catch the events of buttons, listeners, etc */
    private void onEventsFunctions(){
        option_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelected(option_a, "A");
            }
        });

        option_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelected(option_b, "B");
            }
        });

        option_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelected(option_c, "C");
            }
        });

        option_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelected(option_d, "D");
            }
        });

        option_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelected(option_e, "E");
            }
        });
    }

    /* Start chronometer */
    private void startChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        timerRunning = true;
    }

    /* Pause chronometer */
    private void pauseChronometer(){
        if (timerRunning){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            timerRunning = false;
            saveTime();
        }
    }

    /* Save time of answer in array */
    private void saveTime(){
        long stoppedMilliseconds = 0;

        String chronoText = chronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }

        long sum = 0;
        for(int i = 0; i < timeArrayList.size(); i++)
            sum += timeArrayList.get(i);

        timeArrayList.add(stoppedMilliseconds);
    }

    /* Reset buttons style */
    private void resetButtonStyle(){
        option_a.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background));
        option_b.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background));
        option_c.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background));
        option_d.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background));
        option_e.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background));
        option_a.setTextColor(Color.BLACK);
        option_b.setTextColor(Color.BLACK);
        option_c.setTextColor(Color.BLACK);
        option_d.setTextColor(Color.BLACK);
        option_e.setTextColor(Color.BLACK);

    }

    /* Function that changes color of button and values of variables, indicate that is selected */
    private void optionSelected(Button button, String option){
        pauseChronometer();

        animateButton(button);
        String optionSelected = option;
        Button lastButtonClicked = button;

        checkIfAnswerIsCorrect(optionSelected, lastButtonClicked);

        // After 3 seconds showing the correct answer, execute next function
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (questionsModelArrayList.size() >= lastQuestion + 1){
                    resetButtonStyle();
                    showQuestion();
                }else {
                    showLastScreen();
                }
            }
        }, 3000);

    }

    /* Function to create array with all questions, answers and correct answer */
    private void createArrayWithQuestions(){
        QuestionsModel questionsModel;

        // 1 question
        questionsModel = new QuestionsModel(1, "Assuaged",
                "Thirsty", "Devastated", "Untrue", "Unsatisfied",
                "Foiled", "D");
        questionsModelArrayList.add(questionsModel);

        // 2 question
        questionsModel = new QuestionsModel(2, "Hiatus",
                "Nexus", "Atavist", "Cognate", "Vortex",
                "Reflex", "A");
        questionsModelArrayList.add(questionsModel);

        // 3 question
        questionsModel = new QuestionsModel(3, "Adroit",
                "Prim", "Unskillful", "Correct", "Strong",
                "Apt", "B");
        questionsModelArrayList.add(questionsModel);

        // 4 question
        questionsModel = new QuestionsModel(4, "Retrench",
                "Vilify", "Infringe", "Advocate", "Enjoin",
                "Augment", "E");
        questionsModelArrayList.add(questionsModel);

        // 5 question
        questionsModel = new QuestionsModel(5, "Repugnance",
                "Love", "Absolution", "Blame", "Virtue",
                "Awe", "A");
        questionsModelArrayList.add(questionsModel);

        // 6 question
        questionsModel = new QuestionsModel(6, "Fractious",
                "Delicate", "Solid", "Agreeable", "Liberal",
                "Wholesome", "C");
        questionsModelArrayList.add(questionsModel);

        // 7 question
        questionsModel = new QuestionsModel(7, "Admonition",
                "Countenance", "Evasion", "Deposition", "Declaration",
                "Denial", "A");
        questionsModelArrayList.add(questionsModel);

        // 8 question
        questionsModel = new QuestionsModel(8, "Doltish",
                "Casuistic", "Clever", "Qualified", "Disabled",
                "Sharpened", "B");
        questionsModelArrayList.add(questionsModel);

        // 9 question
        questionsModel = new QuestionsModel(9, "Abstruse",
                "Detested", "Detained", "Obvious", "Tight",
                "Rebuilt", "C");
        questionsModelArrayList.add(questionsModel);

        // 10 question
        questionsModel = new QuestionsModel(10, "Eschew",
                "Welcome", "Swallow whole", "Borrow", "Save",
                "Reset", "A");
        questionsModelArrayList.add(questionsModel);

        // 11 question
        questionsModel = new QuestionsModel(11, "Schism",
                "Concealment", "Calm", "Clumsiness", "Union",
                "Reduction", "D");
        questionsModelArrayList.add(questionsModel);

        // 12 question
        questionsModel = new QuestionsModel(12, "Avarice",
                "Cupidity", "Virtue", "Altruism", "Kindness",
                "Stealth", "C");
        questionsModelArrayList.add(questionsModel);
    }

    /* Function to update the points */
    private void updatePoints(){
        final String points = score + " points";
        textViewPoints.setText(points);
    }

    /* Function called by buttons click to check if answer is correct or not*/
    private void checkIfAnswerIsCorrect(String option, Button button){
        animateProgressBar();
        enableDisableButtons(false);
        String correctAnswer = questionsModelArrayList.get(lastQuestion - 1).getCorrectAnswer();

        if (option.equals(correctAnswer)){
            button.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_green));
            button.setTextColor(Color.WHITE);
            score += 400;
            updatePoints();
        }else {
            score -= 100;
            updatePoints();

            button.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_red));
            button.setTextColor(Color.WHITE);


            switch (correctAnswer){
                case "A":
                    option_a.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_green));
                    option_a.setTextColor(Color.WHITE);
                    break;
                case "B":
                    option_b.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_green));
                    option_b.setTextColor(Color.WHITE);
                    break;
                case "C":
                    option_c.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_green));
                    option_c.setTextColor(Color.WHITE);
                    break;
                case "D":
                    option_d.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_green));
                    option_d.setTextColor(Color.WHITE);
                    break;
                case "E":
                    option_e.setBackground(getResources().getDrawable(R.drawable.rounded_complete_border_background_green));
                    option_e.setTextColor(Color.WHITE);
                    break;
            }
        }
    }

    /* Function to enable/disable click of buttons */
    private void enableDisableButtons(Boolean enable){
        if (enable){
            option_a.setClickable(true);
            option_b.setClickable(true);
            option_c.setClickable(true);
            option_d.setClickable(true);
            option_e.setClickable(true);
        }else {
            option_a.setClickable(false);
            option_b.setClickable(false);
            option_c.setClickable(false);
            option_d.setClickable(false);
            option_e.setClickable(false);
        }
    }

    /* Function to animate button */
    private void animateButton(Button button){
        ObjectAnimator animY = ObjectAnimator.ofFloat(button, "translationX", -10f, 0f);
        animY.setDuration(200);//1sec
        animY.setInterpolator(new BounceInterpolator());
        animY.setRepeatCount(2);
        animY.start();
    }

    /* Function to show questions - change the question and options */
    private void showQuestion(){
        progressBar.setProgress(0);
        enableDisableButtons(true);
        startChronometer();
        final String question = questionsModelArrayList.get(lastQuestion).getQuestion();
        final String optionA = questionsModelArrayList.get(lastQuestion).getOptionA();
        final String optionB = questionsModelArrayList.get(lastQuestion).getOptionB();
        final String optionC = questionsModelArrayList.get(lastQuestion).getOptionC();
        final String optionD = questionsModelArrayList.get(lastQuestion).getOptionD();
        final String optionE = questionsModelArrayList.get(lastQuestion).getOptionE();

        textViewQuestion.setText(question);
        option_a.setText(optionA);
        option_b.setText(optionB);
        option_c.setText(optionC);
        option_d.setText(optionD);
        option_e.setText(optionE);

        lastQuestion += 1;
    }

    /* Function to show last screen - with results like score, time, etc */
    private void showLastScreen(){
        Intent intent = new Intent(QuizActivity.this
                , ScoreActivity.class);
        intent.putExtra("score", String.valueOf(score));
        intent.putExtra("time", timeArrayList);
        startActivity(intent);
        this.finish();
    }
}
