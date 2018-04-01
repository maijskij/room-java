package eu.aboutall.room.features.itemslist;

import eu.aboutall.room.data.Item;

/**
 * Created by denis on 25/08/2017.
 */

public interface ItemListEventsCallbacks {

    void onDeleteItem(Item item, int position);
    void onEditItem(Item item, int position);
}
