package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
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

    Globals g;

    long now;

    JSONArray questions;

    int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        g = (Globals) getApplication();

        getQuiz();
        setupButtons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_home:
                now = new Date().getTime();
                if (now - lastActivity > TIMEOUT_MILLI) {
                    inactiveLogout();
                }
                else {
                    Intent dashIntent = new Intent(this, DashActivity.class);
                    startActivity(dashIntent);
                    finish();
                }
                return true;
            case R.id.action_accounts:
                now = new Date().getTime();
                if (now - lastActivity > TIMEOUT_MILLI) {
                    inactiveLogout();
                }
                else {
                    Intent accountIntent = new Intent(this, TransferActivity.class);
                    startActivity(accountIntent);
                    finish();
                }
                return true;
            case R.id.action_quiz:
                now = new Date().getTime();
                if (now - lastActivity > TIMEOUT_MILLI) {
                    inactiveLogout();
                }
                else {
                    Intent quizIntent = new Intent(this, QuizActivity.class);
                    startActivity(quizIntent);
                    finish();
                }
                return true;
            case R.id.action_pet:
                now = new Date().getTime();
                if (now - lastActivity > TIMEOUT_MILLI) {
                    inactiveLogout();
                }
                else {
                    Intent petIntent = new Intent(this, PetActivity.class);
                    startActivity(petIntent);
                    finish();
                }
                return true;
            case R.id.action_shop:
                now = new Date().getTime();
                if (now - lastActivity > TIMEOUT_MILLI) {
                    inactiveLogout();
                }
                else {
                    Intent shopIntent = new Intent(this, ShopActivity.class);
                    startActivity(shopIntent);
                    finish();
                }
                return true;
            case R.id.action_rewards:
                now = new Date().getTime();
                if (now - lastActivity > TIMEOUT_MILLI) {
                    inactiveLogout();
                }
                else {
                    Intent rewardIntent = new Intent(this, RewardsActivity.class);
                    startActivity(rewardIntent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {

        //Create a progress dialog to show the app logging out
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(QuizActivity.this);
        prgDialog.setMessage("Logging out...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        //Create an intent to return back to the initial activity
        final Intent i = new Intent(this, MainActivity.class);

        //Send the logout request to the server
        HttpClient.delete(g.getAuthKey(), "/api/sessions", new JsonHttpResponseHandler() {

            //If successful
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                //Dismiss the loading dialog
                prgDialog.dismiss();
                try {
                    //Check the status code
                    if (statusCode == 200 && response.getString(PrivateFields.TAG_INFO).equals("Logged out")) {

                        //If logged out, show logged out toast
                        Context context = getApplicationContext();
                        CharSequence text = response.getString(PrivateFields.TAG_INFO);
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        //Set global variables to initial state
                        g.reset();

                        //Return to first activity
                        startActivity(i);
                        //Finish this activity
                        finish();
                    }
                    //Something went wrong, prompt user to try again.
                    else {
                        Context context = getApplicationContext();
                        CharSequence text = "Logout failed. Please try again.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Something went wrong, prompt user to try again
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                prgDialog.dismiss();
                Context context = getApplicationContext();
                CharSequence text = "Logout failed. Please try again.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        });

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

            //Send the HTTP post request and get JSON object back
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

        Toast.makeText(getApplicationContext(), "HERE", Toast.LENGTH_LONG).show();

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

            if(question != null) {
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

        Toast.makeText(getApplicationContext(), Integer.toString(i + 1), Toast.LENGTH_LONG).show();

    }

}
