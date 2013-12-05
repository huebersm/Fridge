
package com.example.fridge.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

public abstract class TListAdapter<T> extends ArrayAdapter<T> implements Filterable {

    protected Activity mContext;

    protected List<T> mObjects;
    protected List<T> mOriginalObjects;
    protected List<T> mFitems;

    protected final Object mLock = new Object();



    protected LayoutInflater mInflater;

    public TListAdapter(Activity context, int resource, List<T> objects) {
        super(context, resource, objects);
        mContext = context;
        if (objects == null)
            objects = new ArrayList<T>();
        mObjects = new ArrayList<T>(objects);
        mFitems = new ArrayList<T>(objects);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.i("Adapter", "Adapter Constructed");
        setNotifyOnChange(false);
    }

    /**
     * Zeigt an, wann {@link #notifyDataSetChanged()} gerufen werden wenn
     * {@link #mObjects} modifiziert wurde
     */
    protected boolean mNotifyChanged = true;

    /**
     * Meldet dem Adapter, dass die Daten geändert worden sind. Verursacht
     * update der Liste
     */
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyChanged = true;
    }

    /**
     * Triggert automatisches aufrufen von {@link #notifyDataSetChanged}, wenn
     * durch ({@link #add}, {@link #insert}, {@link #remove}, {@link #clear})
     * die Daten geändert wurden. False: manuell notifyDataSetChanged() aufrufen
     * Sobald notifyDataSetChanged aufgerufen wurde, ist dies wieder automatisch
     * true
     * 
     * @default true
     * @param notifyOnChange=true bewirkt automatisches aufrufen von
     *            {@link #notifyDataSetChanged} wenn die Liste verändert wurde
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyChanged = notifyOnChange;
    }

    /**
     * Ersetzt die komletten Daten des Adapters per Hand
     * 
     * @param data
     */
    public void setData(List<T> data) {
        try {
            mObjects = data;
            mOriginalObjects = data;
            if (mNotifyChanged)
                notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(Collection<? extends T> collection) {
        if (mOriginalObjects != null) {
            synchronized (mLock) {
                mOriginalObjects.addAll(collection);
                if (mNotifyChanged)
                    notifyDataSetChanged();
            }
        } else {
            if (mObjects != null) {
                mObjects.addAll(collection);
                if (mNotifyChanged)
                    notifyDataSetChanged();
            }
        }
    }

    /**
     * Adds the specified items at the end of the array.
     * 
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        if (mOriginalObjects != null) {
            synchronized (mLock) {
                for (T item : items) {
                    mOriginalObjects.add(item);
                }
                if (mNotifyChanged)
                    notifyDataSetChanged();
            }
        } else {
            for (T item : items) {
                mObjects.add(item);
            }
            if (mNotifyChanged)
                notifyDataSetChanged();
        }
    }

    /**
     * Inserts the specified object at the specified index in the array.
     * 
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        if (mOriginalObjects != null) {
            synchronized (mLock) {
                mOriginalObjects.add(index, object);
                if (mNotifyChanged)
                    notifyDataSetChanged();
            }
        } else {
            mObjects.add(index, object);
            if (mNotifyChanged)
                notifyDataSetChanged();
        }
    }

    /**
     * Removes the specified object from the array.
     * 
     * @param object The object to remove.
     */
    public void remove(T object) {
        if (mOriginalObjects != null) {
            synchronized (mLock) {
                mOriginalObjects.remove(object);
            }
        } else {
            mObjects.remove(object);
        }
        if (mNotifyChanged)
            notifyDataSetChanged();
    }

    /**
     * Entfernt alle Elemente aus der Liste
     */
    public void clear() {
        if (mOriginalObjects != null) {
            synchronized (mLock) {
                mOriginalObjects.clear();
            }
        } else {
            if (mObjects != null)
                mObjects.clear();
        }
        if (mNotifyChanged)
            notifyDataSetChanged();
    }

    /**
     * Gibt Objekt der Liste zurück
     */
    @Override
    public T getItem(int position) {
        if (mObjects != null) {
            return mObjects.get(position);
        }
        else
            return null;

    }

    /**
     * Gibt Anzahl der Objekte in der Liste zurück
     */
    @Override
    public int getCount() {

        if (mObjects != null) {
            return mObjects.size();
        }
        else
            return 0;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);
}
