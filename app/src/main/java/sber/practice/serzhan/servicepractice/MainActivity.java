package sber.practice.serzhan.servicepractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button activateServiceButton;
    private Button startSecondActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        activateServiceButton = findViewById(R.id.activate_button);
        startSecondActivityButton = findViewById(R.id.next_button);

    }
    private void initListeners() {
        activateServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(MyService.newIntent(MainActivity.this));
            }
        });
        startSecondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SecondActivity.newIntent(MainActivity.this));
            }
        });
    }
}
