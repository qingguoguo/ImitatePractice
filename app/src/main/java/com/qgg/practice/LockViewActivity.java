package com.qgg.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.qgg.practice.view.LockView;

public class LockViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_view);

        final TextView textView = (TextView) findViewById(R.id.password);
        LockView lockView = (LockView) findViewById(R.id.lock);
        lockView.setOnUnlockedListener(new LockView.onUnlockedListener() {
            @Override
            public boolean unlocking(String password) {
                textView.setText("密码：" + password);
                if ("123456".equals(password)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
