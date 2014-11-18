package com.zirconi.ui;

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
import com.zirconi.etc.GlobalApplication;
import com.zirconi.huaxiaclient.MainActivity;
import com.zirconi.huaxiaclient.R;
import com.zirconi.parser.XMLParser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

public class LoginFragment extends Fragment implements View.OnClickListener {
	Button btn_login_exit;
	Button btn_login_submit;
	EditText et_login_id;
	EditText et_login_pwd;
	GlobalApplication g;
	ProgressDialog pd;
	SharedPreferences sp;
	String id;
	String pwd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(
				com.zirconi.huaxiaclient.R.layout.login_fragment_layout,
				container, false);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		g = (GlobalApplication) getActivity().getApplication();
		this.btn_login_exit = (Button) getActivity().findViewById(
				R.id.btn_cancel);
		this.btn_login_submit = (Button) getActivity()
				.findViewById(R.id.btn_ok);
		this.et_login_id = (EditText) getActivity().findViewById(R.id.et_id);
		this.et_login_pwd = (EditText) getActivity().findViewById(R.id.et_pwd);
		sp = getActivity().getSharedPreferences(
				"com.zirconi.huaxiaclient_preferences", 0);
		btn_login_exit.setOnClickListener(this);
		btn_login_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			// This is Main Logic
			id = this.et_login_id.getText().toString();
			pwd = this.et_login_pwd.getText().toString();
			if (id == null || pwd == null || id.equals("") || pwd.equals("")) {
				// error
				Toast.makeText(getActivity(), "输入有误，请重新输入", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			String serveraddr = g.getServerAddr();
			String academiccode = g.getAcademicCode();
			String url = serveraddr + "MAGIC?num=" + id + "&pwd=" + pwd
					+ "&server=" + academiccode;
			Log.d("URL", url);
			// start roll
			pd = ProgressDialog.show(getActivity(), "正在连接...", "正在连接...");
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
								pd.dismiss();
								Toast.makeText(getActivity(), "服务器负载已满，请稍后重试",
										Toast.LENGTH_LONG).show();
								return;

							}
							if (status.isIs_timeout()) {
								pd.dismiss();
								Toast.makeText(
										getActivity(),
										getResources().getText(
												R.string.academic_timeout),
										Toast.LENGTH_LONG).show();
								return;
							}

							if (status.isIs_syserror()) {
								pd.dismiss();
								Toast.makeText(getActivity(), "教务系统错误",
										Toast.LENGTH_LONG).show();
								return;
							}

							if (status.isIs_wrong_password()) {
								pd.dismiss();
								Toast.makeText(getActivity(), "密码错误",
										Toast.LENGTH_LONG).show();
								return;
							}

							if (status.isIs_stuinfo_ok() == false
									|| status.isIs_timetable_ok() == false
									|| status.isIs_transcript_ok() == false) {
								pd.dismiss();
								Toast.makeText(getActivity(), "获取信息失败",
										Toast.LENGTH_LONG).show();
								return;
							}

							// success
							g.setgDomObj(temp);
							DomDBHelper helper = new DomDBHelper(getActivity());
							DBTools.SaveDomToDB(helper, temp);
							sp.edit().putString("id", id).commit();
							sp.edit().putString("pwd", pwd).commit();
							// cancel roll
							pd.dismiss();
							Intent i = new Intent(getActivity(),
									MainActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().finish();
							startActivity(i);
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							pd.dismiss();
							Toast.makeText(
									getActivity(),
									getResources().getText(
											R.string.server_timeout),
									Toast.LENGTH_LONG).show();
						}
					});
			sr.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
			mQueue.add(sr);

			break;

		case R.id.btn_cancel:
			new AlertDialog.Builder(getActivity()).setTitle("退出")
					.setMessage("确认退出吗？")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							getActivity().finish();
						}
					}).setNegativeButton("取消", null).show();
			break;
		}
	}
}