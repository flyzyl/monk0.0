package cn.andye.fragmenttabhost.clock;
import cn.andye.fragmenttabhost.fra.Home1Fra;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("kk","闹钟执行了");
		Intent intent1=intent;
		String data1=intent1.getStringExtra("shour");
		String data2=intent1.getStringExtra("smin");
		String data3=intent1.getStringExtra("trans");
		Log.d("kk", "onCreate: "+data1+":"+data2);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context, AlarmReceiver.class), 0));
		Intent i = new Intent(context, PlayAlarmAty.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("shour",data1);//名，值
		i.putExtra("smin",data2);//名，值
		i.putExtra("trans",data3);//名，值
		context.startActivity(i);
	}
}
