package cntg.imusm.gchat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cntg.imusm.gchat.R;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/** Demonstrates customizing the info window and/or its contents. */
public class CustomInfoWindowAdapter implements InfoWindowAdapter {
	// These a both viewgroups containing an ImageView with id "badge" and two
	// TextViews with id
	// "title" and "snippet".
	private final View mWindow;
	private final View mContents;
	private Context mContext;

	public CustomInfoWindowAdapter(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindow = inflater.inflate(R.layout.custom_info_window, null);
		mContents = inflater.inflate(R.layout.custom_info_contents, null);
		this.mContext = context;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		render(marker, mWindow);
		return mWindow;
	}

	@Override
	public View getInfoContents(Marker marker) {
//		render(marker, mContents);
//		return mContents;
		return null;
	}

	private void render(Marker marker, View view) {
		//TODO
	}
	
}
