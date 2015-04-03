package team21.cs.ncl.ac.uk.astervo;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class StrokePetActivity extends BaseActivity {

    int strokeCount = 0;
    int strokes = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_pet);

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(StrokePetActivity.this);
        dlgAlert.setMessage("To play with your pet either tap their nose, or stroke them.");
        dlgAlert.setTitle("Play with your pet.");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.create().show();

        RelativeLayout r = (RelativeLayout) findViewById(R.id.strokePet);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strokeCount++;
                if(strokeCount == strokes) {
                    Toast.makeText(getApplicationContext(), "HAPPY++", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
