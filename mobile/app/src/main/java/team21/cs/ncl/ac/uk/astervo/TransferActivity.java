package team21.cs.ncl.ac.uk.astervo;

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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class TransferActivity extends ActionBarActivity {

    Globals g;

    int notDisplayed = 0;

    int fromAccID = -1;
    int transferAmount = -1;
    int toAccID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

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
                int balance = currentAcc.getInt(PrivateFields.TAG_BAL);
                if(balance > 0) {
                    String details = currentAcc.getString(PrivateFields.TAG_TYPE) + " Account: £" + balance;
                    accounts.add(details);
                }
                else {
                    notDisplayed ++;
                }
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
                    fromAccID = jsonAccounts.getJSONObject(position + notDisplayed).getInt(PrivateFields.TAG_ACC_ID);
                    accBal = jsonAccounts.getJSONObject(position + notDisplayed).getInt(PrivateFields.TAG_BAL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getTransferAmount(accBal);
                getToAccount(position + notDisplayed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
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

    public void getTransferAmount(int bal) {

        final SeekBar amount = (SeekBar) findViewById(R.id.transferAmount);
        final TextView displayAmount = (TextView) findViewById(R.id.transferDisplayAmount);

        amount.setMax(bal);

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

    public void getToAccount(int pos) {

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
                    toAccID = jsonAccounts.getJSONObject(position).getInt(PrivateFields.TAG_ACC_ID);
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

    }
}