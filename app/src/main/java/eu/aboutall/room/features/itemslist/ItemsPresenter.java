package eu.aboutall.room.features.itemslist;

import android.support.annotation.NonNull;

import java.util.List;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.ItemsDataSource;
import eu.aboutall.room.data.room.DataSource;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;


public class ItemsPresenter implements ItemsContract.Presenter {


    @NonNull
    private final ItemsDataSource mTasksRepository;

    @NonNull
    private final ItemsContract.View mTasksView;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    ItemsPresenter(@NonNull ItemsDataSource tasksRepository,
                          @NonNull ItemsContract.View tasksView) {

        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mCompositeDisposable = new CompositeDisposable();
        mTasksView.setPresenter(this);
    }


    @Override
    public void addNewItem() {

        Item item = new Item();
        mTasksRepository.insert(item);
        mTasksView.addNewItem(item);
    }

    @Override
    public void deleteItem(Item item, int position) {
        mTasksRepository.delete(item.getUuid());
        mTasksView.deleteItem(position);
    }

    @Override
    public void updateItem(Item item, int position) {
        mTasksRepository.updateItem(item);
        mTasksView.updateItem(item, position);
    }

    @Override
    public void loadItems() {

        mTasksView.setLoadingIndicator(true);

        mCompositeDisposable.clear();
        Disposable disposable = mTasksRepository
                .getAll()
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        items -> {
                            processItems(items);
                            mTasksView.setLoadingIndicator(false);
                        },
                        // onError
                        throwable -> mTasksView.showLoadingTasksError());

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {
        loadItems();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    private void processItems(@NonNull List<Item> items) {
        if (items.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            mTasksView.showNoTasks();
        } else {
            // Show the list of tasks
            mTasksView.showItems(items);
        }
    }
}
