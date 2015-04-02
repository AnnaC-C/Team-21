package team21.cs.ncl.ac.uk.astervo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;


public class PetActivity extends BaseActivity {

    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        g = (Globals) getApplication();

        setupListView();
    }

    public void setupListView() {

        OnClickListener buy = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(i);
                finish();
            }
        };
        OnClickListener use = new OnClickListener() {
            @Override
            public void onClick(View v) {
                PetItem p = (PetItem) v.getTag();
                Toast.makeText(getApplicationContext(), p.getName(), Toast.LENGTH_LONG).show();
            }
        };

        List<PetItem> items = new ArrayList<>();

        items.add(new PetItem("Hat", 0, "h"));
        items.add(new PetItem("Sunglasses", 1, "h"));
        items.add(new PetItem("Bone", 3, "h"));

        PetItemListAdapter adapter = new PetItemListAdapter(getApplicationContext(), R.layout.pet_item, items, buy, use);
        ListView atomPaysListView = (ListView)findViewById(R.id.displayPetItems);
        atomPaysListView.setAdapter(adapter);

    }
}
