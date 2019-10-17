package com.laaltentech.abou.laalten;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;
import static com.laaltentech.abou.laalten.FragmentHome.BROADCASTED;

public class FragmentTheme extends Fragment {

    Button colors [];
    boolean booleans [];
    int seekbarData[];

    TextView progressRed;
    TextView progressGreen;
    TextView progressBlue;
    TextView progressBrightness;

    SeekBar seekBarRed;
    SeekBar seekBarGreen;
    SeekBar seekBarBlue;
    SeekBar seekBarBrightness;

    SharedPreferences pref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_two, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        colors = new Button[20];
        booleans = new boolean[20];
        seekbarData = new int[4];

        colors[0] = getActivity().findViewById(R.id.button);
        colors[1] = getActivity().findViewById(R.id.button2);
        colors[2] = getActivity().findViewById(R.id.button3);
        colors[3] = getActivity().findViewById(R.id.button4);
        colors[4] = getActivity().findViewById(R.id.button5);
        colors[5] = getActivity().findViewById(R.id.button6);
        colors[6] = getActivity().findViewById(R.id.button7);
        colors[7] = getActivity().findViewById(R.id.button8);
        colors[8] = getActivity().findViewById(R.id.button9);
        colors[9] = getActivity().findViewById(R.id.button10);
        colors[10] = getActivity().findViewById(R.id.button12);
        colors[11] = getActivity().findViewById(R.id.button13);
        colors[12] = getActivity().findViewById(R.id.button14);
        colors[13] = getActivity().findViewById(R.id.button15);
        colors[14] = getActivity().findViewById(R.id.button16);
        colors[15] = getActivity().findViewById(R.id.button17);
        colors[16] = getActivity().findViewById(R.id.button19);
        colors[17] = getActivity().findViewById(R.id.button20);
        colors[18] = getActivity().findViewById(R.id.button21);
        colors[19] = getActivity().findViewById(R.id.button22);

        progressRed = getActivity().findViewById(R.id.progress_Red);
        progressGreen = getActivity().findViewById(R.id.progressGreen);
        progressBlue = getActivity().findViewById(R.id.progressBlue);
        progressBrightness = getActivity().findViewById(R.id.progressBrightness);

        seekBarRed = getActivity().findViewById(R.id.seekBar);
        seekBarGreen = getActivity().findViewById(R.id.seekBar2);
        seekBarBlue = getActivity().findViewById(R.id.seekBar4);
        seekBarBrightness = getActivity().findViewById(R.id.seekBar5);

        pref = getActivity().getSharedPreferences("ViewResetData", 0); // 0 - for private mode
        for(int i = 0; i<booleans.length;i++){
            booleans[i] = pref.getBoolean("T"+Integer.toString(i), false);
        }

        for(int i = 0; i< seekbarData.length;i++){
            seekbarData[i] = pref.getInt("P"+Integer.toString(i),0);
        }

//        for(int i = 0; i<=19;i++){
//            booleans[i] = false;
//        }

        for(int count = 0; count< booleans.length; count++){
            if(booleans[count]){
                setStateOfTheView(count);
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    colors[count].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                }
            }
        }

        seekBarRed.setProgress(seekbarData[0]);
        seekBarGreen.setProgress(seekbarData[1]);
        seekBarBlue.setProgress(seekbarData[2]);
        seekBarBrightness.setProgress(seekbarData[3]);

        colors[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[0] = !booleans[0];
                if(booleans[0]){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[0].setForeground(ContextCompat.getDrawable(getActivity(), R.mipmap.done));
                        int [] instArray = setButtonColor(booleans);
                        seekBarRed.setProgress(instArray[0]);
                        seekBarGreen.setProgress(instArray[1]);
                        seekBarBlue.setProgress(instArray[2]);
                    }
                    setStateOfTheView(0);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[0].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[0] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[1] = !booleans[1];
                if(booleans[1]){
                    setStateOfTheView(1);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[1].setForeground(ContextCompat.getDrawable(getActivity(),R.color.colorTransparent));
                    }
                    booleans[1] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[2] = !booleans[2];
                if(booleans[2]){
                    setStateOfTheView(2);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[2].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[2] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[3] = !booleans[3];
                if(booleans[3]){
                    setStateOfTheView(3);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[3].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[3] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[4] = !booleans[4];
                if(booleans[4]){
                    setStateOfTheView(4);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[4].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[4] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[5] = !booleans[5];
                if(booleans[5]){
                    setStateOfTheView(5);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[5].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[5] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[6] = !booleans[6];
                if(booleans[6]){
                    setStateOfTheView(6);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[6].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[6] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[7] = !booleans[7];
                if(booleans[7]){
                    setStateOfTheView(7);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[7].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[7] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[8] = !booleans[8];
                if(booleans[8]){
                    setStateOfTheView(8);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[8].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[8] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[9] = !booleans[9];
                if(booleans[9]){
                    setStateOfTheView(9);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[9].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[9] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[10] = !booleans[10];
                if(booleans[10]){
                    setStateOfTheView(10);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[10].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[10] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[11] = !booleans[11];
                if(booleans[11]){
                    setStateOfTheView(11);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[11].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[11] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[12] = !booleans[12];
                if(booleans[12]){
                    setStateOfTheView(12);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[12].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[12] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[13] = !booleans[13];
                if(booleans[13]){
                    setStateOfTheView(13);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[13].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[13] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[14] = !booleans[14];
                if(booleans[14]){
                    setStateOfTheView(14);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[14].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[14] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[15] = !booleans[15];
                if(booleans[15]){
                    setStateOfTheView(15);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[15].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[15] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[16].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[16] = !booleans[16];
                if(booleans[16]){
                    setStateOfTheView(16);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[16].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[16] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[18].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[18] = !booleans[18];
                if(booleans[18]){
                    setStateOfTheView(18);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[18].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[18] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[19].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[19] = !booleans[19];
                if(booleans[19]){
                    setStateOfTheView(19);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[19].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[19] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        colors[17].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[17] = !booleans[17];
                if(booleans[17]){
                    setStateOfTheView(17);
                    int [] instArray = setButtonColor(booleans);
                    seekBarRed.setProgress(instArray[0]);
                    seekBarGreen.setProgress(instArray[1]);
                    seekBarBlue.setProgress(instArray[2]);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colors[17].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
                    }
                    booleans[17] = false;
                }
                sendMyBroadCastFromTheme();
            }
        });

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressRed.setText(String.valueOf(i) +"%");
//                int length = colors.length;
//
//                for(int count = 0; count< length; count++){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        colors[count].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
//                    }
//                    booleans[count] = false;
//                }
                seekbarData[0] = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                sendMyBroadCastSeekbar();

            }
        });


        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressGreen.setText(String.valueOf(i) +"%");
//                int length = colors.length;
//
//                for(int count = 0; count< length; count++){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        colors[count].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
//                    }
//                    booleans[count] = false;
//                }
                seekbarData[1] = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                sendMyBroadCastSeekbar();

                Log.e(TAG, "onStopTrackingTouch: "+"DIRECT TOUCH" );

            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressBlue.setText(String.valueOf(i) +"%");
//                int length = colors.length;
//
//                for(int count = 0; count< length; count++){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        colors[count].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
//                    }
//                    booleans[count] = false;
//                }
                seekbarData[2] = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendMyBroadCastSeekbar();

            }
        });

        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressBrightness.setText(String.valueOf(i) + "%");
//                int length = colors.length;
//
//                for(int count = 0; count< length; count++){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        colors[count].setForeground(ContextCompat.getDrawable(getActivity(), R.color.colorTransparent));
//                    }
//                    booleans[count] = false;
//                }
                seekbarData[3] = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendMyBroadCastSeekbar();

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    void setStateOfTheView(int stateView){
        for (int num = 0; num<= 19; num++){
            if(num == stateView){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    colors[num].setForeground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.mipmap.done));
                }
                booleans[num] = true;
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    colors[num].setForeground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.color.colorTransparent));
                }
                booleans[num] = false;
            }

        }

    }

    //Send data from theme fragment from buttons

    private void sendMyBroadCastFromTheme()
    {
        try
        {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction("BroadcastedTheme");

            broadCastIntent.putExtra("DataFromTheme", booleans);

            Objects.requireNonNull(getActivity()).sendBroadcast(broadCastIntent);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //Send data from theme fragment from buttons

    private void sendMyBroadCastSeekbar()
    {
        try
        {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction("BroadcastedSeekbar");

            broadCastIntent.putExtra("DataFromSeekbar", seekbarData);

            Objects.requireNonNull(getActivity()).sendBroadcast(broadCastIntent);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    int[] setButtonColor(boolean[] buttonState){
        int [] arrayProgressTilesSet = new int[3];
        int red = 0;
        int green = 0;
        int blue = 0;
        if(buttonState[0]){
            int bgColor =  ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.primaryDarkGreen);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[1]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.kindOfPurple);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[2]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.primaryDarkPink);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[3]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.kindOfYellow);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[4]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.primaryBlue);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[5]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.primaryPurple);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[6]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.contrastYellow);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[7]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.colorAccent);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[8]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.primaryGreen);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[9]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_amber_A700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[10]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_brown_800);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[11]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_cyan_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[12]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_deep_orange_A700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[13]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_indigo_700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[14]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_red_900);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[15]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_blue_light_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[16]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_deep_purple_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[17]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_green_600);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[18]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.md_pink_700);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }

        else if(buttonState[19]){
            int bgColor =  ContextCompat.getColor(getActivity(), R.color.kindOfPink);
            red = Color.red(bgColor);
            green = Color.green(bgColor);
            blue = Color.blue(bgColor);
        }
        arrayProgressTilesSet[0] = (int) (red/2.55);
        arrayProgressTilesSet[1] = (int) (green/2.55);
        arrayProgressTilesSet[2] = (int) (blue/2.55);

        return arrayProgressTilesSet;
    }


}
