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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.sushi.app.R;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.FragmentAccompagnementsBinding;
import fr.sushi.app.ui.checkout.commade.CommadeViewModel;
import fr.sushi.app.ui.checkout.commade.model.AccompagnementResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompagnementsFragment extends Fragment {
    private AccompagnementViewModel accompagnementViewModel;

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
        llSauces.setOnClickListener(view1 -> {
            tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            llSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
            recycler_view_accompagnements.setVisibility(View.VISIBLE);
        });

        recycler_view_accompagnements = view.findViewById(R.id.recycler_view_accompagnements);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_view_accompagnements.setLayoutManager(layoutManager);
        adapter = new AccompagnementsAdapter(getContext());
        recycler_view_accompagnements.setAdapter(adapter);

    }

    private void observeData() {

        accompagnementViewModel = ViewModelProviders.of(this).get(AccompagnementViewModel.class);
        accompagnementViewModel.getCheckoutSideProducts();

        accompagnementViewModel.getSideProductMutableLiveData().observe(this, response -> {
            try {
                JSONObject responseObject = new JSONObject(response.string());
                boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                Log.e("JsonObject", "value =" + responseObject.toString());
                if (error == true) {
                 ErrorResponse   errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                } else {
                    AccompagnementResponse accompagnementResponse = new Gson().fromJson(responseObject.toString(), AccompagnementResponse.class);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
