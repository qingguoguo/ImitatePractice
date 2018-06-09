package com.qgg.practice.RxLogin;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.darren.sharecomponent.ShareApplication;
import com.qgg.practice.R;

import io.reactivex.functions.Consumer;

public class TestRxLoginActivity extends AppCompatActivity {

    private static final String TAG = "TestRxLoginActivity";
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rxjava);
        mImage = (ImageView) findViewById(R.id.image);

        ShareApplication.attach(this);
        mImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                RxLogin.create(TestRxLoginActivity.this)
                        .doAuthVerify(RxLoginPlatform.PLATFORM_QQ)
                        .subscribe(new Consumer<RxLoginResult>() {
                            @Override
                            public void accept(RxLoginResult rxLoginResult) throws Exception {
                                if (rxLoginResult.isSucceed()) {
                                    Toast.makeText(TestRxLoginActivity.this, rxLoginResult.getMsg(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TestRxLoginActivity.this, rxLoginResult.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
