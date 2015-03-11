package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.Date;


public class RewardsActivity extends BaseActivity {

    Globals g;

    long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        g = (Globals) getApplication();
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
        prgDialog = new ProgressDialog(RewardsActivity.this);
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
}
