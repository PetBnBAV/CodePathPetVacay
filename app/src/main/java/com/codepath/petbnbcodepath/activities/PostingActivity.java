package com.codepath.petbnbcodepath.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.PostingArrayAdapter;
import com.codepath.petbnbcodepath.models.Listing;

import java.util.ArrayList;

public class PostingActivity extends ActionBarActivity {
    ListView lvPosting;
    ArrayList<Listing> posts;
    private PostingArrayAdapter aPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        //TODO need to get location from intent.
        setupView();

        aPosts = new PostingArrayAdapter(this,posts);
        lvPosting.setAdapter(aPosts);
    }

    private void setupView() {
        lvPosting = (ListView) findViewById(R.id.lvPost);
        posts = new ArrayList<Listing>();
        for(int i=1;i<7;i++){
            posts.add(new Listing());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_posting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
