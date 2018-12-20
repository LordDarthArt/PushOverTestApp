package tk.lorddarthart.pushovertestapp;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton fabMain = findViewById(R.id.fabMain);
        final FloatingActionButton fabSendPush = findViewById(R.id.fabSend);
        final FloatingActionButton fabScanQR = findViewById(R.id.fabScan);
        final ConstraintLayout tvSendPush = findViewById(R.id.clTvSend);
        final ConstraintLayout tvScanQR = findViewById(R.id.clTvScan);
        final ConstraintLayout clTransp = findViewById(R.id.clTransp);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = new AnimateFab().animateFab(getApplicationContext(), isOpen, clTransp, fabMain, fabSendPush, fabScanQR, tvSendPush, tvScanQR);
            }
        });
        fabSendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendPushActivity.class);
                finish();
                startActivity(intent);
            }
        });
        fabScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
