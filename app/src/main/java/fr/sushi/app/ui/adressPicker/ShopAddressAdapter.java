package fr.sushi.app.ui.adressPicker;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.databinding.AdapterPalceAutoCompleteBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;

public class ShopAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface Listener{
        void onItemClick(ResponseItem responseItem);
    }
    private List<ResponseItem> responseItemList;
    private Context context;
    private Listener itemClickListener;
    public ShopAddressAdapter(Context context, List<ResponseItem> responseItems, Listener listener){
        this.context = context;
        this.responseItemList = responseItems;
        itemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return responseItemList.size();
    }

    private ResponseItem getItem(int index){
        return responseItemList.get(index);
    }

    public void addNewList( List<ResponseItem> responseItems){
        if (responseItemList==null){
            this.responseItemList = responseItems;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_palce_auto_complete, viewGroup, false);

        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PlaceHolder placeHolder = (PlaceHolder)viewHolder;
        ResponseItem item  = getItem(position);

        if(item == null) return;
        placeHolder.address.setText(item.getAddress());
        placeHolder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(item);
            }
        });

    }

    private class PlaceHolder extends RecyclerView.ViewHolder {
        private TextView address;
        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
        }

    }
}
