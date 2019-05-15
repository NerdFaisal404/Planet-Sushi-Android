package fr.sushi.app.ui.carddetails.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.ui.carddetails.CardAdapter;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<Integer> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
    }

    public void addCardItem(int item) {
        mData.add(item);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_card_details, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void bind(int item, View view) {
        ImageView imageView = view.findViewById(R.id.image_view_card);
        imageView.setImageResource(item);
    }

}