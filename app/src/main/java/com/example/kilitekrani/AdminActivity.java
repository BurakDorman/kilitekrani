package com.example.kilitekrani;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AdminActivity extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Admin modu: aktif", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Admin modu : deaktif", Toast.LENGTH_SHORT).show();
    }
}