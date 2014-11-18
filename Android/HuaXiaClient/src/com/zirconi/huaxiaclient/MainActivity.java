package com.zirconi.huaxiaclient;

import com.zirconi.etc.GlobalApplication;
import com.zirconi.ui.DrawerFragment;
import com.zirconi.ui.MainFragment;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

	GlobalApplication g;
	DrawerLayout drawer;
	Fragment f1;
	boolean i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		i = false;
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
				R.drawable.ic_launcher, R.string.hello_world,
				R.string.hello_world) {

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				Log.d("TAG", "CLOSED");
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				Log.d("TAG", "OPENED");
				
			}

		};
		drawer.setDrawerListener(toggle);

		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction ft = fManager.beginTransaction();
		f1 = new MainFragment();
		Fragment f2 = new DrawerFragment();
		ft.replace(R.id.main_content, f1);
		ft.replace(R.id.drawer_content, f2);
		ft.commit();
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		ab.show();
		g = (GlobalApplication) getApplication();
		Log.d("NAME", g.getgDomObj().getStuinfoEntity().getStr_name());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.timetable_item:
			((MainFragment) f1).setPage(0);
			break;
		case R.id.transcript_item:
			((MainFragment) f1).setPage(1);
			break;

		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("退出").setMessage("确认退出吗？")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("取消", null).show();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			i = !i;
			if (i)
				drawer.openDrawer(Gravity.LEFT);
			else {
				drawer.closeDrawer(Gravity.LEFT);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
