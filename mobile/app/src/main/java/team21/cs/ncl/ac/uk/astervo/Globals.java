package team21.cs.ncl.ac.uk.astervo;

import android.app.Application;

import org.json.JSONObject;

public class Globals extends Application {

    private boolean loggedIn = false;
    private String authKey = null;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
