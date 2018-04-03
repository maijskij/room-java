package eu.aboutall.room.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.ItemsDataSource;
import io.reactivex.Flowable;

import java.util.List;

/**
 * Created by denis on 25/08/2017.
 */

@Dao
public interface DaoApi extends ItemsDataSource {

    @Query("SELECT * FROM items")
    Flowable<List<Item>> getAll();

    @Query("SELECT * FROM items WHERE uuid IN (:itemsIds)")
    Flowable<List<Item>> loadAllByIds(String[] itemsIds);

    @Query("SELECT * FROM items WHERE uuid IN (:itemId)")
    Flowable<Item> findById(String itemId);

    @Insert
    void insertAll(Item... items);

    @Insert
    long insert(Item item);

    @Update
    public void updateItems(Item... items);

    @Update
    public void updateItem(Item item);

    @Query("DELETE FROM items WHERE uuid IN (:itemId)")
    void delete(String itemId);

}
