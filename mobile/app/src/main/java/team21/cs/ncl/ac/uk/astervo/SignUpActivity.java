package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class SignUpActivity extends ActionBarActivity {

    //Get connection status
    ConnectionStatus connectionStatus;

    //Create globals
    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Set the connection status
        connectionStatus = new ConnectionStatus(this);

        //Get global variables
        g = (Globals) getApplication();
    }

    public void signUp(View view) {

        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(SignUpActivity.this);
        prgDialog.setMessage("Signing up...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        //Create an intent to open the Dashboard
        final Intent i = new Intent(this, DashActivity.class);

        //Get the text in for email field and password as Strings
        EditText emailIn = (EditText) findViewById(R.id.signUpEmail);
        EditText passIn = (EditText) findViewById(R.id.signUpPass);
        EditText passCIn = (EditText) findViewById(R.id.signUpPassConf);
        String email = emailIn.getText().toString();
        String pass = passIn.getText().toString();
        String cPass = passCIn.getText().toString();



        //If email or password is not entered, prompt user with a toast
        if((email.equals("") || pass.equals("")) || cPass.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter username and password.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //If email or password is not entered, prompt user with a toast
        if(!pass.equals(cPass)) {
            Context context = getApplicationContext();
            CharSequence text = "Passwords don't match.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //Otherwise check the connection status
        else {

            //If phone has data, then attempt sign up
            if(connectionStatus.isConnected()) {

                //Create a new JSON object to store the params
                JSONObject params = new JSONObject();
                JSONObject jsonUser = new JSONObject();
                //Try to add the email and password text in as params
                try {
                    jsonUser.put(PrivateFields.TAG_EMAIL, email);
                    jsonUser.put(PrivateFields.TAG_PASS, pass);
                    jsonUser.put(PrivateFields.TAG_PASS_CONF, cPass);
                    params.put(PrivateFields.TAG_USER, jsonUser);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                //Attempt login
                try {

                    //Send the HTTP post request and get JSON object back
                    HttpClient.post(this.getApplicationContext(), "/api/registrations", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            //Dismiss the progress dialog
                            prgDialog.dismiss();

                            //If successful parse JSON data and create dashboard activity
                            if (statusCode == 200) {
                                if (response != null) {
                                    try {
                                        //Parse the data object
                                        JSONObject data = (JSONObject) response.get(PrivateFields.TAG_DATA);
                                        //Set global variables
                                        g.setLoggedIn(true);
                                        g.setAuthKey(data.getString(PrivateFields.TAG_AUTH));

                                        startActivity(i);
                                        finish();
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

                            //If authorization error, prompt user ot check details
                            if(statusCode == 401) {
                                Context context = getApplicationContext();
                                CharSequence text = "Login failed. Check your details and try again.";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            //Otherwise prompt user to try again later
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
}
