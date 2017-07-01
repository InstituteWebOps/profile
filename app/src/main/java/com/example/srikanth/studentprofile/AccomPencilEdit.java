package com.example.srikanth.studentprofile;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AccomPencilEdit extends Fragment {
    EditText accomPencilOrgan,accomPencilPos,accomPencilFromyear,accomPencilToyear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_accom_edit,container,false);

        accomPencilOrgan = (EditText) v.findViewById(R.id.accom_organ_edittext);
        accomPencilPos= (EditText) v.findViewById(R.id.accom_pos_edittext);
        accomPencilFromyear= (EditText) v.findViewById(R.id.accom_fromyear_edittext);
        accomPencilToyear = (EditText) v.findViewById(R.id.accom_toyear_edittext);

        accomPencilOrgan.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomOrgan);
        accomPencilPos.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomPos);
        accomPencilFromyear.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomFromyear);
        accomPencilToyear.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomToyear);


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button){
            //Checking for validation.
            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

            //Checking validation
            if((accomPencilOrgan.getText().toString().length()==0) && (accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name and position.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if (!(accomPencilOrgan.getText().toString().length()==0) && (accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Position name is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if ((accomPencilOrgan.getText().toString().length()==0) && !(accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }


            // Saving the data.
            builder.setMessage("Do you want to save changes?")
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomOrgan    = accomPencilOrgan   .getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomPos      = accomPencilPos     .getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomFromyear = accomPencilFromyear.getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomToyear   = accomPencilToyear  .getText().toString();
                           startActivity(new Intent(getActivity(),MainActivity.class));
                       }
                   })
                   .setNegativeButton("Cancel",null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }
}

