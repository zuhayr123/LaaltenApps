package com.laaltentech.abou.laalten;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.WorkManager;

import static android.content.ContentValues.TAG;
import static com.laaltentech.abou.laalten.MainActivity.BROADCAST_ACTION;
import static com.laaltentech.abou.laalten.NotificationPanel.CHANNEL_ID;


public class BluetoothService extends Service {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    Object[] devices = (Object[]) pairedDevices.toArray();
    public BluetoothSocket socket = null;
    public BluetoothDevice device;
    public OutputStream outputStream = null;
    public InputStream inputStream = null;
    public boolean BTState = false;
    boolean StopService = false;
    String dataSending;
    public static final String MY_PREFS_NAME = "ViewResetData";
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte readBuffer[];
    String dataOutput ;
    Thread workerThread;

    boolean[] arrayDataMusic;
    boolean[] arrayDataTheme;
    int[] arrayDataSeekbar;

    MyBroadCastReceiver myBroadCastReceivers;
    MyBroadCastReceiverTheme myBroadCastReceiversTheme;
    MyBroadCastReceiverSeekbar myBroadCastReceiversSeekbar;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//           while(true){
////               Thread threadBeginListeningToData = new Thread(new threadBeginListeningToData());
////               threadBeginListeningToData.start();
////               try {
////                   threadBeginListeningToData.sleep(1000);
////               } catch (InterruptedException e) {
////                   e.printStackTrace();
////               }
//           }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myBroadCastReceivers = new MyBroadCastReceiver();
        myBroadCastReceiversTheme = new MyBroadCastReceiverTheme();
        myBroadCastReceiversSeekbar = new MyBroadCastReceiverSeekbar();
        registerMyReceiver();//Register receiver for HomeFragment data.
        arrayDataMusic = new boolean[6];
        arrayDataTheme = new boolean[20];
        arrayDataSeekbar = new int[6];
        boolean stateBT = false;
//        try {
//            stateBT = BtConnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("LaalTen")
                .setContentText("Tap for more options")
                .setSmallIcon(R.mipmap.done)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, notification);
        new Thread(runnable).start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(myBroadCastReceivers);
        unregisterReceiver(myBroadCastReceiversTheme);
        unregisterReceiver(myBroadCastReceiversSeekbar);
        StopService = true;
        super.onDestroy();
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();

    }
    public boolean BtConnect() throws IOException {
        int devArrayLength = devices.length;
        for(int i = 0; i < devArrayLength; i++) {
            device = (BluetoothDevice) devices[i];
            String devaddReal = device.getAddress();
            if(devaddReal.equals("00:21:13:05:6A:91")){
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                break;
            }
            else{
                socket = null;
            }
        }
        if (!socket.isConnected() && socket != null) {
            socket.connect();
            try {
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                BTState = true;
                } catch (IOException e) {
                BTState = false;
                }
        }

        return BTState;
    }
    class threadBeginListeningToData implements Runnable{
        @Override
        public void run() {
            beginListenForData();
        }
    }

    public void write(String s) throws IOException {
        if(s != null && outputStream != null ) {
            outputStream.write(s.getBytes());
        }
    }

   void beginListenForData()
    {
//        final Handler handler = new Handler(Looper.getMainLooper());
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = inputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "ASCII");
                                    readBufferPosition = 0;

                                    dataOutput = data.replaceAll("\\p{C}", "");
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    //Receives data from HomeFragment.
    class MyBroadCastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            try
            {
                arrayDataMusic = intent.getBooleanArrayExtra("Data");
                saveBooleanArray(arrayDataMusic,"M");
                arrayDataTheme = reset(arrayDataTheme);
                saveBooleanArray(arrayDataTheme,"T");

                arrayDataSeekbar = resetProgressArray(arrayDataSeekbar);
                saveProgressArray(arrayDataSeekbar);

                for(int i = 0; i< arrayDataMusic.length;i++){
                    if(arrayDataMusic[i]){
                        Log.e(TAG, "THIS LIGHT NEED TO GLOW"+ Integer.toString(i) );
                    }
                }
//                write(dataSending);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //Receives data from ThemeFragment.
    class MyBroadCastReceiverTheme extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            try
            {

                arrayDataTheme  = intent.getBooleanArrayExtra("DataFromTheme");
                saveBooleanArray(arrayDataTheme,"T");
                arrayDataMusic = reset(arrayDataMusic);
                saveBooleanArray(arrayDataMusic,"M");
                saveProgressArray(arrayDataSeekbar);

                for(int j = 0; j<arrayDataTheme.length; j++){
                    if(arrayDataTheme[j]){
                        Log.e(TAG, "onReceive: "+"THIS LIGHT NEEDS TO GLOW"+ Integer.toString(j) );
                    }
                }
//                write(dataSending);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //Receives data from ThemeFragmentSeekbar.
    class MyBroadCastReceiverSeekbar extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            try
            {
                arrayDataSeekbar  = intent.getIntArrayExtra("DataFromSeekbar");
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.apply();
                arrayDataTheme = reset(arrayDataTheme);
                saveBooleanArray(arrayDataTheme,"T");
                arrayDataMusic = reset(arrayDataMusic);
                saveBooleanArray(arrayDataMusic,"M");
                saveProgressArray(arrayDataSeekbar);
                String result = seekBarArrayDataConditioner(arrayDataSeekbar);
                Log.e(TAG, "onReceive: "+result);


//                write(dataSending);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void registerMyReceiver() {

        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            registerReceiver(myBroadCastReceivers, intentFilter);

            IntentFilter intentFilterTheme = new IntentFilter();
            intentFilterTheme.addAction("BroadcastedTheme");
            registerReceiver(myBroadCastReceiversTheme,intentFilterTheme);

            IntentFilter intentFilterSeekbar = new IntentFilter();
            intentFilterSeekbar.addAction("BroadcastedSeekbar");
            registerReceiver(myBroadCastReceiversSeekbar,intentFilterSeekbar);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.LaalTen";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.done)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    //recently written functions need testing.

    void saveBooleanArray(boolean [] data,String s){
        int length = data.length;
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        for(int i = 0; i< length;i++){
            editor.putBoolean(s+ Integer.toString(i), data[i]);
        }
        editor.apply();
    }

    boolean[] reset(boolean [] dataArray){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        int length = dataArray.length;

        for(int i = 0; i< length ; i++){
            dataArray[i] = false;
        }
        editor.apply();
        return  dataArray;
    }

    int[] resetProgressArray(int[] arrayDataSeekbar){
        int length = arrayDataSeekbar.length;

        for(int i = 0; i< length; i++){
            arrayDataSeekbar[i] = 0;
        }
        return arrayDataSeekbar;
    }

    void saveProgressArray(int[] arrayDataSeekbar){
        int length = arrayDataSeekbar.length;
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        for(int i = 0; i< length ; i++){
            editor.putInt("P"+ Integer.toString(i), arrayDataSeekbar[i]);
        }
        editor.apply();
    }

    String seekBarArrayDataConditioner(int [] seekBarArray){

        Log.e(TAG, Arrays.toString(seekBarArray));
        int red = seekBarArray[0];
        int green = seekBarArray[1];
        int blue = seekBarArray[2];
        int brightness = seekBarArray[3];


        //keep the ratio of R:G:B alive all the time
        red = (int)(red * 2.5);
        green = (int)(green * 2.5);
        blue = (int)(blue * 2.5);
        //One more data needs to be sent that is brightness from C code.

        return "r"+String.format("%03d", red)+"g"+String.format("%03d", green)+"b"+String.format("%03d", blue)+ "i"+ String.format("%03d", brightness)+"@";

    }
}
