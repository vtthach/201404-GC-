package cntg.imusm.gchat.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import cntg.imusm.commonutils.debug.MyDebugLog;
import cntg.imusm.commonutils.notifycation.ToastUtil;
import cntg.imusm.commonutils.view.GraphicsUtils;
import cntg.imusm.gchat.R;
import cntg.imusm.gchat.adapter.CustomInfoWindowAdapter;
import cntg.imusm.gchat.model.FriendModel;
import cntg.imusm.gchat.model.MyItem;
import cntg.imusm.gchat.model.MyItemReader;
import cntg.imusm.gchat.model.Person;
import cntg.imusm.gchat.view.MultiDrawable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentMap extends SupportMapFragment implements
		ClusterManager.OnClusterClickListener<Person>,
		ClusterManager.OnClusterInfoWindowClickListener<Person>,
		ClusterManager.OnClusterItemClickListener<Person>,
		ClusterManager.OnClusterItemInfoWindowClickListener<Person> {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private ClusterManager<Person> mClusterManagerPerson;

	private static final String TAG = "FragmentMap";
	GoogleMap mMap;
	private ClusterManager<MyItem> mClusterManagerItem;

	@Override
	public void onAttach(Activity activity) {
		MyDebugLog.i(TAG, "onAttach ");
		super.onAttach(activity);
	}

	public void changeState(Drawable d, FriendModel model) {
		if (model != null) {
			MyDebugLog.i(TAG, "changeState: " + model.name);
			LatLng latLng = model.getLatLng();
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
			onAddMarker(mMap, latLng, model.name, onGetBitmapFromDrawable(d));
		}
	}

	private Bitmap onGetBitmapFromDrawable(Drawable d) {
		return GraphicsUtils.drawableToBitmap(d);
	}

	private Marker onAddMarker(GoogleMap map, LatLng latlng, String title,
			Bitmap bitmap) {
		MarkerOptions mO = new MarkerOptions().position(latlng);
		if (title != null) {
			mO.title(title);
		}
		if (bitmap != null) {
			mO.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
		}
		mO.snippet("4:pm");
		mO.anchor(0.5f, 0.5f);
		return map.addMarker(mO);
	}

	public void initMapIfNeed() {
		if (mMap != null) {
			return;
		}
		mMap = getMap();
		if (mMap != null) {
			mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
			mMap.getUiSettings().setZoomControlsEnabled(false);
			mMap.getUiSettings().setCompassEnabled(true);
			// start INIT data
			onInitData();
		}
	}

	private void onInitData() {
		if (mClusterManagerPerson == null) {
			mClusterManagerPerson = new ClusterManager<Person>(getActivity(),
					getMap());
			mClusterManagerPerson.setRenderer(new PersonRenderer());
		} else {
			mClusterManagerPerson.clearItems();
			mClusterManagerPerson.cluster();
		}

		getMap().setOnCameraChangeListener(mClusterManagerPerson);
		getMap().setOnMarkerClickListener(mClusterManagerPerson);
		getMap().setOnInfoWindowClickListener(mClusterManagerPerson);
		mClusterManagerPerson.setOnClusterClickListener(this);
		mClusterManagerPerson.setOnClusterInfoWindowClickListener(this);
		mClusterManagerPerson.setOnClusterItemClickListener(this);
		mClusterManagerPerson.setOnClusterItemInfoWindowClickListener(this);

		addItems();
		mClusterManagerPerson.cluster();

		// Test
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		getMap().moveCamera(
				CameraUpdateFactory.newLatLngZoom(new LatLng(10.85642465,
						106.6307473), 13f));
	}

	public void onInitBigClusterData() {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186,
				-0.126446), 10));
		if (mClusterManagerItem == null) {
			mClusterManagerItem = new ClusterManager<MyItem>(getActivity(),
					getMap());
		} else {
			mClusterManagerItem.clearItems();
			mClusterManagerItem.cluster();
		}

		getMap().setOnCameraChangeListener(mClusterManagerItem);
		try {
			readItems();
			mClusterManagerItem.cluster();

		} catch (JSONException e) {
			Toast.makeText(getActivity(), "Problem reading list of markers.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void readItems() throws JSONException {
		InputStream inputStream = getResources().openRawResource(
				R.raw.radar_search);
		List<MyItem> items = new MyItemReader().read(inputStream);
		for (int i = 0; i < 10; i++) {
			double offset = i / 60d;
			for (MyItem item : items) {
				LatLng position = item.getPosition();
				double lat = position.latitude + offset;
				double lng = position.longitude + offset;
				MyItem offsetItem = new MyItem(lat, lng);
				mClusterManagerItem.addItem(offsetItem);
			}
		}
	}

	public boolean isMapReady() {
		return mMap != null ? true : false;
	}

	private Random mRandom = new Random(1984);

	/**
	 * Draws profile photos inside markers (using IconGenerator). When there are
	 * multiple people in the cluster, draw multiple photos (using
	 * MultiDrawable).
	 */
	private class PersonRenderer extends DefaultClusterRenderer<Person> {
		private final IconGenerator mIconGenerator = new IconGenerator(
				getActivity());
		private final IconGenerator mClusterIconGenerator = new IconGenerator(
				getActivity());
		private final ImageView mImageView;
		private final ImageView mClusterImageView;
		private final int mDimension;

		public PersonRenderer() {
			super(getActivity(), getMap(), mClusterManagerPerson);

			View multiProfile = getActivity().getLayoutInflater().inflate(
					R.layout.multi_profile, null);
			mClusterIconGenerator.setContentView(multiProfile);
			mClusterImageView = (ImageView) multiProfile
					.findViewById(R.id.image);

			mImageView = new ImageView(getActivity());
			mDimension = (int) getResources().getDimension(
					R.dimen.custom_profile_image);
			mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension,
					mDimension));
			int padding = (int) getResources().getDimension(
					R.dimen.custom_profile_padding);
			mImageView.setPadding(padding, padding, padding, padding);
			mIconGenerator.setContentView(mImageView);
		}

		@Override
		protected void onBeforeClusterItemRendered(Person person,
				MarkerOptions markerOptions) {
			if (!isDetached()) {
				// Draw a single person.
				// Set the info window to show their name.
				mImageView.setImageResource(person.profilePhoto);
				Bitmap icon = mIconGenerator.makeIcon();
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
						.title(person.name);
			}
		}

		@Override
		protected void onBeforeClusterRendered(Cluster<Person> cluster,
				MarkerOptions markerOptions) {
			if (!isDetached()) {
				// Draw multiple people.
				// Note: this method runs on the UI thread. Don't spend too much
				// time in here (like in this example).
				List<Drawable> profilePhotos = new ArrayList<Drawable>(
						Math.min(4, cluster.getSize()));
				int width = mDimension;
				int height = mDimension;

				for (Person p : cluster.getItems()) {
					// Draw 4 at most.
					if (profilePhotos.size() == 4)
						break;
					Drawable drawable = getResources().getDrawable(
							p.profilePhoto);
					drawable.setBounds(0, 0, width, height);
					profilePhotos.add(drawable);
				}
				MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
				multiDrawable.setBounds(0, 0, width, height);

				mClusterImageView.setImageDrawable(multiDrawable);
				Bitmap icon = mClusterIconGenerator.makeIcon(String
						.valueOf(cluster.getSize()));
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
			}
		}

		@Override
		protected boolean shouldRenderAsCluster(Cluster cluster) {
			// Always render clusters.
			return cluster.getSize() > 1;
		}
	}

	@Override
	public boolean onClusterClick(Cluster<Person> cluster) {
		// Show a toast with some info when the cluster is clicked.
		String firstName = cluster.getItems().iterator().next().name;
		ToastUtil.showToast(cluster.getSize() + " (including " + firstName
				+ ")", getActivity());
		return true;
	}

	@Override
	public void onClusterInfoWindowClick(Cluster<Person> cluster) {
		// Does nothing, but you could go to a list of the users.
	}

	@Override
	public boolean onClusterItemClick(Person item) {
		// Does nothing, but you could go into the user's profile page, for
		// example.
		return false;
	}

	@Override
	public void onClusterItemInfoWindowClick(Person item) {
		// Does nothing, but you could go into the user's profile page, for
		// example.
	}

	private void addItems() {
		// http://www.flickr.com/photos/sdasmarchives/5036248203/
		mClusterManagerPerson.addItem(new Person(position(), "Walter",
				R.drawable.walter));

		// http://www.flickr.com/photos/usnationalarchives/4726917149/
		mClusterManagerPerson.addItem(new Person(position(), "Gran",
				R.drawable.gran));

		// http://www.flickr.com/photos/nypl/3111525394/
		mClusterManagerPerson.addItem(new Person(position(), "Ruth",
				R.drawable.ruth));

		// http://www.flickr.com/photos/smithsonian/2887433330/
		mClusterManagerPerson.addItem(new Person(position(), "Stefan",
				R.drawable.stefan));

		// http://www.flickr.com/photos/library_of_congress/2179915182/
		mClusterManagerPerson.addItem(new Person(position(), "Mechanic",
				R.drawable.mechanic));

		// http://www.flickr.com/photos/nationalmediamuseum/7893552556/
		mClusterManagerPerson.addItem(new Person(position(), "Yeats",
				R.drawable.yeats));

		// http://www.flickr.com/photos/sdasmarchives/5036231225/
		mClusterManagerPerson.addItem(new Person(position(), "John",
				R.drawable.john));

		// http://www.flickr.com/photos/anmm_thecommons/7694202096/
		mClusterManagerPerson.addItem(new Person(position(),
				"Trevor the Turtle", R.drawable.turtle));

		// http://www.flickr.com/photos/usnationalarchives/4726892651/
		mClusterManagerPerson.addItem(new Person(position(), "Teach",
				R.drawable.teacher));
	}

	private LatLng position() {
		return new LatLng(random(10.84774219, 10.86504364), random(106.6181946,
				106.6402102));
	}

	private double random(double min, double max) {
		return mRandom.nextDouble() * (max - min) + min;
	}

	public void onDiscoverModeChange(boolean isChecked) {
		if (mMap != null) {
			if (isChecked) {
				onInitBigClusterData();
			} else {
				onInitData();
			}
		}
	}
}