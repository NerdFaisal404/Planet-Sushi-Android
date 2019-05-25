
package fr.sushi.app.ui.menu;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


import fr.sushi.app.util.swipanim.ItemTouchHelperExtension;

import static fr.sushi.app.util.swipanim.ItemTouchHelperExtension.Callback.makeMovementFlags;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof MenuItemSwipeAdapter.ItemNoSwipeViewHolder) {
            return 0;
        }
        return makeMovementFlags(ItemTouchHelper.UP| ItemTouchHelper.DOWN, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        RecyclerView.Adapter rvAdapter = recyclerView.getAdapter();
        if(rvAdapter instanceof MenuItemSwipeAdapter) {
            MenuItemSwipeAdapter adapter = (MenuItemSwipeAdapter) recyclerView.getAdapter();
            adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY != 0 && dX == 0) super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        MenuItemSwipeAdapter.BaseHolder holder = (MenuItemSwipeAdapter.BaseHolder) viewHolder;
        if (viewHolder instanceof MenuItemSwipeAdapter.ItemSwipeViewHolder) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX = -holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof MenuItemSwipeAdapter.BaseHolder) {
            holder.mViewContent.setTranslationX(dX);
        }
    }
}
