package fr.sushi.app.ui.checkout.accompagnements;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.ui.checkout.commade.CommadeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompagnementsFragment extends Fragment {
    private CommadeViewModel commadeViewModel;

    public AccompagnementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accompagnements, container, false);
    }

    private void observeData() {

        commadeViewModel = ViewModelProviders.of(this).get(CommadeViewModel.class);
        commadeViewModel.getCheckoutSideProducts();

        commadeViewModel.getSideProductMutableLiveData().observe(this, responseBody -> {

        });
    }

}
