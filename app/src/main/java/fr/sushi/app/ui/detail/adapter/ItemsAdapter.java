package fr.sushi.app.ui.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.sushi.app.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    AppCompatActivity activity;
    List<String> dummyList;

    public ItemsAdapter(AppCompatActivity activity, List<String> dummyList) {
        this.activity = activity;
        this.dummyList = dummyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_items, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        setAdapter(viewHolder.recyclerViewChild);
        viewHolder.textViewItemName.setText(dummyList.get(i));
    }

    @Override
    public int getItemCount() {
        return dummyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewChild;
        TextView textViewItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewChild = itemView.findViewById(R.id.recyclerViewChild);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
        }
    }


    void setAdapter(RecyclerView recyclerView) {
        ItemsChildAdapter childAdapter = new ItemsChildAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(childAdapter);

    }
}
