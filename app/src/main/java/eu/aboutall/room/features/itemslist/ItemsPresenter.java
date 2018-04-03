package eu.aboutall.room.features.itemslist;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.ItemsDataSource;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;


public class ItemsPresenter implements ItemsContract.Presenter {


    @NonNull
    private final ItemsDataSource mRepository;

    @NonNull
    private final ItemsContract.View mView;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    ItemsPresenter(@NonNull ItemsDataSource tasksRepository,
                          @NonNull ItemsContract.View tasksView) {

        mRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        mView = checkNotNull(tasksView, "tasksView cannot be null!");

        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }


    @Override
    public void addNewItem() {

        Item item = new Item();

        Observable<Long> insertItemDao = Observable.fromCallable(() -> mRepository.insert(item));

        mCompositeDisposable.clear();
        Disposable disposable = insertItemDao.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        num -> {
                            Log.d("Den", "num:" + num);
                            mView.addNewItem(item);
                        },
                        // onError
                        throwable -> mView.showLoadingTasksError()
                );
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void deleteItem(Item item, int position) {


        Single<Object> deleteItemDao = Single.create((SingleEmitter<Object> emitter) -> {
            mRepository.delete(item.getUuid());
            emitter.onSuccess(item.getUuid());
        });

        mCompositeDisposable.clear();
        Disposable disposable = deleteItemDao.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        num -> {
                            mView.deleteItem(position);
                        },
                        // onError
                        throwable -> mView.showLoadingTasksError()
                );
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void updateItem(Item item, int position) {

        Single<Object> updateItemDao = Single.create((SingleEmitter<Object> emitter) -> {
            mRepository.updateItem(item);
            emitter.onSuccess(item.getUuid());
        });


        mCompositeDisposable.clear();
        Disposable disposable = updateItemDao.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        num -> {
                            mView.updateItem(item, position);
                        },
                        // onError
                        throwable -> mView.showLoadingTasksError()
                );
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadItems() {

        mView.setLoadingIndicator(true);

        mCompositeDisposable.clear();
        Disposable disposable = mRepository
                .getAll()
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        items -> {
                            processItems(items);
                            mView.setLoadingIndicator(false);
                        },
                        // onError
                        throwable -> mView.showLoadingTasksError());

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
            mView.showNoTasks();
        } else {
            // Show the list of tasks
            mView.showItems(items);
        }
    }
}
