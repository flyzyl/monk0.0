package cn.andye.fragmenttabhost.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.andye.fragmenttabhost.model.Comment;
import cn.andye.fragmenttabhost.model.Time;
import cn.andye.fragmenttabhost.fra.Home1Fra;
import cn.andye.fragmenttabhost.save.phoneDB;


public class TimeService extends Service {
    final static String  TAG = "abcd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//实例化
    java.util.Date currentDate = new java.util.Date();
    private phoneDB phoneDB;
    private SQLiteDatabase phoWriter,phoReader;
    private Cursor cursor;
    private String lastDate,content;
    int uu;
    long zong=0;
    Date date1=null,date2=null,date3,date4;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate executed ");
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        TimeService.this.registerReceiver(screenReceiver,screenFilter);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1=intent;
        String fro=intent1.getStringExtra("normal");
        phoneDB=new phoneDB(this);
        phoWriter=phoneDB.getWritableDatabase();
        phoReader=phoneDB.getReadableDatabase();
        content=simpleDateFormat.format(currentDate).toString();
        cursor=phoWriter.query(phoneDB.TABLE_NAME, null, null, null,
                null, null, null);
        if(cursor.getCount()==0) {
            ContentValues ca = new ContentValues();//创建存储变量
            ca.put(phoneDB.DATE, content);
            ca.put(phoneDB.OPEN, Home1Fra.getHome1Fra().getTime());
            ca.put(phoneDB.CLOSE, "empty");
            ca.put(phoneDB.DURING, "empty");
            ca.put(phoneDB.STATION,"on");
            phoWriter.insert(phoneDB.TABLE_NAME, null, ca);//写入数据库
        }
        cursor.moveToLast();
        if(fro.equals("start")){
            Log.d(TAG, "onStartCommand: "+"start");
            ContentValues ca = new ContentValues();//创建存储变量
            ca.put(phoneDB.DATE, content);
            ca.put(phoneDB.OPEN, Home1Fra.getHome1Fra().getTime());
            ca.put(phoneDB.CLOSE, "empty");
            ca.put(phoneDB.DURING, "empty");
            ca.put(phoneDB.STATION,"on");
            phoWriter.insert(phoneDB.TABLE_NAME, null, ca);//写入数据库
        }
        cursor.moveToLast();
        String sta=cursor.getString(cursor.getColumnIndex("station"));
        if(sta.equals("on")){
            timerHandler.sendEmptyMessage(0);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"service停止",Toast.LENGTH_SHORT).show();//由于service每隔几分钟会被回收（杀死）所以需要再重启
        Log.d(TAG, "onDestroy: ");
        Intent intent=new Intent(this,TimeService.class);
        intent.putExtra("normal","circle");
        this.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            content=simpleDateFormat.format(currentDate).toString();
            cursor=phoWriter.query(phoneDB.TABLE_NAME, null, null, null,
                    null, null, null);
            cursor.moveToLast();
            String on=cursor.getString(cursor.getColumnIndex("open"));
            String xi=cursor.getString(cursor.getColumnIndex("close"));
            String station=cursor.getString(cursor.getColumnIndex("station"));
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.d(TAG, "亮屏 ");
                ContentValues ca=new ContentValues();//创建存储变量
                ca.put(phoneDB.DATE,content);
                ca.put(phoneDB.OPEN,Home1Fra.getHome1Fra().getTime());
                ca.put(phoneDB.CLOSE,"empty");
                ca.put(phoneDB.DURING,"empty");
                ca.put(phoneDB.STATION,"on");
                phoWriter.insert(phoneDB.TABLE_NAME, null,ca);//写入数据库
                timerHandler.sendEmptyMessage(0);//在存入亮屏时间后启动handle
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                timerHandler.removeMessages(0);
                Log.d(TAG, "熄屏 ");
                String nowt=Home1Fra.getHome1Fra().getTime();
                phoWriter.execSQL("update phone set close=? where open=?",new String[]{nowt,on});
                phoWriter.execSQL("update phone set station=? where station=?",new String[]{"xi",station});
                    try {
                        date1=format.parse(nowt);
                        date2=format.parse(on);
                    } catch (ParseException e) {
                    }
                   long diff= date1.getTime()-date2.getTime();
                    Log.d(TAG, "onReceive: "+diff);
                    phoWriter.execSQL("update phone set during=? where open=?",new String[]{String.valueOf(diff),on});
            } else {
            }
        }
    };

    public Long ptime(){
//        long endtime=0;
//        String know=Home1Fra.getHome1Fra().getTime();
        cursor=phoWriter.query(phoneDB.TABLE_NAME, null, null, null,
                null, null, null);
        zong=0;
        if(cursor.moveToFirst()) {
            do {
                String me=cursor.getString(cursor.getColumnIndex("date"));
                String mc = cursor.getString(cursor.getColumnIndex("open"));
                String ma = cursor.getString(cursor.getColumnIndex("close"));
                String md = cursor.getString(cursor.getColumnIndex("during"));
                String sta=cursor.getString(cursor.getColumnIndex("station"));
                Log.d(TAG, me+" "+mc+":"+ma+":"+md+sta);
                if(me.equals(simpleDateFormat.format(currentDate).toString())&&!md.equals("empty")){
                    zong=zong+Long.valueOf(md);
                }

            }while (cursor.moveToNext());
        }
//        Log.d(TAG, "onReceive: "+cursor.getCount());
//        cursor.moveToLast();
//        String on=cursor.getString(cursor.getColumnIndex("open"));
//        String xi=cursor.getString(cursor.getColumnIndex("close"));
//        if(!on.equals("empty")&&xi.equals("empty")){
//            try {
//                date3=format.parse(on);
//                date4=format.parse(know);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            endtime= date4.getTime()-date3.getTime();
//        }
//        cursor.close();
        return zong;
    }

    private Handler timerHandler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            content=simpleDateFormat.format(currentDate).toString();
            cursor=phoWriter.query(phoneDB.TABLE_NAME, null, null, null,
                    null, null, null);
            cursor.moveToLast();
            String on=cursor.getString(cursor.getColumnIndex("open"));
            String xi=cursor.getString(cursor.getColumnIndex("close"));
            String nowt=Home1Fra.getHome1Fra().getTime();
            phoWriter.execSQL("update phone set close=? where open=?",new String[]{nowt,on});
            try {
                date1=format.parse(nowt);
                date2=format.parse(on);
            } catch (ParseException e) {
            }
            long diff= date1.getTime()-date2.getTime();
            phoWriter.execSQL("update phone set during=? where open=?",new String[]{String.valueOf(diff),on});
            timerHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };


    public TimeService() {
        timeservice=this;
    }
    public static TimeService getTimeService(){
        return timeservice;
    }
    public static TimeService timeservice;

}
