package team21.cs.ncl.ac.uk.astervo;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONObject;

public class Globals extends Application {

    private boolean loggedIn = false;
    private String authKey = null;
    private JSONArray accounts = null;

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

    public JSONArray getAccounts() {
        return accounts;
    }

    public void setAccounts(JSONArray accounts) {
        this.accounts = accounts;
    }

    public void reset() {
        setLoggedIn(false);
        setAuthKey(null);
    }
}
