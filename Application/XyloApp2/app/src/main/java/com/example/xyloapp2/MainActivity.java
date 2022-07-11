package com.example.xyloapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private View decorView;
    public boolean grantedPermission;

    AlertDialog btConnectDialog;

    /* HC-05 Stuff */
    private ProgressDialog progress;
    String address;
    BluetoothAdapter btAdapter;
    BluetoothSocket btSocket;
    private boolean isConnected = false;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ArrayList<Button> keys = new ArrayList<>();

    Button a, b, c, d, e, f, g, h, i, j, k, l;
    Button m, n, o, p, r, s, t, u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        btConnectDialog = grantPermissionsDialog(getWindow().getContext(),
                "Warning",
                "This app needs permission to access nearby devices (Bluetooth stuff) to work.",
                "Grant",
                "Nope"
                );

        initKeys();
        promptForPermissions();

        if (grantedPermission) {
            Intent intent = getIntent();
            address = intent.getStringExtra("add");

            new ConnectBT().execute(); // start the connection

            handleKeysPressed();
        }
    }

    private void handleKeysPressed() {
        ////////////////////// NOT SHARPS
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('a');
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('b');
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('c');
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('d');
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('e');
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('f');
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('g');
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('h');
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('i');
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('j');
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('k');
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('l');
            }
        });


        ////////////////////// SHARPS
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('m');
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('n');
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('o');
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('p');
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('r');
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('s');
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('t');
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote('u');
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Disconnect();
    }

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                printMsg(e.getMessage(), false);
            }
        }
    }

    protected void initKeys() {
        // NOT SHARPS
        a = (Button) findViewById(R.id.C3);
        b = (Button) findViewById(R.id.D3);
        c = (Button) findViewById(R.id.E3);
        d = (Button) findViewById(R.id.F3);
        e = (Button) findViewById(R.id.G3);
        f = (Button) findViewById(R.id.A4);
        g = (Button) findViewById(R.id.B4);
        h = (Button) findViewById(R.id.C4);
        i = (Button) findViewById(R.id.D4);
        j = (Button) findViewById(R.id.E4);
        k = (Button) findViewById(R.id.F4);
        l = (Button) findViewById(R.id.G4);

        // SHARPS
        m = (Button) findViewById(R.id.Csh3);
        n = (Button) findViewById(R.id.Dsh3);
        o = (Button) findViewById(R.id.Fsh3);
        p = (Button) findViewById(R.id.Gsh3);
        r = (Button) findViewById(R.id.Ash4);
        s = (Button) findViewById(R.id.Csh4);
        t = (Button) findViewById(R.id.Dsh4);
        u = (Button) findViewById(R.id.Fsh4);
    }

    @Override
    protected void onResume() {
        super.onResume();

        promptForPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        grantedPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

        switch (item.getItemId()) {
            case R.id.bluetoothButton:
                if (!grantedPermission)
                    btConnectDialog.show();
                else {
                    Intent intent = new Intent(MainActivity.this, BtScan.class);
                    startActivity(intent);
                }
                break;

            case R.id.btDisconnect:
                Disconnect();
                Intent intent = new Intent(MainActivity.this, BtScan.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN;

    }

    private int hideAllBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void promptForPermissions() {
        grantedPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

        if (!grantedPermission)
            btConnectDialog.show();
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

    public AlertDialog okCancelDialog(boolean finish, Context context, CharSequence title, CharSequence msg, CharSequence okText, CharSequence cancelText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Clicked OK
                if (finish)
                    finish();
            }
        });
        if (!finish) {
            builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Clicked CANCEL
                    dialogInterface.cancel();
                }
            });

            builder.setIcon(R.drawable.ic_baseline_coffee_24);
        } else {
            builder.setIcon(R.drawable.ic_baseline_error_24);
        }

        return builder.create();
    }

    public void printMsg(String msg, boolean finish) {
        AlertDialog dialogMsg = okCancelDialog(finish, getWindow().getContext(), "Hey!", msg, "ok", "cancel");
        dialogMsg.show();
    }

    /* ############## BUTTON CLICK LISTENERS ############## */
    private void playNote(char note) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(note);
            } catch (IOException e) {
                printMsg(e.getMessage(), false);
            }
        }
    }


    /* ***************************************************** */
    /* ******************** SUB-CLASSES ******************** */
    /* ***************************************************** */
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Drink your coffee while you wait ;)");
            progress.setIcon(R.drawable.ic_baseline_coffee_24);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (btSocket == null || !isConnected) {
                    btAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(address);
                    btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                connectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (!connectSuccess) {
                printMsg("Connection failed!\n\nMaybe the device you're trying to connect to isn't HC-05 or HC-06?", true);
                // finish();
            } else {
                printMsg("Connected :)", false);
                isConnected = true;
            }
            progress.dismiss();
        }
    }
}