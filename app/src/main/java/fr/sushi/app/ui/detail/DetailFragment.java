package fr.sushi.app.ui.detail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.TopMenuItem;
import fr.sushi.app.databinding.ActivityMenuListDetailBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.detail.adapter.ItemsAdapter;
import fr.sushi.app.ui.menu.TopMenuAdapter;
import fr.sushi.app.util.RecyclerViewPositionHelper;

public class DetailFragment extends BaseFragment implements TopMenuAdapter.MenuItemClickListener {

    List<String> dummyList = new ArrayList<>();
    private ActivityMenuListDetailBinding binding;
    private int firstVisibleItem;
    private TopMenuAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu_list_detail;

    }

    @Override
    protected void startUI() {
        binding = (ActivityMenuListDetailBinding) getViewDataBinding();
        dummyList.add("Frenc Fries");
        dummyList.add("Tosada");
        dummyList.add("Tosada Vegetrain");
        dummyList.add("Bean Dip");
        dummyList.add("Chalupa");
        dummyList.add("Chiken Wings");
        dummyList.add("CHeese  Dip");
        dummyList.add("Sprite");
        dummyList.add("Fanta");
        dummyList.add("Mineral Still");
        setMenuAdapter();
        setItemsAdapter();
    }

    @Override
    protected void stopUI() {

    }


    void setMenuAdapter() {
        if (adapter == null) {
            //adapter = new TopMenuAdapter((AppCompatActivity) getActivity(), dummyList);
            adapter.itemClickHandler(this);
            binding.recyclerViewMenuTop.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerViewMenuTop.setAdapter(adapter);

        } else {
            adapter.setSelectedItemPosition(firstVisibleItem);
        }
    }


    void setItemsAdapter() {
        ItemsAdapter itemsAdapter = new ItemsAdapter((AppCompatActivity) getActivity(), dummyList);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewItems.setAdapter(itemsAdapter);

        binding.recyclerViewItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private RecyclerViewPositionHelper mRecyclerViewHelper;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    setMenuAdapter();
                    binding.recyclerViewMenuTop.scrollToPosition(firstVisibleItem);
                    /*  var manager = (recyclerViewType.getLayoutManager()!!) as LinearLayoutManager
                      manager.scrollToPositionWithOffset(firstVisibleItem, 0);*/
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
            }

        });
    }

    @Override
    public void onMenuItemClick(TopMenuItem topMenuItem) {
        /*LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }

        };
        smoothScroller.setTargetPosition(position);
        LinearLayoutManager manager = (LinearLayoutManager) binding.recyclerViewItems.getLayoutManager();
        manager.startSmoothScroll(smoothScroller);*/

    }
}
