package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoquiz.util.SerializeUtil;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    //    private static final String KEY_IS_CHEATER = "KEY_IS_CHEATER";
    private static final String KEY_QUESTION_BANK = "KEY_QUESTION_BANK";

    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank;
    private int mCurrentIndex = 0;
    private QuizGrade mQuizGrade;
//    private boolean mIsCheater;

    private void init() {
        mQuestionBank = new Question[]{
                new Question(R.string.question_australia, true),
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true),
        };
        mQuizGrade = new QuizGrade(mQuestionBank.length);
    }

    private void resume(Question[] previous) {
        mQuestionBank = previous;
        mQuizGrade = new QuizGrade(mQuestionBank.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d(TAG, "onCreate(Bundle) called");
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
//            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            byte[] bytes = savedInstanceState.getByteArray(KEY_QUESTION_BANK);
            Question[] previous = SerializeUtil.toObject(bytes);
            resume(previous);

        } else {
            init();
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        resumeQuestion();
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(true);

            }
        });
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP, 0, 0);
//                toast.show();

                checkAnswer(false);

            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        mPrevButton = findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
//            mIsCheater = CheatActivity.wasAnswerShown(data);
            mQuestionBank[mCurrentIndex].setCheated(true);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
//        savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        savedInstanceState.putByteArray(KEY_QUESTION_BANK, SerializeUtil.toBytes(mQuestionBank));

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void next() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
        checkEnableAnswerButton();

    }

    private void prev() {
        int i = mCurrentIndex - 1;
        if (i == -1) {
            i = mQuestionBank.length - 1;
        }
        mCurrentIndex = i % mQuestionBank.length;
        updateQuestion();
        checkEnableAnswerButton();
    }

    private void checkEnableAnswerButton() {
        Question question = mQuestionBank[mCurrentIndex];
        if (question.isAnswered()) {
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
        } else {
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        Question question = mQuestionBank[mCurrentIndex];
        question.setAnswered(true);
        checkEnableAnswerButton();

        boolean answerTrue = question.isAnswerTrue();

        int messageResId;
        if (question.isCheated()) {
            messageResId = R.string.judgment_toast;
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            return;
        }

        if (answerTrue == userPressedTrue) {
            messageResId = R.string.correct_toast;
            mQuizGrade.incRight();
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        mQuizGrade.incAnswered();
        if (mQuizGrade.isOver()) {
            Toast.makeText(this, mQuizGrade.calcScorePercentage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateQuestion() {
//        mIsCheater=false;
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void resumeQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());

    }

}