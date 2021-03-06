package tk.lorddarthart.pushovertestapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import static tk.lorddarthart.pushovertestapp.DBPush.savePushToHistory;

import net.pushover.client.PushoverClient;
import net.pushover.client.PushoverRestClient;

import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendPushActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private Button btnSend;
    private EditText etDK, etNTitle, etNT, etMinutes, etDevID;
    private boolean visible = false;
    private SQLiteDatabase sqLitePushHistory;
    private DBPush dbPush;
    private TextWatcher textWatcher;
    private int resultz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpush);

        btnSend = findViewById(R.id.btnSend);

        dbPush = new DBPush(this, DBPush.DATABASE_NAME, null, DBPush.DATABASE_VERSION);
        sqLitePushHistory = dbPush.getWritableDatabase();

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDK.getText().length() > 0 && etNTitle.getText().length() > 0 && etNT.getText().length() > 0) {
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etDK = findViewById(R.id.etDK);
        etDevID = findViewById(R.id.etDevID);
        etNTitle = findViewById(R.id.etNTitle);
        etNT = findViewById(R.id.etNT);
        etMinutes = findViewById(R.id.etMinutes);

        InputFilter filter= new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    String checkMe = String.valueOf(source.charAt(i));

                    Pattern pattern = Pattern.compile("[1234567890]");
                    Matcher matcher = pattern.matcher(checkMe);
                    boolean valid = matcher.matches();
                    if(!valid){
                        return "";
                    }
                }
                return null;
            }
        };

        etMinutes.setFilters(new InputFilter[]{filter});

        if (etDK.getText().length() > 0 && etNTitle.getText().length() > 0 && etNT.getText().length() > 0) {
            btnSend.setEnabled(true);
        } else {
            btnSend.setEnabled(false);
        }

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

        etDK.addTextChangedListener(textWatcher);
        etNTitle.addTextChangedListener(textWatcher);
        etNT.addTextChangedListener(textWatcher);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final SendPushOverNotification spon = new SendPushOverNotification();
                    if (etMinutes.getText().length() > 0) {
                        new Timer().schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        try {
                                            spon.execute().get();
                                            savePushToHistory(sqLitePushHistory, System.currentTimeMillis()
                                                            + (Long.valueOf(etMinutes.getText().toString()) * 60000),
                                                    etNTitle.getText().toString(), etNT.getText().toString(),
                                                    etDK.getText().toString());
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                (Long.valueOf(etMinutes.getText().toString()) * 60000
                        ));
                    } else {
                        spon.execute().get();
                        savePushToHistory(sqLitePushHistory, System.currentTimeMillis(),
                                etNTitle.getText().toString(), etNT.getText().toString(),
                                etDK.getText().toString());
                    }
                    Snackbar.make(v, "Уведомление отправлено", Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Snackbar.make(v, "Произошла чудовищная ошибка", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    protected class SendPushOverNotification extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                if (etDevID.getText().length() > 0) {
                    resultz = new PushOverSend().sendPush("awr7rp2vn8t32aij7adf28kk8bdbby", etDK.getText().toString(), etDevID.getText().toString(), etNTitle.getText().toString(), etNT.getText().toString());
                } else {
                    resultz = new PushOverSend().sendPush("awr7rp2vn8t32aij7adf28kk8bdbby", etDK.getText().toString(), etNTitle.getText().toString(), etNT.getText().toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
