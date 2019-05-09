package fr.sushi.app.ui.menu;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.TopMenuItem;
import fr.sushi.app.databinding.AdapterTopMenuBinding;

public class TopMenuAdapter extends RecyclerView.Adapter<TopMenuAdapter.ViewHolder> {
    int selectedPosition = 0;
    MenuItemClickListener listener;
    List<TopMenuItem> topMenuItems;

    public TopMenuAdapter( MenuItemClickListener itemClickListener, List<TopMenuItem> dummyList) {
        this.topMenuItems = dummyList;
        this.listener = itemClickListener;
    }
    private TopMenuItem getItem(int index){
        return topMenuItems.get(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.adapter_top_menu, viewGroup, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.bind(topMenuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return topMenuItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       private AdapterTopMenuBinding binding;

        public ViewHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());
            binding = (AdapterTopMenuBinding)itemView;
            binding.textViewItemName.setOnClickListener(this);
        }
        private void bind(TopMenuItem item){
            if(selectedPosition == getAdapterPosition()){
                binding.bottomView.setVisibility(View.VISIBLE);
            }else {
                binding.bottomView.setVisibility(View.GONE);
            }
            binding.textViewItemName.setText(item.getName());
        }

        @Override
        public void onClick(View v) {
            listener.onMenuItemClick(getItem(getAdapterPosition()));
            setSelectedItemPosition(getAdapterPosition());
        }
    }


    public void setSelectedItemPosition(int firstVisibleItem) {
        selectedPosition = firstVisibleItem;
        notifyDataSetChanged();
    }


    public void itemClickHandler(MenuItemClickListener listener) {
        this.listener = listener;
    }


    public interface MenuItemClickListener {
        void onMenuItemClick(TopMenuItem menuItem);
    }
}
