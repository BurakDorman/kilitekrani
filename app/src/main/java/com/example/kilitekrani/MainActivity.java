package com.example.kilitekrani;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MaterialButton lock, disable, enable, pattern;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        getWindow().getDecorView().setBackgroundColor(color);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, AdminActivity.class);

        lock = findViewById(R.id.btnLockScreen);
        enable = findViewById(R.id.btnAdmin);
        disable = findViewById(R.id.btnRemoveAdmin);
        pattern = findViewById(R.id.btnSetPattern);

        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        pattern.setOnClickListener(this);

        updateAdminButtonsVisibility();
    }

    @Override
    public void onClick(View view) {
        if (view == lock) {
            if (isAdminActive()) {
                startActivity(new Intent(MainActivity.this, LockScreenActivity.class));
            } else {
                Toast.makeText(this, "Admin cihaz özelliklerini aktif etmelisiniz.", Toast.LENGTH_SHORT).show();
            }
        } else if (view == enable) {
            enableAdmin();
        } else if (view == disable) {
            disableAdmin();
        } else if (view == pattern) {
            startActivity(new Intent(MainActivity.this, SetPatternActivity.class));
        }
    }

    private void enableAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Bu izne neden ihtiyacımız olduğunu şöyle açıklayabiliriz.");
        startActivityForResult(intent, RESULT_ENABLE);
    }

    private void disableAdmin() {
        devicePolicyManager.removeActiveAdmin(compName);
        updateAdminButtonsVisibility();
    }

    private void updateAdminButtonsVisibility() {
        boolean isActive = isAdminActive();
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    private boolean isAdminActive() {
        return devicePolicyManager.isAdminActive(compName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ENABLE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(MainActivity.this, "Admin cihaz özelliklerini aktif ettik.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Admin cihaz özellikleri aktif edilemiyor.", Toast.LENGTH_SHORT).show();
            }
            updateAdminButtonsVisibility();
        }
    }
}
