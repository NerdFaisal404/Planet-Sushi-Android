package fr.sushi.app.ui.checkout.commade;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.util.Utils;
import fr.sushi.app.util.swipanim.Extension;
import fr.sushi.app.util.swipanim.ItemTouchHelperExtension;

public class CommadeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface Listener {
        void onItemClick(ProductsItem item, ImageView imageView);
        void onItemDeselect(ProductsItem item);
    }

    private Context mContext;
    private final int ITEM_NO_SWIPE = 0;
    private final int ITEM_WITH_SWIPE = 1;
    private List<ProductsItem> productsItems;
    private ItemTouchHelperExtension mItemTouchHelperExtension;
   // private CommadeAdapter.Listener itemClickListener;

    private BottomSheetDialog dialog;
    double totalPrice;
    int count = 1;

    public CommadeAdapter(Context context, List<ProductsItem> itemList){
        this.mContext = context;
        this.productsItems = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        ProductsItem item = productsItems.get(position);

        if(item.isSelected()){
            return ITEM_WITH_SWIPE;
        }
        return ITEM_NO_SWIPE;
    }
    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    public void move(int from, int to) {
        ProductsItem prev = productsItems.remove(from);
        productsItems.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_commade, viewGroup, false);
        if(viewType == ITEM_WITH_SWIPE){
            return new CommadeAdapter.ItemSwipeViewHolder(view);
        }
        return new CommadeAdapter.ItemNoSwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int index) {
        ProductsItem item = productsItems.get(index);
        CommadeAdapter.BaseHolder holder = (CommadeAdapter.BaseHolder)viewHolder;

        if(holder instanceof CommadeAdapter.ItemSwipeViewHolder){
            CommadeAdapter.ItemSwipeViewHolder swipeViewHolder = (CommadeAdapter.ItemSwipeViewHolder)holder;
            swipeViewHolder.mActionViewRefresh.setOnClickListener(v -> {
                mItemTouchHelperExtension.closeOpened();
               // itemClickListener.onItemDeselect(item);
                item.setSelected(false);
                notifyItemChanged(index);
            });
        }

        holder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(item);
            }
        });
        holder.bind(item);
    }

    class BaseHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemPrice, itemCount, tvPice;
        View mViewContent, selectedView;
        View mActionContainer;
        ImageView imageViewItem;
        public BaseHolder(@NonNull View itemView) {
            super(itemView);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.tvPrice);
            imageViewItem =itemView.findViewById(R.id.imageViewItem);

            itemCount = itemView.findViewById(R.id.tvCount);
            tvPice = itemView.findViewById(R.id.tvPiece);
        }
        private void bind(ProductsItem item){
            Glide.with(mContext).load(item.getCoverUrl()).into(imageViewItem);

            String[] title = item.getName().split("\\s");

            if (title.length > 0) {
                String value = null;
                for (int i = 0; i < title.length; i++) {

                    if (i == 0) {
                        value = "<font color=#394F61>" + title[0] + " " + "</font>";
                        // binding.itemName.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.color_darker_gray)));
                    } else {
                        itemName.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.colorPink)));
                        value += "<font font color=#EA148A>" + title[i] + " " + "</font>";
                    }


                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemName.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    itemName.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);
                }

            } else {
                itemName.setText(item.getName());
            }

            itemPrice.setText(Utils.getDecimalFormat(Double.parseDouble(item.getPriceHt())) + "€");
            tvPice.setText(item.getNbrePiece() + " Pieces ");

        }
    }

    class ItemSwipeViewHolder extends CommadeAdapter.BaseHolder implements Extension {
        View mActionViewRefresh;
        public ItemSwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mActionViewRefresh = itemView.findViewById(R.id.view_list_repo);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemNoSwipeViewHolder extends CommadeAdapter.BaseHolder {

        public ItemNoSwipeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void showBottomSheet(ProductsItem item) {
        count = 1;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_item_details, null);

        dialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView tvTitle = (TextView) bottomSheet.findViewById(R.id.tvTitle);
        TextView tvCount = (TextView) bottomSheet.findViewById(R.id.tvCount);
        ImageView ivMinus = bottomSheet.findViewById(R.id.ivMinus);
        ImageView ivPlus = bottomSheet.findViewById(R.id.ivPlus);
        TextView tvPrice = bottomSheet.findViewById(R.id.tvPrice);
        ImageView ivDownArrow = bottomSheet.findViewById(R.id.ivDownArrow);
        ImageView ivItem = bottomSheet.findViewById(R.id.ivItem);
        TextView tvTagList = bottomSheet.findViewById(R.id.tvTagList);
        LinearLayout adjustLayout = bottomSheet.findViewById(R.id.layoutAdjust);

        String[] title = item.getName().split("\\s");

        totalPrice = Double.parseDouble(item.getPriceHt());
        tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");

        if (title.length > 0) {

            for (int i = 0; i < title.length; i++) {
                if (i == 0) {
                    tvTitle.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.color_darker_gray)));
                } else {
                    tvTitle.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.colorPink)));

                }
            }

        } else {
            tvTitle.setText(item.getName());
        }

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 1) {
                    tvCount.setText(count + "");
                    return;
                }
                count -= 1;
                totalPrice -= Double.parseDouble(item.getPriceHt());
                tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
                tvCount.setText(count + "");
            }
        });

        ivPlus.setOnClickListener(v -> {
            count += 1;
            totalPrice += Double.parseDouble(item.getPriceHt());
            tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
            tvCount.setText(count + "");
        });

        tvTagList.setText(Html.fromHtml(item.getDescriptionShort()));


        ivDownArrow.setOnClickListener(v -> dialog.dismiss());
        adjustLayout.setOnClickListener(v -> dialog.dismiss());

        Picasso.get().load(item.getPictureUrl()).into(ivItem);




    }

}