package fr.sushi.app.ui.checkout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.sushi.app.ui.checkout.accompagnements.AccompagnementsFragment;
import fr.sushi.app.ui.checkout.commade.CommadeFragment;
import fr.sushi.app.ui.checkout.paiement.PaiementFragment;

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
                return new CommadeFragment();
            case 1:
                return AccompagnementsFragment.on();
            case 2:
                return new PaiementFragment();
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