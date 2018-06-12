package com.example.a82712.jiakao;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Test extends Activity{
    SQLiteDatabase db;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoretable);
        db = SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir().toString()
                        + "/Jiakao.db3", null);
        listView = findViewById(R.id.listview);
        getMyScore(1);

    }
    private void getMyScore(int uid) {
        Cursor cursor = db.rawQuery("select * from t_user where id = ? "
                , new String[] {uid+""});

        while (cursor.moveToNext()){
            //TODO cursor.
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen())
        {
            db.close();
        }
    }

}
