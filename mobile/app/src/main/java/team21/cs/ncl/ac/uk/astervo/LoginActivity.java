package team21.cs.ncl.ac.uk.astervo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {


    //Get connection status
    ConnectionStatus connectionStatus;

    //Create globals
    Globals g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set the connection status
        connectionStatus = new ConnectionStatus(this);

        //Get global variables
        g = (Globals) getApplication();
    }

    public void login(View view) {

        //Create an intent to open the Dashboard
        final Intent i = new Intent(this, DashActivity.class);

        //Get the text in for email field and password as Strings
        EditText emailIn =  (EditText) findViewById(R.id.loginEmail);
        EditText passIn = (EditText) findViewById(R.id.loginPass);
        String email = emailIn.getText().toString();
        String pass = passIn.getText().toString();

        //If email or password is not entered, prompt user with a toast
        if(email.equals("") || pass.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter username and password.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //Otherwise check the connection status
        else {

            //If phone has data, then attempt login
            if(connectionStatus.isConnected()) {

                //Create a new JSON object to store the params
                JSONObject params = new JSONObject();
                JSONObject jsonUser = new JSONObject();
                //Try to add the email and password text in as params
                try {
                    jsonUser.put(PrivateFields.TAG_EMAIL, email);
                    jsonUser.put(PrivateFields.TAG_PASS, pass);
                    params.put(PrivateFields.TAG_USER, jsonUser);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                //Start logging in dialog
                final ProgressDialog prgDialog;
                prgDialog = new ProgressDialog(LoginActivity.this);
                prgDialog.setMessage("Logging in...");
                prgDialog.setCancelable(false);
                prgDialog.show();

                //Attempt login
                try {

                    //Send the HTTP post request and get JSON object back
                    HttpClient.post(this.getApplicationContext(), "/api/sessions", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            prgDialog.dismiss();

                            //If successful parse JSON data and create dashboard activity
                            if (statusCode == 200) {
                                if (response != null) {
                                    try {
                                        //Check the return messages for success
                                        if(response.getString(PrivateFields.TAG_SUCCESS).equals("true") && response.getString(PrivateFields.TAG_INFO).equals("Logged in")) {
                                            //Parse the data object
                                            JSONObject data = (JSONObject) response.get(PrivateFields.TAG_DATA);
                                            //Set global variables
                                            g.setLoggedIn(true);
                                            g.setAuthKey(data.getString(PrivateFields.TAG_AUTH));
                                            g.setAccounts(data.getJSONArray(PrivateFields.TAG_ACC));

                                            startActivity(i);
                                            finish();
                                        }
                                        //If there was an error, tell user to try later
                                        else {
                                            Context context = getApplicationContext();
                                            CharSequence text = "Something went wrong. Please try again later.";
                                            int duration = Toast.LENGTH_LONG;

                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            //If authorization error, prompt user to check details
                            else if(statusCode == 401) {
                                Context context = getApplicationContext();
                                CharSequence text = "Login failed. Check your details and try again.";
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Display alert
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);
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

    //If user wishes to sign up for an account, prompt them and take them to the online sign up page
    public void signUp(View view) {

        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }

}