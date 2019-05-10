package fr.sushi.app.ui.checkout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.sushi.app.ui.checkout.accompagnements.AccompagnementsFragment;

class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AccompagnementsFragment();
            case 1:
                return new AccompagnementsFragment();
            case 2:
                return new AccompagnementsFragment();
            default:
                return null;

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Commande";
            case 1:
                return "Accompagnements";
            case 2:
                return "Paiement";
            default:
                return null;

        }
    }
}