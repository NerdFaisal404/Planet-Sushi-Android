package fr.sushi.app.ui.shop.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fr.sushi.app.R;


public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    private Context _context;

    public ShopsAdapter(Context _context) {
        this._context = _context;
    }

    @NonNull
    @Override
    public ShopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_each_row_shops,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.imgViewPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForCallShop();
            }
        });
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgViewPhoneCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewPhoneCall = itemView.findViewById(R.id.imgViewPhoneCall);
        }
    }


    private void showDialogForCallShop() {
        final Dialog dialog = new Dialog(_context);
        dialog.setContentView(R.layout.layout_dialog_call_shop);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
