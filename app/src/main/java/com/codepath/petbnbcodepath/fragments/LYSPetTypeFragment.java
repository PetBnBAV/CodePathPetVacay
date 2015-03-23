package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.linearlistview.LinearListView;
import com.linearlistview.LinearListView.OnItemClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by gangwal on 3/14/15.
 */
public class LYSPetTypeFragment extends Fragment {

    private static Activity sActivity;
    private PetTypeSelectListner mCallback;
    private LinearListView llvHouseTypeList;
    public static LYSPetTypeFragment getInstance(Activity activity){
        LYSPetTypeFragment frag = new LYSPetTypeFragment();
        sActivity = activity;
        return frag;

    }

    public interface PetTypeSelectListner {
        public void getPetType(int petType);
        public void setToolbar(String title, String secondaryTitle);

    }

    private class ViewHolder{
        ImageView ivPetTypeIcon;
        TextView tvPetTypeTitle;
        TextView tvPetTypeDescription;
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        private Integer [] petTypeIconList = {R.drawable.dog_color,R.drawable.cat_color,R.drawable.dog_cat_color};
        String [] petTypeTitleList=null;
        String [] petTypeDescriptionList;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(sActivity).inflate(R.layout.list_lsy_item, parent, false);
                viewHolder.ivPetTypeIcon = (ImageView) convertView.findViewById(R.id.ivItemType);
                int color = Color.parseColor(Constants.TEAL_COLOR);
                viewHolder.ivPetTypeIcon.setColorFilter(color, PorterDuff.Mode.DST_ATOP);

                viewHolder.tvPetTypeTitle = (TextView)convertView.findViewById(R.id.tvItemTypeTitle);
                viewHolder.tvPetTypeDescription = (TextView) convertView.findViewById(R.id.tvItemTypeDescription);
                if(petTypeTitleList==null){
                    petTypeTitleList =  getResources().getStringArray(R.array.petTypeTitle);
                    petTypeDescriptionList = getResources().getStringArray(R.array.petTypeDescription);
                }
                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(getActivity())
                    .load(petTypeIconList[position])
                    .fit()
                    .into(viewHolder.ivPetTypeIcon);
            viewHolder.tvPetTypeTitle.setText(petTypeTitleList[position]);
            viewHolder.tvPetTypeDescription.setText(petTypeDescriptionList[position]);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return petTypeIconList.length;
        }
    };

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (PetTypeSelectListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PetTypeSelectListner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback.setToolbar(getActivity().getResources().getString(R.string.list_your_space_title),"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_lsy_hometype,parent,false);

        llvHouseTypeList = (LinearListView) view.findViewById(R.id.llvHouseType);
        llvHouseTypeList.setAdapter(mAdapter);
        llvHouseTypeList.setOnItemClickListener(mListener);
        return view;

    }

    OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void onItemClick(LinearListView parent, View view, int position,
                                long id) {
            Constants.PET_TYPE petType;
            switch (position){
                case 0:
                    petType = Constants.PET_TYPE.DOG;
                    break;
                case 1:
                    petType = Constants.PET_TYPE.CAT;
                    break;
                case 2:
                    petType = Constants.PET_TYPE.BOTH;
                    break;
                default:
                    petType = Constants.PET_TYPE.BOTH;
            }
            mCallback.getPetType(petType.getValue());
        }
    };
}
