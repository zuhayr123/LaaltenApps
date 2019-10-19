package com.laaltentech.abou.laalten;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.WorkManager;

import static android.content.ContentValues.TAG;
import static com.laaltentech.abou.laalten.MainActivity.BROADCAST_ACTION;
import static com.laaltentech.abou.laalten.NotificationPanel.CHANNEL_ID;


public class BluetoothService extends Service implements Visualizer.OnDataCaptureListener{
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

    //
    double magnitude[] = new double[512];
    double re[] = new double[1024];
    double im[] = new double[1024];
    int maxInd;
    int maxmagg;
    private static final int CAPTURE_SIZE = 1024;
    private Visualizer visualiser;
    public static String cl;
    //

    String oldString = "";
    String musicdata = "";
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

//            while(true){
//                Thread myThread = new Thread(new threadmusic());
//                try {
//                    Thread.sleep(100);
//                    if(musicdata.equals("r000g255b255i099@")) {
//                        musicdata = "r255g000b000i099@";
//                        Log.e(TAG, "run: " + "This was called");
//                    }
//                    else{
//                        musicdata = "r000g255b255i099@";
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                myThread.start();
//            }
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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
            startVisualiser();// Set you media player to the visualizer.
        }
        try {
            BtConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (outputStream != null) {
            try {outputStream.close();} catch (Exception e) {}
            outputStream = null;
        }

        if (socket != null) {
            try {socket.close();} catch (Exception e) {}
            socket = null;
        }
        unregisterReceiver(myBroadCastReceivers);
        unregisterReceiver(myBroadCastReceiversTheme);
        unregisterReceiver(myBroadCastReceiversSeekbar);
        StopService = true;
        super.onDestroy();
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (outputStream != null) {
            try {outputStream.close();} catch (Exception e) {}
            outputStream = null;
        }

        if (socket != null) {
            try {socket.close();} catch (Exception e) {}
            socket = null;
        }
        stopSelf();

    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int i) {
        if(arrayDataMusic[0]){

            double maxMag = Double.NEGATIVE_INFINITY;
            for (int h = 0; h < fft.length / 2; h++) {
                re[h] = fft[2 * h];
                im[h] = fft[2 * h + 1];
                magnitude[h] = Math.sqrt(re[h] * re[h] + im[h] * im[h]);
                if (magnitude[h] > maxMag) {
                    maxInd = h*60;
                    maxMag = magnitude[h];
                    maxmagg = (int) ((maxMag/14)*10);
                }
            }

            if(maxInd>100 && maxInd<300 && maxmagg>1){

                if (maxmagg <100) {
                    cl = "r000g255b255i"+String.format("%03d", maxmagg)+"@";
                }
            }

            else if(maxInd>300 && maxInd<600 && maxmagg>1){

                if (maxmagg <100) {
                    cl = "r255g000b255i"+String.format("%03d", maxmagg)+"@";
                }
            }
            else if(maxInd>600 && maxInd<900 && maxmagg>1){
                if (maxmagg <100) {
                    cl = "r000g000b255i"+String.format("%03d", maxmagg)+"@";
                }

            }
            else if(maxInd>900 && maxInd<1200 && maxmagg>1){
                if (maxmagg <100) {
                    cl = "r125g000b255i"+String.format("%03d", maxmagg)+"@";
                }
            }
            else if(maxInd>1200 && maxInd<1500 && maxmagg>1){
                if (maxmagg <100) {
                    cl = "r200g255b255i"+String.format("%03d", maxmagg)+"@";
                }
            }
            else if(maxInd>1500 && maxInd<2000 && maxmagg>1){
                if (maxmagg <100) {
                    cl = "r200g000b255i"+String.format("%03d", maxmagg)+"@";
                }
            }
            else{
                cl = "r000g000b000i000@";
            }

            try {
                write(cl);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        else if(arrayDataMusic[1]){
//            Thread myThread = new Thread(new threadmusic());
//            try {
//
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            myThread.start();
//        }
    }

    public boolean BtConnect() throws IOException {
        int devArrayLength = devices.length;
        for(int i = 0; i < devArrayLength; i++) {
            device = (BluetoothDevice) devices[i];
            Log.e(TAG, "BtConnect: "+device.getAddress() );
            String devaddReal = device.getAddress();
            if(devaddReal.equals("98:D3:31:F5:C1:A1")){
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                break;
            }
            else{
                socket = null;
            }
        }
        assert socket != null;
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
            if (!s.equals(oldString)) {
                outputStream.write(s.getBytes());
                oldString = s;
            }
        }
    }

    class threadmusic implements Runnable{
        @Override
        public void run() {
            try {
                randomState();
                write(musicdata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void beginListenForData() {
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

                if(arrayDataMusic[4]){
                    write("x@");
                }
                else if(arrayDataMusic[2]){
                    write("r255g255b000i100@");
                }
                else if(arrayDataMusic[5]){
                    write("r255g000b000i100@");
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
                String resultColor = setButtonColor(arrayDataTheme);
//                for(int j = 0; j<arrayDataTheme.length; j++){
//                    if(arrayDataTheme[j]){
//                        Log.e(TAG, "onReceive: "+"THIS LIGHT NEEDS TO GLOW"+ Integer.toString(j) );
//                    }
//                }
                String resultTheme = setButtonColor(arrayDataTheme);
                Log.e(TAG, "onReceiveTheme: "+resultTheme );
                write(resultTheme);
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
//                arrayDataTheme = reset(arrayDataTheme);
                saveBooleanArray(arrayDataTheme,"T");
                arrayDataMusic = reset(arrayDataMusic);
                saveBooleanArray(arrayDataMusic,"M");
                saveProgressArray(arrayDataSeekbar);
                String result = seekBarArrayDataConditioner(arrayDataSeekbar);
                write(result);
                Log.e(TAG, "onReceiveSeekbar: "+result);


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

    String setButtonColor(boolean[] buttonState){
        int red = 0;
        int green = 0;
        int blue = 0;
        int brightness = arrayDataSeekbar[3];
        if(buttonState[0]){
            int bgColor =  ContextCompat.getColor(this, R.color.primaryDarkGreen);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[1]){
            int bgColor =  ContextCompat.getColor(this, R.color.kindOfPurple);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[2]){
            int bgColor =  ContextCompat.getColor(this, R.color.primaryDarkPink);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[3]){
            int bgColor =  ContextCompat.getColor(this, R.color.kindOfYellow);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[4]){
            int bgColor =  ContextCompat.getColor(this, R.color.primaryBlue);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[5]){
            int bgColor =  ContextCompat.getColor(this, R.color.primaryPurple);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[6]){
            int bgColor =  ContextCompat.getColor(this, R.color.contrastYellow);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[7]){
            int bgColor =  ContextCompat.getColor(this, R.color.colorAccent);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[8]){
            int bgColor =  ContextCompat.getColor(this, R.color.primaryGreen);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[9]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_amber_A700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[10]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_brown_800);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[11]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_cyan_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[12]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_deep_orange_A700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[13]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_indigo_700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[14]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_red_900);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[15]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_blue_light_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[16]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_deep_purple_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[17]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_green_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[18]){
            int bgColor =  ContextCompat.getColor(this, R.color.md_pink_700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[19]){
            int bgColor =  ContextCompat.getColor(this, R.color.kindOfPink);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }
        return "r"+String.format("%03d", red)+"g"+String.format("%03d", green)+"b"+String.format("%03d", blue)+ "i"+ String.format("%03d", brightness)+"@";

    }

    public void randomState () throws IOException {
        Random r = new Random();
        int i1 = r.nextInt(6 + 1);

        while(arrayDataMusic[1]) {
            if (i1 == 0) {
                musicdata = "a099@";
            } else if (i1 == 1) {
                musicdata = "b099@";
            } else if (i1 == 2) {
                musicdata = "c099@";
            } else if (i1 == 3) {
                musicdata = "d099@";
            } else if (i1 == 4) {
                musicdata = "e099@";
            } else if (i1 == 5) {
                musicdata = "f099@";
            } else {
                musicdata = "f000@";
            }
        }
    }

    public void startVisualiser()
    {
        visualiser = new Visualizer(0);
        visualiser.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, true);
        visualiser.setEnabled(false);
        visualiser.setCaptureSize(CAPTURE_SIZE);
        visualiser.setEnabled(true);
    }

}