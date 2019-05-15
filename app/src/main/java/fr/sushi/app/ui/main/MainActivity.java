package fr.sushi.app.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.remote.network.RetrofitClient;
import fr.sushi.app.databinding.ActivityMainBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.cart.FoodMenuFragment;
import fr.sushi.app.ui.checkout.CheckoutActivity;
import fr.sushi.app.ui.emptyprofile.EmptyProfileFragment;
import fr.sushi.app.ui.home.view.HomeFragment;
import fr.sushi.app.ui.profile.ProfileFragment;
import fr.sushi.app.ui.shop.MapFragment;
import fr.sushi.app.ui.shopping_bag.ShoppingBagFragment;
import fr.sushi.app.util.FragmentFunctions;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private static MainActivity sInatnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInatnce = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // StatusBarUtil.setTranslucentForImageViewInFragment(this, 20, null);
        RetrofitClient.getInstance(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        FragmentFunctions.commitFragment(R.id.fragment_container, this, new HomeFragment());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        BaseFragment baseFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                baseFragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(HomeFragment.class.getName());
                if (baseFragment == null) {
                    baseFragment = new HomeFragment();
                }
                break;

            case R.id.navigation_shop:
                baseFragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(MapFragment.class.getName());
                if (baseFragment == null) {
                    baseFragment = new MapFragment();
                }
                break;

            case R.id.navigation_carts:
                baseFragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(FoodMenuFragment.class.getName());
                if (baseFragment == null) {
                    baseFragment = new FoodMenuFragment();
                }
                break;


            case R.id.navigation_panier:
               /* baseFragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(ShoppingBagFragment.class.getName());
                if (baseFragment == null) {
                    baseFragment = new ShoppingBagFragment();
                }*/

               startActivity(new Intent(MainActivity.this, CheckoutActivity.class));

                break;

            case R.id.navigation_profile:
                if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
                    // profile fragment will be shown

                    baseFragment = (BaseFragment) getSupportFragmentManager()
                            .findFragmentByTag(ProfileFragment.class.getName());
                    if (baseFragment == null) {
                        baseFragment = new ProfileFragment();
                    }
                } else {

                    baseFragment = (BaseFragment) getSupportFragmentManager()
                            .findFragmentByTag(EmptyProfileFragment.class.getName());
                    if (baseFragment == null) {
                        baseFragment = new EmptyProfileFragment();
                    }
                }

                break;


        }
        FragmentFunctions.commitFragment(R.id.fragment_container, this, baseFragment);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void goProfilePage() {
        if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
            FragmentFunctions.commitFragment(R.id.fragment_container, sInatnce, new ProfileFragment());
        }
        binding.fragmentContainer.findViewById(R.id.fragment_container).performClick();
    }

    public void gotoEmptyProfilePage() {
        FragmentFunctions.commitFragment(R.id.fragment_container, sInatnce, new EmptyProfileFragment());
    }
}


/**
 * Commit child fragment of BaseFragment on a frameLayout
 *
 * @param viewId       int value
 * @param baseFragment BaseFragment object
 * @return void
 */

