package team21.cs.ncl.ac.uk.astervo;

import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransferActivity extends BaseActivity {

    Globals g;

    ConnectionStatus connectionStatus;

    long now;

    ImageButton clickLeft;
    ImageButton clickRight;

    private int fromAccID = -1;
    private int transferAmount = -1;
    private int toAccID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        connectionStatus = new ConnectionStatus(this);

        g = (Globals) getApplication();

        clickLeft = (ImageButton) findViewById(R.id.transferAccClickLeft);
        clickRight = (ImageButton) findViewById(R.id.transferAccClickRight);

        updateShownAccount();
        createAccountSelectArrows();
        updateTransferHistory();

        //Get the Array of accounts
        final JSONArray jsonAccounts = g.getAccounts();

        //Create a String of type list to store the item Strings
        List<String> accounts = new ArrayList<String>();

        //Create a for loop to iterate through each account and pull out the relevant data
        for(int i = 0; i < jsonAccounts.length(); i++) {
            //Try to get account details from each JSON object
            try {
                JSONObject currentAcc = jsonAccounts.getJSONObject(i);
                int balance = currentAcc.getInt(PrivateFields.TAG_BAL);
//                if(balance > 0) {
                String details = currentAcc.getString(PrivateFields.TAG_TYPE) + " Account: £" + balance;
                accounts.add(details);
//                }
//                else {
//                    notDisplayed ++;
//                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        //Create an array adapter to set the Spinner view equal to the information of each account
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((Spinner)findViewById(R.id.transferFrom)).getContext(), android.R.layout.simple_spinner_item, accounts);
        Spinner displayFromAcc = (Spinner) findViewById(R.id.transferFrom);
        displayFromAcc.setAdapter(adapter);

        //Set on click listener for each item
        displayFromAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int accBal = -1;

                try {
                    fromAccID = jsonAccounts.getJSONObject(position).getInt(PrivateFields.TAG_ACC_ID);
                    accBal = jsonAccounts.getJSONObject(position).getInt(PrivateFields.TAG_BAL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getTransferAmount(accBal);
                getToAccount(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        prgDialog = new ProgressDialog(TransferActivity.this);
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

    public void getTransferAmount(int bal) {

        final SeekBar amount = (SeekBar) findViewById(R.id.transferAmount);
        final TextView displayAmount = (TextView) findViewById(R.id.transferDisplayAmount);

        amount.setMax(bal);

        if(bal == 0) {
            amount.setEnabled(false);
        }
        else {
            amount.setEnabled(true);
        }

        amount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displayAmount.setText("£" + (int) amount.getProgress());
                transferAmount = (int) amount.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                displayAmount.setText("£" + (int) amount.getProgress());
                transferAmount = (int) amount.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                displayAmount.setText("£" + (int) amount.getProgress());
                transferAmount = (int) amount.getProgress();
            }
        });

    }

    public void getToAccount(final int pos) {

        //Get the Array of accounts
        final JSONArray jsonAccounts = g.getAccounts();

        //Create a String of type list to store the item Strings
        List<String> accounts = new ArrayList<String>();

        //Create a for loop to iterate through each account and pull out the relevant data
        for(int i = 0; i < jsonAccounts.length(); i++) {
            //Try to get account details from each JSON object
            try {
                //If i == position then the current account is the one that is being transferred from
                if(i == pos) {
                    //DO NOTHING
                }
                else {
                    JSONObject currentAcc = jsonAccounts.getJSONObject(i);
                    int balance = currentAcc.getInt(PrivateFields.TAG_BAL);
                    String details = currentAcc.getString(PrivateFields.TAG_TYPE) + " Account: £" + balance;
                    accounts.add(details);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        //Create an array adapter to set the Spinner view equal to the information of each account
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((Spinner)findViewById(R.id.transferTo)).getContext(), android.R.layout.simple_spinner_item, accounts);
        Spinner displayToAcc = (Spinner) findViewById(R.id.transferTo);
        displayToAcc.setAdapter(adapter);


        //Set on click listener for each item
        displayToAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int accPos = position;
                    if(position >= pos) {
                        accPos ++;
                    }
                    toAccID = jsonAccounts.getJSONObject(accPos).getInt(PrivateFields.TAG_ACC_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void submit(View v) {

        if(fromAccID == -1 || (toAccID == -1 || transferAmount == -1)) {

            Context context = getApplicationContext();
            CharSequence text = "Please confirm your choices of account and transfer amount.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if(transferAmount == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Transfer amount can not be £0.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            transfer();
        }

    }

    private void transfer() {

        //If phone has data, then attempt login
        if(connectionStatus.isConnected()) {

            //Create a new JSON object to store the params
            JSONObject params = new JSONObject();
            JSONObject jsonTransfer = new JSONObject();
            //Try to add the email and password text in as params
            try {
                jsonTransfer.put(PrivateFields.TAG_TRANS_TO, Integer.toString(toAccID));
                jsonTransfer.put(PrivateFields.TAG_TRANS_FROM, Integer.toString(fromAccID));
                params.put(PrivateFields.TAG_TRANS_AMOUNT, Integer.toString(transferAmount));
                params.put(PrivateFields.TAG_TRANS, jsonTransfer);
            } catch(JSONException e) {
                e.printStackTrace();
            }

            //Start transfer dialog
            final ProgressDialog prgDialog;
            prgDialog = new ProgressDialog(TransferActivity.this);
            prgDialog.setMessage("Completing transaction...");
            prgDialog.setCancelable(false);
            prgDialog.show();

            //Attempt transfer
            try {

                //Send the HTTP post request and get JSON object back
                HttpClient.post(this.getApplicationContext(), "/api/transfers", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        prgDialog.dismiss();

                        //If successful parse JSON update account details, and return to dashboard
                        if (statusCode == 200) {
                            if (response != null) {
                                try {
                                    JSONObject result = response.getJSONObject(PrivateFields.TAG_TRANS_RESULT);
                                    //Check the return messages for success
                                    if(result.getString(PrivateFields.TAG_SUCCESS).equals("true")) {

                                        g.setAccounts(response.getJSONArray(PrivateFields.TAG_ACC));

                                        finish();

                                        //Display alert
                                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(TransferActivity.this);
                                        dlgAlert.setMessage("Transferred £" + transferAmount + ".00");
                                        dlgAlert.setTitle("Transfer successful:");
                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.create().show();
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
                        } catch (Exception e) {
                            e.printStackTrace();
                            error = "Please Try Again.";
                        }

                        //Display alert
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(TransferActivity.this);
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
            CharSequence text = "Transfer requires an internet connection.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void updateShownAccount() {

        TextView type = (TextView) findViewById(R.id.transferAccType);
        TextView interest = (TextView) findViewById(R.id.transferAccInt);
        TextView balance = (TextView) findViewById(R.id.transferAccBal);

        try {
            JSONObject account = g.getAccounts().getJSONObject(g.getSingleAccountLocation());
            type.setText("Account Type: " + account.getString(PrivateFields.TAG_TYPE));
            interest.setText("Interest: " + account.getString(PrivateFields.TAG_INTEREST) + "%");
            balance.setText("Balance: £" + account.getString(PrivateFields.TAG_BAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(g.getSingleAccountLocation() > 0) {
            clickLeft.setVisibility(View.VISIBLE);
        }
        if(g.getSingleAccountLocation() == 0) {
            clickLeft.setVisibility(View.INVISIBLE);
        }
        if(g.getSingleAccountLocation() == g.getAccounts().length() - 1) {
            clickRight.setVisibility(View.INVISIBLE);
        }
        if(g.getSingleAccountLocation() < g.getAccounts().length() - 1) {
            clickRight.setVisibility(View.VISIBLE);
        }

    }

    public void createAccountSelectArrows() {

        clickLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.setSingleAccountLocation(g.getSingleAccountLocation()-1);
                updateShownAccount();
            }
        });

        clickRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.setSingleAccountLocation(g.getSingleAccountLocation()+1);
                updateShownAccount();
            }
        });

        if(g.getSingleAccountLocation() > 0) {
            clickLeft.setVisibility(View.VISIBLE);
        }
        if(g.getSingleAccountLocation() == 0) {
            clickLeft.setVisibility(View.INVISIBLE);
        }
        if(g.getSingleAccountLocation() == g.getAccounts().length() - 1) {
            clickRight.setVisibility(View.INVISIBLE);
        }
        if(g.getSingleAccountLocation() < g.getAccounts().length() - 1) {
            clickRight.setVisibility(View.VISIBLE);
        }

    }

    public void updateTransferHistory() {

        //Get the Array of accounts
        final JSONArray jsonTransfers = g.getTransfers();

        //Create a String of type list to store the item Strings
        List<String> accounts = new ArrayList<String>();

        //Create a for loop to iterate through each account and pull out the relevant data
        for(int i = 0; i < jsonTransfers.length(); i++) {
            //Try to get account details from each JSON object
            try {
                JSONObject currentTrans = jsonTransfers.getJSONObject(i);
                String details = "";
                details += "To: " + currentTrans.getString(PrivateFields.TAG_TRANS_SENDER) + "\n";
                details += "From: " + currentTrans.getString(PrivateFields.TAG_TRANS_RECEIVER) + "\n";
                details += "Amount: " + currentTrans.getString(PrivateFields.TAG_TRANS_AMOUNT) + "\n";
                details += "Date: " + currentTrans.getString(PrivateFields.TAG_TRANS_DATE);
                accounts.add(details);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        //Create an array adapter to set the List view equal to the information of each account
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((ListView)findViewById(R.id.transferHistoryList)).getContext(), android.R.layout.simple_list_item_1, accounts);
        ListView displayTransHistory = (ListView) findViewById(R.id.transferHistoryList);
        displayTransHistory.setAdapter(adapter);

    }
}