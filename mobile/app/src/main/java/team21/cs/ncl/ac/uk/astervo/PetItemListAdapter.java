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
import android.view.View.OnClickListener;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by thebillington on 02/04/2015.
 */

public class PetItemListAdapter extends ArrayAdapter<PetItem> {

    private List<PetItem> items;
    private Context context;
    private OnClickListener buy;
    private OnClickListener use;
    private Bitmap[] imageArray;


    public PetItemListAdapter(Context context, int layoutResourceId, List<PetItem> items, OnClickListener buy, OnClickListener use) {
        super(context, R.layout.pet_item, items);
        this.context = context;
        this.items = items;
        this.buy = buy;
        this. use = use;
        imageArray = new Bitmap[items.size()];
        for(int i = 0; i < items.size(); i++) {
            getImage(items.get(i).getResource(), i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.pet_item, parent, false);

        PetItem item = items.get(position);
        TextView name = (TextView) row.findViewById(R.id.petItemName);
        ImageView image = (ImageView) row.findViewById(R.id.petItemImage);
        Button b = (Button) row.findViewById(R.id.petItemUse);

        //Set image
        Bitmap thisImage = imageArray[position];
        if(thisImage != null) {
            image.setImageBitmap(thisImage);
        }

        if(item.getQuantity() <= 0) {
            name.setText(item.getName() + " X " + Integer.toString(item.getQuantity()));
            b.setText("BUY");
            b.setOnClickListener(buy);
        }
        else {
            name.setText(item.getName() + " X " + Integer.toString(item.getQuantity()));
            b.setOnClickListener(use);
        }

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
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void setBitmap(Bitmap image, int position) {

        imageArray[position] = image;

    }

}
