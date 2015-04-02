package team21.cs.ncl.ac.uk.astervo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.List;

/**
 * Created by thebillington on 02/04/2015.
 */

public class PetItemListAdapter extends ArrayAdapter<PetItem> {

    private List<PetItem> items;
    private Context context;
    OnClickListener buy;
    OnClickListener use;

    public PetItemListAdapter(Context context, int layoutResourceId, List<PetItem> items, OnClickListener buy, OnClickListener use) {
        super(context, R.layout.pet_item, items);
        this.context = context;
        this.items = items;
        this.buy = buy;
        this. use = use;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.pet_item, parent, false);

        PetItem item = items.get(position);
        TextView name = (TextView) row.findViewById(R.id.petItemName);
        Button b = (Button) row.findViewById(R.id.petItemUse);
        if(item.getQuantity() <= 0) {
            name.setText(item.getName() + " X " + Integer.toString(item.getQuantity()));
            b.setText("BUY");
            b.setOnClickListener(buy);
        }
        else if(item.getQuantity() == 1) {
            name.setText(item.getName() + " X " + Integer.toString(item.getQuantity()));
            b.setOnClickListener(use);
        }
        else {
            name.setText(item.getName() + "s X " + Integer.toString(item.getQuantity()));
            b.setOnClickListener(use);
        }

        b.setTag(item);

        return row;
    }

}
