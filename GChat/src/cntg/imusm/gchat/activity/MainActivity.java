package cntg.imusm.gchat.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cntg.imusm.gchat.R;
import cntg.imusm.gchat.fragment.FragmentMap;
import cntg.imusm.gchat.fragment.NavigationDrawerFragment;
import cntg.imusm.gchat.model.FriendModel;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private FragmentMap mFragMap;

	private CheckBox cb_discover_mode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setScrimColor(Color.TRANSPARENT);
		drawerLayout.setDrawerShadow(R.drawable.ic_launcher, Gravity.TOP);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		cb_discover_mode = (CheckBox) findViewById(R.id.cb_discover_mode);
		cb_discover_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(mFragMap!=null){
					mFragMap.onDiscoverModeChange(isChecked);
				}
			}
		});
		
		// Set up map if needed
		setUpMapIfNeeded();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void setUpMapIfNeeded() {
		if(mFragMap == null){
			mFragMap = (FragmentMap) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
		}
		mFragMap.initMapIfNeed();
    }

	@Override
	public void onNavigationDrawerItemSelected(Drawable d, FriendModel model) {
		// update the main content with position
		
		// 1. change state of map fragment
		onStateMapChanged(d, model);
	}

	private void onStateMapChanged(Drawable d, FriendModel model) {
		setUpMapIfNeeded();
		if (mFragMap.isMapReady()) {
			mFragMap.changeState(d, model);
		}		
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Friend list");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
