package compsci290.edu.duke.quizmaster;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by will on 2/7/2018.
 * The Quiz interface is implemented by LinearQuiz and PersonalityQuiz.
 * It contains methods of updating the quiz data and getting results of the quiz.
 */

public interface Quiz {
    int describeContents();

    void writeToParcel(Parcel dest, int flags);

    String toString();

    String getTitle();

    int size();

    Question getQuestion(int questionIndex);

    /**
     * Updates data of a specific question in a specific quiz by storing user's choice
     * @param qtitle
     * @param choice
     * @param questionIndex
     * @param c
     */
    void update(String qtitle, String choice, int questionIndex, Context c);

    /**
     * Gets the result of the current quiz if the user finishes the quiz.
     * For linear quizzes, the result is a number.
     * For personality quizzes, the result is usually a description string.
     * @param c
     * @return String
     */
    String getResult(Context c);
}

