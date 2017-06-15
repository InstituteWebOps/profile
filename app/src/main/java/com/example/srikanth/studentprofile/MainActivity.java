package com.example.srikanth.studentprofile;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.bitmap;
import static android.R.attr.thumbnail;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    // This is a reference to catch Profile picture imagebutton view.
    ImageButton profilePicImage;
    private final static  int SELECTED_PICTURE_FOR_GALLERY=1;
    private final static  int CAPTURED_PICTURE=0;
    String mCurrentPhotoPath;


    RecyclerView accomRV;
    public static AccomAdapter accomadapter;

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        profilePicImage = (ImageButton) findViewById(R.id.profile_pic);
        profilePicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(MainActivity.this,profilePicImage);
                popupMenu.getMenuInflater().inflate(R.menu.image_dropdown_menu,popupMenu.getMenu());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.upload_image_item){
                            onUploadButtonClicked();
                        }
                        else if(item.getItemId()==R.id.remove_image_item){

                            AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Do you want to remove your Proile Pic?")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            profilePicImage.setImageResource(R.drawable.dummypropic);
                                        }
                                    })
                                    .setNegativeButton("Cancel",null);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else if(item.getItemId()==R.id.capture_image_item){

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File

                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, CAPTURED_PICTURE);
                                }
                            }


                        }

                        return true;


                    }
                });

                popupMenu.show();
            }
        });




        accomRV=(RecyclerView) findViewById(R.id.accom_rv);
        accomadapter=new AccomAdapter(this,AccomDetailArray.getAccomData());
        LinearLayoutManager layoutmanger=new LinearLayoutManager(this);
        accomRV.setLayoutManager(layoutmanger);
        accomRV.setAdapter(accomadapter);
        accomRV.setHasFixedSize(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button){
            AlertDialog.Builder builder  = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to save changes?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Upload the data into the server.




                            /**********************************/
                        }
                    })
                    .setNegativeButton("Cancel",null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }

    // This method invokes when the upload imagebutton is clicked.

    public void onUploadButtonClicked(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECTED_PICTURE_FOR_GALLERY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECTED_PICTURE_FOR_GALLERY){
                Uri image_uri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(image_uri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    profilePicImage.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to locate image",Toast.LENGTH_LONG).show();
                }
            }



            else if(requestCode == CAPTURED_PICTURE){
                Toast.makeText(MainActivity.this,"image saved.",Toast.LENGTH_LONG).show();
                // Get the dimensions of the View
                int targetW = profilePicImage.getWidth();
                int targetH = profilePicImage.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                profilePicImage.setImageBitmap(bitmap);

            }
        }
    }
    /*****************************************************************/


    // This method is when accom_plus_image is clicked.
    public void onAccomPlusClicked(View view){
        Intent intent = new Intent(this,AccomEditActivity.class);
        startActivity(intent);
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



}
