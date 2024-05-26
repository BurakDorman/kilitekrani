package com.example.kilitekrani;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetPatternActivity extends AppCompatActivity {

    private PatternView patternView;
    private Button btnSavePattern;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pattern);

        patternView = findViewById(R.id.patternView);
        btnSavePattern = findViewById(R.id.btnSavePattern);
        sharedPreferences = getSharedPreferences("LockScreenPrefs", MODE_PRIVATE);

        btnSavePattern.setOnClickListener(view -> savePattern());
    }

    private void savePattern() {
        String pattern = patternView.getPattern();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lockPattern", pattern);
        editor.apply();

        Toast.makeText(this, "Kilit ekranı deseni başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
