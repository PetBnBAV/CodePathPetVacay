package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.LYSCityFragment;
import com.codepath.petbnbcodepath.fragments.LYSHomeTypeFragment;
import com.codepath.petbnbcodepath.fragments.LYSMoreInfoFragment;
import com.codepath.petbnbcodepath.fragments.LYSPetTypeFragment;
import com.codepath.petbnbcodepath.helpers.Constants;

public class ListYourSpaceActivity extends ActionBarActivity implements LYSPetTypeFragment.PetTypeSelectListner, LYSHomeTypeFragment.HomeTypeSelectListner, LYSCityFragment.CitySelectListner,
        LYSMoreInfoFragment.MoreInfoListner
{
    FragmentTransaction ft;

    int petType,houseType;
    String city;
    Toolbar toolbar;
    TextView tvToolbatTitle;
    TextView tvToolbarSecondaryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_your_space);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolbatTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarSecondaryTitle = (TextView) findViewById(R.id.tvToolbarSecondaryTitle);
        setSupportActionBar(toolbar);
        if(savedInstanceState==null) {
            LYSPetTypeFragment lysPetTypeFragment = LYSPetTypeFragment.getInstance(this);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flLSY,lysPetTypeFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_your_space, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void getPetType(int petType) {
        LYSHomeTypeFragment lysHomeTypeFragment = LYSHomeTypeFragment.getInstance(this);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLSY,lysHomeTypeFragment);
        ft.commit();
    }

    @Override
    public void setToolbar(String title, String secondaryTitle) {
        tvToolbatTitle.setText(title);
        if(secondaryTitle.isEmpty())
            tvToolbarSecondaryTitle.setVisibility(View.INVISIBLE);
        else
            tvToolbarSecondaryTitle.setText(secondaryTitle);
    }

    @Override
    public void getHomeType(int houseType) {
        String [] houseTypeTitleList =  getResources().getStringArray(R.array.houseTypeTitle);

//        LYSMoreInfoFragment lysMoreInfoFragment = LYSMoreInfoFragment.getInstance(this);
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flLSY,lysMoreInfoFragment);
//        ft.commit();


        //Toast.makeText(getApplicationContext(),
               // "House Type " + houseTypeTitleList[houseType], Toast.LENGTH_SHORT).show();
        LYSCityFragment lysCityFragment = LYSCityFragment.getInstance(this);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLSY,lysCityFragment);
        ft.commit();
    }

    @Override
    public void getCityName(String city) {
        //Toast.makeText(getApplicationContext(),
               // "City Selected " + city, Toast.LENGTH_SHORT).show();
        LYSMoreInfoFragment lysMoreInfoFragment = LYSMoreInfoFragment.getInstance(this);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLSY,lysMoreInfoFragment);
        ft.commit();
    }

    @Override
    public void getMoreInfo(int petCount,int petSize,int playground){
        String petSizeString = "";
        String playgroundString = "";
        while(petSize>0){
            int currentIndex = petSize%10;
            if(currentIndex==1){
                petSizeString = petSizeString + "Small";
            }else if(currentIndex==2){
                petSizeString = petSizeString + " Medium";
            }else if(currentIndex==3){
                petSizeString = petSizeString + "Large";
            }
            petSize = petSize/10;

        }
        while(playground>0){
            int currentIndex = playground%10;
            if(currentIndex==1){
                playgroundString = playgroundString + "Deck";
            }else if(currentIndex==2){
                playgroundString = playgroundString + " Backyard";
            }else if(currentIndex==3){
                playgroundString = playgroundString + "Park";
            }
            playground = playground/10;
        }
        //Toast.makeText(getApplicationContext(),
               // "Pet Count" + petCount + "\t Size " + petSizeString + "\tType " + petTypeString+"\tPlayground " + playgroundString, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,ManageYourListingActivity.class);
////    static ListingCreate create(int petType, int houseType, String city, int petCount, int petSize, int playground, String[] coverImages, String title, String summary, int cost, String address){
//        String city = "San Jose, CA";
//        String title = "Cozy Apartment";
//        String summary = "Some Summary";
//        int cost = 25;
//        String address = "Flora Vista, San Jose, CA";
//        int houseType = 1;
//        static ListingCreate post = ListingCreate.create(petCount,houseType,city,petCount,petSize,playground,new String[]{"abc","def"},title,summary,cost,address);
        //TODO Needs to put in a object and use some libraries for Parcelable
        intent.putExtra(Constants.petTypeKey,petType);
        intent.putExtra(Constants.houseTypeKey,houseType);
        intent.putExtra(Constants.cityKey,city);
        intent.putExtra(Constants.petCountKey,petCount);
        intent.putExtra(Constants.petSizeKey,petSize);
        intent.putExtra(Constants.playgroundKey,playground);
        startActivity(intent);

    }

    public void onPrevious(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
