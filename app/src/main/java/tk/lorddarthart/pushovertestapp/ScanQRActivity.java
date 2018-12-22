package tk.lorddarthart.pushovertestapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tk.lorddarthart.pushovertestapp.DBPush.savePushToHistory;

public class ScanQRActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;
    private EditText txtMsg;
    private int resultz;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private final int requestCameraPermissionID = 1001;
    private boolean visible = false;
    private CheckBox checkBox;
    private Button btnSend;
    private EditText etDK, etNTitle, etNT, etMinutes, etDevID;
    private SQLiteDatabase sqLitePushHistory;
    private DBPush dbPush;
    private TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);

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

        btnSend = findViewById(R.id.btnSendScanQR);

        etDK = findViewById(R.id.etDKScanQR);
        etDevID = findViewById(R.id.etDevIDScanQR);
        etNTitle = findViewById(R.id.etNTitleScanQR);
        etNT = findViewById(R.id.etNTScanQR);
        etMinutes = findViewById(R.id.etMinutesScanQR);


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

        checkBox = findViewById(R.id.checkBoxScanQR);
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

        cameraPreview = findViewById(R.id.surfaceView);
        txtMsg = findViewById(R.id.etNTScanQR);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(size.x, size.y)
                .setAutoFocusEnabled(true)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(ScanQRActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScanQRActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, requestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0) {
                    txtMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(300);
                            txtMsg.setText(qrcodes.valueAt(0).displayValue);
                            barcodeDetector.release();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case requestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(ScanQRActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
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
