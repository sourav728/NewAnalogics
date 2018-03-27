package com.example.tvd.newanalogics;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;

public class ConnectActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    ListView listDevicesFound;
    Button btnScanDevice, backBtnSET;
    TextView stateBluetooth, btaddressTv;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    static final UUID MY_UUID = UUID.randomUUID();
    String address = "";
    EditText bluetoothTXT;
    public final String DATA_PATH1 = Environment.getExternalStorageDirectory()
            + "/";

    ArrayAdapter<String> btArrayAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        btaddressTv = (TextView) findViewById(R.id.btaddTV);
        // ***********************************************************

        try {

            FileInputStream fstream = new FileInputStream(DATA_PATH1
                    + "BTaddress1.txt");

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                btaddressTv.setText(strLine);
            }

            in.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());

        }

        // ***********************************************************

        btnScanDevice = (Button) findViewById(R.id.scandevice);

        stateBluetooth = (TextView) findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listDevicesFound = (ListView) findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        bluetoothTXT = (EditText) findViewById(R.id.bluetoothAds);

        CheckBlueToothState();

        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

        registerReceiver(ActionFoundReceiver, new IntentFilter(
                BluetoothDevice.ACTION_FOUND));

        listDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

				/*
				 * Toast.makeText(getApplicationContext(),
				 * ""+listDevicesFound.getCount(), Toast.LENGTH_SHORT).show();
				 */

                String selection = (String) (listDevicesFound
                        .getItemAtPosition(position));
                Toast.makeText(getApplicationContext(),
                        "BLUETOOTH ADDRESS IS SAVED SUCCESSFULLY",
                        Toast.LENGTH_SHORT).show();

                address = selection.substring(0, 17);
                bluetoothTXT.setText(address);

                try {
                    File myFile = new File(DATA_PATH1 + "BTaddress1.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(address);
                    myOutWriter.close();
                    fOut.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                Alertmessage();

            }
        });

        // *********************back Button***********************************
        backBtnSET = (Button) findViewById(R.id.backBtnSET);
        backBtnSET.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(ConnectActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();

            }

        });

        // **************************************************************

        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device1 : devices) {
            btArrayAdapter.add(  device1.getAddress() + "\n"
                    + device1.getName());

        }
    }



    private void CheckBlueToothState() {
        if (bluetoothAdapter == null) {
            stateBluetooth.setText("Bluetooth NOT support");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    stateBluetooth
                            .setText("Bluetooth is currently in device discovery process.");
                } else {
                    stateBluetooth.setText("Bluetooth is Enabled.");
                    btnScanDevice.setEnabled(true);
                }
            } else {
                stateBluetooth.setText("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private Button.OnClickListener btnScanDeviceOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            btArrayAdapter.clear();
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device1 : devices) {

                btArrayAdapter.add(  device1.getAddress() + "\n"
                        + device1.getName());

            }
            bluetoothAdapter.startDiscovery();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBlueToothState();
        }
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                btArrayAdapter.add(device.getAddress() + "\n"
                        + device.getName());

                btArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    public void Alertmessage() {

        if (mBluetoothAdapter == null) {

            Toast.makeText(this, "Bluetooth is not available.",
                    Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this,
                    "Please enable your BT and re-run this program.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.ActionFoundReceiver);
    }
}
