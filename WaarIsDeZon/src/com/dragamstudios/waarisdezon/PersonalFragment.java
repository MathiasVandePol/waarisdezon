package com.dragamstudios.waarisdezon;

import com.dragamstudios.waarisdezon.PersonalDialog.PersonalDialogListener;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersonalFragment extends BaseFragment implements
		PersonalDialogListener {
	private SharedPreferences prefs;
	protected final String TAG = this.getClass().getSimpleName();
	PersonalDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment, container, false);
		if (prefs.contains("city")) {
			getData(prefs.getString("city", "Antwerpen"),
					prefs.getString("radius", "50"));
		}
		redrawView();
		pd = new PersonalDialog();
		pd.setListener(this);
		return view;

	}

	public void showDialog() {

		pd.show(getFragmentManager(), "personaldialog");
	}

	@Override
	public void onFinishEditDialog(String city, String radius) {
		if (city != null) {
			Editor editor = prefs.edit();
			editor.putString("city", city);
			editor.putString("radius", radius);
			editor.commit();
			getData(city, radius);
		}
	}

	public void getData(String city, String radius) {
		try {
			refresh.setActionView(R.layout.refreshprocess);
		} catch (NullPointerException e) {
			// view not created before getdata
		}
		dataGetter.getClosest(city.trim().replace(" ", "%20"), radius);
		TextView tv = (TextView) view.findViewById(R.id.textView1);
		String title = getString(R.string.zonnigstebuurt, radius, city.trim());
		tv.setText(title);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.personal, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.action_search:
			showDialog();
			break;
		default:
			getData(prefs.getString("city", "Antwerpen"),
					prefs.getString("radius", "50"));
			break;
		}

		return true;
	}

	
	

}
