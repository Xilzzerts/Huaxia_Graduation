package com.zirconi.ui;

import java.util.ArrayList;

import com.zirconi.huaxiaclient.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	ViewPager viewPager;
	ArrayList<Fragment> fList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(
				com.zirconi.huaxiaclient.R.layout.main_fragment_layout,
				container, false);
		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
		Fragment timetableFragment = new TimetableFragment();
		Fragment transcriptFragment = new TranscriptFragment();
		fList = new ArrayList<Fragment>();
		fList.add(timetableFragment);
		fList.add(transcriptFragment);
		viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fList.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fList.get(arg0);
			}
		});
	}

	public void setPage(int n) {

		viewPager.setCurrentItem(n);
	}

}
