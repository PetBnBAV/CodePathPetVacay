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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CameraActivity extends ActionBarActivity {


    ImageView ivPhotoOne;
    ImageView ivPhotoTwo;
    ImageView ivPhotoThree;
    ImageView ivPhotoFour;
    ImageView ivPhotoFive;

    TextView tvAddPhotos;
    ImageView ivDirection;
    Toolbar toolbar;
    int photosCount = 0;
    TextView tvNumOfPhotos;
    boolean mainPhotoEmpty = false;

    LinearLayout llDragandDrop;
    Button btnBackBlack;


    final int RESULT_LAUNCH_CAMERA = 20;
    final int RESULT_LAUNCH_GALLERY = 30;
    final int RESULT_PICTURES_ADDED = 60;

    int MAX_NO_OF_PHOTOS =  5;

    byte photoOne[];
    byte photoTwo[];
    byte photoThree[];
    byte photoFour[];
    byte photoFive[];

    String path;

    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        toolbar = (Toolbar) findViewById(R.id.toolbarforcamera);
        setSupportActionBar(toolbar);

        photoOne = new byte[0];
        photoTwo = new byte[0];
        photoThree = new byte[0];
        photoFour = new byte[0];
        photoFive = new byte[0];



       // ivTakePicture = (ImageView) findViewById(R.id.ivPhotoFive);
        ivPhotoOne = (ImageView) findViewById(R.id.ivPhotoOne);
        ivPhotoTwo = (ImageView) findViewById(R.id.ivPhotoTwo);
        ivPhotoThree = (ImageView) findViewById(R.id.ivPhotoThree);
        ivPhotoFour = (ImageView) findViewById(R.id.ivPhotoFour);
        ivPhotoFive = (ImageView) findViewById(R.id.ivPhotoFive);

        llDragandDrop = (LinearLayout) findViewById(R.id.llDragandDrop);
        ivDirection = (ImageView) findViewById(R.id.ivDirection);
        tvAddPhotos = (TextView) findViewById(R.id.tvAddPhotos);
        tvNumOfPhotos = (TextView) findViewById(R.id.tvNumOfPhotos);
        btnBackBlack = (Button) findViewById(R.id.btnBackBlack);

        tvAddPhotos.setText("Add photos to your listing");

        ivPhotoOne.setOnLongClickListener(longListener);
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

        public boolean onDrag(View v, DragEvent event) {

            final View draggedView = (View) event.getLocalState();

            switch (event.getAction()) {
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

                    View dropTarget = v;

                    // Get owner of the dragged view and remove the view (if needed)
                    ViewGroup owner = (ViewGroup) draggedView.getParent();

                    ImageView iv =  (ImageView) owner.getChildAt(2);

                    iv.setImageResource(0);
                    iv.setImageBitmap(null);

                    mainPhotoEmpty = true;

                    getSupportActionBar().show();
                    photosCount--;
                    updatePhotoCount();

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


            switch(photosCount)
            {
                case 0:
                    ivPhotoOne.setVisibility(View.VISIBLE);
                    test = selectedImagePath;
                    ivPhotoOne.setImageURI(selectedImageUri);
                    tvAddPhotos.setVisibility(View.INVISIBLE);
                    ivDirection.setVisibility(View.INVISIBLE);
                    try {
                        photoOne = readBytesFromURI(selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 1:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageURI(selectedImageUri);
                        mainPhotoEmpty = false;
                        try {
                            photoOne = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        ivPhotoTwo.setImageURI(selectedImageUri);
                        try {
                            photoTwo = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 2:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageURI(selectedImageUri);
                        mainPhotoEmpty = false;
                        try {
                            photoOne = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        ivPhotoThree.setImageURI(selectedImageUri);
                        try {
                            photoThree = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 3:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageURI(selectedImageUri);
                        mainPhotoEmpty = false;
                        try {
                            photoOne = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        ivPhotoFour.setImageURI(selectedImageUri);
                        try {
                            photoFour = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 4:


                    if(mainPhotoEmpty)
                    {
                       ivPhotoOne.setImageURI(selectedImageUri);
                        mainPhotoEmpty = false;
                        try {
                            photoOne = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        ivPhotoFive.setImageURI(selectedImageUri);
                        try {
                            photoFive = readBytesFromURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
            }





        }

        if (requestCode == RESULT_LAUNCH_CAMERA && resultCode == RESULT_OK && null != data) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            path = MediaStore.Images.Media.insertImage(getContentResolver(), bp, "Title", null);



            switch(photosCount)
            {
                case 0:
                    ivPhotoOne.setVisibility(View.VISIBLE);
                    ivPhotoOne.setImageBitmap(bp);
                    tvAddPhotos.setVisibility(View.INVISIBLE);
                    ivDirection.setVisibility(View.INVISIBLE);
                    photoOne = bos.toByteArray();

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 1:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageBitmap(bp);
                        photoOne = bos.toByteArray();
                        mainPhotoEmpty = false;

                    }
                    else
                    {
                        ivPhotoTwo.setImageBitmap(bp);
                        photoTwo = bos.toByteArray();
                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 2:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageBitmap(bp);
                        photoOne = bos.toByteArray();
                        mainPhotoEmpty = false;

                    }
                    else
                    {
                        ivPhotoThree.setImageBitmap(bp);
                        photoThree = bos.toByteArray();
                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 3:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageBitmap(bp);
                        photoOne = bos.toByteArray();
                        mainPhotoEmpty = false;

                    }
                    else
                    {
                        ivPhotoFour.setImageBitmap(bp);
                        photoFour = bos.toByteArray();
                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
                case 4:
                    if(mainPhotoEmpty)
                    {
                        ivPhotoOne.setImageBitmap(bp);
                        photoOne = bos.toByteArray();
                        mainPhotoEmpty = false;

                    }
                    else
                    {
                        ivPhotoFive.setImageBitmap(bp);
                        photoFive = bos.toByteArray();
                    }

                    photosCount++;
                    updatePhotoCount();
                    break;
            }
        }
    }



    public byte[] readBytesFromURI(Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }


    public void updatePhotoCount()
    {

        if(photosCount < 0)
        {
            photosCount = 0;
        }

        if(photosCount == 1) {
            tvNumOfPhotos.setText(String.valueOf(photosCount) + " Photo");
        }
        else
        {
            tvNumOfPhotos.setText(String.valueOf(photosCount) + " Photos");
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

            if(photosCount != MAX_NO_OF_PHOTOS) {
                launchCamera();
            }
            else
            {

            }
        }

        else  if(id == R.id.miGallery) {
           // Toast.makeText(this, "gallery",Toast.LENGTH_SHORT);
            if(photosCount != MAX_NO_OF_PHOTOS) {
                launchGallery();
            }
            else
            {

            }
        }

        return super.onOptionsItemSelected(item);
    }


    public void onBack(View v)
    {

        Intent intent = new Intent();


        intent.putExtra("One", path);
        //intent.putExtra("Two", photoTwo);
        //intent.putExtra("One", photoThree);
        //intent.putExtra("Two", photoFour);
        //intent.putExtra("One", photoFive);

       // Bitmap bmp = BitmapFactory.decodeByteArray(photoOne, 0, photoOne.length);
       //ivTakePicture.setImageBitmap(bmp);


        setResult(RESULT_OK, intent);
        finish();

    }
}
