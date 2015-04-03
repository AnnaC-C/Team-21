package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.view.View.OnClickListener;


public class PetActivity extends BaseActivity {

    Globals g;

    JSONArray allItems;

    private Button cons;
    private Button wear;

    PetItemListAdapter consumableAdapter;
    PetItemListAdapter wearableAdapter;
    ListView consumableListView;
    ListView wearableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        setupMiniGames();

        g = (Globals) getApplication();

        getAllItems();
    }

    public void getAllItems() {

        //Start fetching questions
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(PetActivity.this);
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
        OnClickListener buy = new OnClickListener() {
            @Override
            public void onClick(View v) {
                PetItem p = (PetItem) v.getTag();
                Toast.makeText(getApplicationContext(), "Buy " + p.getName(), Toast.LENGTH_LONG).show();
            }
        };
        OnClickListener use = new OnClickListener() {
            @Override
            public void onClick(View v) {
                PetItem p = (PetItem) v.getTag();
                Toast.makeText(getApplicationContext(), Integer.toString(p.getId()), Toast.LENGTH_LONG).show();
            }
        };

        List<PetItem> consumableItems = new ArrayList<>();
        List<PetItem> wearableItems = new ArrayList<>();

        for (int i = 0; i < allItems.length(); i++) {
            try {
                //Get current object
                JSONObject currentItem = allItems.getJSONObject(i);
                //Set the fields of the object
                String name = currentItem.getString(PrivateFields.TAG_ITEMS_NAME);
                int quantity = currentItem.getInt(PrivateFields.TAG_ITEMS_COST);
                String resource = currentItem.getString(PrivateFields.TAG_ITEMS_IMAGE);
                int id = currentItem.getInt(PrivateFields.TAG_ITEMS_ID);
                boolean consumable = currentItem.getBoolean(PrivateFields.TAG_ITEMS_CONSUMABLE);
                //Create object
                PetItem item = new PetItem(name, quantity, resource, id, consumable);
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

        consumableAdapter = new PetItemListAdapter(getApplicationContext(), R.layout.pet_item, consumableItems, buy, use);
        wearableAdapter = new PetItemListAdapter(getApplicationContext(), R.layout.pet_item, wearableItems, buy, use);
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
        cons.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                consumableListView.setVisibility(View.INVISIBLE);
                wearableListView.setVisibility(View.VISIBLE);
            }
        });

        wear.setText("See consumables.");
        wear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                consumableListView.setVisibility(View.VISIBLE);
                wearableListView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setupMiniGames() {

        final Intent strokeIntent = new Intent(this, StrokePetActivity.class);

        RelativeLayout strokeGame = (RelativeLayout) findViewById(R.id.startStrokePet);
        strokeGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(strokeIntent);
            }
        });

    }

}
