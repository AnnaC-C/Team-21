package team21.cs.ncl.ac.uk.astervo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DashActivity extends BaseActivity {

    //Get global variables
    Globals g;

    long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        //Update fields every time activity is started
        update();

        //Set onClick listeners for buttons
        setButtons();

    }

    public void buttonOnClick1(View v) {
        Button button=(Button) v;
        startActivity(new Intent(getApplication(), TransferActivity.class));
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

                g.setSingleAccountLocation(position);
                Intent i = new Intent(getApplicationContext(), TransferActivity.class);
                startActivity(i);
            }
        });

        TextView noOfAccounts = (TextView) findViewById(R.id.dashNumberOfAccounts);
        noOfAccounts.setText(Integer.toString(jsonAccounts.length()));

    }

    public void setButtons() {

        RelativeLayout acc = (RelativeLayout) findViewById(R.id.dashAccounts);
        RelativeLayout rewards = (RelativeLayout) findViewById(R.id.dashRewards);
        RelativeLayout pet = (RelativeLayout) findViewById(R.id.dashPet);

        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                g.setSingleAccountLocation(0);
                Intent i = new Intent(getApplicationContext(), TransferActivity.class);
                startActivity(i);
                finish();

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(i);
                finish();

            }
        });

        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), PetActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

}