package fr.sushi.app.ui.carddetails.fragment;

import fr.sushi.app.R;
import fr.sushi.app.ui.base.BaseFragment;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/14/2019 at 9:57 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/14/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class PinkCardFragment extends BaseFragment {

    private static PinkCardFragment mFragment;

    public static PinkCardFragment instance() {
        if (mFragment == null) {
            mFragment = new PinkCardFragment();
        }
        return mFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pink_card;
    }

    @Override
    protected void startUI() {

    }

    @Override
    protected void stopUI() {

    }
}