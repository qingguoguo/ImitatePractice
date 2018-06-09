package com.qgg.practice.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.darren.sharecomponent.ShareApplication;
import com.qgg.practice.LogUtils;
import com.qgg.practice.R;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/6
 * @describe :
 */

public class RetrofitTestActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitTestActivity";
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rxjava);
        mImage = (ImageView) findViewById(R.id.image);

        ShareApplication.attach(this);


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DoubanTop250Movie> top250 = RetrofitHttpClient.getServiceApi()
                        .getTop250(0, 2);

                top250.enqueue(new Callback<DoubanTop250Movie>() {

                    @Override
                    public void onResponse(Call<DoubanTop250Movie> call, Response<DoubanTop250Movie> response) {
                       LogUtils.i(TAG, response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<DoubanTop250Movie> call, Throwable t) {
                        LogUtils.i(TAG, t.getMessage());
                    }
                });
            }
        });
    }

}
