package team21.cs.ncl.ac.uk.astervo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;


public class QuizActivity extends BaseActivity {

    ConnectionStatus connectionStatus;

    Globals g;

    long now;

    JSONArray questions;

    int currentQuestion = 0;

    JSONArray answers = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        g = (Globals) getApplication();

        connectionStatus = new ConnectionStatus(getApplicationContext());

        getQuiz();
        setupButtons();
    }

    private void getQuiz() {

        //Start fetching questions
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(QuizActivity.this);
        prgDialog.setMessage("Fetching questions...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        //Attempt to return array of questions
        try {

            //Send the HTTP get request and get JSON object back
            HttpClient.get(this.getApplicationContext(), "/api/questions", new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    prgDialog.dismiss();

                    //If successful parse JSON data for questions and answers
                    if (statusCode == 200) {
                        if (response != null) {
                            try {
                                questions = response.getJSONArray(PrivateFields.TAG_QUESTIONS);
                                askQuestion();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //If authorization error, prompt user to check details
                    else if (statusCode == 401) {
                        Context context = getApplicationContext();
                        CharSequence text = "Unable to fetch questions. Please try again.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    //Otherwise tell user to try again later
                    else {
                        Context context = getApplicationContext();
                        CharSequence text = "Something went wrong. Please try again later.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void askQuestion() {

        String question = null;
        String a = null;
        String b = null;
        String c = null;
        String d = null;

        int i = currentQuestion;

        try {
            question = questions.getJSONObject(i).getString(PrivateFields.TAG_QUESTION);
            JSONArray answers = questions.getJSONObject(i).getJSONArray(PrivateFields.TAG_ANSWERS);
            a = answers.getString(0);
            b = answers.getString(1);
            c = answers.getString(2);
            d = answers.getString(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView questionText = (TextView) findViewById(R.id.quizQuestion);
        TextView answerA = (TextView) findViewById(R.id.quizOptionA);
        TextView answerB = (TextView) findViewById(R.id.quizOptionB);
        TextView answerC = (TextView) findViewById(R.id.quizOptionC);
        TextView answerD = (TextView) findViewById(R.id.quizOptionD);

        if (question != null) {
            questionText.setText(question);
            answerA.setText(a);
            answerB.setText(b);
            answerC.setText(c);
            answerD.setText(d);
        }

    }

    public void setupButtons() {

        RelativeLayout a = (RelativeLayout) findViewById(R.id.quizAnswerA);
        RelativeLayout b = (RelativeLayout) findViewById(R.id.quizAnswerB);
        RelativeLayout c = (RelativeLayout) findViewById(R.id.quizAnswerC);
        RelativeLayout d = (RelativeLayout) findViewById(R.id.quizAnswerD);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer(0);
                currentQuestion++;
                askQuestion();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer(1);
                currentQuestion++;
                askQuestion();
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer(2);
                currentQuestion++;
                askQuestion();
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer(3);
                currentQuestion++;
                askQuestion();
            }
        });

    }

    public void answer(int i) {

        //Create answer JSON object
        JSONObject jsonAnswer = new JSONObject();

        //Try to add the email and password text in as params
        try {
            JSONArray tempAnswers = questions.getJSONObject(currentQuestion).getJSONArray(PrivateFields.TAG_ANSWERS);

            jsonAnswer.put(PrivateFields.TAG_Q_ID, questions.getJSONObject(currentQuestion).getString(PrivateFields.TAG_Q_ID));
            jsonAnswer.put(PrivateFields.TAG_ANSWER, tempAnswers.getString(i));
            answers.put(currentQuestion, jsonAnswer);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (currentQuestion == 4) {
            JSONObject params = new JSONObject();
            try {
                params.put(PrivateFields.TAG_ANSWERS, answers);
            } catch (Exception e) {
                e.printStackTrace();
            }
            submit(params);
        }

    }

    public void submit(JSONObject params) {

        //If phone has data, then attempt submission
        if (connectionStatus.isConnected()) {

            //Start logging in dialog
            final ProgressDialog prgDialog;
            prgDialog = new ProgressDialog(QuizActivity.this);
            prgDialog.setMessage("Submitting...");
            prgDialog.setCancelable(false);
            prgDialog.show();

            //Attempt login
            try {

                //Send the HTTP post request and get JSON object back
                HttpClient.post(this.getApplicationContext(), "/api/answers", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        prgDialog.dismiss();

                        //If successful parse JSON data and create dashboard activity
                        if (statusCode == 200) {
                            if (response != null) {
                                try {

                                    //Display alert
                                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(QuizActivity.this);
                                    dlgAlert.setTitle("Thanks for playing!");
                                    dlgAlert.setMessage("You scored: " + response.getString(PrivateFields.TAG_SCORE) + "/700");
                                    dlgAlert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(QuizActivity.this, DashActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    dlgAlert.setNegativeButton("Replay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(QuizActivity.this, QuizActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    dlgAlert.create().show();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //If authorization error, prompt user to check details
                        else if (statusCode == 401) {
                            Context context = getApplicationContext();
                            CharSequence text = "Submission failed. Check your details and try again.";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        //Otherwise tell user to try again later
                        else {
                            Context context = getApplicationContext();
                            CharSequence text = "Something went wrong. Please try again later.";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }

                    //If post request fails, check status code
                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {

                        prgDialog.dismiss();

                        //Create error String
                        String error = "";

                        try {
                            error = "\u2022 " + errorResponse.getString(PrivateFields.TAG_ERROR);
                        } catch (Exception e) {
                            e.printStackTrace();
                            error = "Please Try Again.";
                        }

                        //Display alert
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(QuizActivity.this);
                        dlgAlert.setMessage(error);
                        dlgAlert.setTitle("Something went wrong:");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.create().show();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //If no internet connection, prompt user to check connection
        else {
            Context context = getApplicationContext();
            CharSequence text = "Login requires an internet connection.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}
