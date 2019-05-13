package fr.sushi.app.ui.checkout.paiement;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.databinding.FragmentPaiementBinding;


public class PaiementFragment extends Fragment {
    private FragmentPaiementBinding binding;

    public PaiementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paiement, container, false);
        View view = binding.getRoot();
        return view;
    }


}
