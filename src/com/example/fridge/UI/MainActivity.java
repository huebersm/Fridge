package com.example.fridge.UI;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.fridge.R;

import fridge.FridgeItem;
import fridge.InFridge;

public class MainActivity extends FragmentActivity implements
		AddDialogInterface {

	MainActivity mMainActivity;
	CacheManager mCacheManager;
	private TextView noitemsview;
	private FridgeItemList fragment;
	private View fragmentView;

	private String TAG = "MainActivity";
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add:


			AddDialog d = new AddDialog();
			d.show(getSupportFragmentManager(), TAG);
			break;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMainActivity = this;
		mCacheManager = CacheManager.getInst(this);
		noitemsview = (TextView) findViewById(R.id.noitems);
		fragmentView = findViewById(R.id.ui_fridgeitemlist_fragment);



		fragment = (FridgeItemList) getSupportFragmentManager()
				.findFragmentById(R.id.ui_fridgeitemlist_fragment);
		fullRefresh();

	}
	
	private void fullRefresh() {
		if (fragment != null) {
			ArrayList<InFridge> items = mCacheManager.getAllInFridgeItems();
			if (items.size() == 0) {
				showNoItems(true);
			} else {
				showNoItems(false);
				fragment.addAll(mCacheManager.getAllInFridgeItems());
			}
		}
	}



	private void showNoItems(boolean b) {
		if (b) {
			noitemsview.setVisibility(View.VISIBLE);
			fragmentView.setVisibility(View.GONE);
		} else {
			noitemsview.setVisibility(View.GONE);
			fragmentView.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void ready(FridgeItem ready) {

		
		fullRefresh();

	}
}
