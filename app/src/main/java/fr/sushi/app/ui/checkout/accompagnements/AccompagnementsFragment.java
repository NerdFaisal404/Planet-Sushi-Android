package fr.sushi.app.ui.checkout.accompagnements;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompagnementsFragment extends Fragment {


    private RecyclerView recycler_view_accompagnements;
    private AccompagnementsAdapter adapter;
    private LinearLayout llSauces;
    private TextView tvSauces;

    public AccompagnementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accompagnements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llSauces=view.findViewById(R.id.llSauces);
        tvSauces=view.findViewById(R.id.tvSauces);
        llSauces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSauces.setTextColor(ContextCompat.getColor(getContext(),R.color.colorWhite));
                llSauces.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_627588));
                recycler_view_accompagnements.setVisibility(View.VISIBLE);
            }
        });

        recycler_view_accompagnements=view.findViewById(R.id.recycler_view_accompagnements);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recycler_view_accompagnements.setLayoutManager(layoutManager);
        adapter=new AccompagnementsAdapter(getContext());
        recycler_view_accompagnements.setAdapter(adapter);

    }
}
