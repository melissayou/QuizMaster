package compsci290.edu.duke.quizmaster;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
/**
 * Created by Melissa on 2/11/18.
 * The MainActivity class displays a list of all quizzes under the asset and allows users
 * to take a quiz, shows all past results and current completeness of the quiz.
 */

public class MainActivity extends AppCompatActivity {
    private Quiz[] allQuizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("creating quiz...");
        Context c = getApplicationContext();
        JSONQuizGenerator.loadAllQuizzes(c);
        allQuizzes = JSONQuizGenerator.getAllQuizzes();

        RecyclerView rv = findViewById(R.id.activity_main_recycler_view);
        rv.setAdapter(new QuizAdapter(this, allQuizzes));
        rv.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            Toast.makeText(MainActivity.this,
                    "create and saved != null",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        RecyclerView rv = findViewById(R.id.activity_main_recycler_view);
        rv.setAdapter(new QuizAdapter(this, allQuizzes));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
