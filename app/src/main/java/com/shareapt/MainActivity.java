package com.shareapt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.shareapt.annotation.BindView;
import com.shareapt.annotation.OnClick;
import com.shareaptfinder.InjectFinder;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 10:16
 * Email：yujunyao@yonglibao.com
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectFinder.inject(this);
        textView.setText("啦啦啦");
    }

    @OnClick(R.id.btn_click)
    public void OnClick() {
        Toast.makeText(this, "事件响应", Toast.LENGTH_SHORT).show();
    }

}
