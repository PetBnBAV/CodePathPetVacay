package com.codepath.petbnbcodepath.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.models.Listing;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by anuscorps23 on 3/18/15.
 */

public class HistoryPageUserAdapter extends ArrayAdapter {
    private static final String TAG = "USERADAPTER";

    private static class ViewHolder {
        TextView tvBookingId;
        ImageView ivListingImage;
        ImageView ivBHProfilePicture;
        TextView tvLocation;
        TextView tvFromDate;
        TextView tvToDate;
    }

    public HistoryPageUserAdapter(Context context, List<Listing> sitters) {
        super(context, android.R.layout.simple_list_item_1, sitters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Listing currSitter = (Listing) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_history_page_user, parent, false);
            viewHolder.tvBookingId = (TextView) convertView.findViewById(R.id.tvBookingId);
            viewHolder.ivListingImage = (ImageView) convertView.findViewById(R.id.ivListingImage);
            viewHolder.ivBHProfilePicture = (ImageView) convertView.findViewById(R.id.ivBHProfilePicture);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvFromDate = (TextView) convertView.findViewById(R.id.tvFromDate);
            viewHolder.tvToDate = (TextView) convertView.findViewById(R.id.tvToDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // resetting all the views

        viewHolder.tvBookingId.setText("Hello");
        viewHolder.ivListingImage.setImageResource(0);
        viewHolder.ivBHProfilePicture.setImageResource(0);
        viewHolder.tvLocation.setText("Hi there");
        viewHolder.tvFromDate.setText("Hey");
        viewHolder.tvToDate.setText("Hola");

        // Getting the screen width so we can resize the image appropriately
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int targetWidth = size.x;


        // Resizing with 0 height, allows the height to be variable, while the width is
        // fixed as the screen width - so the aspect ratio is maintained.
        Picasso.with(getContext())
                .load(currSitter
                .getCoverPictureUrl())
                .resize(targetWidth, 0)
                .placeholder(getContext().getResources().getDrawable(R.drawable.placeholder))
                .into(viewHolder.ivListingImage);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();


        /*Picasso.with(getContext())
                .load(file.getUrl())
                .fit()
                .transform(transformation)
                .into(viewHolder.ivBHProfilePicture);*/


        return convertView;
    }
}
