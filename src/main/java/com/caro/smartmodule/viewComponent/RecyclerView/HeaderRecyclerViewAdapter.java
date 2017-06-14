package com.caro.smartmodule.viewComponent.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by caro on 16/8/16.
 */
public abstract class HeaderRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, H, T, F>
        extends RecyclerView.Adapter<VH> {

    protected static final int TYPE_HEADER = -2;
    protected static final int TYPE_ITEM = -1;
    protected static final int TYPE_FOOTER = -3;
    protected static final int NOITEM_TYPE_ITEM = -4;

    private H header;
    private List<T> items = Collections.EMPTY_LIST;
    private F footer;
    private Object noItem;

    private boolean showFooter = true;

    /**
     * Invokes onCreateHeaderViewHolder, onCreateItemViewHolder or onCreateFooterViewHolder methods
     * based on the view type param.
     */
    @Override public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder;
        if (isHeaderType(viewType)) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else if (isFooterType(viewType)) {
            viewHolder = onCreateFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    /**
     * If you don't need header feature, you can bypass overriding this method.
     */
    protected VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * If you don't need footer feature, you can bypass overriding this method.
     */
    protected VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * Invokes onBindHeaderViewHolder, onBindItemViewHolder or onBindFooterViewHOlder methods based
     * on the position param.
     */
    @Override public final void onBindViewHolder(VH holder, int position) {
        if (isHeaderPosition(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (isFooterPosition(position)) {
            onBindFooterViewHolder(holder, position);
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    /**
     * If you don't need header feature, you can bypass overriding this method.
     */
    protected void onBindHeaderViewHolder(VH holder, int position) {
    }

    protected abstract void onBindItemViewHolder(VH holder, int position);

    //public abstract void onViewDetachedFromWindow(VH holder);

    /**
     * If you don't need footer feature, you can bypass overriding this method.
     */
    protected void onBindFooterViewHolder(VH holder, int position) {
    }

    /**
     * Invokes onHeaderViewRecycled, onItemViewRecycled or onFooterViewRecycled methods based
     * on the holder.getAdapterPosition()
     */
    @Override public final void onViewRecycled(VH holder) {
        int position = holder.getAdapterPosition();

        if (isHeaderPosition(position)) {
            onHeaderViewRecycled(holder);
        } else if (isFooterPosition(position)) {
            onFooterViewRecycled(holder);
        } else {
            onItemViewRecycled(holder);
        }
    }

    protected void onHeaderViewRecycled(VH holder) {
    }

    protected void onItemViewRecycled(VH holder) {
    }

    protected void onFooterViewRecycled(VH holder) {
    }

    /**
     * Returns the type associated to an item given a position passed as arguments. If the position
     * is related to a header item returns the constant TYPE_HEADER or TYPE_FOOTER if the position is
     * related to the footer, if not, returns TYPE_ITEM.
     *
     * If your application has to support different types override this method and provide your
     * implementation. Remember that TYPE_HEADER, TYPE_ITEM and TYPE_FOOTER are internal constants
     * can be used to identify an item given a position, try to use different values in your
     * application.
     */
    @Override public int getItemViewType(int position) {
        int viewType = TYPE_ITEM;
        if (isHeaderPosition(position)) {
            viewType = TYPE_HEADER;
        } else if (isFooterPosition(position)) {
            viewType = TYPE_FOOTER;
        }
        return viewType;
    }

    /**
     * Returns the items list size if there is no a header configured or the size taking into account
     * that if a header or a footer is configured the number of items returned is going to include
     * this elements.
     */
    @Override public int getItemCount() {
        int size = items.size();
        if (hasHeader()) {
            size++;
        }
        if (hasFooter()) {
            size++;
        }
        return size;
    }

    /**
     * Get header data in this adapter, you should previously use {@link #setHeader(H header)}
     * in the adapter initialization code to set header data.
     *
     * @return header data
     */
    public H getHeader() {
        return header;
    }

    /**
     * Get item data in this adapter with the specified postion,
     * you should previously use {@link #setHeader(H header)}
     * in the adapter initialization code to set header data.
     *
     * @return item data in the specified postion
     */
    public T getItem(int position) {
        if (hasHeader() && hasItems()) {
            --position;
        }
        return items.get(position);
    }

    /**
     * Get footer data in this adapter, you should previously use {@link #setFooter(F footer)}
     * in the adapter initialization code to set footer data.
     *
     * @return footer data
     */
    public F getFooter() {
        return footer;
    }

    /**
     * If you need a header, you should set header data in the adapter initialization code.
     *
     * @param header header data
     */
    public void setHeader(H header) {
        this.header = header;
    }

    /**
     * You should set header data in the adapter initialization code.
     *
     * @param items item data list
     */
    public void setItems(List<T> items) {
        validateItems(items);
        this.items = items;
    }

    /**
     * you can get the item data list
     * @return
     */
    public List<T> getItems(){
        validateItems(this.items);
        return this.items;
    }

    /**
     * If you need a footer, you should set footer data in the adapter initialization code.
     */
    public void setFooter(F footer) {
        this.footer = footer;
    }

    /**
     * Call this method to show hiding footer.
     */
    public void showFooter() {
        this.showFooter = true;
        notifyDataSetChanged();
    }

    /**
     * Call this method to hide footer.
     */
    public void hideFooter() {
        this.showFooter = false;
        notifyDataSetChanged();
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to 0 and the adapter
     * has a not null header already configured.
     */
    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to
     * <code>getItemCount() - 1</code>
     * and the adapter has a not null header already configured.
     */
    public boolean isFooterPosition(int position) {
        int lastPosition = getItemCount() - 1;
        return hasFooter() && position == lastPosition;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_HEADER.
     */
    protected boolean isHeaderType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_FOOTER.
     */
    protected boolean isFooterType(int viewType) {
        return viewType == TYPE_FOOTER;
    }

    /**
     * Returns true if the header configured is not null.
     */
    protected boolean hasHeader() {
        return getHeader() != null;
    }

    /**
     * Returns true if the footer configured is not null.
     */
    protected boolean hasFooter() {
        return getFooter() != null && showFooter;
    }

    /**
     * Returns true if the item configured is not empty.
     */
    private boolean hasItems() {
        return items.size() > 0;
    }

    private void validateItems(List<T> items) {
        if (items == null) {
            throw new IllegalArgumentException("You can't use a null List<Item> instance.");
        }
    }

    public boolean getshowFooterState(){
        return showFooter;
    }

    public OnRecyclerItemClickListener getOnItemClickListener() {return onItemClickListener;}
    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {this.onItemClickListener = onItemClickListener;}
    public OnRecyclerItemClickListener onItemClickListener;
    public interface OnRecyclerItemClickListener{
        void onItemClick(int position);
    }
    public LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }


}
