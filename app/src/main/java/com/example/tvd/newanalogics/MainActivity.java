package com.example.tvd.newanalogics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.example.tvd.newanalogics.values.FunctionCalls;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


@SuppressLint("NewApi")
public class MainActivity extends Activity {

    Button print;
    Button Settings_Btn;
    EditText ET_print;
    BluetoothAdapter bluetoothAdapter;
    Typeface typeface_DroidSansMono;
    private static final int REQUEST_ENABLE_BT = 1;
    Bluetooth_Printer_3inch_prof_ThermalAPI api;
    FunctionCalls fcall;

    protected String btAddressDir = Environment.getExternalStorageDirectory() + "";
    String address = null;
    AnalogicsThermalPrinter conn = new AnalogicsThermalPrinter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Full Screen

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        print = (Button) findViewById(R.id.print_btn);
        Settings_Btn = (Button) findViewById(R.id.Settings_Btn);
        api = new Bluetooth_Printer_3inch_prof_ThermalAPI();
        fcall = new FunctionCalls();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    conn.openBT(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 5000);

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(BTReceiver, filter1);
        this.registerReceiver(BTReceiver, filter2);
        this.registerReceiver(BTReceiver, filter3);

        CheckBlueToothState();
        // ***********************************************************
        File f = new File(btAddressDir + "/BTaddress1.txt");

        if (f.exists()) {

            try {

                FileInputStream fstream = new FileInputStream(btAddressDir + "/BTaddress1.txt");

                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                while ((strLine = br.readLine()) != null) {
                    address = strLine;
                    Log.d("Debugg", "BluetoothAddress" + address);
                }

                in.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());

            }
        } else {

            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            startActivity(intent);
            finish();
        }

        // *********************Print Button************************

        print.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                try {
                    /**************For Image Printing use below code***********************/
                   /* typeface_DroidSansMono = Typeface.createFromAsset(getAssets(), "fonts/DroidSansMono.ttf");

                    conn.multiLinguallinePrint(address,"" , 22, typeface_DroidSansMono,15);
                    conn.multiLinguallinePrint(address,"" , 22, typeface_DroidSansMono,10);
                    conn.multiLinguallinePrint(address,"नमस्ते ಹೈ"+ "BA123345",22,typeface_DroidSansMono,4);
                   *//* conn.multiLinguallinePrint(address,"HUBLI ELECTRICITY BOARD\n",30,typeface_DroidSansMono,4);
                    conn.multiLinguallinePrint(address,"SubDivision   : Belagavi" , 22, typeface_DroidSansMono,3);
                    conn.multiLinguallinePrint(address,"RR Number     : (540038)" , 22, typeface_DroidSansMono,3);
                    conn.multiLinguallinePrint(address,"Account ID    : BA123345" , 22, typeface_DroidSansMono,3);
                    conn.multiLinguallinePrint(address,"Mtr.Rdr.Code  : 54008301" , 22, typeface_DroidSansMono,3);
                    conn.multiLinguallinePrint(address,"" , 18, typeface_DroidSansMono,4);
                    conn.multiLinguallinePrint(address,"Anil Reddy G" , 18, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Opp:-GSI, Bandlaguda" , 18, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Banglore" , 18, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Tariff        : 5LT2A2" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Sanct.Load    : 0KW:   2" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Billing Period: 10/10/2017-24/11/2017" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Reading Date  : 24/11/2017" , 22, typeface_DroidSansMono,2);

                    conn.multiLinguallinePrint(address,"Bill Number   : 1234567" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"MeterSl.No.   : 5000101010" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Pres.Rdg      : 120" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Prev.Rdg      : 220" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Constant      : 1" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Consumption   : 100" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Average       : 250" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Recorded MD   : 1" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"PowerFactor   : 0.75" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"1.0  x   40.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"1.0  x   40.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"" , 22, typeface_DroidSansMono,3);
                    conn.multiLinguallinePrint(address,"30.0   x  3.25      97.50" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"70.0   x  4.70      329.00 " , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"100.0  x  6.25      528.75" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"150.0  x  7.15      1075.50" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Rebates/TOD Charges    :   00.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"PP Penalty             :   00.00 " , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Ex.Load/MD Penalty     :   00.00" , 22, typeface_DroidSansMono,2);

                    conn.multiLinguallinePrint(address,"Intrest                :   00.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Others                 :   00.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Tax                    :   50.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Current Bill Amt       :   2107.00" , 22, typeface_DroidSansMono,5);
                    conn.multiLinguallinePrint(address,"Arrears                :   00.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"Credits&Adj.           :   00.00" , 22, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"GOK Subsidy            :   00.00" , 22, typeface_DroidSansMono,1);
                    conn.multiLinguallinePrint(address,"NetAmt.Due      :   3102.00" , 30, typeface_DroidSansMono,2);
                    conn.multiLinguallinePrint(address,"DueDate                :   25/11/2017" , 22, typeface_DroidSansMono,0);*//*
                    Bluetooth_Printer_3inch_prof_ThermalAPI printer = new Bluetooth_Printer_3inch_prof_ThermalAPI();
                    String feeddata = "";
                    feeddata = printer.barcode_Code_128_Alpha_Numerics_VIP("ANA1234567");
                    conn.printData(feeddata);

                    conn.multiLinguallinePrint(address,"" , 22, typeface_DroidSansMono,15);
                    conn.multiLinguallinePrint(address,"" , 22, typeface_DroidSansMono,10);
*/

                    /******************For Text Printing use below code*********************/
                    StringBuilder stringBuilder = new StringBuilder();
                    analogics_header__double_print(fcall.aligncenter("HUBLI ELECTRICITY SUPPLY COMPANY LTD", 38), 6);
                    analogicsprint(fcall.aligncenter("Belagavi", 30), 6);

                    analogicsprint(fcall.space("Sub Division", 16) + ":" + " " + "540038", 6);
                    analogicsprint(fcall.space("RRNO", 16) + ":" + " " + "BA12345", 6);
                    analogics_double_print(fcall.space("Account ID", 16) + ":" + " " + "1234567890", 6);
                    analogics_48_print(fcall.aligncenter("Name and Address", 48), 6);
                    analogics_48_print("Mr. XYZ", 3);
                    analogics_48_print("Peneya, Bangalore", 3);
                    analogics_48_print("Sub register office", 6);

                    analogicsprint(fcall.space("Tariff", 16) + ":" + " " + "5LT2A2", 6);
                    analogicsprint(fcall.space("Sanct Load", 14) + ":" + "HP:" + fcall.alignright("0", 4) + " " + "KW:" + fcall.alignright("2", 4), 6);
                    analogicsprint(fcall.space("Billing", 8) + ":" + "10/10/2017" + "-" + "10/11/2017", 6);
                    analogicsprint(fcall.space("Reading Date", 16) + ":" + " " + "10/11/2017", 6);
                    analogicsprint(fcall.space("BillNo", 7) + ":" + " " + "1234567890" + "-" + "10/11/2017", 6);
                    analogicsprint(fcall.space("Meter SlNo.", 16) + ":" + " " + "5000101010", 6);
                    analogicsprint(fcall.space("Pres Rdg", 16) + ":" + " " + "4000", 6);
                    analogicsprint(fcall.space("Prev Rdg", 16) + ":" + " " + "3000", 6);
                    analogicsprint(fcall.space("Constant", 16) + ":" + " " + "1", 6);
                    analogicsprint(fcall.space("Consumption", 16) + ":" + " " + "250", 6);
                    analogicsprint(fcall.space("Average", 16) + ":" + " " + "250", 6);

                    stringBuilder.append("\n");

                    analogicsprint(fcall.space("Rebates/TOD", 11) + "(-)" + ":" + " " + fcall.alignright("0.00", 14), 5);
                    analogicsprint(fcall.space("PF Penalty", 14) + ":" + " " + fcall.alignright("0.00", 14), 5);
                    analogicsprint(fcall.space("MD Penalty", 14) + ":" + " " + fcall.alignright("0.00", 14), 5);
                    analogicsprint(fcall.space("Interest", 11) + "@1%" + ":" + " " + fcall.alignright("1.00", 14), 5);
                    analogicsprint(fcall.space("Others", 14) + ":" + " " + fcall.alignright("0.05", 14), 5);
                    analogicsprint(fcall.space("Tax", 11) + "@6%" + ":" + " " + fcall.alignright("0.01", 14), 5);
                    analogicsprint(fcall.space("Cur Bill Amt", 14) + ":" + " " + fcall.alignright("100.00", 14), 6);
                    analogicsprint(fcall.space("Arrears", 14) + ":" + " " + fcall.alignright("0.00", 14), 4);
                    analogicsprint(fcall.space("Credits&Adj", 11) + "(-)" + ":" + " " + fcall.alignright("0.00", 14), 4);
                    analogicsprint(fcall.space("GOK Subsidy", 11) + "(-)" + ":" + " " + fcall.alignright("0.05", 14), 0);
                    analogics_double_print(fcall.space("Net Amt Due", 14) + ":" + " " + fcall.alignright("300.00", 14), 0);
                    analogicsprint(fcall.space("Due Date", 14) + ":" + " " + fcall.alignright("25/11/2017", 14), 4);
                    analogicsprint(fcall.space("Billed On", 12) + ":" + " " + fcall.alignright(fcall.currentDateandTime(), 16), 6);

                    print_bar_code("1234567890" + "300");
                    analogicsprint(fcall.space(" ", 3) + "12345678" + "54003801", 6);
                    stringBuilder.setLength(0);
                    stringBuilder.append("\n");
                    stringBuilder.append("\n");
                    stringBuilder.append("\n");
                    analogicsprint(stringBuilder.toString(), 4);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        Settings_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    private void CheckBlueToothState() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {

            Toast.makeText(getBaseContext(), "Bluetooth is NOT Enabled", Toast.LENGTH_LONG).show();

        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    Toast.makeText(getBaseContext(), "Bluetooth is currently in device discovery process.",
                            Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getBaseContext(), "Bluetooth is Enabled.", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getBaseContext(), "Bluetooth is NOT Enabled.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver BTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Do something if connected
                Toast.makeText(getApplicationContext(), "BT Device Connected..", Toast.LENGTH_SHORT).show();
                print.setVisibility(View.VISIBLE);
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Do something if disconnected
                Toast.makeText(getApplicationContext(), "BT Device Disconnected..", Toast.LENGTH_SHORT).show();
                print.setVisibility(View.INVISIBLE);
            }

        }
    };

    public void analogics_double_print(String Printdata, int feed_line) {

        conn.printData(api.font_Double_Height_On_VIP());
        analogicsprint(Printdata, feed_line);
        conn.printData(api.font_Double_Height_Off_VIP());

    }

    public void analogics_header_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_38_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void text_line_spacing(int space) {
        conn.printData(api.variable_Size_Line_Feed_VIP(space));
    }

    public void analogics_48_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_48_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void analogicsprint(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_30_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    private void print_bar_code(String msg) {
        String feeddata = "";
        feeddata = api.barcode_Code_128_Alpha_Numerics_VIP(msg);
        conn.printData(feeddata);
    }

    public void analogics_header__double_print(String Printdata, int feed_line) {

        try {
            conn.printData(api.font_Double_Height_On_VIP());
            analogics_header_print(Printdata, feed_line);
            conn.printData(api.font_Double_Height_Off_VIP());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BTReceiver);
    }

}