package fr.sushi.app.ui.adressPicker.bottom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.address_picker.Order;

public class WheelTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    public interface Listener{
        void onItemClick(int position, Order item);
    }
    private List<Order> itemList = new ArrayList<>();
    private Context context;
    private int selectedPosition = 0;

    private Listener itemClickListener;

    public WheelTimeAdapter(Context context, List<Order> itemList){
        this.itemList.addAll(itemList);
        this.context =context;
    }

    public void setNewDataList(List<Order> itemList){
        if(itemList == null) return;
        this.itemList.clear();
        this.itemList.addAll(itemList);
        selectedPosition = 0;
        notifyDataSetChanged();
        if(itemClickListener != null){
            itemClickListener.onItemClick(0,this.itemList.get(0));
        }
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public Order getSelectedOrder(int position) {
        return itemList.get(position);
    }

    public void setListener(Listener listener){
        itemClickListener = listener;
    }


    private Order getItem(int index){
        return itemList.get(index);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_slider, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int index) {
        ItemHolder holder = (ItemHolder)viewHolder;
        holder.time.setText(itemList.get(index).getSchedule());
        if(selectedPosition == index){
            holder.time.setTextColor(context.getResources().getColor(R.color.colorPink));
        }else {
            holder.time.setTextColor(context.getResources().getColor(R.color.colorDarkGrey));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_item);
            time.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         if(itemClickListener != null){
             itemClickListener.onItemClick(getAdapterPosition(), getItem(getAdapterPosition()));
         }
        }
    }
}
