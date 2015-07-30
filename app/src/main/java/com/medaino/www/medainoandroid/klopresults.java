package com.medaino.www.medainoandroid;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class klopresults extends Activity{
    BluetoothSocket mmSocket;
    InputStream mmInStream=null;
    OutputStream mmOutStream=null;
    String data=new String(" ");
    String red=new String();
    String ir=new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klopresults);
        BluetoothDevice tmpl=(BluetoothDevice)getIntent().getExtras().getParcelable("key2");

        ConnectThread con_bl=new ConnectThread(tmpl);
        con_bl.start();

    } //end of oncreate method


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try {
            mmInStream.close();
            mmOutStream.close();
            mmSocket.close();
        }
        catch (IOException e)
        {

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_klopresults, menu);
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

    class ConnectThread extends Thread
    {
        BluetoothDevice mmDevice;
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device)
        {

            mmDevice = device;


            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try
            {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            }
            catch (IOException e)
            {

            }

        }

        public void run()
        {

            try
            {
                mmSocket.connect();
                Log.d("balle1", "connectivity done") ;
            }
            catch (IOException connectException)
            {
                Log.d("balle2","connectivity cant be done") ;
            }

            ConnectedThread manageConnectedSocket=new ConnectedThread(mmSocket);
            manageConnectedSocket.start();

        }
    }







    class ConnectedThread extends Thread
    {
        int readBufferPosition=0;

        private void closeConnection()
        {
            if (mmInStream != null)
            {
                try {mmInStream.close();} catch (Exception e) {}
                mmInStream = null;
            }

            if (mmOutStream != null)
            {
                try {mmOutStream.close();} catch (Exception e) {}
                mmOutStream = null;
            }

            if (mmSocket != null)
            {
                try {mmSocket.close();} catch (Exception e) {}
                mmSocket = null;
            }

        }

        public ConnectedThread(BluetoothSocket socket)
        {
            mmSocket = socket;

            try
            {
                mmInStream = socket.getInputStream();
                mmOutStream = socket.getOutputStream();

            }
            catch (IOException e)
            {
                Log.d("yyyy","cant take");
            }
        }

        public void run()
        {

            byte[] readBuffer=new byte[1024];
            while (true)
            {
                try
                {
                    int bytesAvailable = mmInStream.available();
                    if(bytesAvailable > 0)
                    {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mmInStream.read(packetBytes);
                        for(int i=0;i<bytesAvailable;i++)
                        {
                            byte b = packetBytes[i];
                            if (b==',')
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                data = new String(encodedBytes, "US-ASCII");
                                String[] parts = data.split(" ");
                                red=parts[0];
                                ir=parts[1];
                                readBufferPosition = 0;


                            }
                            else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }

                    }
                    Log.d("xxxx", "storing and transmitting data");

                }
                catch (IOException e) {
                    Log.d("iiii","can't store");
                    break;
                }
                //    //within while   */


            }    //end of while

        }          //end of run


    }

}








