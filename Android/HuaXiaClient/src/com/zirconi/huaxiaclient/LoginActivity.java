package com.zirconi.huaxiaclient;

import com.zirconi.etc.GlobalApplication;
import com.zirconi.ui.LoginFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LoginActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		if (((GlobalApplication) getApplication()).isHasLogin()) {
			finish();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		setContentView(R.layout.login_layout);
		ActionBar bar = getSupportActionBar();
		bar.hide();

		// if has login
		// todo

		//
		Fragment loginFragment = new LoginFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.login_content, loginFragment)
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_for_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_setting:
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_about:
			new AlertDialog.Builder(this).setTitle("关于")
					.setMessage("By Zirconi").setPositiveButton("确定", null)
					.show();
			break;
		case R.id.menu_exit:
			new AlertDialog.Builder(this).setTitle("退出").setMessage("确认退出吗？")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("取消", null).show();
		}
		return true;
	}

	
}
