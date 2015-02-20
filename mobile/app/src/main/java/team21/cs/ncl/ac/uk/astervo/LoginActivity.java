package team21.cs.ncl.ac.uk.astervo;

import android.content.Context;
import android.content.Intent;
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

    ConnectionStatus connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectionStatus = new ConnectionStatus(this);
    }


    @Override
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
        final Intent i = new Intent(this, DashActivity.class);

        //Get the text in for email field and password as Strings
        EditText emailIn =  (EditText) findViewById(R.id.loginEmail);
        EditText passIn = (EditText) findViewById(R.id.loginPass);
        String email = emailIn.getText().toString();
        String pass = passIn.getText().toString();

        //If email or password is not entered, prompt user
        if(email.equals("") || pass.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter username and password.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        else {

            if(connectionStatus.isConnected()) {

                final TextView alert = (TextView) findViewById(R.id.loginAlert);

                JSONObject params = new JSONObject();
                JSONObject jsonUser = new JSONObject();
                try {
                    jsonUser.put(PrivateFields.TAG_EMAIL, email);
                    jsonUser.put(PrivateFields.TAG_PASS, pass);
                    params.put(PrivateFields.TAG_USER, jsonUser);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                alert.setText(params.toString());

                try {

                    HttpClient.post(this.getApplicationContext(), "/api/sessions", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                if (response != null) {
                                    try {
                                        alert.setText(response.toString());
                                        JSONObject data = (JSONObject) response.get(PrivateFields.TAG_DATA);
                                        i.putExtra(PrivateFields.TAG_AUTH, data.getString(PrivateFields.TAG_AUTH));
                                        i.putExtra(PrivateFields.TAG_SUCCESS, response.getString(PrivateFields.TAG_SUCCESS));
                                        i.putExtra(PrivateFields.TAG_INFO, response.getString(PrivateFields.TAG_INFO));
                                        startActivity(i);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                alert.setText("Please try again.");
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                            if(statusCode == 401) {
                                Context context = getApplicationContext();
                                CharSequence text = "Login failed. Check your details and try again.";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
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
            else {
                Context context = getApplicationContext();
                CharSequence text = "Login requires an internet connection.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    }

    public void signUp(View view) {

        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }

}