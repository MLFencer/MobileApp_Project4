package net.nofool.dev.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemsAdapter extends ArrayAdapter<ListCompanies>{
    public ListItemsAdapter(Context context, ArrayList<ListCompanies> items){
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListCompanies note = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_row, parent, false);
        }

        TextView itemTitle = (TextView)convertView.findViewById(R.id.ListItemTitle);
        TextView itemBody = (TextView)convertView.findViewById(R.id.listItemBody);
        ImageView itemImage = (ImageView)convertView.findViewById(R.id.listItemImage);

        itemTitle.setText(note.getName());
        itemBody.setText(note.getMessage());
        itemImage.setImageResource(note.getAssociationDrawable());

        return convertView;
    }
}
