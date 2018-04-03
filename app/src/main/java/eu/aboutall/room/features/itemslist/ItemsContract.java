package eu.aboutall.room.features.itemslist;

import android.support.annotation.NonNull;

import java.util.List;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.shared.BasePresenter;
import eu.aboutall.room.shared.BaseView;

public interface ItemsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showItems(List<Item> items);

        void addNewItem(Item item);

        void updateItem(Item item, int position);

        void deleteItem(int position);

        void showLoadingTasksError();

        void showNoTasks();
    }

    interface Presenter extends BasePresenter {

        void addNewItem();

        void deleteItem(Item item, int position);

        void updateItem(Item item, int position);

        void loadItems();
    }
}
