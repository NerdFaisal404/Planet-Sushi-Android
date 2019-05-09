package fr.sushi.app.ui.cart.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.util.menu_recyclerview_utils.FeatureRecyclerViewAdapter;


public class RestaurantMenuAdapter extends FeatureRecyclerViewAdapter<RecyclerView.ViewHolder> {

    public interface Listener{
        void onItemClicked(int index, CategoriesItem categoriesItem);
    }

    private final int REAL_VIEW = 0;
    private final int DUMMY_VIEW = 1;

    private List<CategoriesItem> categoriesItems;
    private Listener itemClickListener;
    public RestaurantMenuAdapter(Listener listener) {
        categoriesItems = new ArrayList<>();
        this.itemClickListener = listener;
    }

    public void swapData(List<CategoriesItem> categoriesItems) {
        this.categoriesItems.addAll(categoriesItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < categoriesItems.size()) {
            return REAL_VIEW;
        } else {
            return DUMMY_VIEW;
        }
    }

    private CategoriesItem getItem(int position){
        return categoriesItems.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFeaturedViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == REAL_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_featured_item, parent, false);
            return new FeaturedViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dummy_item, parent, false);
            return new DummyViewHolder(view);
        }
    }

    @Override
    public void onBindFeaturedViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == DUMMY_VIEW) return;

        CategoriesItem item = categoriesItems.get(position);
        if (item == null) return;
        FeaturedViewHolder featuredViewHolder = (FeaturedViewHolder) holder;
        featuredViewHolder.tvHeading.setText(item.getName());
        Glide.with(holder.itemView.getContext()).load(item.getProducts().get(0).getCoverUrl())
                .into(featuredViewHolder.ivBackground);
    }

    @Override
    public int getFeaturedItemsCount() {
        return categoriesItems.size() + 1; /* Return 2 extra dummy items */
    }

    private class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivBackground;
        TextView tvHeading;

        FeaturedViewHolder(View itemView) {
            super(itemView);

            ivBackground = (ImageView) itemView.findViewById(R.id.iv_background);
            tvHeading = (TextView) itemView.findViewById(R.id.tv_heading);
            ivBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(getAdapterPosition(),getItem(getAdapterPosition()));
        }
    }

    private class DummyViewHolder extends RecyclerView.ViewHolder {

        public DummyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onSmallItemResize(RecyclerView.ViewHolder holder, int position, float offset) {
        if (holder instanceof FeaturedViewHolder) {
            FeaturedViewHolder featuredViewHolder = (FeaturedViewHolder) holder;
            //featuredViewHolder.tvHeading.setAlpha(offset / 100f);
        }
    }

    @Override
    public void onBigItemResize(RecyclerView.ViewHolder holder, int position, float offset) {
        if (holder instanceof FeaturedViewHolder) {
            FeaturedViewHolder featuredViewHolder = (FeaturedViewHolder) holder;
            //featuredViewHolder.tvHeading.setAlpha(offset / 100f);
        }
    }

}
