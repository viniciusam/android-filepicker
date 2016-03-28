package com.viniciusam.filepicker;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Vinicius Avellar on 13/01/2016.
 */
public abstract class ListRecyclerAdapter<E, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected List<E> mItems;

    public ListRecyclerAdapter() {
    }

    abstract public void onBindViewHolder(VH viewHolder, E object);

    @Override
    public void onBindViewHolder(VH viewHolder, int pos) {
        onBindViewHolder(viewHolder, getItem(pos));
    }

    @Override
    public long getItemId(int i) {
        throw new RuntimeException("Not Implemented! Use getItem instead.");
    }

    /**
     * Retrieve the object in position.
     * @param pos the position of the object in the list.
     * @return the object.
     */
    public E getItem(int pos) {
        if (mItems == null) {
            return null;
        }

        return mItems.get(pos);
    }

    @Override
    public int getItemCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

    /**
     * Update the entire list with new objects.
     * @param objList the new object list.
     */
    public void updateList(List<E> objList) {
        mItems = objList;
        notifyDataSetChanged();
    }

}
