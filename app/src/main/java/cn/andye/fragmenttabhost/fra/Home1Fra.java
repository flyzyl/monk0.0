package cn.andye.fragmenttabhost.fra;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OptionalDataException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.os.Environment;
import android.os.Bundle;
import android.widget.Toast;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import cn.andye.fragmenttabhost.CommentFun;
import cn.andye.fragmenttabhost.CustomTagHandler;
import cn.andye.fragmenttabhost.CustomDialog;
import cn.andye.fragmenttabhost.PieChartView;
import cn.andye.fragmenttabhost.clock.AlarmReceiver;
import cn.andye.fragmenttabhost.model.Comment;
import cn.andye.fragmenttabhost.model.Moment;
import cn.andye.fragmenttabhost.save.ifDB;
import cn.andye.fragmenttabhost.save.dailyDB;
import cn.andye.fragmenttabhost.save.prepareDB;
import cn.andye.fragmenttabhost.save.monkeyDB;
import cn.andye.fragmenttabhost.save.gradeDB;
import cn.andye.fragmenttabhost.save.fractionDB;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.RandomAccessFile;
import java.util.TimeZone;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import cn.andye.fragmenttabhost.fra.Home2Fra;


import cn.andye.fragmenttabhost.MomentAdapter;
import cn.andye.fragmenttabhost.R;


public class Home1Fra extends Fragment implements View.OnClickListener{
	public static Home1Fra instance=null;
	public static int requestCode=0;
	private ListView mListView;//块列表
	private MomentAdapter mAdapter;//适配器
	private CustomTagHandler customTagHandler;
	private CommentFun commentFun;
	private View v;
	private Cursor cursor1,cursor2,cursor3,cursor4,cursor5,cursor6,cursor7;
	private ifDB ifDB;
	private dailyDB dailyDB;
	private prepareDB prepareDB;
	private monkeyDB monkeyDB;
	private gradeDB gradeDB;
	private fractionDB fractionDB;
	private SQLiteDatabase dbWriter,dbReader,daiWriter,daiReader,preWriter,preReader,
			monWriter,monReader,graWriter,graReader,fraWriter,fraReader;
	private int i,j,G=0,To=0,ye=0,be=0,n=0,num,numbe=0;
	private Date yesterday,byesterday,currentDate,cdate;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private ArrayList<Moment> moments;
	private ArrayList<Comment> comments;
	private String content,getdata;
	private String[] number={"一","二","三","四","五","六","七","八","九","十"};
	private String[] words={
	"你是一棵树",
	"你是一棵树",
	"你是一棵树",
	"你是一棵树",
	"你是一棵树"
	};
	Messagesent message;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v= inflater.inflate(R.layout.index_1, container,false);//获得first界面的xml
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		instance=this;
		ifDB=new ifDB(getActivity());//引入数据库创建函数
		dbWriter=ifDB.getWritableDatabase();//获得数据库写的权限
		dbReader=ifDB.getReadableDatabase();//获得读取权限
		dailyDB=new dailyDB(getActivity());
		daiWriter=dailyDB.getWritableDatabase();
		daiReader=dailyDB.getReadableDatabase();
		prepareDB=new prepareDB(getActivity());
		preWriter=prepareDB.getWritableDatabase();
		preReader=prepareDB.getReadableDatabase();
		monkeyDB=new monkeyDB(getActivity());
		monWriter=monkeyDB.getWritableDatabase();
		monReader=monkeyDB.getReadableDatabase();
		gradeDB=new gradeDB(getActivity());
		graWriter=gradeDB.getWritableDatabase();
		graReader=gradeDB.getReadableDatabase();
		fractionDB=new fractionDB(getActivity());
		fraWriter=fractionDB.getWritableDatabase();
		fraReader=fractionDB.getReadableDatabase();
		num=numb();


		initView();//对first界面初始化
	}

	private  void initView() {
		AlarmView();
		ye=0;be=0;
		currentDate = new java.util.Date();//获取今天日期
		Calendar calender=new GregorianCalendar();
		calender.setTime(currentDate);
		calender.add(calender.DATE,-1);
		yesterday=calender.getTime();
		calender.add(calender.DATE,-1);
		byesterday=calender.getTime();
		Log.d("hhh", "initView: "+simpleDateFormat.format(yesterday).toString());
		Log.d("hhh", "initView: "+simpleDateFormat.format(byesterday).toString());
		Log.d("hhh", "initView: "+simpleDateFormat.format(currentDate).toString());
		boolean isStore = false;//初值定为日期还没有存储
		cursor7=fraWriter.query(fractionDB.TABLE_NAME, null, null, null,
				null, null, null);
		if(cursor7.getCount()!=0) {
			if (cursor7.moveToFirst()) {
				do {
					String mf = cursor7.getString(cursor7.getColumnIndex("time"));
					String mft=cursor7.getString(cursor7.getColumnIndex("content"));
					if(mf.equals(simpleDateFormat.format(yesterday).toString())){
						ye=Integer.valueOf(mft).intValue();

					}
					if(mf.equals(simpleDateFormat.format(byesterday).toString())){
						be=Integer.valueOf(mft).intValue();

					}
					if (mf.equals(simpleDateFormat.format(currentDate).toString())) {
						isStore = true;//将所有已经存储的日期与今天的日期进行对比，如果有表示今天日期已经存在
						To=Integer.valueOf(mft).intValue();
						break;
					}

				} while (cursor7.moveToNext());
			}
			cursor7.close();
		}
		if (isStore == false) {//一天一次
			showupdata();
		}
		mListView = (ListView)getActivity().findViewById(R.id.list_moment);
		moments = new ArrayList<>();
		for (i=0;i<5;i++){//这个for函数将数据库中的数据导入到一个数组中用于数据展示
			comments = new ArrayList<>();
			if(i==0){
				cursor1=dbReader.query(ifDB.TABLE_NAME, null, null, null,
						null, null, null);
				if(cursor1.getCount()==0) {
					addDB("if",i);
				}
				if(cursor1.moveToFirst()) {
					do {
						String ma = cursor1.getString(cursor1.getColumnIndex("content"));
						comments.add(new Comment(0, ma));
					}while (cursor1.moveToNext());
				}
				cursor1.close();
			}
			else if(i==1){
				cursor2=daiReader.query(dailyDB.TABLE_NAME, null, null, null,
						null, null, null);
				if(cursor2.getCount()==0) {
					addDB("daily",i);
				}
				if(cursor2.moveToFirst()) {
					do {
						String mb = cursor2.getString(cursor2.getColumnIndex("content"));
						comments.add(new Comment(1000, mb));
					}while (cursor2.moveToNext());
				}
				cursor2.close();
			}
			else if(i==2){
				cursor3=preReader.query(prepareDB.TABLE_NAME, null, null, null,
						null, null, null);
				if(cursor3.getCount()==0) {
					addDB("prepare",i);
				}
				if(cursor3.moveToFirst()) {
					do {
						String mc = cursor3.getString(cursor3.getColumnIndex("content"));
						comments.add(new Comment(2000, mc));
					}while (cursor3.moveToNext());
				}
				cursor3.close();
			}
			else if(i==3){
				cursor4=monReader.query(monkeyDB.TABLE_NAME, null, null, null,
						null, null, null);
				if(cursor4.getCount()==0) {
					addDB("monkey",i);
				}
				if(cursor4.moveToFirst()) {
					do {
						String md = cursor4.getString(cursor4.getColumnIndex("content"));
						comments.add(new Comment(3000, md));
					}while (cursor4.moveToNext());
				}
				cursor4.close();
			}
			else if(i==4){
				cursor5=graReader.query(gradeDB.TABLE_NAME, null, null, null,
						null, null, null);
				if(cursor5.getCount()==0) {
					addDB("grade#3",i);
				}
				if(cursor5.moveToFirst()) {
					do {
						String me = cursor5.getString(cursor5.getColumnIndex("content"));
						comments.add(new Comment(4000, me));
					}while (cursor5.moveToNext());
				}
				cursor5.close();
			}
			moments.add(new Moment("动态 " + i, comments));
		}
		if (!TextUtils.isEmpty(load())) {
			G=Integer.valueOf(load()).intValue();
		}
		getdata=G+":"+To+":"+ye+":"+be;
	mAdapter = new MomentAdapter(getActivity(),moments,words,num,new CustomTagHandler(getActivity(), new CustomTagHandler.OnCommentClickListener() {
			// 点击事件弹窗签到或删除
			@Override
			public void onContentClick(View view, final Comment comment) {//点击文字
				onClickCopy(comment.mContent);
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setTitle("提示");
				builder.setMessage("你要干什么？");
				builder.setPositiveButton("删除",comment.mContent, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String a=comment.mContent;
		  				dbWriter.execSQL("delete from note where content=?",new String[]{a});
						daiWriter.execSQL("delete from daily where content=?",new String[]{a});
						preWriter.execSQL("delete from prepare where content=?",new String[]{a});
						monWriter.execSQL("delete from monkey where content=?",new String[]{a});
						graWriter.execSQL("delete from grade where content=?",new String[]{a});
						for(i=0;i<moments.size();i++){
							for(j=0;j<moments.get(i).mComment.size();j++){
								String aa=moments.get(i).mComment.get(j).mContent;
								if(aa.equals(a)){
									moments.get(i).mComment.remove(j);
								}
							}
						}
						mAdapter.notifyDataSetChanged();
						dialog.dismiss();
						String retu=transform(a);
						StringTokenizer st=new StringTokenizer(retu,":");
						String retu1=st.nextToken();
						String retu2=st.nextToken();
						if(!retu1.equals("non")){
							deleteAlarm(Integer.valueOf(retu1),Integer.valueOf(retu2));
						}

					}
				});
				builder.setNegativeButton("签到",comment.mContent, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String b=comment.mContent;
						cursor6=graReader.query(gradeDB.TABLE_NAME, null, null, null,
								null, null, null);
//						if(cursor6.moveToFirst()) {
//							do {
//								String mf = cursor6.getString(cursor6.getColumnIndex("content"));
//								StringTokenizer st=new StringTokenizer(mf,"#");
//								String mf1=st.nextToken();
//								String mf2=st.nextToken();
//								int mf22=Integer.parseInt(mf2);
//								if(b.contains(mf1)){
//									G=G+mf22;
//									save(String.valueOf(G));
//									To=To+mf22;
//									String sTo=String.valueOf(To);
//									fraWriter.execSQL("update fraction set content=? where time=?",
//											new String[]{sTo,simpleDateFormat.format(currentDate).toString()});
//								}
//
//							}while (cursor6.moveToNext());
//						}
//						cursor6.close();
						G=G+1;
						save(String.valueOf(G));
						To=To+1;
						String sTo=String.valueOf(To);
						fraWriter.execSQL("update fraction set content=? where time=?",
								new String[]{sTo,simpleDateFormat.format(currentDate).toString()});
						String c=b+" √";
						dbWriter.execSQL("update note set content=? where content=?",new String[]{c,b});
						daiWriter.execSQL("update daily set content=? where content=?",new String[]{c,b});
						preWriter.execSQL("update prepare set content=? where content=?",new String[]{c,b});
						monWriter.execSQL("update monkey set content=? where content=?",new String[]{c,b});
						for(i=0;i<moments.size();i++){
							for(j=0;j<moments.get(i).mComment.size();j++){
								String aa=moments.get(i).mComment.get(j).mContent;
								if(aa.equals(b)){
									moments.get(i).mComment.set(j,new Comment(i*1000,c));
								}
							}
						}
						mAdapter.notifyDataSetChanged();
						initView();
						dialog.dismiss();
					}
				});

				builder.create().show();
				Log.d("hhh",""+comment.mContent);
				int n=0;
				for(i=0;i<4;i++) {
					for (j = 0; j < moments.get(i).mComment.size(); j++) {
						n++;
						String dd = moments.get(i).mComment.get(j).mContent;
						if (dd.contains("√")) {
							n--;
						}
					}
				}
				n=n-4;
				message.sentmessage(n);

			}
		}));

		mListView.setAdapter(mAdapter);
		//点击某个item的响应
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
//				Toast.makeText(getActivity(), "click " + position,
//						Toast.LENGTH_SHORT).show();
			}
		});

		mAdapter.setmItemOnClickListener(new MomentAdapter.ItemOnClickListener() {
			@Override
			public void itemOnClickListener(View view,int position) {
				inputComment(view,position);
			}
		});

	}

	public Home1Fra(){
		home1fra=this;
	}
	public static Home1Fra getHome1Fra(){
		return home1fra;
	}
	public static Home1Fra home1fra;

	public void addDB(String content,int i){
		if(i==0) {
		ContentValues ca=new ContentValues();//创建存储变量
		ca.put(ifDB.CONTENT,content);
		ca.put(ifDB.TIME,getTime());
			dbWriter.insert(ifDB.TABLE_NAME, null,ca);//写入数据库
		}else if(i==1){
			ContentValues cb=new ContentValues();//创建存储变量
			cb.put(dailyDB.CONTENT,content);
			cb.put(dailyDB.TIME,getTime());
			daiWriter.insert(dailyDB.TABLE_NAME, null,cb);//写入数据库
		}else if(i==2) {
			ContentValues cc = new ContentValues();//创建存储变量
			cc.put(prepareDB.CONTENT, content);
			cc.put(prepareDB.TIME, getTime());
			preWriter.insert(prepareDB.TABLE_NAME, null, cc);//写入数据库
		}else if(i==3){
			ContentValues cd = new ContentValues();//创建存储变量
			cd.put(monkeyDB.CONTENT, content);
			cd.put(monkeyDB.TIME, getTime());
			monWriter.insert(monkeyDB.TABLE_NAME, null, cd);//写入数据库
		}
		else if(i==4){
			ContentValues ce = new ContentValues();//创建存储变量
			ce.put(gradeDB.CONTENT, content);
			ce.put(gradeDB.TIME, getTime());
			graWriter.insert(gradeDB.TABLE_NAME, null, ce);//写入数据库
		}
		else if(i==5){
			ContentValues cf = new ContentValues();//创建存储变量
			java.util.Date currentDate = new java.util.Date();//获取今天日期
			cf.put(fractionDB.CONTENT, content);
			cf.put(fractionDB.TIME, simpleDateFormat.format(currentDate).toString());
			fraWriter.insert(fractionDB.TABLE_NAME, null, cf);//写入数据库
		}

	}
	public void onClickCopy(String copy){
		if(copy.contains("#")){
			StringTokenizer st=new StringTokenizer(copy,"#");
			String copy1=st.nextToken();
			copy=copy1;
		}
			ClipboardManager cmb = (ClipboardManager)getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
			// 将文本内容放到系统剪贴板里。由于API11不能用暂时注释掉
			cmb.setPrimaryClip(ClipData.newPlainText(null,copy));

	}

	public void save(String inputText) {//存储grade
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = getActivity().openFileOutput("data", getActivity().MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(inputText);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String load() {//读取grade
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			in = getActivity().openFileInput("data");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}

	public String getTime(){	//获取时间
		SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//实例化
		Date curDate=new Date();
		String str=format.format(curDate);
		return str;
	}

	public int numb(){//生成一个随机数，用来随机展示words数组中随机的一句话
		int min=1;
		int max=words.length;
		Random random=new Random();
		int number=random.nextInt(max-min)+min;
		numbe=number;
		return numbe;
	}

	//writeTxtToFile(),makeFilePath()两个函数是为了创建一个txt文件用来记录以往的事件日志
	// 将字符串写入到文本文件中
	public void writeTxtToFile(String strcontent,String filePath, String fileName) {
		makeFilePath(filePath, fileName);
		String strFilePath = filePath+fileName;
		String strContent = strcontent + "\r\n";//每次写入时，都换行写
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + strFilePath);
				file.getParentFile().mkdirs();
				file.createNewFile();//创建新文件
				}
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(file.length());//寻找文件
			raf.write(strContent.getBytes());//写入字符串
			raf.close();//关闭文件
			} catch (Exception e) {
			Log.e("TestFile","Error on write File:" + e);//如果发生错误打印日志而不致崩溃
			}
	}

	public File makeFilePath(String filePath, String fileName) {	//生成文件
		File file = null;
		try {
			file = new File(filePath);//生成文件夹
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			Log.i("error:", e+"");
		}
		//生成文件夹之后，再生成文件，不然会出错
		try {
			file = new File(filePath + fileName);//生成文件
			if (!file.exists()) {
				file.createNewFile();
				}
			}catch(Exception e) {
			e.printStackTrace();
			}
		return file;

	}




	//弹出评论对话框
	public void inputComment(final View view,final int position) {
		//引用函数，传出上下文，动态列表，当前视图，评论者名字
		CommentFun.inputComment(getActivity(), mListView, view,position,
				new CommentFun.InputCommentListener() {
					//重写并填充函数
			@Override
			public void onCommitComment() {
				mAdapter.notifyDataSetChanged();//评论成功后更新数据（内置函数）
			}
		});
	}
	@Override
	public void onClick(View v) {
	}

	@Override
	public void onResume() {
		super.onResume();
		cdate= new java.util.Date();
		if(!simpleDateFormat.format(cdate).toString().equals(simpleDateFormat.format(currentDate).toString())){
//			addDB("0",5);//保存今天的日期
			currentDate=cdate;
			Log.d("kk", "onResume:执行 ");
			To=0;
			for(i=0;i<moments.size();i++){
				for(j=0;j<moments.get(i).mComment.size();j++){
					String dd=moments.get(i).mComment.get(j).mContent;
					StringTokenizer sd=new StringTokenizer(dd,"——");
					String mhh=sd.nextToken();
					dbWriter.execSQL("update note set content=? where content=?",new String[]{mhh,dd});
					daiWriter.execSQL("update daily set content=? where content=?",new String[]{mhh,dd});
					preWriter.execSQL("update prepare set content=? where content=?",new String[]{mhh,dd});
					monWriter.execSQL("update monkey set content=? where content=?",new String[]{mhh,dd});
					moments.get(i).mComment.get(j).mContent=mhh;
				}
			}

		}

	}
	private static final String KEY_ALARM_LIST = "alarmList";
	private ArrayAdapter<AlarmData> adapter;
	private AlarmManager alarmManager;
	public void AlarmView() {
		alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		adapter = new ArrayAdapter<AlarmData>(getActivity(), android.R.layout.simple_list_item_1);
		readSavedAlarmList();
	}

	public void deleteAlarm(int a,int b) {
		int pos=-1;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, a);
		cal.set(Calendar.MINUTE, b);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String time = String.valueOf(cal.getTimeInMillis());
		Log.d("kk", "deleteAlarm: " + time);
		SharedPreferences sp = getActivity().getSharedPreferences("clock", Context.MODE_PRIVATE);
		String content = sp.getString(KEY_ALARM_LIST, null);
		if (content != null) {
			String[] timest = content.split(",");
			for(int i=0;i<timest.length;i++){
				if(time.equals(timest[i])){
					pos=i;
				}
			}
		}
		if(pos!=-1){
			Log.d("kk", "deleteAlarm: "+pos);
			AlarmData ac=adapter.getItem(pos);
			adapter.remove(ac);
			saveAlarmList();
			alarmManager.cancel(PendingIntent.getBroadcast(getActivity(), ac.getId(), new Intent(getActivity(), AlarmReceiver.class), 0));
		}

	}
	//闹钟时间设置
	public void onTimeSet(int hourOfDay, int minute,int dur,String thing) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);//hourofday是指今天
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Calendar currentTime = Calendar.getInstance();
		if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
			calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
		}
		AlarmData ad = new AlarmData(calendar.getTimeInMillis());
		adapter.add(ad);
		Intent intent=new Intent(getActivity(), AlarmReceiver.class);
		Log.d("kk", "onTimeSet: "+hourOfDay+":"+minute);
		intent.putExtra("shour",String.valueOf(hourOfDay));//名，值
		intent.putExtra("smin",String.valueOf(minute));//名，值
		intent.putExtra("trans",thing);//名，值
		alarmManager.set(AlarmManager.RTC_WAKEUP, ad.getTime(),
				PendingIntent.getBroadcast(getActivity(), ad.getId(),intent , 0));
		saveAlarmList();
	}

	private void saveAlarmList() {
		Editor editor = getActivity().getSharedPreferences("clock", Context.MODE_PRIVATE).edit();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < adapter.getCount(); i++) {
			sb.append(adapter.getItem(i).getTime()).append(",");
		}
		if (sb.length() > 1) {
			String content = sb.toString().substring(0, sb.length() - 1);
			editor.putString(KEY_ALARM_LIST, content);
			System.out.println(content);
		} else {
			editor.putString(KEY_ALARM_LIST, null);
		}
		editor.commit();
	}

	private void readSavedAlarmList() {
		SharedPreferences sp = getActivity().getSharedPreferences("clock", Context.MODE_PRIVATE);
		String content = sp.getString(KEY_ALARM_LIST, null);
		Log.d("kk", content+"");
		if (content != null) {
			String[] timeStrings = content.split(",");
			for (String string : timeStrings) {
				adapter.add(new AlarmData(Long.parseLong(string)));
			}
		}
	}

	public void showupdata(){
		Log.d("kk", "执行");
		writeTxtToFile(simpleDateFormat.format(currentDate).toString(),"/sdcard/Aha/","log.txt");
		cursor1=dbReader.query(ifDB.TABLE_NAME, null, null, null,
				null, null, null);
		if(cursor1.moveToFirst()) {
			do {
				String ma = cursor1.getString(cursor1.getColumnIndex("content"));
				String mach=ma;
				if(ma.contains("每天")){
					Log.d("kk", "mt");
					if(ma.contains(" √")){
						mach=ma.replace(" √","");
						Log.d("kk", "qu");
						dbWriter.execSQL("update note set content=? where content=?",new String[]{mach,ma});
						SimpleDateFormat format=new SimpleDateFormat("HH:mm");//实例化
						Date curDate=new Date();
						String str=format.format(curDate)+"  ";
						Home1Fra.getHome1Fra().writeTxtToFile(str+mach,"/sdcard/Aha/","log.txt");
					}
					String retu=transform(mach);
					StringTokenizer st=new StringTokenizer(retu,":");
					String shour=st.nextToken();
					String smin=st.nextToken();
					String dur=st.nextToken();
					String thin=st.nextToken();
					int alahour=0,alamin=0,aladur=0;
					if (!shour.equals("non")) {
						alahour = Integer.valueOf(shour);
						if (!smin.equals("non")) {
							alamin = Integer.valueOf(smin);
						}
						if (!dur.equals("non")) {
							aladur = Integer.valueOf(dur);
						}
						onTimeSet(alahour, alamin, aladur, thin);
					}
				}else {
					if(ma.contains(" √")){
						dbWriter.execSQL("delete from note where content=?",new String[]{ma});
						String retu=transform(ma);
						StringTokenizer st=new StringTokenizer(retu,":");
						String retu1=st.nextToken();
						String retu2=st.nextToken();
						if(!retu1.equals("non")){
							deleteAlarm(Integer.valueOf(retu1),Integer.valueOf(retu2));
						}
					}
				}
			}while (cursor1.moveToNext());
		}
		cursor1.close();
		cursor2=daiReader.query(dailyDB.TABLE_NAME, null, null, null,
				null, null, null);
		if(cursor2.moveToFirst()) {
			do {
				String mb = cursor2.getString(cursor2.getColumnIndex("content"));
				String mbch=mb;
				if(mb.contains("每天")){
					if(mb.contains(" √")){
						mbch=mb.replace(" √","");
						daiWriter.execSQL("update daily set content=? where content=?",new String[]{mbch,mb});
						SimpleDateFormat format=new SimpleDateFormat("HH:mm");//实例化
						Date curDate=new Date();
						String str=format.format(curDate)+"  ";
						Home1Fra.getHome1Fra().writeTxtToFile(str+mbch,"/sdcard/Aha/","log.txt");
					}
					String retu=transform(mbch);
					StringTokenizer st=new StringTokenizer(retu,":");
					String shour=st.nextToken();
					String smin=st.nextToken();
					String dur=st.nextToken();
					String thin=st.nextToken();
					int alahour=0,alamin=0,aladur=0;
					if (!shour.equals("non")) {
						alahour = Integer.valueOf(shour);
						if (!smin.equals("non")) {
							alamin = Integer.valueOf(smin);
						}
						if (!dur.equals("non")) {
							aladur = Integer.valueOf(dur);
						}
						onTimeSet(alahour, alamin, aladur, thin);
					}
				}else {
					if(mb.contains(" √")){
						daiWriter.execSQL("delete from daily where content=?",new String[]{mb});
						String retu=transform(mb);
						StringTokenizer st=new StringTokenizer(retu,":");
						String retu1=st.nextToken();
						String retu2=st.nextToken();
						if(!retu1.equals("non")){
							deleteAlarm(Integer.valueOf(retu1),Integer.valueOf(retu2));
						}
					}
				}
			}while (cursor2.moveToNext());
		}
		cursor2.close();
		cursor3=preReader.query(prepareDB.TABLE_NAME, null, null, null,
				null, null, null);
		if(cursor3.moveToFirst()) {
			do {
				String mc = cursor3.getString(cursor3.getColumnIndex("content"));
				String mcch=mc;
				if(mc.contains("每天")){
					if(mc.contains(" √")){
						mcch=mc.replace(" √","");
						preWriter.execSQL("update prepare set content=? where content=?",new String[]{mcch,mc});
						SimpleDateFormat format=new SimpleDateFormat("HH:mm");//实例化
						Date curDate=new Date();
						String str=format.format(curDate)+"  ";
						Home1Fra.getHome1Fra().writeTxtToFile(str+mcch,"/sdcard/Aha/","log.txt");
					}
					String retu=transform(mcch);
					StringTokenizer st=new StringTokenizer(retu,":");
					String shour=st.nextToken();
					String smin=st.nextToken();
					String dur=st.nextToken();
					String thin=st.nextToken();
					int alahour=0,alamin=0,aladur=0;
					if (!shour.equals("non")) {
						alahour = Integer.valueOf(shour);
						if (!smin.equals("non")) {
							alamin = Integer.valueOf(smin);
						}
						if (!dur.equals("non")) {
							aladur = Integer.valueOf(dur);
						}
						onTimeSet(alahour, alamin, aladur, thin);
					}
				}else {
					if(mc.contains(" √")){
						preWriter.execSQL("delete from prepare where content=?",new String[]{mc});
						String retu=transform(mc);
						StringTokenizer st=new StringTokenizer(retu,":");
						String retu1=st.nextToken();
						String retu2=st.nextToken();
						if(!retu1.equals("non")){
							deleteAlarm(Integer.valueOf(retu1),Integer.valueOf(retu2));
						}
					}
				}
			}while (cursor3.moveToNext());
		}
		cursor3.close();
		addDB("0",5);//上述过程执行完成后保存今天的日期
	}

	private static class AlarmData {
		private long time = 0;
		public AlarmData(long time) {
			this.time = time;

		}
		public long getTime() {
			return time;
		}

		public int getId() {
			return (int) (getTime() / 1000 / 60);
		}
	}

// 		AlarmManager.RTC，硬件闹钟，不唤醒手机（也可能是其它设备）休眠；当手机休眠时不发射闹钟。
//		AlarmManager.RTC_WAKEUP，硬件闹钟，当闹钟发躰时唤醒手机休眠；
//		AlarmManager.ELAPSED_REALTIME，真实时间流逝闹钟，不唤醒手机休眠；当手机休眠时不发射闹钟。
//		AlarmManager.ELAPSED_REALTIME_WAKEUP，真实时间流逝闹钟，当闹钟发躰时唤醒手机休眠；
//		RTC闹钟和ELAPSED_REALTIME最大的差别就是前者可以通过修改手机时间触发闹钟事件，后者要通过真实时间的流逝，即使在休眠状态，时间也会被计算。

//提取字符串中的信息
	public String transform(String content){
		String[] other={":","个","分钟","小时","每天","上午","下午","晚上","*"};
		String shour="non",smin="non",sdur="non",sn,ret,lon="non",thing="non",content1,content2;
		int alahour,alamin;
		this.content=content;
		for(i=0;i<9;i++){
			if(content.contains(number[i])){
				int po=content.indexOf(number[i]);//获取汉字位置，第一个汉字位置是0
				String sa=String.valueOf(i+1);//数字转字符
				String sb=content.substring(po,po+1);//获得汉字,不包含po+1位置的汉字
				content=content.replace(sb,sa);//数字字符代替汉字
			}
		}
		if(content.contains(number[9])){
			String s1=String.valueOf(1);
			String s10=String.valueOf(10);//数字转字符
			String s0=String.valueOf(0);//数字转字符
			int pos=content.indexOf(number[9]);//获取汉字位置，第一个汉字位置是0
			String spo=content.substring(pos,pos+1);//获得汉字
			if(pos-1>=0){
				int chr=content.charAt(pos-1);//获得某个字符的ASCII码
				if(47 < chr & chr < 58){
					content=content.replace(spo,s0);//数字字符代替汉字
				} else {
					if(pos+1<=content.length()){
						chr=content.charAt(pos-1);//获得某个字符的ASCII码
						if(47 < chr & chr < 58){
							content=content.replace(spo,s1);//数字字符代替汉字
						}else {
							content=content.replace(spo,s10);//数字字符代替汉字
						}
					}else {
						content=content.replace(spo,s10);//数字字符代替汉字
					}
				}
			} else {
				if(pos+1<=content.length()){
					int chr=content.charAt(pos-1);//获得某个字符的ASCII码
					if(47 < chr & chr < 58){
						content=content.replace(spo,s1);//数字字符代替汉字
					}else {
						content=content.replace(spo,s10);//数字字符代替汉字
					}
				}else {
					content=content.replace(spo,s10);//数字字符代替汉字
				}
			}
		}
		Log.d("kk", "position="+content);
		//上面是将字符串中的汉字数字转阿拉伯数字
		//下面是提取数字的意义，并设置闹钟
		if(content.contains("点") || content.contains(":") || content.contains("：")) {
			for (i = 0; i < content.length(); i++) {
				sn = content.substring(i, i + 1);//获得汉字,不包含po+1位置的汉字
				if (sn.equals("点") || sn.equals(":") || sn.equals("：")) {
					Log.d("kk", "transform: "+i);
					if (i - 1 >= 0) {
						int chr1 = content.charAt(i - 1);//获得某个字符的ASCII码
						if (47 < chr1 & chr1 < 58) {
							shour = content.substring(i - 1, i);//获得汉字
							if (i - 2 >= 0) {
								int chr2 = content.charAt(i - 2);//获得某个字符的ASCII码
								if (47 < chr2 & chr2 < 58) {
									shour = content.substring(i - 2, i);//获得汉字
								}
							}
						}
					}
					if (i + 1 <= content.length()) {
						int chr3 = content.charAt(i + 1);//获得某个字符的ASCII码
						if (47 < chr3 & chr3 < 58) {
							smin = content.substring(i + 1, i + 2);//获得汉字
							if (i + 2 <= content.length()) {
								int chr4 = content.charAt(i + 2);//获得某个字符的ASCII码
								if (47 < chr4 & chr4 < 58) {
									smin = content.substring(i + 1, i + 3);//获得汉字
								}
							}
						}
					}
				}
			}
		}

		Log.d("kk", "包含"+shour+"时"+smin);

		if (content.contains("分钟") || content.contains("小时")) {
			for(i = 0; i < content.length()-1; i++){
				sn = content.substring(i, i + 2);//获得汉字,不包含po+1位置的汉字
				if (sn.equals("分钟")) {
					Log.d("kk", "tran "+i);
					if (i - 1 >= 0) {
						int chr1 = content.charAt(i - 1);//获得某个字符的ASCII码
						if (47 < chr1 & chr1 < 58) {
							lon = content.substring(i - 1, i);//获得汉字
							if (i - 2 >= 0) {
								int chr2 = content.charAt(i - 2);//获得某个字符的ASCII码
								if (47 < chr2 & chr2 < 58) {
									lon = content.substring(i - 2, i);//获得汉字
								}
							}
						}
					}
				}
				if (sn.equals("小时")) {
					if (i - 1 >= 0) {
						int chr1 = content.charAt(i - 1);//获得某个字符的ASCII码
						if (47 < chr1 & chr1 < 58) {
							lon = content.substring(i - 1, i);//获得汉字
							if (i - 2 >= 0) {
								int chr2 = content.charAt(i - 2);//获得某个字符的ASCII码
								if (47 < chr2 & chr2 < 58) {
									lon = content.substring(i - 2, i);//获得汉字
								}
							}
							lon=String.valueOf(Integer.valueOf(lon)*60);//小时转为分钟单位
						}
					}
				}

			}

		}
		for(i = 0; i < content.length(); i++){
			int chr1 = content.charAt(i);//获得某个字符的ASCII码
			if (47 < chr1 & chr1 < 58) {
				sn = content.substring(i, i + 1);//获得汉字,不包含po+1位置的汉字
				content=content.replace(sn,"*");
			}
		}
		for(i=0;i<other.length;i++){
			if(content.contains(other[i])){
				content=content.replace(other[i],"");
			}
		}
		if(content!=null&content.length()!=0){
			thing=content;
			Log.d("kk", "zhi");
		}

		ret=shour+":"+smin+":"+lon+":"+thing;
		Log.d("kk", "包含"+ret);
		return ret;
	}

	public interface Messagesent{
		 void sentmessage(int n);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//当前fragment从activity重写了回调接口  得到接口的实例化对象
		message =(Messagesent) getActivity();
	}

	public String getdata(){
		return getdata;
	}

	@Override
	public void onStop() {
		super.onStop();

	}

}
