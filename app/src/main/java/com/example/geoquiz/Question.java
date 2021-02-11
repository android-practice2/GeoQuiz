package com.example.geoquiz;

import java.io.Serializable;

public class Question implements Serializable {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean isAnswered;
    private boolean isCheated;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public boolean isCheated() {
        return isCheated;
    }

    public void setCheated(boolean cheated) {
        isCheated = cheated;
    }
}
