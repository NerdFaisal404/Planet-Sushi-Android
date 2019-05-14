package fr.sushi.app.ui.carddetails.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.sushi.app.ui.carddetails.fragment.BlackCardFragment;
import fr.sushi.app.ui.carddetails.fragment.GoldCardFragment;
import fr.sushi.app.ui.carddetails.fragment.PinkCardFragment;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/14/2019 at 9:52 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/14/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class CardDetailsAdapter extends FragmentPagerAdapter {
    private static final int PAGE_SIZE = 3;

    public CardDetailsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return PinkCardFragment.instance();
            case 1:
                return GoldCardFragment.instance();
            case 2:
                return BlackCardFragment.instance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_SIZE;
    }
}
