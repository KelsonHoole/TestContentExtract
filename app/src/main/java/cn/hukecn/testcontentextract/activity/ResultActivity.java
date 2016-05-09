package cn.hukecn.testcontentextract.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.hukecn.testcontentextract.R;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        String content = intent.getStringExtra("content");

        ((TextView)findViewById(R.id.content)).setText(content);
        ((TextView)findViewById(R.id.info)).setText(info);

    }
}
