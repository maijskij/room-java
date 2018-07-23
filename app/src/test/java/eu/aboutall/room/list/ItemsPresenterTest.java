package eu.aboutall.room.list;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.source.Repository;
import eu.aboutall.room.features.list.ItemsContract;
import eu.aboutall.room.features.list.ItemsPresenter;
import eu.aboutall.room.utils.schedulers.BaseSchedulerProvider;
import eu.aboutall.room.utils.schedulers.ImmediateSchedulerProvider;
import io.reactivex.Flowable;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemsPresenterTest {


    private static List<Item> LIST;

    @Mock
    private Repository mRepository;

    @Mock
    private ItemsContract.View mView;

    private BaseSchedulerProvider mSchedulerProvider;

    private ItemsPresenter mPresenter;


    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        mPresenter = new ItemsPresenter(mRepository, mView, mSchedulerProvider);


        // We subscribe the items to 3, with one active and two completed
        LIST = Lists.newArrayList(new Item("id1", "item1"),
                new Item("id2", "Item2"), new Item("id3", "Item3"));
    }

    @Test
    public void loadAllItemsFromRepositoryAndLoadIntoView() {
        // Given an initialized TasksPresenter with initialized items
        when(mRepository.getAll()).thenReturn(Flowable.just(LIST));
        // When loading of Tasks is requested
        mPresenter.refreshItems();

        // Then progress indicator is shown
        verify(mView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all items are shown in UI
        verify(mView).setLoadingIndicator(false);
    }

    @Test
    public void clickOnFab_ShowsAddItemUi() {
        // When adding a new item
        mPresenter.addNewItem();

        // Then add item UI is shown
        verify(mView).showAddItemView();
    }

    @Test
    public void errorLoadingItems_ShowsError() {
        // Given that no items are available in the repository
        when(mRepository.getAll()).thenReturn(Flowable.error(new Exception()));

        mPresenter.refreshItems();

        // Then an error message is shown
        verify(mView).showLoadingTasksError();
    }


    @Test
    public void deleteItemFromAList() {

        Item itemToDelete = new Item();
        // When adding a new item
        mPresenter.deleteItem(itemToDelete);

        // Then add item UI is shown
        verify(mView).deleteItemFromList(itemToDelete);

        // Then add item UI is shown
        verify(mRepository).delete(itemToDelete.getUuid());
    }

    @Test
    public void updateItemFromAList() {

        Item itemToUpdate = new Item();
        // When adding a new item
        mPresenter.editItem(itemToUpdate);

        // Then add item UI is shown
        verify(mView).updateItemFromList(itemToUpdate);

        // Then add item UI is shown
        verify(mRepository).updateItem(itemToUpdate);
    }
}
