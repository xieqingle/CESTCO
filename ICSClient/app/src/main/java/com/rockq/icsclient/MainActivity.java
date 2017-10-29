package com.rockq.icsclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    /**
     *
     */
    @BindView(R.id.et_port)
    EditText mEtPort;
    @BindView(R.id.et_message)
    EditText mEtMessage;
    @BindView(R.id.tv_message)
    TextView mTvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    public void createServer(View view) {
    }

    public void sendMessage(View v) {

    }
}
