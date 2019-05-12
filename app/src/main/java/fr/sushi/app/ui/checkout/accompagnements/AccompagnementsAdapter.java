package fr.sushi.app.ui.checkout.accompagnements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.sushi.app.R;

public class AccompagnementsAdapter extends RecyclerView.Adapter<AccompagnementsAdapter.ViewHolder>{

    private Context mContext;
    private ClickListener  clickListener;
    private int count=0;
    List<Integer> arr = Arrays.asList(new Integer[10]);

    public AccompagnementsAdapter(Context mContext,ClickListener listener) {
        this.mContext = mContext;
        this.clickListener=listener;
    }

    public interface ClickListener  {
        void iconImageViewPlusOnClick( int position);
        void iconImageViewMinusOnClick( int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_each_row_accompagnemennts, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i, clickListener);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgViewPlus;
        private ImageView imgViewMinus;
        private TextView tvCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewPlus=itemView.findViewById(R.id.imgViewPlus);
            imgViewMinus=itemView.findViewById(R.id.imgViewMinus);
            tvCount=itemView.findViewById(R.id.tvCount);
        }

        public void bind(int index, ClickListener clickListener) {
            imgViewPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.iconImageViewPlusOnClick(index);
                    count+=1;
                    tvCount.setText(String.valueOf(count));
                }
            });

            imgViewMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.iconImageViewMinusOnClick(index);
                    count-=1;
                    tvCount.setText(String.valueOf(count));
                }
            });
        }
    }
}
