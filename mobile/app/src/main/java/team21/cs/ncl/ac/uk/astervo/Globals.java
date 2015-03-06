package team21.cs.ncl.ac.uk.astervo;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONObject;

public class Globals extends Application {

    private boolean loggedIn = false;
    private String authKey = null;
    private JSONArray accounts = null;
    private int singleAccountLocation = 0;
    private JSONArray transfers = null;

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

    public int getSingleAccountLocation() {
        return singleAccountLocation;
    }

    public JSONArray getTransfers() {
        return transfers;
    }

    public void setTransfers(JSONArray transfers) {
        this.transfers = transfers;
    }

    public void setSingleAccountLocation(int singleAccountLocation) {
        this.singleAccountLocation = singleAccountLocation;

    }

    public void reset() {
        setLoggedIn(false);
        setAuthKey(null);
        setAccounts(null);
        setSingleAccountLocation(0);
        setTransfers(null);
    }
}