package fr.sushi.app.ui.cart.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.ui.detail.DetailFragment;
import fr.sushi.app.util.FragmentFunctions;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    AppCompatActivity activity;

    public MenuAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_fragment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragment baseFragment = (DetailFragment) activity.getSupportFragmentManager()
                        .findFragmentByTag(DetailFragment.class.getName());
                if (baseFragment == null) {
                    baseFragment = new DetailFragment();
                }
                FragmentFunctions.commitFragment(R.id.fragment_container, activity, baseFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
