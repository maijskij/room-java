package eu.aboutall.room.data.source.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.source.Repository;
import io.reactivex.Flowable;

/**
 * Created by denis on 25/08/2017.
 */

@Dao
public interface DaoApi extends Repository {

    @Query("SELECT * FROM items")
    Flowable<List<Item>> getAll();

    @Insert
    long insert(Item item);

    @Update
    void updateItem(Item item);

    @Query("DELETE FROM items WHERE uuid IN (:itemId)")
    void delete(String itemId);

}
