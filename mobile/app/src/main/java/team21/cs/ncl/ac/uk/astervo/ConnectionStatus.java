package team21.cs.ncl.ac.uk.astervo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionStatus {

    Context context;
    public ConnectionStatus(Context context){
        this.context = context;
    }

    public boolean isConnected() {
        //Check that phone is connected to the internet - if not, return false
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
