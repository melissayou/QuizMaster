package compsci290.edu.duke.quizmaster;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 2/7/2018.
 *
 * This class takes the data in as a string and returns a corresponding Quiz(Linear or Personality) object
 */

public class JSONParser {
    /**
     * Parses a json string to a Quiz object
     * @param jString
     * @return Quiz
     */
    public static Quiz parse(String jString) {
        try {
            JSONObject all = new JSONObject(jString);
            String qtitle = all.getString("title");
            String qgenre = all.getString("genre");
            JSONArray questionArray = all.getJSONArray("questions");
            Question[] questions = new Question[questionArray.length()];
            String type = all.getString("type");

            for (int k = 0; k < questionArray.length(); k++) {
                JSONObject current = questionArray.getJSONObject(k);
                String prompt = current.getString("prompt");
                JSONArray choiceArray = current.getJSONArray("choice");
                String[] choices = new String[choiceArray.length()];
                for (int j = 0; j < choiceArray.length(); j++) {
                    choices[j] = choiceArray.getString(j);
                }
                Question q;
                if (type.equals("linear")) {
                    String correctAnswer = current.getString("answer");
                    q = new Question(prompt, choices, correctAnswer);
                } else {
                    q = new Question(prompt, choices);
                }
                questions[k] = q;
            }
            Quiz qz;
            if (type.equals("linear")) {
                qz = new LinearQuiz(qtitle, qgenre, questions);
            } else {
                JSONArray resultArray = all.getJSONArray("results");
                String[] results = new String[resultArray.length()];
                for (int i = 0; i < resultArray.length(); i++) {
                    results[i] = resultArray.getString(i);
                }
                qz = new PersonalityQuiz(qtitle, qgenre, questions, results);
            }
            return qz;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
