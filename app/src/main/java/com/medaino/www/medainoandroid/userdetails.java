package com.medaino.www.medainoandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class userdetails extends Activity {

    Button logn_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       final SharedPreferences prefs = getSharedPreferences("medaino", MODE_PRIVATE);

        String usrstr = prefs.getString("usr", null);
        String passstr = prefs.getString("pass", null);
        String agestr = prefs.getString("age", null);
        String weightstr = prefs.getString("weight", null);
        String heightstr = prefs.getString("height", null);
        String sexstr = prefs.getString("sex", null);

        if(usrstr!="" && passstr!="" && agestr!=null && weightstr!=null && heightstr!=null && sexstr!=null)
        {
            Intent intent=new Intent(userdetails.this,Scanklop.class);
            startActivity(intent);
        }


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_userdetails);

            final Intent k = new Intent(this, Scanklop.class);

            logn_button = (Button) findViewById(R.id.Login_button);

            logn_button.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    EditText username=(EditText)findViewById(R.id.editText1);
                    EditText password=(EditText)findViewById(R.id.editText2);
                    EditText age=(EditText)findViewById(R.id.editText3);
                    EditText weight=(EditText)findViewById(R.id.editText4);
                    EditText height=(EditText)findViewById(R.id.editText5);
                    EditText sex=(EditText)findViewById(R.id.editText6);
                    String usernamevalue=username.getText().toString();
                    String passwordvalue=password.getText().toString();
                    String agevalue=age.getText().toString();
                    String weightvalue=weight.getText().toString();
                    String heightvalue=height.getText().toString();
                    String sexvalue=sex.getText().toString();

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("usr",usernamevalue);
                    editor.putString("pass",passwordvalue);
                    editor.putString("age",agevalue);
                    editor.putString("weight",weightvalue);
                    editor.putString("height",heightvalue);
                    editor.putString("sex",sexvalue);

                    editor.commit();

                    startActivity(k);
                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userdetails, menu);
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
}
