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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.sushi.app.R;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.FragmentAccompagnementsBinding;
import fr.sushi.app.ui.checkout.commade.model.AccompagnementResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompagnementsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG=AccompagnementsFragment.class.getSimpleName();
    private AccompagnementViewModel accompagnementViewModel;
    private RecyclerView recycler_view_accompagnements;
    private AccompagnementsAdapter adapter;
    private RelativeLayout rlSauces,rlAccompagnements,rlBoissons;
    private RelativeLayout rlCountForSauces,rlCountForAccompagnements,rlCountForBoissons;
    private TextView tvCountSauces,tvCountAccompagnements,tvCountBoissons;
    private TextView tvSauces,tvAccompagnements,tvBoissons;
    private FragmentAccompagnementsBinding binding;
    
    private int selectedAccompagnements;
    private static final int SAUCES = 1;
    private static final int ACCOMPAGNEMENTS = 2;
    private static final int BOISSONS = 3;
    private int countSauces = 0, countAccompagnements = 0, countBoissons = 0;



    public AccompagnementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_accompagnements, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeData();

        rlSauces = view.findViewById(R.id.rlSauces);
        rlSauces.setOnClickListener(this);
        rlCountForSauces = view.findViewById(R.id.rlCountForSauces);
        tvCountSauces = view.findViewById(R.id.tvCountSauces);
        tvSauces = view.findViewById(R.id.tvSauces);


        rlAccompagnements=view.findViewById(R.id.rlAccompagnements);
        rlAccompagnements.setOnClickListener(this);
        rlCountForAccompagnements=view.findViewById(R.id.rlCountForAccompagnements);
        tvCountAccompagnements=view.findViewById(R.id.tvCountAccompagnements);
        tvAccompagnements=view.findViewById(R.id.tvAccompagnements);

        rlBoissons=view.findViewById(R.id.rlBoissons);
        rlBoissons.setOnClickListener(this);
        rlCountForBoissons=view.findViewById(R.id.rlCountForBoissons);
        tvCountBoissons=view.findViewById(R.id.tvCountBoissons);
        tvBoissons=view.findViewById(R.id.tvBoissons);


        recycler_view_accompagnements = view.findViewById(R.id.recycler_view_accompagnements);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_view_accompagnements.setLayoutManager(layoutManager);
        adapter = new AccompagnementsAdapter(getContext(), new AccompagnementsAdapter.ClickListener() {
            @Override
            public void iconImageViewPlusOnClick(int position) {
               switch (selectedAccompagnements){
                   case SAUCES:
                       Log.e(TAG, "iconImageViewPlusOnClick: SAUCES clicked");
                       countSauces+=1;
                       tvCountSauces.setText(String.valueOf(countSauces));
                       if (countSauces>0){
                           rlCountForSauces.setVisibility(View.VISIBLE);
                       }else {
                           rlCountForSauces.setVisibility(View.GONE);
                       }
                       break;
                   case ACCOMPAGNEMENTS:
                       Log.e(TAG, "iconImageViewPlusOnClick: ACCOMPAGNEMENTS clicked");
                       countAccompagnements+=1;
                       tvCountAccompagnements.setText(String.valueOf(countAccompagnements));
                       if (countAccompagnements>0){
                           rlCountForAccompagnements.setVisibility(View.VISIBLE);
                       }else {
                           rlCountForAccompagnements.setVisibility(View.GONE);
                       }
                       break;
                   case BOISSONS:
                       Log.e(TAG, "iconImageViewPlusOnClick: BOISSONS clicked");
                       countBoissons+=1;
                       tvCountBoissons.setText(String.valueOf(countBoissons));
                       if (countBoissons>0){
                           rlCountForBoissons.setVisibility(View.VISIBLE);
                       }else {
                           rlCountForBoissons.setVisibility(View.GONE);
                       }
                       break;
               }
            }

            @Override
            public void iconImageViewMinusOnClick(int position) {
                switch (selectedAccompagnements){
                    case SAUCES:
                        Log.e(TAG, "iconImageViewPlusOnClick: SAUCES clicked");
                        countSauces-=1;
                        tvCountSauces.setText(String.valueOf(countSauces));
                        if (countAccompagnements>0){
                            rlCountForAccompagnements.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForAccompagnements.setVisibility(View.GONE);
                        }
                        break;
                    case ACCOMPAGNEMENTS:
                        Log.e(TAG, "iconImageViewPlusOnClick: ACCOMPAGNEMENTS clicked");
                        countAccompagnements-=1;
                        tvCountAccompagnements.setText(String.valueOf(countAccompagnements));
                        if (countAccompagnements>0){
                            rlCountForAccompagnements.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForAccompagnements.setVisibility(View.GONE);
                        }
                        break;
                    case BOISSONS:
                        Log.e(TAG, "iconImageViewPlusOnClick: BOISSONS clicked");
                        countBoissons-=1;
                        tvCountBoissons.setText(String.valueOf(countBoissons));
                        if (countBoissons>0){
                            rlCountForBoissons.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForBoissons.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rlSauces:
                selectedAccompagnements=1;
                tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

                rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                recycler_view_accompagnements.setVisibility(View.VISIBLE);
            break;
            case R.id.rlAccompagnements:
                selectedAccompagnements=2;
                tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

                rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                recycler_view_accompagnements.setVisibility(View.VISIBLE);
                break;
            case R.id.rlBoissons:
                selectedAccompagnements=3;
                tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

                rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
                rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                recycler_view_accompagnements.setVisibility(View.VISIBLE);
                break;
        }
    }
}
