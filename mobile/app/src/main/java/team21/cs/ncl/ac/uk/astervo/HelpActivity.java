package team21.cs.ncl.ac.uk.astervo;

import android.os.Bundle;


public class HelpActivity extends BaseActivity {

    Globals g;

    long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        g = (Globals) getApplication();
    }
}
