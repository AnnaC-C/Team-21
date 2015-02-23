package team21.cs.ncl.ac.uk.astervo;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {

        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(LoginActivity.this);
        prgDialog.setMessage("Logging in...");
        prgDialog.setCancelable(false);
        prgDialog.show();

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
                                        //Parse the data object
                                        JSONObject data = (JSONObject) response.get(PrivateFields.TAG_DATA);
                                        //Set global variables
                                        g.setLoggedIn(true);
                                        g.setAuthKey(data.getString(PrivateFields.TAG_AUTH));

                                        i.putExtra(PrivateFields.TAG_SUCCESS, response.getString(PrivateFields.TAG_SUCCESS));
                                        i.putExtra(PrivateFields.TAG_INFO, response.getString(PrivateFields.TAG_INFO));
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

    //If user wishes to sign up for an account, prompt them and take them to the online sign up page
    public void signUp(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Please sign up using your PC or Mobile browser.";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://astervo.herokuapp.com/users/sign_up"));
        startActivity(browserIntent);

    }

}