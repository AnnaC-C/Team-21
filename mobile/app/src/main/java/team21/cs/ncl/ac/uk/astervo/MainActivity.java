package team21.cs.ncl.ac.uk.astervo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    //Get global variables
    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup global variables
        g = (Globals) getApplication();

        //Check connection status
        ConnectionStatus connected = new ConnectionStatus(this);
        //If not connected to the internet, display error and app reload button
        if (!connected.isConnected()) {
            //Get text view and buttons to show/hide
            TextView msg = (TextView) findViewById(R.id.welcomeMessage);
            Button btnEnt = (Button) findViewById(R.id.welcomeEnter);
            Button btnRst = (Button) findViewById(R.id.welcomeReload);

            //Display error message, hide continue button and show reload button
            msg.setText("Sorry, an internet connection is required to use the app.");
            btnEnt.setVisibility(View.INVISIBLE);
            btnRst.setVisibility(View.VISIBLE);
        }
    }

    //On resume, check if someone is already logged in
    @Override
    protected void onResume() {
        super.onResume();

        //Check if already logged in
        if(g.isLoggedIn()) {
            //If logged in, start Dash activity
            Intent i = new Intent(this, DashActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //Start login activity
    public void enterApp(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    //Reload app
    public void restartApp(View view) {
        Intent i = getIntent();
        finish();
        startActivity(i);
    }
}
