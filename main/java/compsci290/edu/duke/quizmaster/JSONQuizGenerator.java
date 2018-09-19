package compsci290.edu.duke.quizmaster;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Melissa on 2/10/18.
 * This class contains methods that reads all from assets and prepares json string ready to be parsed
 */

public class JSONQuizGenerator {
    private static Map<String,Quiz> ourQuizzes;
    private static final String sQUIZDIR = "quizzes";

    static {
        ourQuizzes = new HashMap<>();
    }

    /**
     *
     * @param context
     * @param quizfile
     * @return a json string that corresponds to the specific quizfile
     */
    private static String loadQuiz(Context context, String quizfile) {
        String jstring = null;
        try {
            InputStream is = context.getAssets().open(sQUIZDIR + "/" + quizfile);
            int length = is.available();
            byte[] data = new byte[length];
            is.read(data);
            is.close();
            jstring = new String(data,"UTF-8");
            Quiz q = (new JSONParser().parse(jstring));
            ourQuizzes.put(q.getTitle(), q);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jstring;
    }

    /**
     * Read in quizzes from the asset directory and create quizzes for each file found
     * @param context
     */
    public static void loadAllQuizzes(Context context){
        try {
            String[] quizfiles = context.getResources().getAssets().list(sQUIZDIR);
            System.out.println("show all quizzes"+ quizfiles[1]);
            for (String quizfile: quizfiles) {
                Log.d("quizmaster", "json.createQuizzes" + quizfile);
                loadQuiz(context, quizfile);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public static Quiz[] getAllQuizzes() {
        return ourQuizzes.values().toArray(new Quiz[0]);
    }
    private JSONQuizGenerator(){}
}