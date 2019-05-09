package fr.sushi.app.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import fr.sushi.app.R;
import fr.sushi.app.SushiApp;

public class Glider {
    public static void loadImage(String path, ImageView imageView){
        if(imageView == null)return;
        Context context = SushiApp.getContext();
        RequestOptions requestOptions = new
                RequestOptions();

        requestOptions.placeholder(context.getResources().getDrawable(R.drawable.ic_place_holder));
        Glide.with(context)
                .load(path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model,
                                                Target<Drawable> target,
                                                boolean isFirstResource) {

                        imageView.setImageDrawable(context.getDrawable(R.drawable.home_dummy_image));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource,
                                                   Object model,
                                                   Target<Drawable> target,
                                                   DataSource dataSource,
                                                   boolean isFirstResource) {


                        return false;
                    }
                })
                .into(imageView);
    }
}
