package com.dragamstudios.waarisdezon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KustFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataGetter.getSunniestCoast();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		TextView info = (TextView) view.findViewById(R.id.textView1);
		info.setText(getResources().getString(R.string.zonnigstekust));
		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		default:
			refresh.setActionView(R.layout.refreshprocess);
			dataGetter.getSunniestCoast();
			break;
		}
		return true;
	}

}
