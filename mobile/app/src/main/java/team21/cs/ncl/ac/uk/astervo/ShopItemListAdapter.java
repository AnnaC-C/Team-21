package team21.cs.ncl.ac.uk.astervo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by thebillington on 05/04/2015.
 */
public class ShopItemListAdapter extends ArrayAdapter<ShopItem> {

    private List<ShopItem> items;
    private Context context;
    private View.OnClickListener buy;
    private Bitmap[] imageArray;


    public ShopItemListAdapter(Context context, int layoutResourceId, List<ShopItem> items, View.OnClickListener buy) {
        super(context, R.layout.pet_item, items);
        this.context = context;
        this.items = items;
        this.buy = buy;
        imageArray = new Bitmap[items.size()];
        for (int i = 0; i < items.size(); i++) {
            getImage(items.get(i).getResource(), i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.pet_item, parent, false);

        ShopItem item = items.get(position);
        TextView name = (TextView) row.findViewById(R.id.petItemName);
        ImageView image = (ImageView) row.findViewById(R.id.petItemImage);
        Button b = (Button) row.findViewById(R.id.petItemUse);

        //Set image
        Bitmap thisImage = imageArray[position];
        if (thisImage != null) {
            image.setImageBitmap(thisImage);
        }

        name.setText(item.getName() + " £" + Integer.toString(item.getCost()));
        b.setText("BUY");
        b.setOnClickListener(buy);

        b.setTag(item);

        return row;
    }

    private void getImage(String url, final int position) {

        try {

            HttpClient.get(getContext(), url, new FileAsyncHttpResponseHandler(getContext()) {

                @Override
                public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                    //Do nothing
                }

                @Override
                public void onSuccess(int i, Header[] headers, File file) {
                    setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), position);
                    notifyDataSetChanged();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void setBitmap(Bitmap image, int position) {

        imageArray[position] = image;

    }

}
