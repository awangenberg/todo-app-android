package com.example.finaltest.home;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finaltest.R;
import com.example.finaltest.databinding.ActivityHomeBinding;
import com.example.finaltest.home.goals.GoalsFragment;
import com.example.finaltest.home.todos.TodosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private GoalsFragment goalsFragment;
    private TodosFragment todosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        todosFragment = new TodosFragment();

        goalsFragment = new GoalsFragment();

        switchToFragment(goalsFragment);

        navView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_goals) {
                switchToFragment(goalsFragment);
                return true;
            }
            if (item.getItemId() == R.id.navigation_todos) {

                switchToFragment(todosFragment);
                return true;
            }

            return false;
        });


        var actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.actionbar_title_goalMaker);
        }

        setupDailyReminder();
    }

    private void setupDailyReminder() {
        var calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        var alarmTimeMillis = calendar.getTimeInMillis();
        var alarmIntent = new Intent(this, AlarmReceiver.class);
        var pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);
        var alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms();
        }

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                pendingIntent);
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_home, fragment, GoalsFragment.TAG)
                .commit();
    }
}