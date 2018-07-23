package eu.aboutall.room.features.add;

public interface AddItemContract {

    interface View  {

        void showItemIsEmpty();

        void showItemsList();

        void setName(String name);

        void showDbError();
    }

    interface Presenter  {

        void saveItem(String name);

        void subscribe();

        void unsubscribe();
    }
}
