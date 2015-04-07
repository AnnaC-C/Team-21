package team21.cs.ncl.ac.uk.astervo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;


public class BaseActivity extends ActionBarActivity {

    long lastActivity;
    Globals g;

    long now;

    final int TIMEOUT_MILLI = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastActivity = new Date().getTime();
    }

    @Override
    public void onResume() {
        super.onResume();

        g = (Globals) getApplication();

        now = new Date().getTime();

        if (timeout()) {
            inactiveLogout();
        }
    }

    public boolean timeout() {

        if (now - lastActivity > TIMEOUT_MILLI) {
            return true;
        }
        return false;
    }

    public void inactiveLogout() {
        HttpClient.reset();
        g.reset();
        Intent i = new Intent(this, LoginActivity.class);
        Toast.makeText(getApplicationContext(), "Logged out due to inactivity.", Toast.LENGTH_LONG).show();
        finish();
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
                if (timeout()) {
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
                if (timeout()) {
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
                if (timeout()) {
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
                if (timeout()) {
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
                if (timeout()) {
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
                if (timeout()) {
                    inactiveLogout();
                }
                else {
                    Intent rewardIntent = new Intent(this, HelpActivity.class);
                    startActivity(rewardIntent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {

        HttpClient.reset();
        g.reset();
        Intent i = new Intent(this, LoginActivity.class);
        Toast.makeText(getApplicationContext(), "Logged out.", Toast.LENGTH_LONG).show();
        finish();

    }

}
