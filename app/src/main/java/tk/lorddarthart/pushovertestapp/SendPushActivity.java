package tk.lorddarthart.pushovertestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class SendPushActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private EditText etMinutes;
    private boolean visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpush);

        etMinutes = findViewById(R.id.etMinutes);

        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (visible) {
                    etMinutes.setVisibility(View.GONE);
                    etMinutes.setText("");
                    visible = false;
                } else {
                    etMinutes.setVisibility(View.VISIBLE);
                    visible = true;
                }
            }
        });
    }
}
