package team21.cs.ncl.ac.uk.astervo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view) {

        EditText userIn =  (EditText) findViewById(R.id.loginUser);
        EditText passIn = (EditText) findViewById(R.id.loginPass);

        String user = userIn.getText().toString();
        String pass = passIn.getText().toString();

        Intent i = new Intent(this, DashActivity.class);

        i.putExtra(PrivateFields.TAG_USER, user);
        i.putExtra(PrivateFields.TAG_PASS, pass);

        startActivity(i);
    }

    public void signUp(View view) {

        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }
}
