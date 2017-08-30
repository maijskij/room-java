package eu.aboutall.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import eu.aboutall.room.model.Item;

/**
 * Created by denis on 25/08/2017.
 */

@Database(entities = {Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemsDao itemsDao();
}
