package tyt.android.bigplanettracks;

import tyt.android.bigplanettracks.maps.NavControls;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;

public class BigPlanetTracks extends TabActivity implements OnTouchListener {	
	/*
	 * Tabs/View navigation:
	 */
	private static final int NUM_TABS = 2;
	private int currentTab = 0;

	private NavControls navControls;

	private final int icons[] = {
			R.drawable.menu_by_map,
			R.drawable.menu_by_time };

	private final Runnable nextActivity = new Runnable() {
		public void run() {
			currentTab = (currentTab + 1) % NUM_TABS;
			navControls.setLeftIcon(icons[(currentTab + NUM_TABS - 1) % NUM_TABS]);
			navControls.setRightIcon(icons[(currentTab + NUM_TABS + 1) % NUM_TABS]);
			getTabHost().setCurrentTab(currentTab);
			navControls.show();
		}
	};

	private final Runnable prevActivity = new Runnable() {
		public void run() {
			currentTab--;
			if (currentTab < 0) {
				currentTab = NUM_TABS - 1;
			}
			navControls.setLeftIcon(icons[(currentTab + NUM_TABS - 1) % NUM_TABS]);
			navControls.setRightIcon(icons[(currentTab + NUM_TABS + 1) % NUM_TABS]);
			getTabHost().setCurrentTab(currentTab);
			navControls.show();
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			navControls.show();
		}
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// R.drawable.globe won't appear but needed
		final TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1")
			.setIndicator("Map", getResources().getDrawable(R.drawable.globe))
			.setContent(new Intent(this, BigPlanet.class)));
		tabHost.addTab(tabHost.newTabSpec("tab2")
			.setIndicator("Stats", getResources().getDrawable(R.drawable.globe))
			.setContent(new Intent(this, StatsActivity.class)));
		
		// Hide the tab widget itself. We'll use overlayed prev/next buttons to
		// switch between the tabs:
		tabHost.getTabWidget().setVisibility(View.GONE);

		RelativeLayout layout = new RelativeLayout(this);
		LayoutParams params = new LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		navControls = new NavControls(this, layout, prevActivity, nextActivity);
		navControls.setLeftIcon(icons[NUM_TABS - 1]);
		navControls.setRightIcon(icons[1]);
		navControls.show();
		tabHost.addView(layout);
		layout.setOnTouchListener(this);
	}
	
}