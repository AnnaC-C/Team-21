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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShopActivity extends BaseActivity {

    Globals g;

    JSONArray allItems;

    private Button cons;
    private Button wear;

    ShopItemListAdapter consumableAdapter;
    ShopItemListAdapter wearableAdapter;
    ListView consumableListView;
    ListView wearableListView;

    ConnectionStatus connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        g = (Globals) getApplication();

        connectionStatus = new ConnectionStatus(this);

        getAllItems();
    }

    public void getAllItems() {

        //Start fetching questions
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(ShopActivity.this);
        prgDialog.setMessage("Fetching items...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        //Attempt to return array of questions
        try {

            //Send the HTTP get request and get JSON object back
            HttpClient.get(this.getApplicationContext(), "/api/items", new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    prgDialog.dismiss();

                    //If successful parse JSON data for questions and answers
                    if (statusCode == 200) {
                        if (response != null) {
                            try {
                                allItems = response.getJSONArray(PrivateFields.TAG_ITEMS);
                                setupListView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //If authorization error, prompt user to check details
                    else if (statusCode == 401) {
                        Context context = getApplicationContext();
                        CharSequence text = "Unable to fetch items. Please try again.";
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

    public void setupListView() {

        //Setup on click listeners for each item
        View.OnClickListener use = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopItem s = (ShopItem) v.getTag();
                buyItem(s.getId());
            }
        };

        List<ShopItem> consumableItems = new ArrayList<>();
        List<ShopItem> wearableItems = new ArrayList<>();

        for (int i = 0; i < allItems.length(); i++) {
            try {
                //Get current object
                JSONObject currentItem = allItems.getJSONObject(i);
                //Set the fields of the object
                String name = currentItem.getString(PrivateFields.TAG_ITEMS_NAME);
                int cost = currentItem.getInt(PrivateFields.TAG_ITEMS_COST);
                String resource = currentItem.getString(PrivateFields.TAG_ITEMS_IMAGE);
                int id = currentItem.getInt(PrivateFields.TAG_ITEMS_ID);
                boolean consumable = currentItem.getBoolean(PrivateFields.TAG_ITEMS_CONSUMABLE);
                //Create object
                ShopItem item = new ShopItem(name, cost, resource, id, consumable);
                //Add the object
                if (consumable) {
                    consumableItems.add(item);
                } else {
                    wearableItems.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        consumableAdapter = new ShopItemListAdapter(getApplicationContext(), R.layout.pet_item, consumableItems, use);
        wearableAdapter = new ShopItemListAdapter(getApplicationContext(), R.layout.pet_item, wearableItems, use);
        wearableListView = (ListView) findViewById(R.id.displayWearableItems);
        consumableListView = (ListView) findViewById(R.id.displayConsumableItems);

        wearableListView.setAdapter(wearableAdapter);
        consumableListView.setAdapter(consumableAdapter);

        setupHeaderButtons();

        wearableListView.addFooterView(wear);
        consumableListView.addFooterView(cons);

    }

    public void setupHeaderButtons() {
        cons = new Button(getApplicationContext());
        wear = new Button(getApplicationContext());

        cons.setText("See wearables.");
        cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumableListView.setVisibility(View.INVISIBLE);
                wearableListView.setVisibility(View.VISIBLE);
            }
        });

        wear.setText("See consumables.");
        wear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumableListView.setVisibility(View.VISIBLE);
                wearableListView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void buyItem(int id) {


        //If phone has data, then attempt to buy item
        if (connectionStatus.isConnected()) {

            //Create a new JSON object to store the params
            JSONObject params = new JSONObject();
            //Try to add the email and password text in as params
            try {
                params.put(PrivateFields.TAG_ACC_ID, Integer.toString(id));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Start buying item dialog
            final ProgressDialog prgDialog;
            prgDialog = new ProgressDialog(ShopActivity.this);
            prgDialog.setMessage("Buying item...");
            prgDialog.setCancelable(false);
            prgDialog.show();

            //Attempt to purchase
            try {

                //Send the HTTP post request and get JSON response
                HttpClient.post(this.getApplicationContext(), "/api/buy", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        prgDialog.dismiss();

                        //If successful parse JSON data and create dashboard activity
                        if (statusCode == 200) {
                            if (response != null) {
                                try {
                                    //Parse the data object
                                    JSONObject data = (JSONObject) response.get(PrivateFields.TAG_INFO);
                                    Toast.makeText(getApplicationContext(), data.getString(PrivateFields.TAG_MESSAGE).toString(), Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //If authorization error, throw error
                        else if (statusCode == 401) {
                            Context context = getApplicationContext();
                            CharSequence text = "Something went wrong, please try again later.";
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
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShopActivity.this);
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
            CharSequence text = "Purchase requires an internet connection.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

}