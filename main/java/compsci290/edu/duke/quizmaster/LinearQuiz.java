package compsci290.edu.duke.quizmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by will on 2/11/2018.
 * A Linear Quiz is a quiz that displays a numerical or letter score at the end of the quiz.
 * It implements Quiz and Parcelable interface.
 */

public class LinearQuiz implements Quiz, Parcelable {
    private String name;
    private String genre;
    private Question[] questions;
    private List<Question> qs;
    private int score;
    private static final String sINDEX = "CURRENTINDEX";

    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(genre);
        dest.writeTypedList(qs);
        dest.writeInt(score);
        System.out.println("WritetoParcel" + qs.get(0));
    }

    public static final Parcelable.Creator<LinearQuiz> CREATOR = new Parcelable.Creator<LinearQuiz>() {
        public LinearQuiz createFromParcel(Parcel in) {
            return new LinearQuiz(in);
        }
        public LinearQuiz[] newArray(int size) {
            return new LinearQuiz[size];
        }
    };

    public LinearQuiz(Parcel in) {
        name = in.readString();
        genre = in.readString();
        qs = new ArrayList<>();
        in.readTypedList(qs, Question.CREATOR);
        questions = new Question[qs.size()];
        questions = qs.toArray(questions);
        score = in.readInt();
    }

    public LinearQuiz(String name, String genre, Question[] questions) {
        this.name = name;
        this.genre = genre;
        this.questions = questions;
        qs = Arrays.asList(questions);
        score = 0;
    }

    public String getTitle() {
        return name;
    }

    public Question getQuestion(int index) {
        if (0 <= index && index < questions.length){
            return questions[index];
        }
        throw new IndexOutOfBoundsException("bad index "+index);
    }

    @Override

    public void update(String qtitle, String answer, int questionIndex, Context c) {
        SharedPreferences sp = c.getSharedPreferences("DATA", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(sINDEX, questionIndex);
        if (answer.equals(getQuestion(questionIndex).getCorrectAnswer())) {
            score = sp.getInt(qtitle + "Score",0) + 1;
        } else {
            score = sp.getInt(qtitle + "Score",0);
        }
        editor.putInt(qtitle + "Score", score);
        editor.commit();
    }

    @Override
    public String getResult(Context c) {
        return String.valueOf(score);
    }

    public int size() {
        return questions.length;
    }

    public String toString() {
        return "Name: " + name + ", genre: " + genre + ", questions: " + questions.length;
    }
}
