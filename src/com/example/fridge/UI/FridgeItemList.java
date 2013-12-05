package com.example.fridge.UI;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import fridge.InFridge;

/**
 * FridgeItemList
 * 
 * @author Huebers
 */
public class FridgeItemList extends ListFragment {
	public final static String ROW_TAG_ID_MAILLIST = "scom.antony.mail.rowtagid";

	protected static final String TAG = "FridgeItemList";
	public FridgeItemList mThis;
	private MailListAdapter mListItemAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i("FridgeItemList", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onAttach(Activity activity) {
		Log.i("FridgeItemList", "onAttach");
		super.onAttach(activity);

		setListAdapter(mListItemAdapter = new MailListAdapter(getActivity(),
				null));

	}


	public void addAll(ArrayList<InFridge> allInFridgeItems) {
		if (mListItemAdapter != null) {
			mListItemAdapter.addAll(allInFridgeItems);
			
		}

	}

}
