package com.example.srikanth.studentprofile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AccomEditActivity extends Fragment {

    public static EditText accomOrgan,accomPos,accomFromyear,accomToyear;
    Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_accom_edit,container,false);
        accomOrgan = (EditText) v.findViewById(R.id.accom_organ_edittext);
        accomPos = (EditText) v.findViewById(R.id.accom_pos_edittext);
        accomFromyear = (EditText) v.findViewById(R.id.accom_fromyear_edittext);
        accomToyear = (EditText) v.findViewById(R.id.accom_toyear_edittext);

        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromUpdateLabel();
            }

        };

        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toUpdateLabel();
            }

        };

        accomFromyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), fromDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        accomToyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), toDate,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        return v;
    }



    private void fromUpdateLabel() {

        String myFormat = "MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        accomFromyear.setText(sdf.format(myCalendar.getTime()));
    }

    private void toUpdateLabel() {

        String myFormat = "MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        accomToyear.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.accom_save_action,menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button){
            //Checking for validation.
            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

            //Checking validation
            if((accomOrgan.getText().toString().length()==0) && (accomPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name and position.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if (!(accomOrgan.getText().toString().length()==0) && (accomPos.getText().toString().length()==0)){
                builder.setMessage("Position name is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if ((accomOrgan.getText().toString().length()==0) && !(accomPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }


            // Saving the data.
            //grabbing the details entered in the fields into cardview.
            AccomDetails current=new AccomDetails();
            current.accomOrgan=AccomEditActivity.accomOrgan.getText().toString();
            current.accomPos  =AccomEditActivity.accomPos.getText().toString();
            current.accomFromyear =AccomEditActivity.accomFromyear.getText().toString();
            current.accomToyear =AccomEditActivity.accomToyear.getText().toString();
            AccomDetailArray.makeAccomCard(current);


            Toast.makeText(getActivity(),"Saved.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }


}


