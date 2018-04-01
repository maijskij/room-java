package eu.aboutall.room.features.itemslist;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import eu.aboutall.room.data.room.DataSource;
import eu.aboutall.room.data.Item;

/**
 * Created by denis on 29/08/2017.
 */

public class ItemsLoader extends AsyncTaskLoader<List<Item>> {

    private List<Item> mData;

    ItemsLoader(Context context) {
        super(context);
    }

    @Override
    public List<Item> loadInBackground() {
        return DataSource.getInstance(getContext()).db.itemsDao().getAll();
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Item> data) {
        mData = data;
        if (isStarted()){
            super.deliverResult(data);
        }
    }
}
