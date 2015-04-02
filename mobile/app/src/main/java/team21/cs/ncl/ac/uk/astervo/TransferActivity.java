package team21.cs.ncl.ac.uk.astervo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TransferActivity extends BaseActivity {

    Globals g;

    ConnectionStatus connectionStatus;

    long now;

    ImageButton clickLeft;
    ImageButton clickRight;

    int selectPounds = 0;
    int selectPence = 0;

    private int fromAccID = -1;
    private float transferAmount = -1;
    private int toAccID = -1;

    Button seeAll;
    Button hideExtra;

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
        setupFooterButtons();
        updateTransferHistory(false);

        //Get the Array of accounts
        final JSONArray jsonAccounts = g.getAccounts();

        //Create a String of type list to store the item Strings
        List<String> accounts = new ArrayList<String>();

        //Create a for loop to iterate through each account and pull out the relevant data
        for(int i = 0; i < jsonAccounts.length(); i++) {
            //Try to get account details from each JSON object
            try {
                JSONObject currentAcc = jsonAccounts.getJSONObject(i);
                float balance = currentAcc.getInt(PrivateFields.TAG_BAL);
                String details = currentAcc.getString(PrivateFields.TAG_TYPE) + " Account: £" + String.format("%.2f", balance);
                accounts.add(details);
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

    public void getTransferAmount(int bal) {

        final TextView amount = (TextView) findViewById(R.id.transferAmount);

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAmountPicker(amount);
            }
        });

    }

    public void showAmountPicker(final TextView amount) {

        final Dialog dlg = new Dialog(TransferActivity.this);
        dlg.setTitle("Please enter an amount:");
        dlg.setContentView(R.layout.number_pick_dialog);

        final NumberPicker pounds = (NumberPicker) dlg.findViewById(R.id.pounds);
        final NumberPicker pence = (NumberPicker) dlg.findViewById(R.id.pence);

        pounds.setMaxValue(1000);
        pounds.setValue(selectPounds);
        pence.setMaxValue(99);
        pence.setValue(selectPence);

        pounds.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if(value < 10) {
                    return "0" + value;
                }
                return Integer.toString(value);
            }
        });
        pence.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if(value < 10) {
                    return "0" + value;
                }
                return Integer.toString(value);
            }
        });

        Button okay = (Button) dlg.findViewById(R.id.confirmTransAmount);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPounds = pounds.getValue();
                selectPence = pence.getValue();

                transferAmount = selectPounds;
                transferAmount += ((float) selectPence) / 100;

                amount.setText("£" + String.format("%.2f", transferAmount));

                dlg.dismiss();

            }
        });

        dlg.show();

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
                    float balance = currentAcc.getInt(PrivateFields.TAG_BAL);
                    String details = currentAcc.getString(PrivateFields.TAG_TYPE) + " Account: £" + String.format("%.2f", balance);
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
                params.put(PrivateFields.TAG_TRANS_AMOUNT, Float.toString(transferAmount));
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
                
                final Intent i = new Intent(this, TransferActivity.class);

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

                                        //Display alert
                                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(TransferActivity.this);
                                        dlgAlert.setMessage("Transferred £" + String.format("%.2f", transferAmount));
                                        dlgAlert.setTitle("Transfer successful:");
                                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getTransfers();
                                            }
                                        });
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
                            error = "\u2022 " + errorResponse.getJSONObject(PrivateFields.TAG_TRANS_RESULT).getString(PrivateFields.TAG_MESSAGE);
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

    public void updateTransferHistory(boolean showAll) {

        //Get the Array of accounts
        final JSONArray jsonTransfers = g.getTransfers();

        //Create a String of type list to store the item Strings
        List<String> accounts = new ArrayList<String>();

        //Create a for loop to iterate through each account and pull out the relevant data
        for(int i = jsonTransfers.length(); i > 0; i--) {
            //If show all hasn't been clicked, show only 5 transfers
            if(!showAll && i == jsonTransfers.length() - 6) {
                break;
            }
            //Try to get account details from each JSON object
            try {
                JSONObject currentTrans = jsonTransfers.getJSONObject(i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date transDate = sdf.parse(currentTrans.getString(PrivateFields.TAG_TRANS_DATE));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(transDate);
                String details = "";
                details += "To: " + currentTrans.getString(PrivateFields.TAG_TRANS_SENDER) + "\n";
                details += "From: " + currentTrans.getString(PrivateFields.TAG_TRANS_RECEIVER) + "\n";
                details += "Amount: £" + currentTrans.getString(PrivateFields.TAG_TRANS_AMOUNT) + "\n";
                details += "Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH)+ "-" + calendar.get(Calendar.YEAR);
                accounts.add(details);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        //Create an array adapter to set the List view equal to the information of each account
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((ListView)findViewById(R.id.transferHistoryList)).getContext(), android.R.layout.simple_list_item_1, accounts);
        ListView displayTransHistory = (ListView) findViewById(R.id.transferHistoryList);
        displayTransHistory.setAdapter(adapter);

        //If show all hasn't been clicked add button to end to show all transfers
        if(!showAll) {
            displayTransHistory.removeFooterView(hideExtra);
            displayTransHistory.addFooterView(seeAll);
        }
        //Otherwise display button to hide
        else {
            displayTransHistory.addFooterView(hideExtra);
            displayTransHistory.removeFooterView(seeAll);
        }

    }

    public void setupFooterButtons() {
        seeAll = new Button(getApplicationContext());
        hideExtra = new Button(getApplicationContext());

        seeAll.setText("See all");
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransferHistory(true);
            }
        });

        hideExtra.setText("Hide");
        hideExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransferHistory(false);
            }
        });
    }

    public void getTransfers() {

        //Create an intent to open the Dashboard
        final Intent i = new Intent(this, TransferActivity.class);

        //Start fetching transfer history dialog
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(TransferActivity.this);
        prgDialog.setMessage("Fetching account info...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        //Attempt to return array of transfers
        try {

            //Send the HTTP post request and get JSON object back
            HttpClient.get(this.getApplicationContext(), "/api/transfers", new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    prgDialog.dismiss();

                    //If successful parse JSON data and create dashboard activity
                    if (statusCode == 200) {
                        if (response != null) {
                            try {
                                g.setTransfers(response.getJSONArray(PrivateFields.TAG_TRANS_ARRAY));
                                startActivity(i);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //If authorization error, prompt user to check details
                    else if (statusCode == 401) {
                        Context context = getApplicationContext();
                        CharSequence text = "Unable to fetch account. Please try again.";
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

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}