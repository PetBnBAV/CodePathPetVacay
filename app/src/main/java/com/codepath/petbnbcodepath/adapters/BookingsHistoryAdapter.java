package com.codepath.petbnbcodepath.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.models.Booking;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by anuscorps23 on 3/18/15.
 */

public class BookingsHistoryAdapter extends ArrayAdapter {

    private static class ViewHolder {
        ImageView ivBHCoverPicture;
        ImageView ivBHProfilePicture;
        TextView tvBHTitle;
        TextView tvBHLocation;
        TextView tvBHStartDate;
        TextView tvBHEndDate;
    }

    public BookingsHistoryAdapter(Context context, List<Booking> historyOfBookings) {
        super(context, android.R.layout.simple_list_item_1, historyOfBookings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Booking bHistory = (Booking) getItem(position);
        ViewHolder vwHolder;

        if (convertView == null) {
            vwHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_booking_history, parent, false);
            vwHolder.ivBHCoverPicture = (ImageView) convertView.findViewById(R.id.ivBHCoverPicture);
            vwHolder.ivBHProfilePicture = (ImageView) convertView.findViewById(R.id.ivBHProfilePicture);
            vwHolder.tvBHTitle = (TextView) convertView.findViewById(R.id.tvBHTitle);
            vwHolder.tvBHLocation = (TextView) convertView.findViewById(R.id.tvBHLocation);
            vwHolder.tvBHStartDate = (TextView) convertView.findViewById(R.id.tvBHStartDate);
            vwHolder.tvBHEndDate = (TextView) convertView.findViewById(R.id.tvBHEndDate);
            convertView.setTag(vwHolder);
        } else {
            vwHolder = (ViewHolder) convertView.getTag();
        }

        // resetting all the views


        if(bHistory.getListingTitle() != null) {
            vwHolder.tvBHTitle.setText(bHistory.getListingTitle().toString());
        }

        if(bHistory.getBookingStartDate() != null) {
            vwHolder.tvBHStartDate.setText(" From " + bHistory.getBookingStartDate().toString());
        }
        if(bHistory.getBookingEndDate() != null) {
            vwHolder.tvBHEndDate.setText("  To " + bHistory.getBookingEndDate().toString());
        }

        if(bHistory.getLatlng() != null) {

            Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(bHistory.getLatlng().getLatitude(), bHistory.getLatlng().getLongitude(), 1);
                vwHolder.tvBHLocation.setText(addresses.get(0).getAddressLine(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(bHistory.getListingCoverPicture() != null) {

            // Getting the screen width so we can resize the image appropriately
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int targetWidth = size.x;

            Picasso.with(getContext())
                    .load(bHistory.getListingCoverPicture().getUrl())
                    .resize(targetWidth, 0)
                    .into(vwHolder.ivBHCoverPicture);
        }


        if(bHistory.getUserProfilePicture() != null) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.LTGRAY)
                    .borderWidthDp(3)
                    .cornerRadiusDp(30)
                    .oval(true)
                    .build();


            Picasso.with(getContext())
                    .load(bHistory.getUserProfilePicture().getUrl())
                    .fit()
                    .transform(transformation)
                    .into(vwHolder.ivBHProfilePicture);

            vwHolder.ivBHProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


        return convertView;
    }
}
