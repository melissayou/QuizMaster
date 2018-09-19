package compsci290.edu.duke.quizmaster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by will on 2/7/2018.
 * A Question has a prompt and a list of choices for users to choose from.
 */

public class Question implements Parcelable{
    String prompt;
    String rightAnswer;
    String[] answers;

    public Question(String prompt, String[] answers, String rightAnswer) { // for linear quiz
        this.prompt = prompt;
        this.rightAnswer = rightAnswer;
        this.answers = answers;
    }

    public Question(String prompt, String[] answers) { // for personality quiz
        this.prompt = prompt;
        this.answers = answers;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getCorrectAnswer() {
        return rightAnswer;
    }

    public int getAnswerIndex(String answer) {
        for (int i = 0; i < answers.length; i++) {
            if (answer.equals(answers[i])) {
                return i;
            }
        }
        return -1; // error
    }

    public String[] getAnswers() {
        return answers;
    }
    public String getAnswerByIndex(int i) {
        return answers[i];
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prompt);
        dest.writeString(rightAnswer);
        dest.writeStringArray(answers);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) { return new Question[size];}
    };

    private Question(Parcel in) {
        prompt = in.readString();
        rightAnswer = in.readString();
        answers = new String[4];
        in.readStringArray(answers);
    }

    @Override
    public boolean equals(Object question) {
        Question q = (Question) question;
        return q.rightAnswer.equals(rightAnswer) && q.prompt.equals(prompt) && Arrays.equals(answers, q.answers);
    }

}
