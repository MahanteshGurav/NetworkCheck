package com.mahantesh.networkcheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MyReceiver.ConnectivityReceiverListener {

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter;
    MyReceiver receiver;
    private View parent_view;
    private Context context;
    private AppCompatButton btnCheck;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        parent_view = findViewById(android.R.id.content);

        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new MyReceiver();
        btnCheck = findViewById(R.id.btnCheck);
        parent_view = findViewById(android.R.id.content);
        snackbar = Snackbar.make(parent_view, "", Snackbar.LENGTH_SHORT);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Tools.checkForInternet(context)) {
                    showSnack(false);
                } else {
                    showSnack(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void showSnack(boolean isConnected) {
        String message = "";
        int duration;
        if (!isConnected) {
            message = "No connection";
            duration = Snackbar.LENGTH_INDEFINITE;
        } else {
            message = "Back online";
            duration = Snackbar.LENGTH_LONG;
        }
        snackbar = Snackbar.make(parent_view, message, duration);

        if (!isConnected) {
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Tools.checkForInternet(context)) {
                        showSnack(false);
                    } else {
                        showSnack(true);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        } else {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green_900));
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}

