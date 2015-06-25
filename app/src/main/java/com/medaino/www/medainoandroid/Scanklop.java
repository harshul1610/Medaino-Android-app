package com.medaino.www.medainoandroid;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class Scanklop extends ListActivity {


    Button scan;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ArrayList listb = new ArrayList();
    BluetoothDevice tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanklop);
        BA = BluetoothAdapter.getDefaultAdapter();

        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            //Intent turnvis = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //startActivityForResult(turnvis, 0);
            //Toast.makeText(getApplicationContext(), "Made discoverable", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Intent turnvis = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //startActivityForResult(turnvis, 0);
            //Toast.makeText(getApplicationContext(),"Made discoverable", Toast.LENGTH_LONG).show();
        }

    }

    public void scan_klops(View v)
    {
        scan=(Button)findViewById(R.id.con_button);
        scan.setText("scanned");
        scan.setEnabled(false);
        //lv=(ListView)findViewById(R.id.list);

        pairedDevices = BA.getBondedDevices();

        for(BluetoothDevice bt : pairedDevices)
            listb.add(bt.getName());

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listb);
        setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView list, View v, int position, long id)
    {



        for(BluetoothDevice bt : pairedDevices)
        {
            if((bt.getName()).equals(listb.get(position)))
            {

                tmp=bt;
            }
        }

        Intent intent = new Intent(this, Medoptions.class);
        intent.putExtra("key1",tmp);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scanklop, menu);
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
