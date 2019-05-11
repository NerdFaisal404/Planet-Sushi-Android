package fr.sushi.app.ui.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.util.focuslib.FocusResizeAdapter;

public class FoodMenuAdapter extends FocusResizeAdapter<RecyclerView.ViewHolder> {
    public interface Listener{
        void onItemClicked(int index, CategoriesItem categoriesItem);
    }
    private List<CategoriesItem> categoriesItems;
    private Listener itemClickListener;
    private Context context;

    public FoodMenuAdapter(Context context, Listener listener,int height) {
        super(context, height);
        categoriesItems = new ArrayList<>();
        this.itemClickListener = listener;
        this.context = context;
    }

    public void swapData(List<CategoriesItem> categoriesItems) {
        this.categoriesItems.addAll(categoriesItems);
        notifyDataSetChanged();
    }

    @Override
    public int getFooterItemCount() {
        return categoriesItems.size();
    }

    private CategoriesItem getItem(int position){
        return categoriesItems.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_featured_item, parent, false);
            return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoriesItem item = categoriesItems.get(position);
        if (item == null) return;
        FeaturedViewHolder featuredViewHolder = (FeaturedViewHolder) holder;
        featuredViewHolder.tvHeading.setText(item.getName());

        /*Glide.with(holder.itemView.getContext()).load(item.getPictureUrl())
                .into(featuredViewHolder.ivBackground);*/

        Picasso.get().load(item.getPictureUrl())
                .placeholder(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_place_holder))
                .into(featuredViewHolder.ivBackground);
    }

    private class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivBackground;
        TextView tvHeading;
        View viewOpacity;

        FeaturedViewHolder(View itemView) {
            super(itemView);
            viewOpacity = (View) itemView.findViewById(R.id.viewOpacity);
            ivBackground = (ImageView) itemView.findViewById(R.id.iv_background);
            tvHeading = (TextView) itemView.findViewById(R.id.tv_heading);
            ivBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(getAdapterPosition(),getItem(getAdapterPosition()));
        }
    }

    @Override
    public void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (FeaturedViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse_expand));

    }

    @Override
    public void onItemBigResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (FeaturedViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse_expand));

    }

    @Override
    public void onItemSmallResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (FeaturedViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse));

    }

    @Override
    public void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (FeaturedViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse));
    }

    @Override
    public void onItemInit(RecyclerView.ViewHolder viewHolder) {

    }

}
