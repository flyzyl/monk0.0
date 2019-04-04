package cn.andye.fragmenttabhost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.andye.fragmenttabhost.Service.TimeService;
import cn.andye.fragmenttabhost.fra.Home1Fra;
import cn.andye.fragmenttabhost.fra.Home2Fra;
import cn.andye.fragmenttabhost.fra.Home3Fra;
import cn.andye.fragmenttabhost.save.ifDB;
import cn.andye.fragmenttabhost.save.phoneDB;


//主活动
public class MainActivity extends FragmentActivity implements OnClickListener,Home1Fra.Messagesent{
    ArrayList data;
    private ImageView mBt1, mBt2, mBt3, mBt4;
    private ImageView mSelBg;
    private LinearLayout mTab_item_container;
    private FragmentManager mFM = null;
    private ifDB noteDB;//数据库对象


    LinearLayout content_container, content_container2;
    Intent m_Intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        changePerson();
        Intent startIntent = new Intent(this, TimeService.class);
        startIntent.putExtra("normal","start");
        startService(startIntent);//后台进程

    }

    private void init() {

        mTab_item_container = (LinearLayout) findViewById(R.id.tab_item_container);

        mBt1 = (ImageView) findViewById(R.id.tab_bt_1);
        mBt2 = (ImageView) findViewById(R.id.tab_bt_2);
        mBt3 = (ImageView) findViewById(R.id.tab_bt_3);

        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);

        mSelBg = (ImageView) findViewById(R.id.tab_bg_view);
        LayoutParams lp = mSelBg.getLayoutParams();
        lp.width = mTab_item_container.getWidth() / 3;
        
        content_container = (LinearLayout) findViewById(R.id.content_container);
        content_container2 = (LinearLayout) findViewById(R.id.content_container2);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        LayoutParams lp = mSelBg.getLayoutParams();
        lp.width = mTab_item_container.getWidth() / 3;
    }

    private int mSelectIndex = 0;
    private View last, now;
    View v1, v2;

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.tab_bt_1:
            last = mTab_item_container.getChildAt(mSelectIndex);
            now = mTab_item_container.getChildAt(0);
            startAnimation(last, now);
            mSelectIndex = 0;
            changePerson();
            break;
        case R.id.tab_bt_2:
            last = mTab_item_container.getChildAt(mSelectIndex);
            now = mTab_item_container.getChildAt(1);
            startAnimation(last, now);
            mSelectIndex = 1;
            changeBussiness();
            break;
        case R.id.tab_bt_3:
            last = mTab_item_container.getChildAt(mSelectIndex);
            now = mTab_item_container.getChildAt(2);
            startAnimation(last, now);
            mSelectIndex = 2;

            changeMessage();
            break;

        default:
            break;
        }
    }

    private void startAnimation(View last, View now) {
        TranslateAnimation ta = new TranslateAnimation(last.getLeft(), now.getLeft(), 0, 0);
        ta.setDuration(300);
        ta.setFillAfter(true);
        mSelBg.startAnimation(ta);
    }

    /**
     * �л�fragement
     */
    private void changePerson() {
        Fragment f = new Home1Fra();
        if (null == mFM)
            mFM = getSupportFragmentManager();
        FragmentTransaction ft = mFM.beginTransaction();
        ft.replace(R.id.content_container, f);
        ft.commit();
    }

    /**
     * �л�fragement
     */
    public void changeBussiness() {
        Fragment f2 = new Home2Fra();
        Bundle bundle=new Bundle();
        bundle.putStringArrayList("kk",data);
        f2.setArguments(bundle);
        if (null == mFM)
            mFM = getSupportFragmentManager();
        FragmentTransaction ft = mFM.beginTransaction();
        ft.replace(R.id.content_container, f2);
        ft.commit();
    }

    /**
     * �л�fragement
     */
    public void changeMessage() {
        Fragment f = new Home3Fra();
        if (null == mFM)
            mFM = getSupportFragmentManager();
        FragmentTransaction ft = mFM.beginTransaction();
        ft.replace(R.id.content_container, f);
        ft.commit();
    }

    /**
     * �л�fragement
     */


    private static Boolean isQuit = false;
    private Timer timer = new Timer();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isQuit == false) {
                isQuit = true;
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
            }
        } else {
        }
        return false;
    }



    @Override
    public void sentmessage(int n) {
        super.onResume();
        TextView tv1=(TextView)findViewById(R.id.mess);
        Log.d("kk", "sentmessage: 执行");
        if(String.valueOf(n)!=null) {
            tv1.setText(String.valueOf(n));
        }
    }


    public void onDestroy( ) {
        super.onDestroy();
        Log.d("abcd", "mainonDestroy: ");
        Toast.makeText(this,"main停止",Toast.LENGTH_SHORT).show();
    }

}
