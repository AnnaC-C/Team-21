package team21.cs.ncl.ac.uk.astervo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DashActivity extends ActionBarActivity {

    //Get global variables
    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        //Get globals
        g = (Globals) getApplication();

        //Get bundled extras from previous activity
        Bundle extras = getIntent().getExtras();

        //Display global variables
        TextView displayAuth = (TextView) findViewById(R.id.dashAuth);
        displayAuth.setText("Authorization: " + g.getAuthKey());

        //If there are extras, then display them
        if(extras != null) {

            TextView displaySuccess = (TextView) findViewById(R.id.dashSuccess);
            TextView displayInfo = (TextView) findViewById(R.id.dashInfo);
            displaySuccess.setText("Success: " + extras.getString(PrivateFields.TAG_SUCCESS));
            displayInfo.setText("Info: " + extras.getString(PrivateFields.TAG_INFO));

        }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
