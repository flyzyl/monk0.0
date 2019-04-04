package cn.andye.fragmenttabhost.fra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import cn.andye.fragmenttabhost.R;
/**
 * home3
 * @author andye
 *
 */
public class Home3Fra extends Fragment implements OnClickListener {
	private View v;
	private Intent i;
	private TextView history, grade;
	private Button bt_up;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.index_3, null);
		init(v);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	private void init(View v) {
		bt_up=(Button)v.findViewById(R.id.setting_exit);
		bt_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i=new Intent(getActivity(),upAbout.class);
				i.putExtra("flag", "1");
				startActivity(i);
			}
		});
	}

	/*
	        ** 按钮点击事件处理
	 */
	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.message_delete:
		//
		// break;
		//
		// default:
		// break;
		// }
	}
}
