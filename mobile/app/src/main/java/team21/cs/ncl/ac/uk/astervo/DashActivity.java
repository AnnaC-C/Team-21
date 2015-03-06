package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
    public void buttonOnClick1(View v) {
        Button button=(Button) v;
        startActivity(new Intent(getApplication(),TransferActivity.class));
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

    public void update() {
        //Get globals
        g = (Globals) getApplication();

        //Get the Array of accounts
        final JSONArray jsonAccounts = g.getAccounts();

        //Create a String of type list to store the item Strings
        List<String> accounts = new ArrayList<String>();

        //Create a for loop to iterate through each account and pull out the relevant data
        for(int i = 0; i < jsonAccounts.length(); i++) {
            //Try to get account details from each JSON object
            try {
                JSONObject currentAcc = jsonAccounts.getJSONObject(i);
                String details = "";
                details += "Account Type: " + currentAcc.getString(PrivateFields.TAG_TYPE) + "\n";
                details += "Balance: £" + currentAcc.getString(PrivateFields.TAG_BAL) + "\n";
                details += "Interest Rate: " + currentAcc.getString(PrivateFields.TAG_INTEREST) + "%";
                accounts.add(details);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        //Create an array adapter to set the List view equal to the information of each account
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((ListView)findViewById(R.id.accListView)).getContext(), android.R.layout.simple_list_item_1, accounts);
        ListView displayAcc = (ListView) findViewById(R.id.accListView);
        displayAcc.setAdapter(adapter);

        //Set on click listener for each item
        displayAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                try {
                    g.setSingleAccount(jsonAccounts.getJSONObject(position));
                    Intent i = new Intent(getApplicationContext(), SingleAccountActivity.class);
                    startActivity(i);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void transfer(View view) {

        Intent i = new Intent(getApplicationContext(), TransferActivity.class);
        startActivity(i);

    }

}