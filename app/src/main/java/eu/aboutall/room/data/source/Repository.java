package eu.aboutall.room.data.source;

import java.util.List;

import eu.aboutall.room.data.Item;
import io.reactivex.Flowable;


public interface Repository {

    Flowable<List<Item>> getAll();

    long insert(Item item);

    void updateItem(Item item);

    void delete(String itemId);
}
