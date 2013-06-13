package com.dragamstudios.waarisdezon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class PersonalDialog extends DialogFragment {

	private String selectedRadius = null;
	private String city = null;
	private PersonalDialogListener listener;

	private PersonalFragment fragment;

	public void setListener(PersonalDialogListener listener) {
		this.listener = listener;
	}

	public String getSelectedRadius() {
		return selectedRadius;
	}

	public void setSelectedRadius(String selectedRadius) {
		this.selectedRadius = selectedRadius;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_personal, null);
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this.getActivity(), R.array.radius,
						android.R.layout.simple_spinner_item);
		builder.setView(view)
				// Add action buttons
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								city = (String) ((TextView) view
										.findViewById(R.id.city)).getText()
										.toString();
								if (listener != null) {			//TODO Fix bug when opening dialog, then rotating and press ok --> listener == null
									listener.onFinishEditDialog(city,		
											selectedRadius);
								}

							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								selectedRadius = null;
							}
						});
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				selectedRadius = (String) adapter.getItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedRadius = (String) adapter.getItem(0);
			}
		});
		return builder.create();
	}

	public interface PersonalDialogListener {
		void onFinishEditDialog(String city, String radius);
	}
}
