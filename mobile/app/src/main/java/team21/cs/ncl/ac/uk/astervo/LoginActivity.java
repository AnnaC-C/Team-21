package team21.cs.ncl.ac.uk.astervo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        EditText emailIn =  (EditText) findViewById(R.id.loginEmail);
        EditText passIn = (EditText) findViewById(R.id.loginPass);

        String email = emailIn.getText().toString();
        String pass = passIn.getText().toString();

        final Intent i = new Intent(this, DashActivity.class);

        final TextView alert = (TextView) findViewById(R.id.loginAlert);

        RequestParams params = new RequestParams();
        params.put(PrivateFields.TAG_EMAIL, email);
        params.put(PrivateFields.TAG_PASS, pass);
        params.put(PrivateFields.TAG_REMEMBER, 1);
        params.put(PrivateFields.TAG_COMMIT, "Log in");

        HttpClient.post("/users", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                alert.setText(String.valueOf(statusCode));
                if(statusCode == 200) {

                    if(response!=null) {
                        try {
                            String decodedData = new String(response, "UTF-8");
                            alert.setText(String.valueOf(statusCode) + decodedData);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable thrown) {
                alert.setText(String.valueOf(statusCode) + "Failed");

            }

        });

    }

    public void signUp(View view) {

        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }
}