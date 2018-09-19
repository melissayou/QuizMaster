package compsci290.edu.duke.quizmaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
/**
 * Created by Melissa on 2/11/18.
 * The QuizActivity class handles the events of user taking quizzes, saving data along the way
 * and returns to the exact spot if the app is killed or backgrounded.
 */

public class QuizActivity extends AppCompatActivity{
    private Context mContext;
    protected Quiz mQuiz;
    private TextView mQuizTitle;
    private int mQuestionIndex;
    private Question mCurrentQ;
    private TextView mPrompt;
    private String mResult;
    private RadioButton mCheckedRadioButton = null;
    private String mChosenAns;
    private SharedPreferences mSharedPreferences;

    private static final String sOKAY = "Okay";
    private static final String sINDEX = "CURRENTINDEX";
    private static final String sRESULT = "CURRENTSCORE";
    private static final String sQUIZ = "CURRENTQUIZ";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        this.mContext = getApplicationContext();
        mSharedPreferences = getSharedPreferences("DATA", 0);
        Intent receivedIntent = this.getIntent();
        mQuiz = (Quiz) receivedIntent.getParcelableExtra("quiz_object_key");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (mSharedPreferences.getBoolean(mQuiz.getTitle(), false)) { //if already completed
            editor.remove(mQuiz.getTitle() + "Score");
            editor.commit();
            setQuizTitle();
            mQuestionIndex = mQuiz.size() - 1;
            mResult = mSharedPreferences.getString(mQuiz.getTitle() + "_score", "NA");
            setQuestion();
            finishQuizAlertDialog();
        } else {
            if (! mSharedPreferences.getString(sQUIZ, "").equals(mQuiz.getTitle())) { //if first time opening the quiz
            editor.remove(mQuiz.getTitle() + "Score");
            editor.commit();
            editor.putString(mQuiz.getTitle() + "pastResults:", "");
            editor.putString(sQUIZ, mQuiz.getTitle());
            editor.putString(sRESULT, "0");
            editor.putInt(sINDEX, 0);
            editor.commit();
            setQuizTitle();
            mQuestionIndex = 0;
            mResult = "0";
            setQuestion();
            onClickNextButton();
            } else { //if enter the same quiz as before(redo it or midway killed the app)
                if (mSharedPreferences.getInt(sINDEX,0) == 0) { //if redo the quiz
                    editor.remove(mQuiz.getTitle() + "Score");
                    editor.commit();
                }
                setQuizTitle();
                mQuestionIndex = mSharedPreferences.getInt(sINDEX, 0); //default to 0, the previous attempt only entered the 1st question but not click the next button yet
                mResult = mSharedPreferences.getString(sRESULT, "0");
                setQuestion();
                onClickNextButton();
            }
        }
    }

    private void setQuizTitle() {
        mQuizTitle = findViewById(R.id.quiz_title_text_view_for_question);
        mQuizTitle.setText(mQuiz.getTitle());
    }

    /**
     * Displays a list of answer choices and allows user to choose only one answer
     */
    private void setQuestion() {
        mCurrentQ = mQuiz.getQuestion(mQuestionIndex);
        mPrompt = findViewById(R.id.question_prompt);
        mPrompt.setText(mCurrentQ.getPrompt());
        RadioGroup rdg = findViewById(R.id.radioGroup);
        rdg.clearCheck();
        rdg.removeAllViews();
        for (int i = 0; i < mCurrentQ.getAnswers().length; i ++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(mCurrentQ.getAnswerByIndex(i));
            radioButton.setId(i);
            rdg.addView(radioButton);
        }
    }

    /**
     * Stores the user's input and updates sharedPreference data when clicking to the next question
     */
    private void onClickNextButton() {
        Button button = findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            RadioGroup rdg = findViewById(R.id.radioGroup);
            public void onClick(View v) {
                if (rdg.getCheckedRadioButtonId() == -1) { //if didn't choose any choice
                    final AlertDialog.Builder didNotChoose = new AlertDialog.Builder(QuizActivity.this);
                    final AlertDialog alert = didNotChoose.create();
                    didNotChoose.setMessage("Please select a choice");
                    didNotChoose.setPositiveButton(sOKAY, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.cancel();
                        }
                    });
                    didNotChoose.show();
                } else  {
                    mCheckedRadioButton = rdg.findViewById(rdg.getCheckedRadioButtonId());
                    mChosenAns = mCheckedRadioButton.getText().toString();
                    updateQuiz();
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(sQUIZ,mQuiz.getTitle());
                    editor.putInt(sINDEX, mQuestionIndex);
                    editor.commit();

                }
            }
        });
    }

    /**
     * Stores the current quiz title, current quiz score, current question index in sharedPreference
     */
    private void updateQuiz() {
        mQuiz.update(mQuiz.getTitle(), mChosenAns, mQuestionIndex,mContext);
        if (mQuestionIndex == mQuiz.size() - 1) { //if end of quiz, store "complete" and its result
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(mQuiz.getTitle(), true);//write the completed status to sharedPreference
            editor.putString(mQuiz.getTitle() + "_score", mQuiz.getResult(mContext));
            editor.commit();
            String pastResult = mSharedPreferences.getString(mQuiz.getTitle() + "pastResults", "");
            String concatPrevious = pastResult + mSharedPreferences.getString(mQuiz.getTitle() + "_score","0")+ "," ;
            editor.putString(mQuiz.getTitle() + "pastResults", concatPrevious);
            editor.commit();
            finishQuizAlertDialog();
        } else {
            mQuestionIndex ++;
            setQuestion();
        }
    }

    /**
     * Displays a dialog upon completion which shows the result and allows users
     * to either redo or done with this quiz
     */
    private void finishQuizAlertDialog() {
        AlertDialog.Builder finishQuiz = new AlertDialog.Builder(this);
        final AlertDialog alert = finishQuiz.create();
        finishQuiz.setMessage("Your result is: " + mSharedPreferences.getString(mQuiz.getTitle() + "_score","0"));
        finishQuiz.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alert.cancel();
                QuizActivity.this.finish();
            }
        });
        finishQuiz.setNegativeButton("Redo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(sRESULT, "0");
                editor.putInt(sINDEX, 0);
                editor.putBoolean(mQuiz.getTitle(),false);
                editor.putString(mQuiz.getTitle() + "_score", "0");
                editor.commit();
                alert.cancel();
                QuizActivity.this.finish();
            }
        });
        finishQuiz.show();
    }
}