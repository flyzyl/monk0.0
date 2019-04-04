package cn.andye.fragmenttabhost.clock;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import cn.andye.fragmenttabhost.fra.Home1Fra;

import cn.andye.fragmenttabhost.R;

public class PlayAlarmAty extends Activity{
	private MediaPlayer mp;
	int shour=0,smin=0;
	String data1,data2,data3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//						|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//						|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		);//能点亮屏幕，能在锁屏的时候显示，自动解锁（无密码时），保持屏幕常亮
		setContentView(R.layout.alarm_player_aty);
		Intent intent=getIntent();
		data1=intent.getStringExtra("shour");
		data2=intent.getStringExtra("smin");
		data3=intent.getStringExtra("trans");
		if (!data1.equals("non")){
			shour=Integer.valueOf(data1);
			if(!data2.equals("non")){
				smin=Integer.valueOf(data2);
			}else {
				smin=0;
			}
		}
		Log.d("kk", "开始执行"+shour);
//		PowerManager manager = (PowerManager) getSystemService(this.POWER_SERVICE);
//		WakeLock newWakeLock = manager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
//		newWakeLock.acquire();//点亮屏幕(常亮)
//		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//		KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
//		keyguardLock.disableKeyguard();
		mp = MediaPlayer.create(this, R.raw.music);
		mp.start();
		TextView grade=(TextView)findViewById(R.id.kkkk);
		grade.setText(data3);

	}
	
	@Override
	protected void onPause() {
		super.onPause();

//		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("kk", "已经被回收 ");
		Toast.makeText(this,"通知被回收",Toast.LENGTH_SHORT).show();
		Home1Fra.getHome1Fra().deleteAlarm(shour,smin);
		mp.stop();
		mp.release();
	}
}
