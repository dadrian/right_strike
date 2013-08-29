package com.rightstrike.screens;

import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightstrike.R;

public class DeviceListFragment extends DialogFragment {	
	
	private List<String> mDevices;
	
	public DeviceListFragment() {
		mDevices = new ArrayList<String>();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.device_list, container, false);
		return v;
	}
}
