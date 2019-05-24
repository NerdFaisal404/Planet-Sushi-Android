package fr.sushi.app.ui.base;

import android.view.View;

/*
* ****************************************************************************
* * Copyright © 2018 W3 Engineers Ltd., All rights reserved.
* *
* * Created by:
* * Name : Azizul Islam
* * Date : 10/13/17
* * Email : aziz@w3engineers.com
* *
* * Purpose: In Recycler view, to get click events, you have to implement in this interface
* ****************************************************************************
*/

/**
 * Works with {@link BaseAdapter} normally
 */
public interface ItemClickListener<T> {
    /**
     * Called when a item has been clicked.
     *
     * @param view The view that was clicked.
     * @param item The T type object that was clicked.
     */
    void onItemClick(View view, T item);
    default void onItemClick(View view, T item, int position){

    }
}
