package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class DashActivity extends ActionBarActivity {

    //Get global variables
    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        update();

    }

    @Override
    public void onResume() {
        super.onResume();

        update();

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {

        final Intent i = new Intent(this, MainActivity.class);

        HttpClient.delete(g.getAuthKey(), "/api/sessions", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                try {
                    if (statusCode == 200 && response.getString(PrivateFields.TAG_INFO).equals("Logged out")) {

                        Context context = getApplicationContext();
                        CharSequence text = response.getString(PrivateFields.TAG_INFO);
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        g.setLoggedIn(false);
                        g.setAuthKey(null);

                        startActivity(i);
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Please try again.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Context context = getApplicationContext();
                CharSequence text = "Please try again.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        });

    }

    public void update() {
        //Get globals
        g = (Globals) getApplication();

        //Get bundled extras from previous activity
        Bundle extras = getIntent().getExtras();

        //Display global variables
        TextView displayAuth = (TextView) findViewById(R.id.dashAuth);
        displayAuth.setText("Authorization: " + extras.getString(PrivateFields.TAG_AUTH));

        //If there are extras, then display them
        if(extras != null) {

            TextView displaySuccess = (TextView) findViewById(R.id.dashSuccess);
            TextView displayInfo = (TextView) findViewById(R.id.dashInfo);
            displaySuccess.setText("Success: " + extras.getString(PrivateFields.TAG_SUCCESS));
            displayInfo.setText("Info: " + extras.getString(PrivateFields.TAG_INFO));

        }
    }

}
