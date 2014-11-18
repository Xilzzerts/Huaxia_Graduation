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
import com.zirconi.entity.TranscriptEntity;
import com.zirconi.etc.GlobalApplication;

import com.zirconi.huaxiaclient.R;
import com.zirconi.parser.XMLParser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TranscriptFragment extends Fragment implements OnRefreshListener {
	ListView lv;
	ArrayList<Map<String, String>> item;
	DomEntity g;
	SwipeRefreshLayout swipeLayout;
	SharedPreferences sp;
	GlobalApplication ga;
	boolean guard;

	static final String[] FROM = { "academic_year", "assessment_methods",
			"course_name", "course_teacher", "course_type", "credit",
			"grade_point", "makeup_mark", "rebuild_mark", "semester",
			"total_mark" };
	static final int[] TO = { R.id.tv_trans_academic_year,
			R.id.tv_trans_assessment_methods, R.id.tv_trans_course_name,
			R.id.tv_trans_course_teacher, R.id.tv_trans_course_type,
			R.id.tv_trans_credit, R.id.tv_trans_grade_point,
			R.id.tv_trans_makeup_mark, R.id.tv_trans_rebuild_mark,
			R.id.tv_trans_semester, R.id.tv_trans_total_mark };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(
				com.zirconi.huaxiaclient.R.layout.transcript_layout, container,
				false);
		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		guard = false;
		lv = (ListView) getActivity().findViewById(R.id.lv_transcript);
		g = ((GlobalApplication) (getActivity().getApplication())).getgDomObj();
		List<TranscriptEntity> t = g.getTranscriptEntity();
		item = new ArrayList<Map<String, String>>();
		ga = (GlobalApplication) getActivity().getApplication();
		sp = getActivity().getSharedPreferences(
				"com.zirconi.huaxiaclient_preferences", 0);

		swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(
				R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(R.color.lightblue, R.color.lightgreen,
				R.color.orange, R.color.red);

		for (TranscriptEntity x : t) {
			Map<String, String> child = new HashMap<String, String>();

			child.put("academic_year", x.getStr_academic_year());
			child.put("assessment_methods", x.getAssessment_methods());
			child.put("course_name", x.getCourse_name());
			child.put("course_teacher", x.getCourse_teacher());
			child.put("course_type", x.getCourse_type());
			if (x.getCredit().equals(""))
				child.put("credit", "无");
			else
				child.put("credit", x.getCredit());

			if (x.getGrade_point().equals(""))
				child.put("grade_point", "无");
			else
				child.put("grade_point", x.getGrade_point());

			if (x.getMakeup_mark().equals(""))
				child.put("makeup_mark", "无");
			else
				child.put("makeup_mark", x.getMakeup_mark());

			if (x.getRebuild_mark().equals(""))
				child.put("rebuild_mark", "无");
			else
				child.put("rebuild_mark", x.getRebuild_mark());
			child.put("semester", x.getSemester());

			if (x.getTotal_mark().equals(""))
				child.put("total_mark", "无");
			else
				child.put("total_mark", x.getTotal_mark());
			item.add(child);
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), item,
				R.layout.transcript_item, FROM, TO);
		lv.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		// get data again

		if(guard)
			return;
		guard = true;
		String id = sp.getString("id", null);
		String pwd = sp.getString("pwd", null);
		if (id == null || pwd == null) {
			// impossible
			swipeLayout.setRefreshing(false);
			return;
		}

		String serveraddr = ga.getServerAddr();
		String academiccode = ga.getAcademicCode();
		String url = serveraddr + "MAGIC?num=" + id + "&pwd=" + pwd
				+ "&server=" + academiccode;
		Log.d("URL", url);
		// start roll
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
						ga.setgDomObj(temp);
						DomDBHelper helper = new DomDBHelper(getActivity());
						DBTools.SaveDomToDB(helper, temp);
						g = ((GlobalApplication) (getActivity()
								.getApplication())).getgDomObj();
						List<TranscriptEntity> t = g.getTranscriptEntity();
						item = new ArrayList<Map<String, String>>();

						for (TranscriptEntity x : t) {
							Map<String, String> child = new HashMap<String, String>();
							child.put("academic_year", x.getStr_academic_year());
							child.put("assessment_methods",
									x.getAssessment_methods());
							child.put("course_name", x.getCourse_name());
							child.put("course_teacher", x.getCourse_teacher());
							child.put("course_type", x.getCourse_type());
							if (x.getCredit().equals(""))
								child.put("credit", "无");
							else
								child.put("credit", x.getCredit());

							if (x.getGrade_point().equals(""))
								child.put("grade_point", "无");
							else
								child.put("grade_point", x.getGrade_point());

							if (x.getMakeup_mark().equals(""))
								child.put("makeup_mark", "无");
							else
								child.put("makeup_mark", x.getMakeup_mark());

							if (x.getRebuild_mark().equals(""))
								child.put("rebuild_mark", "无");
							else
								child.put("rebuild_mark", x.getRebuild_mark());
							child.put("semester", x.getSemester());

							if (x.getTotal_mark().equals(""))
								child.put("total_mark", "无");
							else
								child.put("total_mark", x.getTotal_mark());
							item.add(child);
						}

						SimpleAdapter adapter = new SimpleAdapter(
								getActivity(), item, R.layout.transcript_item,
								FROM, TO);
						lv.setAdapter(adapter);
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
