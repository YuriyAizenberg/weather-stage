package com.aizenberg.wheather.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import com.aizenberg.wheather.utils.Logger;
import com.aizenberg.wheather.utils.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Юрий on 06.07.14.
 */
abstract public class AbsAdapter<T> extends BaseAdapter {

    protected List<T> mData = new ArrayList<T>();
    protected Logger logger = LoggerFactory.getLogger(AbsAdapter.class.getSimpleName());
    protected Context context;
    protected LayoutInflater inflater;
    private IEmptyCallback callback;

    public AbsAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCallback(IEmptyCallback callback) {
        this.callback = callback;
    }

    public void setData(List<T> values, Boolean clear) {
        if (values == null) return;
        setData(values, clear, false);
    }

    public void setData(List<T> values, Boolean clear, Boolean addToStart) {
        if (clear) {
            mData.clear();
        }
        if (values != null) {
            if (addToStart) {
                mData.addAll(0, values);
            } else {
                mData.addAll(values);
            }
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> values) {
        setData(values, true, false);
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean notify) {
        mData.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void removeItem(T item) {
        mData.remove(item);
    }

    public void addItem(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void insertItem(int to, T item) {
        mData.add(to, item);
    }

    public T getItem(long position) {
        return getItem((int) position);
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public T getItem(int position) {
        return position >= 0 && position < getCount() ? mData.get(position) : null;
    }

    public T getLastItem() {
        return getItem(getCount() - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItemsToStart(List<T> messages) {
        mData.addAll(0, messages);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (callback != null) {
            callback.onChange(isEmpty());
        }
    }

    public interface IEmptyCallback {
        void onChange(boolean isEmpty);
    }
}
