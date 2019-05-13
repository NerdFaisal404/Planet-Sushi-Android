
package fr.sushi.app.ui.checkout.commade;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import fr.sushi.app.ui.menu.MenuItemSwipeAdapter;
import fr.sushi.app.util.swipanim.ItemTouchHelperExtension;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        return makeMovementFlags(ItemTouchHelper.UP| ItemTouchHelper.DOWN, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        CommadeAdapter adapter = (CommadeAdapter) recyclerView.getAdapter();
        adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
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
        CommadeAdapter.BaseHolder holder = (CommadeAdapter.BaseHolder) viewHolder;
        if (viewHolder instanceof CommadeAdapter.ItemSwipeViewHolder) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX = -holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof CommadeAdapter.BaseHolder) {
            holder.mViewContent.setTranslationX(dX);
        }
    }
}
