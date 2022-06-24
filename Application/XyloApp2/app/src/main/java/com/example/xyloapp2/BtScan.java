package com.example.xyloapp2;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.service.controls.Control;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class BtScan extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;

    String noDevicesPaired = "No paired devices yet...";
    ArrayList<String> pairedDevices = new ArrayList<>();

    public ListView lvBtPaired;
    public ArrayAdapter<String> arrAdapter;
    public BluetoothAdapter btAdapter;

    public boolean grantedPermission;
    AlertDialog btConnectDialog;


    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_scan);
        btConnectDialog = grantPermissionsDialog(getWindow().getContext(),
                "Warning",
                "This app needs permission to access nearby devices (Bluetooth stuff) to work.",
                "Grant",
                "Nope"
        );

        grantedPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

        if (!grantedPermission)
            btConnectDialog.show();
        else {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            lvBtPaired = findViewById(R.id.bt_conns);
            arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pairedDevices);

            queryPairedDevices();

            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            lvBtPaired.setOnItemClickListener(listListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bt_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        grantedPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

        switch (item.getItemId()) {
            case R.id.refreshView:
                finish();
                startActivity(getIntent());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!grantedPermission)
            promptForPermissions();
    }

    public void promptForPermissions() {
        grantedPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

        if (!grantedPermission)
            btConnectDialog.show();
    }

    private AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String clickedString = ((TextView) view).getText().toString();
            if (!clickedString.equals(noDevicesPaired)) {
                String[] deviceInfo = clickedString.split("\n");
                String name = deviceInfo[0];
                String mac = deviceInfo[1];

                Intent intent = new Intent(BtScan.this, MainActivity.class);
                intent.putExtra("add", mac);
                startActivity(intent);
            }
        }
    };

    public void openBtSettings(View view) {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }

    public AlertDialog grantPermissionsDialog(Context context, CharSequence title, CharSequence msg, CharSequence okText, CharSequence cancelText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Clicked OK
                Intent intentOpenAppSettings = new Intent();
                intentOpenAppSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intentOpenAppSettings.setData(uri);
                startActivity(intentOpenAppSettings);
            }
        });
        builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Clicked CANCEL
                dialogInterface.cancel();
            }
        });

        builder.setIcon(R.drawable.ic_baseline_warning_amber_24);

        return builder.create();
    }

    public AlertDialog listDialog(Context context, CharSequence title, String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                // index -> index positino of the selected item
            }
        });

        return builder.create();
    }

    public AlertDialog okCancelDialog(Context context, CharSequence title, CharSequence msg, CharSequence okText, CharSequence cancelText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Clicked OK
            }
        });
        builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Clicked CANCEL
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }

    public void printlnDialog(String str) {
        AlertDialog dialogPrint = okCancelDialog(getWindow().getContext(), "Debug", str, "ok", "cancel");
        dialogPrint.show();
    }

    public boolean checkIfListEmpty() {
        return lvBtPaired.getCount() == 0;
    }

    public void queryPairedDevices() {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        this.pairedDevices = new ArrayList<>();

        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                this.pairedDevices.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            this.pairedDevices.add(noDevicesPaired);
        }

        updateArrayAdapter();
        lvBtPaired.setAdapter(arrAdapter);
    }

    public void updateArrayAdapter() {
        arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pairedDevices);
    }

}