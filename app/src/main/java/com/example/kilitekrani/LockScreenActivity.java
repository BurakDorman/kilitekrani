package com.example.kilitekrani;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.example.kilitekrani.PatternView;

public class LockScreenActivity extends AppCompatActivity {

    private TextView textViewTime;
    private TextView textViewDate;
    private TextView textViewNotifications;
    private PatternView patternView;
    private SharedPreferences sharedPreferences;
    private String savedPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        patternView = findViewById(R.id.patternView);
        sharedPreferences = getSharedPreferences("LockScreenPrefs", MODE_PRIVATE);
        savedPattern = sharedPreferences.getString("lockPattern", "");

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int backgroundColor = getBackgroundColorForDay(dayOfWeek);
        findViewById(R.id.layoutLockScreen).setBackgroundColor(backgroundColor);

        patternView.setOnPatternListener(new PatternView.OnPatternListener() {
            @Override
            public void onPatternDetected(String pattern) {
                checkPattern(pattern);
            }
        });

        textViewTime = findViewById(R.id.textViewTime);
        textViewDate = findViewById(R.id.textViewDate);
        textViewNotifications = findViewById(R.id.textViewNotifications);

        updateTimeAndDate();

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    private void checkPattern(String pattern) {
        if (pattern.equals(savedPattern)) {
            Toast.makeText(this, "Desen doğru. Ekran kilidi açılıyor...", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Yanlış desen. Tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateTimeAndDate() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault());

        String currentTime = timeFormat.format(new Date());
        String currentDate = dateFormat.format(new Date());

        textViewTime.setText(currentTime);
        textViewDate.setText(currentDate);
    }
    private int getBackgroundColorForDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return ContextCompat.getColor(this, R.color.colorMonday);
            case Calendar.TUESDAY:
                return ContextCompat.getColor(this, R.color.colorTuesday);
            case Calendar.WEDNESDAY:
                return ContextCompat.getColor(this, R.color.colorWednesday);
            case Calendar.THURSDAY:
                return ContextCompat.getColor(this, R.color.colorThursday);
            case Calendar.FRIDAY:
                return ContextCompat.getColor(this, R.color.colorFriday);
            case Calendar.SATURDAY:
                return ContextCompat.getColor(this, R.color.colorSaturday);
            case Calendar.SUNDAY:
                return ContextCompat.getColor(this, R.color.colorSunday);
            default:
                return Color.WHITE; // Varsayılan olarak beyaz renk
        }
    }
}

