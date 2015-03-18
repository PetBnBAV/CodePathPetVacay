package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.LYSCityFragment;
import com.codepath.petbnbcodepath.fragments.LYSHomeTypeFragment;
import com.codepath.petbnbcodepath.fragments.LYSMoreInfoFragment;

public class ListYourSpaceActivity extends ActionBarActivity implements LYSHomeTypeFragment.HomeTypeSelectListner, LYSCityFragment.CitySelectListner,
        LYSMoreInfoFragment.MoreInfoListner
{
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_your_space);

        if(savedInstanceState==null) {
            LYSHomeTypeFragment lysHomeTypeFragment = LYSHomeTypeFragment.getInstance(this);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flLSY,lysHomeTypeFragment);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getHomeType(int houseType) {
        String [] houseTypeTitleList =  getResources().getStringArray(R.array.houseTypeTitle);

//        LYSMoreInfoFragment lysMoreInfoFragment = LYSMoreInfoFragment.getInstance(this);
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flLSY,lysMoreInfoFragment);
//        ft.commit();


//        Toast.makeText(getApplicationContext(),
//                "House Type " + houseTypeTitleList[houseType], Toast.LENGTH_SHORT).show();
        LYSCityFragment lysCityFragment = LYSCityFragment.getInstance(this);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLSY,lysCityFragment);
        ft.commit();
    }

    @Override
    public void getCityName(String city) {
//        Toast.makeText(getApplicationContext(),
//                "City Selected " + city, Toast.LENGTH_SHORT).show();
        LYSMoreInfoFragment lysMoreInfoFragment = LYSMoreInfoFragment.getInstance(this);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLSY,lysMoreInfoFragment);
        ft.commit();
    }

    @Override
    public void getMoreInfo(int petCount,int petSize,int petType,int playground){
        String petSizeString = "";
        String petTypeString = "Dog";
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
//        Toast.makeText(getApplicationContext(),
//                "Pet Count" + petCount + "\t Size " + petSizeString + "\tType " + petTypeString+"\tPlayground " + playgroundString, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,ManageYourListingActivity.class);
        //TODO send all the details got till this point
        startActivity(intent);

    }
}
