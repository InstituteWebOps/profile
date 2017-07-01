package com.example.srikanth.studentprofile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class AccomPencilEdit extends AppCompatActivity {
    EditText accomPencilOrgan,accomPencilPos,accomPencilFromyear,accomPencilToyear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accom_edit);

        accomPencilOrgan = (EditText) findViewById(R.id.accom_organ_edittext);
        accomPencilPos= (EditText) findViewById(R.id.accom_pos_edittext);
        accomPencilFromyear= (EditText) findViewById(R.id.accom_fromyear_edittext);
        accomPencilToyear = (EditText) findViewById(R.id.accom_toyear_edittext);



        accomPencilOrgan.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomOrgan);
        accomPencilPos.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomPos);
        accomPencilFromyear.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomFromyear);
        accomPencilToyear.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomToyear);

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
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomOrgan    = accomPencilOrgan   .getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomPos      = accomPencilPos     .getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomFromyear = accomPencilFromyear.getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomToyear   = accomPencilToyear  .getText().toString();
                           startActivity(new Intent(AccomPencilEdit.this,MainActivity.class));
                       }
                   })
                   .setNegativeButton("Cancel",null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }
}

