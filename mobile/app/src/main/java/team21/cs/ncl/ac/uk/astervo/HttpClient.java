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

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        reset();
        client.get(getAbsoluteUrl(url), params, responseHandler);
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

    public static void delete(String auth_key, String url, AsyncHttpResponseHandler responseHandler) {
        //Reset the client to avoid connection issues
        reset();
        //Set content type to JSON
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
        //Delete and receive response
        client.delete(getAbsoluteUrl(url + "/?auth_token=" + auth_key), responseHandler);
    }

    //Get base URL plus string extension
    private static String getAbsoluteUrl(String relativeUrl) {
        return PrivateFields.BASE_URL + relativeUrl;
    }

    private static void reset() {
        client = new AsyncHttpClient();
    }

}