package eu.aboutall.room.features.itemslist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.aboutall.room.data.Item;

import java.util.List;

/**
 * Created by denis on 25/08/2017.
 */

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder> {


    private final List<Item> mValues;
    private final ItemListEventsCallbacks mListener;

    ItemsListAdapter(List<Item> items, ItemListEventsCallbacks listener) {
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


    public void setData(List<Item> data){

        if (data == null) {
            return;
        }
        // TODO: remake with a range
        clearData();
        mValues.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData(){
        notifyItemRangeRemoved(0, mValues.size());
        mValues.clear();

    }

    public void removeAtPosition(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Item newItem) {

        mValues.add(0,newItem);
        notifyItemInserted(0);
    }


    public void dataChanged(int num, Item item) {
        notifyItemChanged(num, item);
    }

    public Item getItem(int position) {
        return  mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItem.setRandomName(); // simulate  item update
                    mListener.onEditItem(mItem, getAdapterPosition());
                }
            });

            // remove item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onDeleteItem(mItem, getAdapterPosition());
                    return false;
                }
            });

        }
    }
}
