package fr.sushi.app.ui.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
 * ****************************************************************************
 * * Copyright © 2018 W3 Engineers Ltd., All rights reserved.
 * *
 * * Created by:
 * * Name : Azizul Islam
 * * Date : 10/13/17
 * * Email : azizul@w3engineers.com
 * *
 * * Purpose: Child view holder class inherit this class

 * ****************************************************************************
 */

/**
 * Abstract class. All common BaseViewHolder related task happens here. Works being coupled with {@link BaseAdapter}
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected ViewDataBinding mViewDataBinding;

    public BaseViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());

        mViewDataBinding = viewDataBinding;
    }

    public ViewDataBinding getViewDataBinding() {
        return mViewDataBinding;
    }

    /**
     * Child class have to implement this method.
     **/
    public abstract void bind(T item);

    /**
     * To set click listener on any view, You can pass multiple view at a time
     *
     * @param views View as params
     */
    protected void setClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }
}