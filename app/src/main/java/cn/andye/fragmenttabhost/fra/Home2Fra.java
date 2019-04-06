package cn.andye.fragmenttabhost.fra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.andye.fragmenttabhost.PieChartView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import cn.andye.fragmenttabhost.Service.TimeService;
import cn.andye.fragmenttabhost.R;
import cn.andye.fragmenttabhost.model.Moment;
import cn.andye.fragmenttabhost.save.dailyDB;
import cn.andye.fragmenttabhost.save.fractionDB;
import cn.andye.fragmenttabhost.save.gradeDB;
import cn.andye.fragmenttabhost.save.ifDB;
import cn.andye.fragmenttabhost.save.monkeyDB;
import cn.andye.fragmenttabhost.save.prepareDB;

/**
 * 第二个页面
 */
public class Home2Fra extends Fragment implements OnClickListener {
	String getdata;
	TextView phtime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.index_2,container,false);
		//这里会找不到控件，因为页面还未加载完全
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getdata=Home1Fra.getHome1Fra().getdata();
		StringTokenizer st = new StringTokenizer(getdata, ":");
		String shour = st.nextToken();
		String smin = st.nextToken();
		String dur = st.nextToken();
		String thing = st.nextToken();
		int mg=Integer.valueOf(shour);
		int mto=Integer.valueOf(smin);
		int mye=Integer.valueOf(dur);
		int mbe=Integer.valueOf(thing);
		TextView grade=(TextView)getActivity().findViewById(R.id.headv);
		grade.setText(""+mg);
		TextView today=(TextView)getActivity().findViewById(R.id.headt);
		today.setText(""+mto);//展示today的成绩
		TextView yesterday=(TextView)getActivity().findViewById(R.id.heady);
		yesterday.setText(""+mye);//展示yesterday的成绩
		TextView byesterday=(TextView)getActivity().findViewById(R.id.headb);
		byesterday.setText(""+mbe);//展示before yesterday的成绩
		phtime=(TextView)getActivity().findViewById(R.id.phtime);

	}

	private Handler timerHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.d("abcd", "handleMessage: ");
			Long mph = TimeService.getTimeService().ptime();
			long day = mph / (24 * 60 * 60 * 1000);
			long hour = (mph / (60 * 60 * 1000) - day * 24);
			long min = ((mph / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (mph / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			long ms = (mph - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
			phtime.setText("亮屏时间：" + hour + "小时" + min + "分" + s + "秒");
			timerHandler.sendEmptyMessageDelayed(0, 3000);
		}
	};

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("abcd", "onResume: ");
		timerHandler.sendEmptyMessage(0);
	}

		@Override
	public void onClick(View v) {
	}

	@Override
	public void onPause() {
		super.onPause();
		timerHandler.removeMessages(0);
	}
}
