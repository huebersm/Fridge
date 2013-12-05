package com.example.fridge.UI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fridge.R;

import fridge.FridgeItem;
import fridge.InFridge;

public class MailListAdapter extends BaseAdapter {

	ArrayList<InFridge> items;
	Context context;
	private LayoutInflater mInflater;
	private CacheManager mCacheManager;
	AddDialogInterface callback;

	public MailListAdapter(Activity context, ArrayList<InFridge> items) {
		if (items == null)
			items = new ArrayList<InFridge>();
		mCacheManager = CacheManager.getInst(context);
		this.items = items;
		this.context = context;
		callback = (AddDialogInterface) context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private static class ViewHolder {
		TextView name;
		TextView expire;
		ImageView icon;
		ImageButton trash;

	}

	@Override
	public void notifyDataSetChanged() {
		System.out.println("notifyDataSetChanged");
	
		Collections.sort(items, new MyFridgeItemComparator());
		
		super.notifyDataSetChanged();
	}
	private class MyFridgeItemComparator implements Comparator<InFridge> {

		@Override
		public int compare(InFridge lhs, InFridge rhs) {
			if(lhs.getExpire()>rhs.getExpire())
				return 1;
			if(lhs.getExpire()<rhs.getExpire()) 
				return -1;
			return 0;
		}
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView " + position + " " + convertView);
		ViewHolder vh = null;
		if (convertView != null) {
			vh = (ViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.fridgelistitem, null);

			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.item_name);
			vh.expire = (TextView) convertView.findViewById(R.id.item_expire);
			vh.icon = (ImageView) convertView.findViewById(R.id.item_icon);
			vh.trash = (ImageButton) convertView.findViewById(R.id.item_trash);

			convertView.setTag(vh);

		}
		final InFridge actual = getItem(position);

		FridgeItem fridgeItem = mCacheManager.getFridgeItem(actual
				.getInternalId());
		StringBuilder sb = new StringBuilder();
		sb.append(fridgeItem.getName());
		if (actual.getAnzahl() > 1) {
			sb.append(" (" + actual.getAnzahl() + ")");
		}
		vh.name.setText(sb.toString());
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeInMillis(actual.getExpire());
		String expireDate = c.get(Calendar.DAY_OF_MONTH) + "."
				+ (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR);
		vh.expire.setText(expireDate);
		vh.trash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder ab = new AlertDialog.Builder(context);
				ab.setTitle("Sicher?");
				ab.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (actual.getAnzahl() > 1) {
									InFridge newItem = actual;
									newItem.setAnzahl(newItem.getAnzahl() - 1);
									mCacheManager.updateInFridgeItem(newItem);
								} else {
									mCacheManager.deleteInFridge(actual);
								}
								callback.ready(null);
							}
						}).setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				ab.show();
			}
		});
		return convertView;
	}

	@Override
	public int getCount() {
		if (items == null)
			return 0;
		return items.size();
	}

	@Override
	public InFridge getItem(int arg0) {
		if (items == null)
			return null;
		InFridge t = items.get(arg0);
		if (t == null)
			return null;
		return t;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addAll(ArrayList<InFridge> allInFridgeItems) {
		items.clear();
		items = allInFridgeItems;
		notifyDataSetChanged();

	}
}
