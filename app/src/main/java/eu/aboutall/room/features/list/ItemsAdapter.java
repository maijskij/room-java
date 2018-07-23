package eu.aboutall.room.features.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eu.aboutall.room.data.Item;

/**
 * Created by denis on 25/08/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface Events {

        void onItemLongClick(Item item);
        void onItemClick(Item item);
    }

    private static final int NOT_FOUND = -1;
    private final List<Item> mValues;
    private final Events mListener;

    ItemsAdapter(List<Item> items, Events listener) {
        if (listener == null)
            throw new IllegalArgumentException("ItemListEventsCallbacks listener should not be null!");

        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.two_line_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bindRecord( mValues.get(position) );
    }


    public void replaceData(List<Item> data){

        if (data == null) {
            return;
        }

        mValues.clear();
        mValues.addAll(data);
        notifyDataSetChanged();
    }

    public void updateItem(Item item) {

        int position = getItemPosition(item);
        if ( position < 0) return;

        //mValues.get( position ) = item;
        notifyItemChanged( position );
    }

    public void removeItem(Item item) {

        int position = getItemPosition(item);
        if ( position < 0) return;

        mValues.remove( position );
        notifyItemRemoved( position );

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    private int getItemPosition(Item item){

        for(int i = 0; i < mValues.size(); i++ ){

            if (mValues.get(i).equals(item)) return i;
        }

        return NOT_FOUND;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView  mName;

        private Item mItem;

        ViewHolder(View view) {
            super(view);
            mName = view.findViewById(android.R.id.text1);
        }

        void bindRecord(Item i) {

            mItem = i;
            mName.setText( mItem.getName() );

            // update item
            itemView.setOnClickListener(view -> {
                mListener.onItemClick(mItem);
            });

            // remove item
            itemView.setOnLongClickListener(view -> {
                mListener.onItemLongClick(mItem);
                return false;
            });

        }
    }
}
