package com.dragamstudios.waarisdezon;

import java.util.Observable;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.Toast;

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
		getJson("http://waarisdezon.be/rest/sunniestplace.php", false);
	}

	public void getSunniestCoast() {
		getJson("http://waarisdezon.be/rest/sunniestcoast.php", false);
	}

	public void getClosest(String name, String radius) {
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
						setChanged();
						notifyObservers();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Crouton.makeText(fragment.getActivity(),
								R.string.nocityfound, Style.INFO,
								(ViewGroup) fragment.getView()).show();
						setChanged();
						notifyObservers();
					}
				});
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
			city.setDate(json.optString("Date"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parsePersonal(JSONObject json) {
		city = null;
		try {
			city = new City();
			city.setCityname(json.optString("name"));
			city.setClouds(json.optString("clouds"));
			city.setTemperature(json.optString("temp"));
			city.setWind(json.optString("wind"));
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
