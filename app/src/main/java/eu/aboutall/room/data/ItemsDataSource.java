package eu.aboutall.room.data;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;


public interface ItemsDataSource {


    Flowable<List<Item>> getAll();

    Flowable<List<Item>> loadAllByIds(String[] itemsIds);

    Flowable<Item> findById(String itemId);

    void insertAll(Item... items);

    long insert(Item item);

    public void updateItems(Item... items);

    public void updateItem(Item item);

    void delete(String itemId);
}
