package com.example.geoquiz;

import java.text.NumberFormat;

public class QuizGrade {
    private final int total;
    private int rightCount;
    private int answeredCount;

    public QuizGrade(int total) {
        this.total = total;
    }

    public void incAnswered() {
        if (isOver()) {
            return;
        }
        this.answeredCount++;
    }
    public void incRight() {
        if (isOver()) {
            return;
        }
        this.rightCount++;
    }

    public boolean isOver() {
        return answeredCount >= total;
    }

    public String calcScorePercentage() {
        double d = rightCount * 1.0 / total;
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(d);
    }

    public int getTotal() {
        return total;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(int answeredCount) {
        this.answeredCount = answeredCount;
    }
}
