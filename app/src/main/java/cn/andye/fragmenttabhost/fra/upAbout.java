package cn.andye.fragmenttabhost.fra;

import android.app.Activity;
import android.os.Bundle;

import cn.andye.fragmenttabhost.R;

/**
 * Created by Administrator on 2018/10/3 0003.
 */

public class upAbout extends Activity{
    private String val;//做接按键的信号接受1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutup);
        val=getIntent().getStringExtra("flag");//接受

    }
}
