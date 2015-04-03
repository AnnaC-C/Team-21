package team21.cs.ncl.ac.uk.astervo;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HttpClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        //Set header to JSON
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
        //Post and receive response
        client.get(context, getAbsoluteUrl(url), responseHandler);
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        //Post and receive response
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void post(Context context, String url, JSONObject params, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        //Create an entity to send as parameters
        StringEntity entity = new StringEntity(params.toString());
        //Set header to JSON
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
        //Post and receive response
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    //Get base URL plus string extension
    private static String getAbsoluteUrl(String relativeUrl) {
        return PrivateFields.BASE_URL + relativeUrl;
    }

    public static void reset() {
        client = new AsyncHttpClient();
    }

}