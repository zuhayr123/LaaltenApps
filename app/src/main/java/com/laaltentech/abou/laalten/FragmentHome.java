package com.laaltentech.abou.laalten;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static android.content.ContentValues.TAG;

public class FragmentHome extends Fragment {

    public static final String BROADCASTED= "broadcast";

    Button musicButton;
    Button strobeButton;
    Button soothingButton;
    Button ambientButton;
    Button intenseButton;
    Button micButton;

    TextView textMusic;
    TextView textStrobe;
    TextView textIntense;
    TextView textSoothing;
    TextView textMic;
    TextView textAmbient;

    TextView textViews[];
    ConstraintLayout constraintLayouts[];
    boolean booleans[];

    ConstraintLayout musicConstraint;
    ConstraintLayout strobeConstraint;
    ConstraintLayout intenseConstraint;
    ConstraintLayout ambientConstraint;
    ConstraintLayout soothingConstraint;
    ConstraintLayout micConstraint;

    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        textViews = new TextView[6];
        constraintLayouts = new ConstraintLayout[6];
        booleans = new boolean[6];

        musicButton = Objects.requireNonNull(getActivity()).findViewById(R.id.musicButton);
        strobeButton = Objects.requireNonNull(getActivity()).findViewById(R.id.strobeButton);
        ambientButton = Objects.requireNonNull(getActivity()).findViewById(R.id.ambientButton);
        intenseButton = Objects.requireNonNull(getActivity()).findViewById(R.id.intenseButton);
        micButton = Objects.requireNonNull(getActivity()).findViewById(R.id.microphoneButton);
        soothingButton = Objects.requireNonNull(getActivity()).findViewById(R.id.soothingButton);


        musicConstraint = getActivity().findViewById(R.id.constraintLayout5);
        strobeConstraint = getActivity().findViewById(R.id.constraintLayout7);
        ambientConstraint = getActivity().findViewById(R.id.constraintLayout6);
        micConstraint = getActivity().findViewById(R.id.constraintLayout4);
        soothingConstraint = getActivity().findViewById(R.id.constraintLayout8);
        intenseConstraint = getActivity().findViewById(R.id.constraintLayout9);

        textMusic = getActivity().findViewById(R.id.musicText);
        textStrobe = getActivity().findViewById(R.id.strobeText);
        textAmbient = getActivity().findViewById(R.id.ambientText);
        textMic = getActivity().findViewById(R.id.micText);
        textSoothing = getActivity().findViewById(R.id.soothingText);
        textIntense = getActivity().findViewById(R.id.intenseText);

        pref = getActivity().getSharedPreferences("ViewResetData", 0); // 0 - for private mode
        for(int i = 0; i<booleans.length;i++){
            booleans[i] = pref.getBoolean("M"+Integer.toString(i), false);
        }

        textViews[0] = textMusic;
        textViews[1] = textStrobe;
        textViews[2] = textAmbient;
        textViews[3] = textMic;
        textViews[4] = textSoothing;
        textViews[5] = textIntense;

        constraintLayouts[0] = musicConstraint;
        constraintLayouts[1] = strobeConstraint;
        constraintLayouts[2] = ambientConstraint;
        constraintLayouts[3] = micConstraint;
        constraintLayouts[4] = soothingConstraint;
        constraintLayouts[5] = intenseConstraint;

        for(int count = 0; count< booleans.length; count++){
            if(booleans[count]){
                setStateOfTheView(count);
            }
            else{
                textViews[count].setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    constraintLayouts[count].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                }
            }
        }

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[0] = !booleans[0];
                if(booleans[0]) {
                    setStateOfTheView(0);
                }
                else{
                    textViews[0].setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        constraintLayouts[0].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                    }
                    booleans[0] = false;
                }
                sendMyBroadCast();
            }
        });

        strobeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[1] = !booleans[1];
                if(booleans[1]){
                    setStateOfTheView(1);
                }

                else{
                    textViews[1].setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        constraintLayouts[1].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                    }
                    booleans[1] = false;
                }
                sendMyBroadCast();
            }
        });

        intenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[5] = !booleans[5];
                if(booleans[5]){
                    setStateOfTheView(5);
                }
                else{
                    textViews[5].setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        constraintLayouts[5].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                    }
                    booleans[5] = false;
                }
                sendMyBroadCast();
            }
        });

        ambientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[2] = !booleans[2];
                if(booleans[2]){
                    setStateOfTheView(2);
                }
                else{
                    textViews[2].setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        constraintLayouts[2].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                    }
                    booleans[2] = false;
                }
                sendMyBroadCast();
            }
        });

        soothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[4] = !booleans[4];
                if(booleans[4]){
                    setStateOfTheView(4);
                }
                else{
                    textViews[4].setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        constraintLayouts[4].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                    }
                    booleans[4] = false;
                }
                sendMyBroadCast();
            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleans[3] = !booleans[3];
                if(booleans[3]){
                    setStateOfTheView(3);
                }
                else{
                    textViews[3].setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        constraintLayouts[3].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                    }
                    booleans[3] = false;
                }
                sendMyBroadCast();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    void setStateOfTheView(int stateView){
        for (int num = 0; num<= 5; num++){
            if(num == stateView){
                textViews[num].setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    constraintLayouts[num].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners_selected));
                }
                booleans[num] = true;
            }
            else{
                textViews[num].setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    constraintLayouts[num].setForeground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corners));
                }
                booleans[num] = false;
            }

        }

    }

    private void sendMyBroadCast()
    {
        try
        {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(BROADCASTED);

            broadCastIntent.putExtra("Data", booleans);

            Objects.requireNonNull(getActivity()).sendBroadcast(broadCastIntent);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
