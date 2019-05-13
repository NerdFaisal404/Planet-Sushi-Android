package fr.sushi.app.ui.carddetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ActivityCardDetailsBinding;
import fr.sushi.app.ui.base.BaseActivity;

public class CardDetailsActivity extends BaseActivity {

    private CardPagerAdapter mCardAdapter;

    private ActivityCardDetailsBinding mBinding;
    private int[] mCardList = {R.drawable.card_pink, R.drawable.card_gold, R.drawable.card_black};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_card_details;
    }


    @Override
    protected void startUI() {
        mBinding = (ActivityCardDetailsBinding) getViewDataBinding();

        setClickListener(mBinding.imageViewBack);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(mCardList[0]);
        mCardAdapter.addCardItem(mCardList[1]);
        mCardAdapter.addCardItem(mCardList[2]);

        mBinding.viewPagerCard.setAdapter(mCardAdapter);
        mBinding.viewPagerCard.setOffscreenPageLimit(3);

        mBinding.dotsIndicator.setViewPager(mBinding.viewPagerCard);
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.image_view_back) {
            finish();
        }
    }
}
