package com.zirconi.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zirconi.db.DBTools;
import com.zirconi.db.DomDBHelper;
import com.zirconi.entity.StuInfoEntity;
import com.zirconi.etc.GlobalApplication;
import com.zirconi.huaxiaclient.LoginActivity;
import com.zirconi.huaxiaclient.R;
import com.zirconi.huaxiaclient.SettingActivity;

import android.R.anim;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DrawerFragment extends Fragment {

	TextView tv_name;
	TextView tv_class;
	TextView tv_id;
	TextView tv_major;
	GlobalApplication g;
	ListView lv;
	List<Map<String, String>> item;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		tv_name = (TextView) getActivity().findViewById(
				com.zirconi.huaxiaclient.R.id.tv_name);
		tv_class = (TextView) getActivity().findViewById(
				com.zirconi.huaxiaclient.R.id.tv_class);
		tv_id = (TextView) getActivity().findViewById(
				com.zirconi.huaxiaclient.R.id.tv_id);
		tv_major = (TextView) getActivity().findViewById(
				com.zirconi.huaxiaclient.R.id.tv_major);
		g = (GlobalApplication) getActivity().getApplication();

		StuInfoEntity si = g.gDomObj.getStuinfoEntity();
		tv_name.setText(si.getStr_name());
		tv_class.setText(si.getStr_class());
		tv_id.setText(si.getStr_id());
		tv_major.setText(si.getStr_major());

		lv = (ListView) getActivity().findViewById(R.id.lv_item);

		item = new ArrayList<Map<String, String>>();
		Map<String, String> a = new HashMap<String, String>();
		a.put("item", "选项");
		Map<String, String> b = new HashMap<String, String>();
		b.put("item", "关于");
		Map<String, String> c = new HashMap<String, String>();
		c.put("item", "注销");
		Map<String, String> d = new HashMap<String, String>();
		d.put("item", "退出");
		item.add(a);
		item.add(b);
		item.add(c);
		item.add(d);

		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), item,
				R.layout.option_item, new String[] { "item" },
				new int[] { R.id.tv_option });
		lv.setAdapter(simpleAdapter);
		lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				switch (position) {
				case 0:
					Intent i = new Intent(getActivity(),
							SettingActivity.class);
					startActivity(i);
					break;
				case 1:
					new AlertDialog.Builder(getActivity()).setTitle("关于")
							.setMessage("By Zirconi")
							.setPositiveButton("确定", null).show();
					break;
				case 2:
					new AlertDialog.Builder(getActivity()).setTitle("退出")
							.setMessage("确认注销吗？")
							.setPositiveButton("确定", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									DBTools.delAll(new DomDBHelper(getActivity()));
									Intent i = new Intent(getActivity(),
											LoginActivity.class);
									startActivity(i);
									getActivity().finish();
								}
							}).setNegativeButton("取消", null).show();
					break;
				case 3:
					new AlertDialog.Builder(getActivity()).setTitle("退出")
							.setMessage("确认退出吗？")
							.setPositiveButton("确定", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									getActivity().finish();
								}
							}).setNegativeButton("取消", null).show();
					break;

				}

			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(
				com.zirconi.huaxiaclient.R.layout.drawer_layout, container,
				false);
		return v;

	}

}
