package fr.sushi.app.ui.checkout.accompagnements;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.sushi.app.R;
import fr.sushi.app.databinding.FragmentAccompagnementsBinding;
import fr.sushi.app.ui.checkout.commade.CommadeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompagnementsFragment extends Fragment {
    private CommadeViewModel commadeViewModel;

    private RecyclerView recycler_view_accompagnements;
    private AccompagnementsAdapter adapter;
    private LinearLayout llSauces;
    private TextView tvSauces;
    private FragmentAccompagnementsBinding binding;

    public AccompagnementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_accompagnements, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeData();

        llSauces = view.findViewById(R.id.llSauces);
        tvSauces = view.findViewById(R.id.tvSauces);
        llSauces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                llSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                recycler_view_accompagnements.setVisibility(View.VISIBLE);
            }
        });

        recycler_view_accompagnements = view.findViewById(R.id.recycler_view_accompagnements);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_view_accompagnements.setLayoutManager(layoutManager);
        adapter = new AccompagnementsAdapter(getContext());
        recycler_view_accompagnements.setAdapter(adapter);

    }

    private void observeData() {

        commadeViewModel = ViewModelProviders.of(this).get(CommadeViewModel.class);
        commadeViewModel.getCheckoutSideProducts();

        commadeViewModel.getSideProductMutableLiveData().observe(this, responseBody -> {

        });
    }
}
