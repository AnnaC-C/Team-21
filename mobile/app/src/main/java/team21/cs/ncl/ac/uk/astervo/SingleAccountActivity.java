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


public class SingleAccountActivity extends ActionBarActivity {

    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_account);

        //Get text fields
        TextView type = (TextView) findViewById(R.id.singleAccType);
        TextView bal = (TextView) findViewById(R.id.singleAccBal);
        TextView interest = (TextView) findViewById(R.id.singleAccInt);

        //Set text fields to that of global single account
        g = (Globals) getApplication();

        //Get the account details
        JSONObject thisAcc = g.getSingleAccount();

        try {
            type.setText("Account Type: " + thisAcc.getString(PrivateFields.TAG_TYPE));
            bal.setText("Balance: £" + thisAcc.getString(PrivateFields.TAG_BAL));
            interest.setText("Interest: " + thisAcc.getString(PrivateFields.TAG_INTEREST) + "%");
        } catch(JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_account, menu);
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
        prgDialog = new ProgressDialog(SingleAccountActivity.this);
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