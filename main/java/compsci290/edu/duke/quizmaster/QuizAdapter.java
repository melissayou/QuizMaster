package compsci290.edu.duke.quizmaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by Melissa on 2/8/18.
 * The quiz adapter correctly draws the quiz title, itscompleteness, start button and past result button.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private Context mContext;
    private Quiz[] mQuizzes;
    private SharedPreferences mSharedPreference;
    private static final String sCOMPLETE = "COMPLETE";
    private static final String sIMCOMPLETE = "INCOMPLETE";
    public static final String sOKAY = "Okay";


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView mQuizTitle;
        TextView mQuizComplete;
        Button mButton;
        Button mResults;
        public ViewHolder(View itemView) {
            super(itemView);
            this.mLinearLayout = itemView.findViewById(R.id.quiz_main_page_linear_layout) ;
            this.mQuizTitle = itemView.findViewById(R.id.quiz_title_text_view);
            this.mQuizComplete = itemView.findViewById(R.id.complete_text_view);
            this.mButton = itemView.findViewById(R.id.start_quiz_button);
            this.mResults = itemView.findViewById(R.id.previous_result_button);
        }
    }

    public QuizAdapter(final Context context, Quiz[] quizzes) {
        this.mContext = context;
        mSharedPreference = mContext.getSharedPreferences("DATA", 0);
        //if you tried too many and want to clear the msharedpreference:
        //mSharedPreference.edit().clear().commit();
        this.mQuizzes = quizzes;
    }

    public int getItemCount(){
        return mQuizzes.length;
    }

    public QuizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = mInflator.inflate(R.layout.quiz_holder, parent, false);
        final ViewHolder quizHolder = new ViewHolder(row);
        quizHolder.mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startQuiz(mQuizzes[quizHolder.getAdapterPosition()]);
            }
        });
        quizHolder.mResults.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //display all previous scores
                Quiz q = mQuizzes[quizHolder.getAdapterPosition()];
                loadPreviousResults(q);
            }
        });
        return quizHolder;
    }

    private void loadPreviousResults(Quiz q) {
        String qtitle = q.getTitle();
        AlertDialog.Builder pastQuiz = new AlertDialog.Builder(mContext);
        final AlertDialog alert = pastQuiz.create();
        String preResult =  mSharedPreference.getString(qtitle + "pastResults","NO pastt");
        preResult = preResult.substring(0, preResult.length() - 1);
        pastQuiz.setMessage("Your past results are: " + preResult);
        pastQuiz.setPositiveButton(sOKAY, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alert.cancel();
            }
        });
        pastQuiz.show();
    }

    private void startQuiz(Quiz quizObject) {
        Intent intent = new Intent(mContext, QuizActivity.class);
        intent.putExtra("quiz_object_key", (Parcelable) quizObject);
        mContext.startActivity(intent);
    }

     public void onBindViewHolder(QuizAdapter.ViewHolder holder,int position) {
        String quizTitle = mQuizzes[position].getTitle();
        holder.mQuizTitle.setText(quizTitle);
        if (mSharedPreference.getBoolean(quizTitle, false)) { //if it is completed
            holder.mQuizComplete.setText(sCOMPLETE);
            holder.mQuizComplete.setTextColor(Color.GREEN);
        } else {
            holder.mQuizComplete.setText(sIMCOMPLETE);
            holder.mQuizComplete.setTextColor(Color.RED);
        }
        holder.mButton.setText("Start");
    }
}
