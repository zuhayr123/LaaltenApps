package com.laaltentech.abou.laalten;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MiddleScreen extends Activity {
    int REQUEST_ENABLE_BT = 100;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    Object[] devices = (Object[]) pairedDevices.toArray();
    public String ArrayBt[] = new String[100];
    public static BluetoothDevice device;
    String keyString;
    String name = "details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_list_layout);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        final SharedPreferences.Editor sharedPreferencesEdit = getSharedPreferences(name, MODE_PRIVATE).edit();
        sharedPreferencesEdit.clear();
        sharedPreferencesEdit.commit();
        final CheckBox itemCheckboxes = findViewById(R.id.HC_05_MACs);
        //
        final ListView listViewWithCheckbox = findViewById(R.id.listBT);

        // Initiate listview data.
        final List<ListViewItem> initItemList = this.getInitViewItemDtoList();

        // Create a custom list view adapter with checkbox control.
        final ListViewItemCheckbox listViewDataAdapter = new ListViewItemCheckbox(getApplicationContext(), initItemList);

        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        // When list view item is clicked.
        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                Log.e("data fetched", "onItemClick: "+itemIndex );
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ListViewItem itemDto = (ListViewItem)itemObject;

                // Get the checkbox.
                final CheckBox itemCheckbox = view.findViewById(R.id.HC_05_MACs);
                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    Log.e("CheckBox Clicked", "onItemClick: ");
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    Log.e("CheckBox Clicked", "onItemClick: " );
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }

//                Toast.makeText(getApplicationContext(), "select item text : " , Toast.LENGTH_SHORT).show();
            }
        });


        //
        final Button connectButton = findViewById(R.id.ConnectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MiddleScreen.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
    // Return an initialize list of ListViewItemDTO.
    private List<ListViewItem> getInitViewItemDtoList()
    {
        int devArrayLength = devices.length;
        for(int i = 0; i < devArrayLength; i++) {
            device = (BluetoothDevice) devices[i];
            String devaddReal = device.getAddress();
            ArrayBt[i] = devaddReal;
//            if(device.getName().equals("HC-05")){
//                ArrayBt[i] = devaddReal;
//            }
        }
        List<ListViewItem> ret = new ArrayList<ListViewItem>();

            for (int i = 0; i < ArrayBt.length; i++) {
                String itemText = ArrayBt[i];
                if(itemText != null) {
                    ListViewItem dto = new ListViewItem();
                    dto.setChecked(false);
                    dto.setItemText(itemText);

                    ret.add(dto);
                }
            }


        return ret;
    }
}


