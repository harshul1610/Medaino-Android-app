package com.medaino.www.medainoandroid;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class klopresults extends Activity
{
    BluetoothSocket mmSocket;
    InputStream mmInStream=null;
    OutputStream mmOutStream=null;
    String data=new String(" ");
    String red=new String("0");
    String ir=new String("0");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klopresults);
        BluetoothDevice tmpl = (BluetoothDevice) getIntent().getExtras().getParcelable("key3");

        ConnectThread con_bl = new ConnectThread(tmpl);
        con_bl.start();
        Log.d("1","pass1");
        hr_resp rateobj=new hr_resp();
        Log.d("2","pass2");
        rateobj.start();

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

    class hr_resp extends Thread
    {
        int N=64;
        double[] real=new double[N+1];
        double[] imag=new double[N+1];
        double[] mag=new double[N+1];
        double[] freq=new double[N+1];
        fft fftobj;


        public void run() {
            int i=0,j=1;

           while(true) {
               for (i = 0, j = 1; i < N; i++)
               {

                   int x = Integer.parseInt(ir);
                   real[i] = (double) x;
                   imag[i] = (double) 0;
                   freq[i] = ((1 / 0.200) * j) / N;
                   j++;
               }
               fftobj = new fft(real, imag, N);
               mag = fftobj.cal_fft();
               Log.d("6", "pass6");
           }
        }

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



    public class fft
    {
        int N;
        double[] inreal;
        double[] inimag;
        double[] out;


        public fft(double[]real1,double[]imag1,int N1)
        {

            this.N=N1;
            inreal=new double[N+1];
            inimag=new double[N+1];
            out=new double[N+1];
            int j=0;
            for(j=0;j<N;j++)
            {
                inreal[j]=real1[j];
                inimag[j]=imag1[j];
            }
        }
        public double[] cal_fft()
        {
            int span,subpart,node,n=0,k=0;
            double temp,imagtwiddle,realtwiddle,primitive_root,angle;
            for(span=N;span>=1;span>>=1)
            {

                primitive_root=((2*3.14)/N);

                for(subpart=0;subpart<((N>>1))/span;subpart++)
                {
                    for( node=0;node<span;node++)
                    {
                        temp=inreal[n]+inreal[n+span];
                        inreal[n+span]=inreal[n]-inreal[n+span];
                        inreal[n]=temp;
                        temp=inimag[n]+inimag[n+span];
                        inimag[n+span]=inimag[n]-inimag[n+span];
                        inimag[n]=temp;

                        angle=primitive_root*node;
                        realtwiddle=Math.cos(angle);
                        imagtwiddle=Math.sin(angle);
                        temp=realtwiddle*inreal[n+span]-imagtwiddle*inimag[n+span];
                        inimag[n+span]=realtwiddle*inimag[n+span]+imagtwiddle*inreal[n+span];
                        inreal[n+span]=temp;

                        n++;
                    }
                    n=((n+span)&(N-1));
                }

            }

            for(k=0;k<N;k++)
            {
                out[k]=Math.sqrt(inreal[k]*inreal[k]+inimag[k]*inimag[k]);
            }
            return out;
















































        }
    }

}








