package com.example.fridge.UI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.example.fridge.R;

import fridge.FridgeItem;
import fridge.InFridge;

public class AddDialog extends DialogFragment {
	AutoCompleteTextView nametext;
	CacheManager mCacheManager;
	DatePicker datepicker;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		actualSelectedItemBySuggestion = null;
		mCacheManager = CacheManager.getInst(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder;
		View dialogView = inflater.inflate(R.layout.additem, null);

		builder = new AlertDialog.Builder(getActivity())
				.setTitle("Hinzufï¿½gen")
				.setView(dialogView)
				.setPositiveButton("Add", new MyDialogInterface())
				.setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {

							}
						});

		nametext = (AutoCompleteTextView) dialogView
				.findViewById(R.id.dialog_object_name);
		nametext.setThreshold(1);
		
		datepicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

		nametext.addTextChangedListener(new FridgeItemTextWatcher());
		nametext.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				actualSelectedItemBySuggestion = oldAdapter.getItem(arg2).o;
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(new Date().getTime()
						+ actualSelectedItemBySuggestion.getAvg_haltbarkeit());
				datepicker.updateDate(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(nametext.getWindowToken(), 0);

			}
		});

		return builder.create();

	}

	private class FridgeItemW {
		FridgeItem o;

		public FridgeItemW(FridgeItem k) {
			o = k;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return o.getName();
		}
	}

	FridgeItem actualSelectedItemBySuggestion;
	ArrayAdapter<FridgeItemW> adapter;
	ArrayAdapter<FridgeItemW> oldAdapter;

	private class FridgeItemTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() == 0)
				actualSelectedItemBySuggestion = null;
			else {
				oldAdapter = adapter;
				List<FridgeItem> items = mCacheManager.getFridgeItemByChars(s);
				ArrayList<FridgeItemW> wrapper = new ArrayList<FridgeItemW>();
				for (FridgeItem k : items) {
					wrapper.add(new FridgeItemW(k));
				}
				if (items != null) {
					adapter = new ArrayAdapter<FridgeItemW>(getActivity(),
							android.R.layout.simple_dropdown_item_1line,
							wrapper);
					nametext.setAdapter(adapter);
					
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

	private class MyDialogInterface implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			String name = nametext.getText().toString();
			FridgeItem fridgeItem = null;
			if (actualSelectedItemBySuggestion != null) {
				fridgeItem = actualSelectedItemBySuggestion;

				Calendar calendar = Calendar.getInstance();
				int yearNow, monthNow, dayNow;
				yearNow = calendar.get(Calendar.YEAR);
				monthNow = calendar.get(Calendar.MONTH);
				dayNow = calendar.get(Calendar.DAY_OF_MONTH);
				calendar.clear();
				calendar.set(yearNow, monthNow, dayNow);

				Date today = new Date();
				today.setTime(calendar.getTimeInMillis());
				long todayTime = today.getTime();
				calendar.clear();
				calendar.set(datepicker.getYear(), datepicker.getMonth(),
						datepicker.getDayOfMonth());
				long expire = calendar.getTimeInMillis();
				
				calendar.setTimeInMillis(calendar.getTimeInMillis()-today.getTime());
				
				long new_haltbarkeit = calendar.getTimeInMillis();
				
				
				long nearlyOneDay = 1000*60*60*23;
				if (new_haltbarkeit - fridgeItem.getAvg_haltbarkeit() > nearlyOneDay) {
					long anz = fridgeItem.getTimes_changed();
					if (anz > 50)
						anz = 50; // um die veränderungen bemerkbar zu halten
					else
						anz++;
					long recalcHaltbarkeit = anz
							* fridgeItem.getAvg_haltbarkeit() + new_haltbarkeit;
					recalcHaltbarkeit = recalcHaltbarkeit / (anz + 1);
					fridgeItem.setAvg_haltbarkeit(recalcHaltbarkeit);
					fridgeItem.setTimes_changed(anz);
					mCacheManager.insertOrReplaceFridgeItem(fridgeItem);
				} else {
					fridgeItem
							.setTimes_changed(fridgeItem.getTimes_changed() + 1);
					mCacheManager.insertOrReplaceFridgeItem(fridgeItem);
				}

				InFridge inFridgeItem = new InFridge();
				ArrayList<InFridge> allProductsThatAreAlreadyInFridge = mCacheManager
						.getInFridgeItem(fridgeItem.getInternalId());
				boolean keinsmitgleichemexpirevorhanden = true;
				inFridgeItem.setInternalId(fridgeItem.getInternalId());
				for (InFridge itemausliste : allProductsThatAreAlreadyInFridge) {
					
					if (itemausliste.getExpire() - expire > nearlyOneDay|| itemausliste.getExpire() - expire< -(nearlyOneDay )) {
			
					} else {
						keinsmitgleichemexpirevorhanden=false;
						inFridgeItem.setExpire(itemausliste.getExpire());
						inFridgeItem.setAnzahl(itemausliste.getAnzahl() + 1);
						inFridgeItem.setInserted(itemausliste.getInserted());
						mCacheManager.updateInFridgeItem(inFridgeItem);//BREAK!
					}
				}
				if(keinsmitgleichemexpirevorhanden || allProductsThatAreAlreadyInFridge.size() == 0) {
					inFridgeItem.setInserted(todayTime);
					inFridgeItem.setExpire(expire);
					inFridgeItem.setAnzahl(1);
					mCacheManager.insertInFrigeItem(inFridgeItem);
				}

			} else {
				// FridgeItemW testitem = adapter.getItem(i);
				// TODO if(object already found bei eingabe)..
				// wenn hier kein objekt vorhanden dann ist sicher, dass es noch
				// nicht im cache ist!

				fridgeItem = null;

				fridgeItem = new FridgeItem();
				fridgeItem.setName(name);

				Calendar calendar = Calendar.getInstance();
				int yearNow, monthNow, dayNow;
				yearNow = calendar.get(Calendar.YEAR);
				monthNow = calendar.get(Calendar.MONTH);
				dayNow = calendar.get(Calendar.DAY_OF_MONTH);
				calendar.clear();
				calendar.set(yearNow, monthNow, dayNow);

				Date today = new Date();
				today.setTime(calendar.getTimeInMillis());
				long todayTime = today.getTime();
				calendar.clear();
				calendar.set(datepicker.getYear(), datepicker.getMonth(),
						datepicker.getDayOfMonth());
				long expire = calendar.getTimeInMillis();
				
				calendar.setTimeInMillis(calendar.getTimeInMillis()-today.getTime());
				
				long new_haltbarkeit = calendar.getTimeInMillis();
				

				
				fridgeItem.setAvg_haltbarkeit(new_haltbarkeit);
				fridgeItem.setTimes_changed((long) 1);
				long insertId = mCacheManager.insertFridgeItem(fridgeItem);
				FridgeItem inDbItem = mCacheManager.getFridgeItem(insertId);
				InFridge inFridgeItem;
				inFridgeItem = new InFridge();
				inFridgeItem.setInternalId(insertId);
				inFridgeItem.setAnzahl(1);
				inFridgeItem.setInserted(todayTime);
				inFridgeItem.setExpire(expire);

				mCacheManager.insertOrReplaceInFridgeItem(inFridgeItem);
			}
			((MainActivity) getActivity()).ready(fridgeItem);
		}
	}
}
