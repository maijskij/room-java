package eu.aboutall.room.features.list;

import java.util.List;

import eu.aboutall.room.data.Item;


public interface ItemsContract {

    interface View {

        void setLoadingIndicator(boolean active);

        void replaceListItems(List<Item> items);

        void showAddItemView();

        void showMessage(String message);

        void updateItemFromList(Item item);

        void deleteItemFromList(Item item);

        void showLoadingTasksError();

        void showNoTasks();

        void showSuccessfullySavedMessage();

        void showDbError();
    }

    interface UserActionsListener  {

        void addNewItem();

        void refreshItems();

        void deleteItem(Item item);

        void editItem(Item item);

        void result(int requestCode, int resultCode);

        void subscribe();

        void unsubscribe();
    }
}
