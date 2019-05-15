package fr.sushi.app.ui.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.library.FocusResizeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.CategoriesItem;

public class FoodMenuAdapterFocus extends FocusResizeAdapter<RecyclerView.ViewHolder> {

    public static final int OFFSET_TEXT_SIZE = 4;
    public static final float OFFSET_TEXT_ALPHA = 100f;
    public static final float ALPHA_SUBTITLE = 0.81f;
    public static final float ALPHA_SUBTITLE_HIDE = 0f;

    private List<CategoriesItem> items;
    private Context context;
    private Listener itemClickListener;

    public interface Listener{
        void onItemClicked(int index, CategoriesItem categoriesItem);
    }

    public FoodMenuAdapterFocus(Context context, Listener listener, int height) {
        super(context, height);
        this.context = context;
        items = new ArrayList<>();
        this.itemClickListener = listener;
    }

    public void swapData(List<CategoriesItem> categoriesItems) {
        this.items.addAll(categoriesItems);
        notifyDataSetChanged();
    }
    @Override
    public int getFooterItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_custom, parent, false);
        return new CustomViewHolder(v);
    }

    private CategoriesItem getItem(int position){
        return items.get(position);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoriesItem customObject = items.get(position);
        fill((CustomViewHolder) holder, customObject);
    }

    private void fill(CustomViewHolder holder, CategoriesItem item) {
        holder.titleTextView.setText(item.getName());
        //holder.subtitleTextView.setText(customObject.getSubTitle());
        if(item.getPictureUrl() == null) return;
        /*Picasso.get().load(item.getPictureUrl())
                .placeholder(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_place_holder))
                .into(holder.image);;*/
        Glide.with(context).load(item.getPictureUrl()).into(holder.image);
    }

    @Override
    public void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (CustomViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse_expand));
        if (((CustomViewHolder) viewHolder).titleTextView.getTextSize() + (dyAbs / OFFSET_TEXT_SIZE) >= context.getResources().getDimension(R.dimen.featuredItemHeight)) {
            ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xxxlarge));
        } else {
            ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((CustomViewHolder) viewHolder).titleTextView.getTextSize() + (dyAbs / OFFSET_TEXT_SIZE));
        }

        float alpha = dyAbs / OFFSET_TEXT_ALPHA;
        if (((CustomViewHolder) viewHolder).subtitleTextView.getAlpha() + alpha >= ALPHA_SUBTITLE) {
            ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(ALPHA_SUBTITLE);
        } else {
            ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(((CustomViewHolder) viewHolder).subtitleTextView.getAlpha() + alpha);
        }
    }

    @Override
    public void onItemBigResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (CustomViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse_expand));
        ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xxxlarge));
        ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(ALPHA_SUBTITLE);
    }

    @Override
    public void onItemSmallResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (CustomViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse));
        ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_medium));
        ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(ALPHA_SUBTITLE_HIDE);
    }

    @Override
    public void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ( (CustomViewHolder) viewHolder).viewOpacity.setBackground(context.getResources().getDrawable(R.drawable.shadow_inverse));
        if (((CustomViewHolder) viewHolder).titleTextView.getTextSize() - (dyAbs / OFFSET_TEXT_SIZE) <= context.getResources().getDimension(R.dimen.font_medium)) {
            ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_medium));
        } else {
            ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((CustomViewHolder) viewHolder).titleTextView.getTextSize() - (dyAbs / OFFSET_TEXT_SIZE));
        }

        float alpha = dyAbs / OFFSET_TEXT_ALPHA;
        if (((CustomViewHolder) viewHolder).subtitleTextView.getAlpha() - alpha < ALPHA_SUBTITLE_HIDE) {
            ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(ALPHA_SUBTITLE_HIDE);
        } else {
            ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(((CustomViewHolder) viewHolder).subtitleTextView.getAlpha() - alpha);
        }
    }

    @Override
    public void onItemInit(RecyclerView.ViewHolder viewHolder) {
        ((CustomViewHolder) viewHolder).titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimensionPixelSize(R.dimen.sp20));
        ((CustomViewHolder) viewHolder).subtitleTextView.setAlpha(ALPHA_SUBTITLE);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView titleTextView;
        TextView subtitleTextView;
        View viewOpacity;

        public CustomViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image_custom_item);
            titleTextView = (TextView) v.findViewById(R.id.title_custom_item);
            subtitleTextView = (TextView) v.findViewById(R.id.subtitle_custom_item);
            viewOpacity = (View) v.findViewById(R.id.viewOpacity);
            image.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(getAdapterPosition(),getItem(getAdapterPosition()));
        }
    }
}