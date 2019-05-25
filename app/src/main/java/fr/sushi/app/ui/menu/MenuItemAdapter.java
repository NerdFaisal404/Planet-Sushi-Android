package fr.sushi.app.ui.menu;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.databinding.AdapterItemsChildBinding;
import fr.sushi.app.util.PicassoUtil;
import fr.sushi.app.util.Utils;

public class MenuItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface Listener {
        void onItemClick(ProductsItem item, ImageView imageView);
    }

    private List<ProductsItem> productsItems;
    private Context mContext;
    private Listener itemClickListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private BottomSheetDialog dialog;
    double totalPrice;
    int count = 1;

    public MenuItemAdapter(Context context, List<ProductsItem> items, Listener listener) {
        this.mContext = context;
        productsItems = items;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.adapter_items_child, viewGroup, false);

        return new ItemViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ProductsItem item = productsItems.get(position);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.bind(item);
        viewBinderHelper.bind(holder.binding.rowItemList, item.getIdCategory());
        holder.binding.tvMuteChatThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBinderHelper.closeLayout(item.getIdCategory());
                Toast.makeText(mContext,"Click",Toast.LENGTH_SHORT).show();
            }
        });
        holder.binding.imageViewPlus.setOnClickListener(v -> {
            item.setSelected(true);
            notifyDataSetChanged();
            itemClickListener.onItemClick(item, holder.binding.imageViewItemAnim);
        });

        holder.binding.mainLayout.setOnClickListener(v -> {
            showBottomSheet(item);
        });

    }

    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterItemsChildBinding binding;

        public ItemViewHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());
            binding = (AdapterItemsChildBinding) itemView;
        }

        public void bind(ProductsItem item) {
            Glide.with(mContext).load(item.getCoverUrl()).into(binding.imageViewItem);
            Glide.with(mContext).load(item.getCoverUrl()).into(binding.imageViewItemAnim);
            // binding.itemName.setText(item.getName());

            String[] title = item.getName().split("\\s");

            if (title.length > 0) {
                String value = null;
                for (int i = 0; i < title.length; i++) {

                    if (i == 0) {
                        value = "<font color=#394F61>" + title[0] + " " + "</font>";
                        // binding.itemName.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.color_darker_gray)));
                    } else {
                        binding.itemName.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.colorPink)));
                        value += "<font font color=#EA148A>" + title[i] + " " + "</font>";
                    }


                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.itemName.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    binding.itemName.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);
                }

            } else {
                binding.itemName.setText(item.getName());
            }

            binding.itemPrice.setText(Utils.getDecimalFormat(Double.parseDouble(item.getPriceTtc())) + "€");
            binding.itemCount.setText(item.getNbrePiece() + " Pieces ");
            if (item.isSelected()) {
                binding.selectView.setVisibility(View.VISIBLE);
            } else {
                binding.selectView.setVisibility(View.GONE);
            }
        }
    }

    private void showBottomSheet(ProductsItem item) {
        count = DBManager.on().getProductCountById(item.getIdProduct());
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

        totalPrice = Double.parseDouble(item.getPriceTtc());
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
                totalPrice -= Double.parseDouble(item.getPriceTtc());
                tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
                tvCount.setText(count + "");
            }
        });

        ivPlus.setOnClickListener(v -> {
            count += 1;
            totalPrice += Double.parseDouble(item.getPriceTtc());
            tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
            tvCount.setText(count + "");
        });

        tvTagList.setText(Html.fromHtml(item.getDescriptionShort()));


        ivDownArrow.setOnClickListener(v -> dialog.dismiss());
        adjustLayout.setOnClickListener(v -> dialog.dismiss());

        Picasso.get().load(item.getPictureUrl()).into(ivItem);




    }


}
