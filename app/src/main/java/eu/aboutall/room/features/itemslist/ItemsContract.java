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

      /*  void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();
*/
        void showLoadingTasksError();

        void showNoTasks();

  /*      void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();*/
    }

    interface Presenter extends BasePresenter {
/*
        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetails(@NonNull Item requestedTask);

        void completeTask(@NonNull Item completedTask);

        void activateTask(@NonNull Item activeTask);

        void clearCompletedTasks();

*/
        void addNewItem();
        void deleteItem(Item item, int position);
        void updateItem(Item item, int position);

        void loadItems();
    }
}
