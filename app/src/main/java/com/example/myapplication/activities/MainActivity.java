package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.utils.DesignUtils;

public class MainActivity extends AppCompatActivity {

    private Button buttonStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DesignUtils.setStatusBarGradiant(this);

        findVisualElements();
        onEventsFunctions();
    }

    /* Function to identify the elements on XML */
    private void findVisualElements(){
        buttonStartQuiz = findViewById(R.id.buttonStartQuiz);
    }

    /* Function to catch the events of buttons, listeners, etc */
    private void onEventsFunctions(){
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(QuizActivity.class);
            }
        });
    }

    /* Function to open a new activity */
    private void openNewActivity(final Class<? extends Activity> ActivityToOpen){
        Intent intent = new Intent(MainActivity.this, ActivityToOpen);
        startActivity(intent);
    }

}
