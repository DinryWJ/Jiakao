package com.example.a82712.jiakao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Conclude extends Activity {
    TextView result;
    Button mywrongs;
    Button back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conclude);
        ActivityController.addActivity(this);
        Bundle bundle = this.getIntent().getExtras();
        int val = bundle.getInt("result");
        result = findViewById(R.id.chengji);
        result.setText(val+"");
        mywrongs = findViewById(R.id.mywrongs);
        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.finishAll();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
