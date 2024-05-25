package com.example.kilitekrani;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kilitekrani.PatternView;

import java.util.Random;

public class SetPatternActivity extends AppCompatActivity {

    private PatternView patternView;
    private Button btnSavePattern;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        getWindow().getDecorView().setBackgroundColor(color);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pattern);

        patternView = findViewById(R.id.patternView);
        btnSavePattern = findViewById(R.id.btnSavePattern);
        sharedPreferences = getSharedPreferences("LockScreenPrefs", MODE_PRIVATE);

        btnSavePattern.setOnClickListener(view -> savePattern());
    }

    private void savePattern() {
        // Kullanıcının çizdiği deseni al
        String pattern = patternView.getPattern();

        // Deseni SharedPreferences üzerine kaydet
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lockPattern", pattern);
        editor.apply();

        Toast.makeText(this, "Kilit ekranı deseni başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();

        // Bu aktiviteyi kapat ve ana ekrana dön
        finish();
    }
}
