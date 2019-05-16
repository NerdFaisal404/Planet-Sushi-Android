package fr.sushi.app.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.florent37.viewanimator.ViewAnimator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicBoolean;

import fr.sushi.app.R;
import fr.sushi.app.SushiApp;

public class PicassoUtil {
    public static void loadImage(String path, ImageView imageView){
        if(imageView == null)return;
        Context context = SushiApp.getContext();
        final AtomicBoolean playTransition = new AtomicBoolean();
        Picasso.get().load(path).networkPolicy(NetworkPolicy.NO_CACHE).fetch(new Callback(){
            @Override
            public void onSuccess() {
                //imageView.setAlpha(0f);
                Picasso.get().load(path).into(imageView);
                //imageView.animate().setDuration(1000).alpha(1f).start();
                ViewAnimator
                        .animate(imageView)
                        .fadeIn()
                        .duration(2000)
                        .start();
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(path).placeholder(context.getResources().getDrawable(R.drawable.ic_place_holder)).into(imageView);
            }

        });
    }
}
