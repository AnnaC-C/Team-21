package team21.cs.ncl.ac.uk.astervo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.Date;


public class BaseActivity extends ActionBarActivity {

    long lastActivity;
    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastActivity = new Date().getTime();
    }

    @Override
    public void onResume() {
        super.onResume();

        g = (Globals) getApplication();

        long now = new Date().getTime();

        if (now - lastActivity > 3000) {
            HttpClient.reset();
            g.reset();
            Intent i = new Intent(this, LoginActivity.class);
            Toast.makeText(getApplicationContext(), "Logged out due to inactivity.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
