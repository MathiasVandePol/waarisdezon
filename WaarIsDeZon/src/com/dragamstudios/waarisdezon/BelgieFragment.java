package com.dragamstudios.waarisdezon;

import android.os.Bundle;
import android.view.MenuItem;

public class BelgieFragment extends BaseFragment {
	protected final String TAG = this.getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataGetter.getSunniestPlace();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.action_refresh:
			item.setActionView(R.layout.refreshprocess);
			dataGetter.getSunniestPlace();
			break;
		}
		return true;
	}

}
