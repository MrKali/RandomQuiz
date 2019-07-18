package com.example.myapplication.models;

public class TimeModel {
    private String numberOfQuestion, timeOfQuestion;

    public TimeModel(String numberOfQuestion, String timeOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
        this.timeOfQuestion = timeOfQuestion;
    }

    public String getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(String numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public String getTimeOfQuestion() {
        return timeOfQuestion;
    }

    public void setTimeOfQuestion(String timeOfQuestion) {
        this.timeOfQuestion = timeOfQuestion;
    }
}
