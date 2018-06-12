package com.example.a82712.jiakao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainPower extends Activity {
    Bundle bundle;
    String currentname;
    int currentid;
    TextView currentName;
    Button startQuiz;
    Button myScore;
    Button quit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bundle = this.getIntent().getExtras();
        currentname = bundle.getString("currentName");
        currentid = bundle.getInt("currentId");

        currentName = findViewById(R.id.currentName);
        startQuiz = findViewById(R.id.startQuiz);
        myScore = findViewById(R.id.myScore);
        quit = findViewById(R.id.quit);

        currentName.setText(currentname);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPower.this.finish();
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPower.this,Question.class);
                Bundle bundle = new Bundle();
                bundle.putInt("currentId",currentid);
                bundle.putString("currentName",currentname);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
