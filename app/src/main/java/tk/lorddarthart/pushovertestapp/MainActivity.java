package tk.lorddarthart.pushovertestapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean isOpen = false;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SQLiteDatabase sqLitePushHistory;
    private DBPush dbPush;
    private ArrayList<PushMessage> pushmsg = new ArrayList<>();
    private Cursor cursor;

    private void initializeAdapter() {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), pushmsg);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void getTodayEvents() {
        cursor.moveToFirst();
        cursor.moveToPrevious();
        pushmsg.clear();
        while (cursor.moveToNext()) {
            PushMessage msg = new PushMessage();
            msg.setPushkey(cursor.getString(cursor.getColumnIndex(dbPush.PUSHHISTORY_PUSHKEY)));
            msg.setPushtitle(cursor.getString(cursor.getColumnIndex(dbPush.PUSHHISTORY_PUSHTITLE)));
            msg.setPushtime(cursor.getLong(cursor.getColumnIndex(dbPush.PUSHHISTORY_PUSHTIME)));
            msg.setPushtext(cursor.getString(cursor.getColumnIndex(dbPush.PUSHHISTORY_PUSHTEXT)));
            pushmsg.add(msg);
        }
        initializeAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvMain);

        dbPush = new DBPush(this, DBPush.DATABASE_NAME, null, DBPush.DATABASE_VERSION);
        sqLitePushHistory = dbPush.getWritableDatabase();
        String query = "SELECT " + DBPush.PUSHHISTORY_PUSHKEY + ", " + DBPush.PUSHHISTORY_PUSHTITLE + ", " + DBPush.PUSHHISTORY_PUSHTEXT + ", "
                + DBPush.PUSHHISTORY_PUSHTIME + " FROM " + DBPush.DATABASE_PUSHHISTORY +
                " ORDER BY " + DBPush.PUSHHISTORY_PUSHTIME + " DESC";
        cursor = sqLitePushHistory.rawQuery(query, new String[0]);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
        getTodayEvents();
    }
}
