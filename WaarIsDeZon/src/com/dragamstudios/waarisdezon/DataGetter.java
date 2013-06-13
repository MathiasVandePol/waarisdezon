package com.dragamstudios.waarisdezon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dragamstudios.waarisdezon.model.City;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DataGetter extends Observable {

	private Fragment fragment;
	private RequestQueue mRequestQueue;
	private City city;

	public DataGetter(Fragment fragment) {
		this.fragment = fragment;
	}

	public void getSunniestPlace() {
		((BaseFragment) fragment).setRefreshing();
		getJson("http://waarisdezon.be/rest/sunniestplace.php", false);
	}

	public void getSunniestCoast() {
		((BaseFragment) fragment).setRefreshing();
		getJson("http://waarisdezon.be/rest/sunniestcoast.php", false);
	}

	public void getClosest(String name, String radius) {
		((BaseFragment) fragment).setRefreshing();
		getJson("http://waarisdezon.be/rest/personal.php?city=" + name
				+ "&distance=" + radius, true);
	}

	private void getJson(String url, final boolean personal) {
		mRequestQueue = Volley.newRequestQueue(fragment.getActivity());
		JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (personal) {
							parsePersonal(response);
						} else {
							parseJSON(response);
						}
						try {
							((BaseFragment) fragment).succesfulDataGet();
						} catch (Exception e) {

						}
						setChanged();
						notifyObservers();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						try {
							if (error instanceof NoConnectionError) {
								((BaseFragment) fragment).noConnection();
							} else {
								((BaseFragment) fragment).unSuccesfulDataGet();
							}
						} catch (Exception e) {

						}
						setChanged();
						notifyObservers();

					}
				});
		jr.setTimeoutMs(2000);
		if (personal) {
			jr.setTimeoutMs(10000);
		}
		mRequestQueue.add(jr);
	}

	private void parseJSON(JSONObject json) {
		city = null;
		try {
			city = new City();
			city.setCityname(json.optString("CityName"));
			city.setClouds(json.optString("Clouds"));
			city.setTemperature(json.optString("Temperature"));
			city.setWind(json.optString("Wind"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			city.setDate(sdf.parse(json.optString("Date")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parsePersonal(JSONObject json) {
		city = null;
		try {
			Calendar cal = Calendar.getInstance();
			city = new City();
			city.setCityname(json.optString("name"));
			city.setClouds(json.optString("clouds"));
			city.setTemperature(json.optString("temp"));
			city.setWind(json.optString("wind"));
			city.setDate(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

}
