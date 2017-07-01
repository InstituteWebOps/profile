package com.example.srikanth.studentprofile;


import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    // This is a reference to catch Profile picture imagebutton view.
    CircleImageView profilePicImage;
    private final static  int SELECTED_PICTURE_FOR_GALLERY=1;
    private final static  int CAPTURED_PICTURE=0;
    String mCurrentPhotoPath;

    EditText email,phoneno;
    static android.app.FragmentManager  fragmentManager;

    RecyclerView accomRV;
    public static AccomAdapter accomadapter;

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.contactEmail);
        phoneno = (EditText) findViewById(R.id.contactPhoneno);

        fragmentManager = getFragmentManager();

        profilePicImage = (CircleImageView) findViewById(R.id.profile_pic);
        profilePicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(MainActivity.this,profilePicImage);
                popupMenu.getMenuInflater().inflate(R.menu.image_dropdown_menu,popupMenu.getMenu());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                         if(item.getItemId()==R.id.remove_image_item){

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

                        if(item.getItemId()==R.id.default_image_item){
                            setDefaultProfilePic();
                        }
                        
                        /*else if(item.getItemId()==R.id.upload_image_item){
                            onUploadButtonClicked();
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


                        }*/

                        return true;


                    }
                });

                popupMenu.show();
            }
        });




        accomRV=(RecyclerView) findViewById(R.id.accom_rv);
        accomadapter=new AccomAdapter(this, AccomDetailArray.getAccomData());
        LinearLayoutManager layoutmanger=new LinearLayoutManager(this);
        accomRV.setLayoutManager(layoutmanger);
        accomRV.setAdapter(accomadapter);
        accomRV.setHasFixedSize(true);


    }

    private void setDefaultProfilePic() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button){
            final AlertDialog.Builder builder  = new AlertDialog.Builder(this);

            //Checking validation
            if(!isEmailValid(email.getText().toString()) && !isPhonenoValid(phoneno.getText().toString())){
                builder.setMessage("Invalid Email and phone no.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if (!isEmailValid(email.getText().toString()) && isPhonenoValid(phoneno.getText().toString())){
                builder.setMessage("Invalid Email.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if (isEmailValid(email.getText().toString()) && !isPhonenoValid(phoneno.getText().toString())){
                builder.setMessage("Invalid Phone no.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }



            builder.setMessage("Do you want to save changes?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Upload the data into the server.
                            Toast.makeText(MainActivity.this,"Uploading",Toast.LENGTH_SHORT).show();

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

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    private boolean isEmailValid(String email) {
        if( email.length()==0)     // email field can be vacant.
            return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isPhonenoValid(String s) {

        if(s.length()==10 || s.length()==0)
            return true;
        return false;
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


    // This method is invoked when accom_plus_image is clicked.
    public void onAccomPlusClicked(View view) {
        /*Intent intent = new Intent(this,AccomEditActivity.class);
        startActivity(intent);*/
        /*Fragment fragment = new AccomEditActivity();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityRelativeLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/
        // Create new fragment and transaction
        android.app.Fragment newFragment = new AccomEditActivity();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.mainActivityRelativeLayout, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }


    // Required for Capture image ,this method creates a file for saving the captured image.
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
