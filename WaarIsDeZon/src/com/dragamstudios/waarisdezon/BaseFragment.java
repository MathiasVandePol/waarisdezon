package com.dragamstudios.waarisdezon;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import com.dragamstudios.waarisdezon.model.City;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment implements Observer {
	protected DataGetter dataGetter;
	private City city = new City();
	View view;
	MenuItem refresh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dataGetter = new DataGetter(this);
		dataGetter.addObserver(this);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment, container, false);
		redrawView();
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public String getTitle() {
		return getArguments().getString("title");
	}

	protected void redrawView() {
		TextView cityname = (TextView) view.findViewById(R.id.txtCityname);
		TextView clouds = (TextView) view.findViewById(R.id.txtClouds);
		TextView temperature = (TextView) view
				.findViewById(R.id.txtTemperature);
		TextView wind = (TextView) view.findViewById(R.id.txtWind);
		TextView date = (TextView) view.findViewById(R.id.txtDate);
		if (city.getCityname() != null) {
			cityname.setText(city.getCityname());
			clouds.setText(city.getClouds() + "%");
			temperature.setText(city.getTemperature() + "°C");
			wind.setText(city.getWind() + "km/h");
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM HH:mm:ss");
			try {
				date.setText(getString(R.string.datum,
						sdf.format(city.getDate())));
			} catch (Exception e) {

			}
		} else {
			cityname.setText(getString(R.string.noinfo));
			clouds.setText("N/A");
			temperature.setText("N/A");
			wind.setText("N/A");
			date.setText("N/A");
		}

	}

	public void succesfulDataGet() {
		Crouton.showText(getActivity(), R.string.cityfound, Style.CONFIRM,
				(ViewGroup) getView());
	}
	
	public void unSuccesfulDataGet() {
		Crouton.showText(getActivity(), R.string.nocityfound, Style.INFO,
				(ViewGroup) getView());
	}
	
	public void noConnection() {
		Crouton.showText(getActivity(), R.string.noconnection, Style.INFO,
				(ViewGroup) getView());
	}

	@Override
	public void update(Observable observable, Object data) {
		if (dataGetter.getCity() != null) {
			this.city = dataGetter.getCity();
			if (view != null) {
				redrawView();
			}
		}
		try {
			refresh.setActionView(null);
		} catch (Exception e) {
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.normal, menu);
		refresh = menu.findItem(R.id.action_refresh);
	}

	public void setRefreshing() {
		try {
			refresh.setActionView(R.layout.refreshprocess);
		} catch (Exception e) {

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_gotodragam:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://search?q=pub:Dragam%20Studios"));
			startActivity(intent);
			break;
		case R.id.action_mobilesite:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.waarisdezon.be"));
			startActivity(browserIntent);
			break;
		case R.id.action_dragamfacebook:
			Intent facebook = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.facebook.com/DragamStudios"));
			startActivity(facebook);
			break;
		}
		return true;
	}

}
