package fr.sushi.app.ui.adressPicker.bottom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.sushi.app.R;

public class AddressNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public interface Listener {
        void onItemClick(int position, String item);
    }

    private int selectedItemPosition = 0;
    private List<String> itemList;
    private Context context;
    private Listener itemClickListener;

    public AddressNameAdapter(Context context, List<String> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public void setSelectedPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        itemClickListener = listener;
    }

    public String getItem(int index) {
        return itemList.get(index);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_slider_item, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int index) {
        ItemHolder holder = (ItemHolder) viewHolder;
        holder.name.setText(itemList.get(index));
        if (selectedItemPosition == index) {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorPink));
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorDarkGrey));
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition(), getItem(getAdapterPosition()));
            }
        }
    }
}
