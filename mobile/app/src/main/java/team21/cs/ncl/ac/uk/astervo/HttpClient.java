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
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(url), headers, params, "applications/json", responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, JSONObject params, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(params.toString());
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return PrivateFields.BASE_URL + relativeUrl;
    }

}