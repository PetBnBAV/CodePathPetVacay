package com.codepath.petbnbcodepath.activities;


import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;

public class CameraActivity extends ActionBarActivity {

    ImageView ivTakePicture;
    TextView tvAddPhotos;
    ImageView ivDirection;
    Toolbar toolbar;
    int photosCount = 0;
    TextView tvNumOfPhotos;

    LinearLayout llDragandDrop;
    Button btnBackBlack;


    final int RESULT_LAUNCH_CAMERA = 20;
    final int RESULT_LAUNCH_GALLERY = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        toolbar = (Toolbar) findViewById(R.id.toolbarforcamera);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("Hello");
        //toolbar.setTitle(String.valueOf(photosCount) + "Photos");

        ivTakePicture = (ImageView) findViewById(R.id.ivTakePicture);
        llDragandDrop = (LinearLayout) findViewById(R.id.llDragandDrop);
        ivDirection = (ImageView) findViewById(R.id.ivDirection);
        tvAddPhotos = (TextView) findViewById(R.id.tvAddPhotos);
        tvNumOfPhotos = (TextView) findViewById(R.id.tvNumOfPhotos);
        btnBackBlack = (Button) findViewById(R.id.btnBackBlack);

        tvAddPhotos.setText("Add photos to your listing");

        ivTakePicture.setOnLongClickListener(longListener);
        llDragandDrop.setOnDragListener(DropListener);

    }


    View.OnLongClickListener longListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            DragShadow dragShadow = new DragShadow(v);


            ClipData data = ClipData.newPlainText("", "");
            v.startDrag(data, dragShadow, v, 0);
            return false;
        }
    };

    private class DragShadow extends View.DragShadowBuilder {
        ColorDrawable greyBox;

        public DragShadow(View view) {
            super(view);
            greyBox = new ColorDrawable(Color.rgb(211, 211, 211));
        }

        public void DragShadow(Canvas canvas) {
            greyBox.draw(canvas);
        }


        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View v = getView();

            int height = v.getHeight();
            int width = v.getWidth();

            greyBox.setBounds(0, 0, width, height);

            shadowSize.set(width, height);
            shadowTouchPoint.set((int) width / 2, (int) height / 2);
        }
    }


    View.OnDragListener DropListener = new View.OnDragListener() {
        @Override

        public boolean onDrag(View layoutview, DragEvent dragevent) {
            int action = dragevent.getAction();


            switch (action) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag Event","Entered");
                    getSupportActionBar().hide();
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag Event","Exited");
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("Drag Event","Ended");



                case DragEvent.ACTION_DROP:
                    Log.i("Drag Event","Dropped");
                    View view = (View) dragevent.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) layoutview;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();

                    break;

            }
            return true;
        }
    };



    public void launchCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_LAUNCH_CAMERA);
    }



    public void launchGallery() {
        Intent i = new Intent(
        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LAUNCH_GALLERY);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LAUNCH_GALLERY&& resultCode == RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = getPath(selectedImageUri);
            System.out.println("Image Path : " + selectedImagePath);

            ivTakePicture.setVisibility(View.VISIBLE);

            ivTakePicture.setImageURI(selectedImageUri);
            tvAddPhotos.setVisibility(View.INVISIBLE);
            ivDirection.setVisibility(View.INVISIBLE);
            photosCount++;
            if(photosCount == 1) {
                tvNumOfPhotos.setText(String.valueOf(photosCount) + " Photo");
            }
            else
            {
                tvNumOfPhotos.setText(String.valueOf(photosCount) + " Photos");
            }

        }

        if (requestCode == RESULT_LAUNCH_CAMERA && resultCode == RESULT_OK && null != data) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            ivTakePicture.setImageBitmap(bp);
            tvAddPhotos.setVisibility(View.INVISIBLE);
            ivDirection.setVisibility(View.INVISIBLE);
            photosCount++;
            if(photosCount == 1) {
                tvNumOfPhotos.setText(String.valueOf(photosCount) + " Photo");
            }
            else
            {
                tvNumOfPhotos.setText(String.valueOf(photosCount) + " Photos");
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.

        int id = item.getItemId();
        //Toast.makeText(this, String.valueOf(id),Toast.LENGTH_SHORT);


        // Handle action buttons
        if(id == R.id.miCamera) {
            //Toast.makeText(this, "camera",Toast.LENGTH_SHORT);
            launchCamera();
        }

        else  if(id == R.id.miGallery) {
           // Toast.makeText(this, "gallery",Toast.LENGTH_SHORT);
            launchGallery();
        }

        return super.onOptionsItemSelected(item);
    }


    public void onBack(View v)
    {
        finish();

    }
}
