package com.medaino.www.medainoandroid;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Medoptions extends Activity {
    BluetoothDevice tmp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medoptions);
        tmp1=(BluetoothDevice)getIntent().getExtras().getParcelable("key1");


    }

  public void term(View v)
    {
        Intent intent = new Intent(this, terminalact.class);
        intent.putExtra("key2", tmp1);
        startActivity(intent);
    }

   public void result(View v)
    {
        Intent intent = new Intent(this, klopresults.class);
        startActivity(intent);
    }
  public void plot(View v)
  {
      Intent intent = new Intent(this, plotdata.class);
      intent.putExtra("key2", tmp1);
      startActivity(intent);
  }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medoptions, menu);
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
