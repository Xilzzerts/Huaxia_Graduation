package com.zirconi.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zirconi.db.DBTools;
import com.zirconi.db.DomDBHelper;
import com.zirconi.entity.DomEntity;
import com.zirconi.entity.StatusEntity;
import com.zirconi.entity.TimetableEntity;
import com.zirconi.entity.TranscriptEntity;
import com.zirconi.etc.GlobalApplication;
import com.zirconi.huaxiaclient.MainActivity;
import com.zirconi.huaxiaclient.R;
import com.zirconi.huaxiaclient.R.id;
import com.zirconi.parser.XMLParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimetableFragment extends Fragment implements OnRefreshListener {

	ExpandableListView el;
	List<Map<String, String>> groupData;
	List<List<Map<String, String>>> childData;
	GlobalApplication g;
	SwipeRefreshLayout swipeLayout;
	Boolean guard;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(
				com.zirconi.huaxiaclient.R.layout.timeable_layout, container,
				false);
		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		guard = false;
		swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(
				R.id.swipe_transcript);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(R.color.lightblue, R.color.lightgreen,
				R.color.orange, R.color.red);

		el = (ExpandableListView) getActivity().findViewById(R.id.el_timetable);
		groupData = new ArrayList<Map<String, String>>();
		childData = new ArrayList<List<Map<String, String>>>();
		g = (GlobalApplication) getActivity().getApplication();
		List<TimetableEntity> lte = g.getgDomObj().getTimetableEntity();
		// 0-3 1
		// 4-7 2
		// ...
		List<Map<String, String>> monday = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 4; ++i) {
			Map<String, String> temp = new HashMap<String, String>();
			TimetableEntity te = lte.get(i);
			temp.put(
					"classname",
					te.getStr_classname().equals("") ? "无" : te
							.getStr_classname());
			temp.put(
					"classroom",
					te.getStr_classroom().equals("") ? "无" : te
							.getStr_classroom());
			temp.put("teacher",
					te.getStr_teacher().equals("") ? "无" : te.getStr_teacher());
			temp.put("times",
					te.getStr_times().equals("") ? "无" : te.getStr_times());
			// temp.put("group_name", "星期一");
			monday.add(temp);
		}

		List<Map<String, String>> tuesday = new ArrayList<Map<String, String>>();
		for (int i = 4; i < 8; ++i) {
			Map<String, String> temp = new HashMap<String, String>();
			TimetableEntity te = lte.get(i);
			temp.put(
					"classname",
					te.getStr_classname().equals("") ? "无" : te
							.getStr_classname());
			temp.put(
					"classroom",
					te.getStr_classroom().equals("") ? "无" : te
							.getStr_classroom());
			temp.put("teacher",
					te.getStr_teacher().equals("") ? "无" : te.getStr_teacher());
			temp.put("times",
					te.getStr_times().equals("") ? "无" : te.getStr_times());
			// temp.put("group_name", "星期二");
			tuesday.add(temp);
		}

		List<Map<String, String>> wednesday = new ArrayList<Map<String, String>>();
		for (int i = 8; i < 12; ++i) {
			Map<String, String> temp = new HashMap<String, String>();
			TimetableEntity te = lte.get(i);
			temp.put(
					"classname",
					te.getStr_classname().equals("") ? "无" : te
							.getStr_classname());
			temp.put(
					"classroom",
					te.getStr_classroom().equals("") ? "无" : te
							.getStr_classroom());
			temp.put("teacher",
					te.getStr_teacher().equals("") ? "无" : te.getStr_teacher());
			temp.put("times",
					te.getStr_times().equals("") ? "无" : te.getStr_times());
			// temp.put("group_name", "星期三");
			wednesday.add(temp);
		}

		List<Map<String, String>> thusday = new ArrayList<Map<String, String>>();
		for (int i = 12; i < 16; ++i) {
			Map<String, String> temp = new HashMap<String, String>();
			TimetableEntity te = lte.get(i);
			temp.put(
					"classname",
					te.getStr_classname().equals("") ? "无" : te
							.getStr_classname());
			temp.put(
					"classroom",
					te.getStr_classroom().equals("") ? "无" : te
							.getStr_classroom());
			temp.put("teacher",
					te.getStr_teacher().equals("") ? "无" : te.getStr_teacher());
			temp.put("times",
					te.getStr_times().equals("") ? "无" : te.getStr_times());
			// temp.put("group_name", "星期四");
			thusday.add(temp);
		}

		List<Map<String, String>> friday = new ArrayList<Map<String, String>>();
		for (int i = 16; i < 20; ++i) {
			Map<String, String> temp = new HashMap<String, String>();
			TimetableEntity te = lte.get(i);
			temp.put(
					"classname",
					te.getStr_classname().equals("") ? "无" : te
							.getStr_classname());
			temp.put(
					"classroom",
					te.getStr_classroom().equals("") ? "无" : te
							.getStr_classroom());
			temp.put("teacher",
					te.getStr_teacher().equals("") ? "无" : te.getStr_teacher());
			temp.put("times",
					te.getStr_times().equals("") ? "无" : te.getStr_times());
			// temp.put("group_name", "星期五");
			friday.add(temp);
		}
		childData.add(monday);
		childData.add(tuesday);
		childData.add(wednesday);
		childData.add(thusday);
		childData.add(friday);

		Map<String, String> m1 = new HashMap<String, String>();
		m1.put("group_name", "星期一");
		Map<String, String> m2 = new HashMap<String, String>();
		m2.put("group_name", "星期二");
		Map<String, String> m3 = new HashMap<String, String>();
		m3.put("group_name", "星期三");
		Map<String, String> m4 = new HashMap<String, String>();
		m4.put("group_name", "星期四");
		Map<String, String> m5 = new HashMap<String, String>();
		m5.put("group_name", "星期五");

		groupData.add(m1);
		groupData.add(m2);
		groupData.add(m3);
		groupData.add(m4);
		groupData.add(m5);

		BaseExpandableListAdapter be = new AdapterSP(getActivity());
		el.setAdapter(be);
	}

	private class AdapterSP extends BaseExpandableListAdapter {
		Context context;

		public AdapterSP(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupData.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return childData.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupData.get(groupPosition).get("group_name");
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childData.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.expandlist_group, null);
			}

			TextView title = (TextView) view.findViewById(R.id.el_group);
			title.setText((String) getGroup(groupPosition));

			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.expandlist_item, null);
			}
			final TextView tv_classroom = (TextView) view
					.findViewById(R.id.tv_time_class);
			tv_classroom.setText(childData.get(groupPosition)
					.get(childPosition).get("classroom").toString());
			final TextView tv_classname = (TextView) view
					.findViewById(R.id.tv_time_name);
			tv_classname.setText(childData.get(groupPosition)
					.get(childPosition).get("classname").toString());

			final TextView tv_teacher = (TextView) view
					.findViewById(R.id.tv_time_teacher);
			tv_teacher.setText(childData.get(groupPosition).get(childPosition)
					.get("teacher").toString());

			final TextView tv_times = (TextView) view
					.findViewById(R.id.tv_time_times);
			tv_times.setText(childData.get(groupPosition).get(childPosition)
					.get("times").toString());

			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		if(guard)
			return;
		guard = true;
		String serveraddr = g.getServerAddr();
		String academiccode = g.getAcademicCode();
		SharedPreferences sp = getActivity().getSharedPreferences(
				"com.zirconi.huaxiaclient_preferences", 0);
		
		String id = sp.getString("id", null);
		String pwd = sp.getString("pwd", null);
		if (id == null || pwd == null) {
			// impossible
			swipeLayout.setRefreshing(false);
			guard = false;
			return;
		}

		String url = serveraddr + "MAGIC?num=" + id + "&pwd=" + pwd
				+ "&server=" + academiccode;
		Log.d("URL", url);

		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		StringRequest sr = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						DomEntity temp = XMLParser.ParseXML(response);
						// process response

						StatusEntity status = temp.getStatusEntity();
						if (temp.getIs_server_full()) {
							Toast.makeText(getActivity(), "服务器负载已满，请稍后重试",
									Toast.LENGTH_LONG).show();
							swipeLayout.setRefreshing(false);
							guard = false;
							return;

						}
						if (status.isIs_timeout()) {
							Toast.makeText(
									getActivity(),
									getResources().getText(
											R.string.academic_timeout),
									Toast.LENGTH_LONG).show();
							swipeLayout.setRefreshing(false);
							guard = false;
							return;
						}

						if (status.isIs_syserror()) {
							Toast.makeText(getActivity(), "教务系统错误",
									Toast.LENGTH_LONG).show();
							swipeLayout.setRefreshing(false);
							guard = false;
							return;
						}

						if (status.isIs_wrong_password()) {
							Toast.makeText(getActivity(), "密码错误",
									Toast.LENGTH_LONG).show();
							swipeLayout.setRefreshing(false);
							guard = false;
							return;
						}

						if (status.isIs_stuinfo_ok() == false
								|| status.isIs_timetable_ok() == false
								|| status.isIs_transcript_ok() == false) {
							Toast.makeText(getActivity(), "获取信息失败",
									Toast.LENGTH_LONG).show();
							swipeLayout.setRefreshing(false);
							guard = false;
							return;
						}

						// success
						g.setgDomObj(temp);
						DomDBHelper helper = new DomDBHelper(getActivity());
						DBTools.SaveDomToDB(helper, temp);

						List<TimetableEntity> lte = g.getgDomObj()
								.getTimetableEntity();
						// 0-3 1
						// 4-7 2
						// ...
						List<Map<String, String>> monday = new ArrayList<Map<String, String>>();
						for (int i = 0; i < 4; ++i) {
							Map<String, String> mtemp = new HashMap<String, String>();
							TimetableEntity te = lte.get(i);
							mtemp.put("classname", te.getStr_classname()
									.equals("") ? "无" : te.getStr_classname());
							mtemp.put("classroom", te.getStr_classroom()
									.equals("") ? "无" : te.getStr_classroom());
							mtemp.put(
									"teacher",
									te.getStr_teacher().equals("") ? "无" : te
											.getStr_teacher());
							mtemp.put(
									"times",
									te.getStr_times().equals("") ? "无" : te
											.getStr_times());
							// temp.put("group_name", "星期一");
							monday.add(mtemp);
						}

						List<Map<String, String>> tuesday = new ArrayList<Map<String, String>>();
						for (int i = 4; i < 8; ++i) {
							Map<String, String> mtemp = new HashMap<String, String>();
							TimetableEntity te = lte.get(i);
							mtemp.put("classname", te.getStr_classname()
									.equals("") ? "无" : te.getStr_classname());
							mtemp.put("classroom", te.getStr_classroom()
									.equals("") ? "无" : te.getStr_classroom());
							mtemp.put(
									"teacher",
									te.getStr_teacher().equals("") ? "无" : te
											.getStr_teacher());
							mtemp.put(
									"times",
									te.getStr_times().equals("") ? "无" : te
											.getStr_times());
							// temp.put("group_name", "星期二");
							tuesday.add(mtemp);
						}

						List<Map<String, String>> wednesday = new ArrayList<Map<String, String>>();
						for (int i = 8; i < 12; ++i) {
							Map<String, String> mtemp = new HashMap<String, String>();
							TimetableEntity te = lte.get(i);
							mtemp.put("classname", te.getStr_classname()
									.equals("") ? "无" : te.getStr_classname());
							mtemp.put("classroom", te.getStr_classroom()
									.equals("") ? "无" : te.getStr_classroom());
							mtemp.put(
									"teacher",
									te.getStr_teacher().equals("") ? "无" : te
											.getStr_teacher());
							mtemp.put(
									"times",
									te.getStr_times().equals("") ? "无" : te
											.getStr_times());
							// temp.put("group_name", "星期三");
							wednesday.add(mtemp);
						}

						List<Map<String, String>> thusday = new ArrayList<Map<String, String>>();
						for (int i = 12; i < 16; ++i) {
							Map<String, String> mtemp = new HashMap<String, String>();
							TimetableEntity te = lte.get(i);
							mtemp.put("classname", te.getStr_classname()
									.equals("") ? "无" : te.getStr_classname());
							mtemp.put("classroom", te.getStr_classroom()
									.equals("") ? "无" : te.getStr_classroom());
							mtemp.put(
									"teacher",
									te.getStr_teacher().equals("") ? "无" : te
											.getStr_teacher());
							mtemp.put(
									"times",
									te.getStr_times().equals("") ? "无" : te
											.getStr_times());
							// temp.put("group_name", "星期四");
							thusday.add(mtemp);
						}

						List<Map<String, String>> friday = new ArrayList<Map<String, String>>();
						for (int i = 16; i < 20; ++i) {
							Map<String, String> mtemp = new HashMap<String, String>();
							TimetableEntity te = lte.get(i);
							mtemp.put("classname", te.getStr_classname()
									.equals("") ? "无" : te.getStr_classname());
							mtemp.put("classroom", te.getStr_classroom()
									.equals("") ? "无" : te.getStr_classroom());
							mtemp.put(
									"teacher",
									te.getStr_teacher().equals("") ? "无" : te
											.getStr_teacher());
							mtemp.put(
									"times",
									te.getStr_times().equals("") ? "无" : te
											.getStr_times());
							// temp.put("group_name", "星期五");
							friday.add(mtemp);
						}
						childData.clear();
						childData.add(monday);
						childData.add(tuesday);
						childData.add(wednesday);
						childData.add(thusday);
						childData.add(friday);



						BaseExpandableListAdapter be = new AdapterSP(
								getActivity());
						el.setAdapter(be);
						Toast.makeText(getActivity(), "刷新成功",
								Toast.LENGTH_SHORT).show();
						swipeLayout.setRefreshing(false);
						guard = false;
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(
								getActivity(),
								getResources().getText(R.string.server_timeout),
								Toast.LENGTH_LONG).show();
						swipeLayout.setRefreshing(false);
						guard = false;
					}
				});
		sr.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(sr);

	}
}
