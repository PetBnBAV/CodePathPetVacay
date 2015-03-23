package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Utils;
import com.linearlistview.LinearListView;
import com.linearlistview.LinearListView.OnItemClickListener;

/**
 * Created by gangwal on 3/14/15.
 */
public class LYSHomeTypeFragment extends Fragment {

    private static Activity sActivity;
    private HomeTypeSelectListner mCallback;
    private LinearListView llvHouseTypeList;
    public static LYSHomeTypeFragment getInstance(Activity activity){
        LYSHomeTypeFragment frag = new LYSHomeTypeFragment();
        sActivity = activity;
        return frag;

    }

    public interface HomeTypeSelectListner {
        public void getHomeType(int houseType);
        public void setToolbar(String title, String secondaryTitle);
    }

    private class ViewHolder{
        ImageView ivHomeTypeIcon;
        TextView tvHomeTypeTitle;
        TextView tvHomeTypeDescription;
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        private Integer [] houseTypeIconList = {R.drawable.icon_entire_place,R.drawable.icon_private_room,R.drawable.icon_shared_space};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            String [] houseTypeTitleList=null;
            String [] houseTypeDescriptionList={};
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(sActivity).inflate(R.layout.list_lsy_item, parent, false);
                viewHolder.ivHomeTypeIcon = (ImageView) convertView.findViewById(R.id.ivItemType);
                Utils.setTealBorder(viewHolder.ivHomeTypeIcon);
                viewHolder.tvHomeTypeTitle = (TextView)convertView.findViewById(R.id.tvItemTypeTitle);
                viewHolder.tvHomeTypeDescription = (TextView) convertView.findViewById(R.id.tvItemTypeDescription);
                if(houseTypeTitleList==null){
                    houseTypeTitleList =  getResources().getStringArray(R.array.houseTypeTitle);
                    houseTypeDescriptionList = getResources().getStringArray(R.array.houseTypeDescription);
                }
                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TypedArray imgs = getResources().obtainTypedArray(R.array.houseTypeImage);
            viewHolder.ivHomeTypeIcon.setImageResource(imgs.getResourceId(position, -1));
            imgs.recycle();
            viewHolder.tvHomeTypeTitle.setText(houseTypeTitleList[position]);
            viewHolder.tvHomeTypeDescription.setText(houseTypeDescriptionList[position]);
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
            return houseTypeIconList.length;
        }
    };

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (HomeTypeSelectListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement HomeTypeSelectListne");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback.setToolbar(getActivity().getResources().getString(R.string.property_type),"");
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
            mCallback.getHomeType(position);
        }
    };
    /**
     * OnClick of Submit Button, save the task and repopulate the list
     * @param v
     */
    public void onSelect(View v) {
//        String item = mTitle.getText().toString();
//        if (TextUtils.isEmpty(item)) {return;}
//
//        if(mCallback != null){
//            int id = 0;
//            TodoTask todo = new TodoTask(item, mDatePicker.getText().toString(),mTimePicker.getText().toString(),
//                    mPriority.getSelectedItem().toString(), mDescription.getText().toString());
//            if(sTask == null) {
//                mTaskDataSource.createTask(todo);
//            }else{
//                id = sTask.getId();
//                todo.setId(id);
//                mTaskDataSource.updateTask(todo);
//            }
//            mCallback.generateList();
//        }
//        this.dismiss();
    }


}
