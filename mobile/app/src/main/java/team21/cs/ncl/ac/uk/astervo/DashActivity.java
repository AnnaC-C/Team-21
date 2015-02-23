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

        //Update fields every time activity is started
        update();

    }

    @Override
    public void onResume() {
        super.onResume();

        //Update fields every time activity is resumed
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

        //Create a progress dialog to show the app logging out
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(DashActivity.this);
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
                } catch(JSONException e) {
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

    public void update() {
        //Get globals
        g = (Globals) getApplication();

        //Get bundled extras from previous activity
        Bundle extras = getIntent().getExtras();

        //Display global variables
        TextView displayAuth = (TextView) findViewById(R.id.dashAuth);
        displayAuth.setText("Authorization: " + g.getAuthKey());
    }

}
