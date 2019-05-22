package fr.sushi.app.ui.carddetails;

import android.support.v4.view.ViewPager;
import android.view.View;

import fr.sushi.app.R;

import fr.sushi.app.databinding.ActivitySusuCardDetailsBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.carddetails.adapter.CardDetailsAdapter;
import fr.sushi.app.ui.carddetails.adapter.CardPagerAdapter;

public class SusuCardDetailsActivity extends BaseActivity {

    private CardPagerAdapter mCardAdapter;
    private CardDetailsAdapter mCardDetailsAdapter;

    private ActivitySusuCardDetailsBinding mBinding;
    private int[] mCardList = {R.drawable.card_pink, R.drawable.card_gold, R.drawable.card_black};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_susu_card_details;
    }


    @Override
    protected void startUI() {
        mBinding = (ActivitySusuCardDetailsBinding) getViewDataBinding();

        setClickListener(mBinding.imageViewBack);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(mCardList[0]);
        mCardAdapter.addCardItem(mCardList[1]);
        mCardAdapter.addCardItem(mCardList[2]);

        mBinding.viewPagerCard.setAdapter(mCardAdapter);
        mBinding.viewPagerCard.setOffscreenPageLimit(3);

        mBinding.dotsIndicator.setViewPager(mBinding.viewPagerCard);
        mBinding.dotsIndicator.setViewPager(mBinding.viewPagerCardContent);

        mCardDetailsAdapter = new CardDetailsAdapter(getSupportFragmentManager());
        mBinding.viewPagerCardContent.setAdapter(mCardDetailsAdapter);

        syncViewpager();
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

    private void syncViewpager() {
        mBinding.viewPagerCard.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                // mBinding.viewPagerCardContent.scrollTo(mBinding.viewPagerCard.getScrollX(), mBinding.viewPagerCard.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mBinding.dotsIndicator.setViewPager(mBinding.viewPagerCardContent);
                    mBinding.viewPagerCardContent.setCurrentItem(mBinding.viewPagerCard.getCurrentItem(), false);
                }
            }
        });

        mBinding.viewPagerCardContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                // mBinding.viewPagerCard.scrollTo(mBinding.viewPagerCardContent.getScrollX(), mBinding.viewPagerCard.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mBinding.dotsIndicator.setViewPager(mBinding.viewPagerCard);
                    mBinding.viewPagerCard.setCurrentItem(mBinding.viewPagerCardContent.getCurrentItem(), false);
                }
            }
        });
    }
}
