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
 * Personality Quiz is a quiz that gives you a personal result depending on users' inputs.
 * It implements Quiz and Parcelable interface.
 */

public class PersonalityQuiz implements Quiz, Parcelable {
    private String name;
    private String genre;
    private Question[] questions;
    private String[] results;
    private List<Question> qs;
    private String[] response;

    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(genre);
        dest.writeTypedList(qs);
        dest.writeStringArray(results);
        System.out.println("WritetoParcel" + qs.get(0));
    }

    public static final Parcelable.Creator<PersonalityQuiz> CREATOR = new Parcelable.Creator<PersonalityQuiz>() {
        public PersonalityQuiz createFromParcel(Parcel in) {
            return new PersonalityQuiz(in);
        }

        public PersonalityQuiz[] newArray(int size) {
            return new PersonalityQuiz[size];
        }
    };

    private PersonalityQuiz(Parcel in) {
        name = in.readString();
        genre = in.readString();
        qs = new ArrayList<>();
        in.readTypedList(qs, Question.CREATOR);
        questions = new Question[qs.size()];
        questions = qs.toArray(questions);
        results = in.createStringArray();
    }

    public PersonalityQuiz(String name, String genre, Question[] questions, String[] results) {
        this.name = name;
        this.genre = genre;
        this.questions = questions;
        this.results = results;
        qs = Arrays.asList(questions);
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
        String qKey = qtitle + "_" + questionIndex;
        editor.putString(qKey, answer); //ex: { olaquiz_1: 3}
        editor.commit();
    }

    @Override
    public String getResult(Context c) {
        response = new String[this.size()];
        for (int i = 0; i < response.length; i++) {
            SharedPreferences sp = c.getSharedPreferences("DATA", 0);
            String chosen = sp.getString(name + "_" + String.valueOf(i), "DIDNOTCHOOSE");
            response[i] = chosen;
        }
        int[] resIndex = new int[response.length]; //the array containing the index of each answer chosen
        for (int i = 0; i < this.size(); i++) {
            Question q = questions[i];
            for (int j = 0; j < q.answers.length; j++) {
                if (q.getAnswerByIndex(j).equals(response[i])) {
                    resIndex[i] = j;
                }
            }
        }
        int[] category = new int[results.length]; //the array of the score of each category in the results
        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < resIndex.length; j++) {
                if (resIndex[j] == i) {
                    category[i] ++;
                }
            }
        }
        int scoreIndex = 0;
        for (int i = 1; i < category.length; i++) {
            if (category[i] > category[scoreIndex]) {
                scoreIndex = i;
            }
        }
        return String.valueOf(results[scoreIndex]);
    }

    public int size() {
        return questions.length;
    }

    public String toString() {
        return "Name: " + name + ", genre: " + genre + ", questions: " + questions.length;
    }
}
