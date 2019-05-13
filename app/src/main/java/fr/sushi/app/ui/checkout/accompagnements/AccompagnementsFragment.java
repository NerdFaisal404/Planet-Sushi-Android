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
    private RelativeLayout rlSauces,rlAccompagnements,rlBoissons,rlDesserts,rlWasbi,rlBaguettes;
    private RelativeLayout rlCountForSauces,rlCountForAccompagnements,rlCountForBoissons,rlCountForDesserts,rlCountForWasbi,rlCountForBaguettes;
    private TextView tvCountSauces,tvCountAccompagnements,tvCountBoissons,tvCountDesserts,tvCountWasbi,tvCountBaguettes;
    private TextView tvSauces,tvAccompagnements,tvBoissons,tvDesserts,tvWasbi,tvBaguettes;
    private FragmentAccompagnementsBinding binding;
    
    private int selectedAccompagnements;
    private static final int SAUCES = 1;
    private static final int ACCOMPAGNEMENTS = 2;
    private static final int BOISSONS = 3;
    private static final int DESSERTS = 4;
    private static final int WASBI = 5;
    private static final int BAGUETTES = 6;
    private int countSauces = 0, countAccompagnements = 0, countBoissons = 0, countDesserts = 0 , countWasbi = 0 ,countBauettes=0 ;



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

        rlDesserts=view.findViewById(R.id.rlDesserts);
        rlDesserts.setOnClickListener(this);
        rlCountForDesserts=view.findViewById(R.id.rlCountForDesserts);
        tvCountDesserts=view.findViewById(R.id.tvCountDesserts);
        tvDesserts=view.findViewById(R.id.tvDesserts);

        rlWasbi=view.findViewById(R.id.rlWasbi);
        rlWasbi.setOnClickListener(this);
        rlCountForWasbi=view.findViewById(R.id.rlCountForWasbi);
        tvCountWasbi=view.findViewById(R.id.tvCountWasbi);
        tvWasbi=view.findViewById(R.id.tvWasbi);

        rlBaguettes=view.findViewById(R.id.rlBaguettes);
        rlBaguettes.setOnClickListener(this);
        rlCountForBaguettes=view.findViewById(R.id.rlCountForBaguettes);
        tvCountBaguettes=view.findViewById(R.id.tvCountBaguettes);
        tvBaguettes=view.findViewById(R.id.tvBaguettes);


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
                   case DESSERTS:
                       Log.e(TAG, "iconImageViewPlusOnClick: DESSERTS clicked");
                       countDesserts+=1;
                       tvCountDesserts.setText(String.valueOf(countDesserts));
                       if (countDesserts>0){
                           rlCountForDesserts.setVisibility(View.VISIBLE);
                       }else {
                           rlCountForDesserts.setVisibility(View.GONE);
                       }
                       break;
                   case WASBI:
                       Log.e(TAG, "iconImageViewPlusOnClick: WASBI clicked");
                       countWasbi+=1;
                       tvCountWasbi.setText(String.valueOf(countWasbi));
                       if (countWasbi>0){
                           rlCountForWasbi.setVisibility(View.VISIBLE);
                       }else {
                           rlCountForWasbi.setVisibility(View.GONE);
                       }
                       break;
                   case BAGUETTES:
                       Log.e(TAG, "iconImageViewPlusOnClick: BAGUETTES clicked");
                       countBauettes +=1;
                       tvCountBaguettes.setText(String.valueOf(countBauettes));
                       if (countBauettes>0){
                           rlCountForBaguettes.setVisibility(View.VISIBLE);
                       }else {
                           rlCountForBaguettes.setVisibility(View.GONE);
                       }
                       break;
               }
            }

            @Override
            public void iconImageViewMinusOnClick(int position) {
                switch (selectedAccompagnements){
                    case SAUCES:
                        Log.e(TAG, "iconImageViewMinusOnClick: SAUCES clicked");
                        countSauces-=1;
                        tvCountSauces.setText(String.valueOf(countSauces));
                        if (countSauces>0){
                            rlCountForSauces.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForSauces.setVisibility(View.GONE);
                        }
                        break;
                    case ACCOMPAGNEMENTS:
                        Log.e(TAG, "iconImageViewMinusOnClick: ACCOMPAGNEMENTS clicked");
                        countAccompagnements-=1;
                        tvCountAccompagnements.setText(String.valueOf(countAccompagnements));
                        if (countAccompagnements>0){
                            rlCountForAccompagnements.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForAccompagnements.setVisibility(View.GONE);
                        }
                        break;
                    case BOISSONS:
                        Log.e(TAG, "iconImageViewMinusOnClick: BOISSONS clicked");
                        countBoissons-=1;
                        tvCountBoissons.setText(String.valueOf(countBoissons));
                        if (countBoissons>0){
                            rlCountForBoissons.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForBoissons.setVisibility(View.GONE);
                        }
                        break;
                    case DESSERTS:
                        Log.e(TAG, "iconImageViewMinusOnClick: DESSERTS clicked");
                        countDesserts-=1;
                        tvCountDesserts.setText(String.valueOf(countDesserts));
                        if (countDesserts>0){
                            rlCountForDesserts.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForDesserts.setVisibility(View.GONE);
                        }
                        break;
                    case WASBI:
                        Log.e(TAG, "iconImageViewMinusOnClick: WASBI clicked");
                        countWasbi-=1;
                        tvCountWasbi.setText(String.valueOf(countWasbi));
                        if (countWasbi>0){
                            rlCountForWasbi.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForWasbi.setVisibility(View.GONE);
                        }
                        break;
                    case BAGUETTES:
                        Log.e(TAG, "iconImageViewMinusOnClick: BAGUETTES clicked");
                        countBauettes -=1;
                        tvCountBaguettes.setText(String.valueOf(countBauettes));
                        if (countBauettes>0){
                            rlCountForBaguettes.setVisibility(View.VISIBLE);
                        }else {
                            rlCountForBaguettes.setVisibility(View.GONE);
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
                handleSauce();
            break;
            case R.id.rlAccompagnements:
                handleAccompanements();
                break;
            case R.id.rlBoissons:
                handleBoissons();
                break;
            case R.id.rlDesserts:
                handleDesserts();
                break;
            case R.id.rlWasbi:
               handleWasbi();
                break;
            case R.id.rlBaguettes:
                handleBaguettes();
                break;

        }
    }

    private void handleBaguettes() {
        selectedAccompagnements=6;

        tvBaguettes.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        tvWasbi.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvDesserts.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

        rlBaguettes.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        rlWasbi.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlDesserts.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));


        recycler_view_accompagnements.setVisibility(View.VISIBLE);
    }

    private void handleWasbi() {
        selectedAccompagnements=5;

        tvWasbi.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        tvDesserts.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBaguettes.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

        rlWasbi.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        rlDesserts.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBaguettes.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));


        recycler_view_accompagnements.setVisibility(View.VISIBLE);
    }

    private void handleDesserts() {
        selectedAccompagnements=4;

        tvDesserts.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvWasbi.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBaguettes.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

        rlDesserts.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlWasbi.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBaguettes.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        recycler_view_accompagnements.setVisibility(View.VISIBLE);
    }

    private void handleBoissons() {
        selectedAccompagnements=3;
        tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvDesserts.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvWasbi.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBaguettes.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

        rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlDesserts.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlWasbi.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBaguettes.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        recycler_view_accompagnements.setVisibility(View.VISIBLE);
    }

    private void handleAccompanements() {
        selectedAccompagnements=2;
        tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvDesserts.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvWasbi.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBaguettes.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

        rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlDesserts.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlWasbi.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBaguettes.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        recycler_view_accompagnements.setVisibility(View.VISIBLE);
    }

    private void handleSauce() {
        selectedAccompagnements=1;
        tvSauces.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        tvAccompagnements.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBoissons.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvDesserts.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvWasbi.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        tvBaguettes.setTextColor(ContextCompat.getColor(getContext(), R.color.color_627588));

        rlSauces.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_627588));
        rlAccompagnements.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBoissons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlDesserts.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlWasbi.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        rlBaguettes.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        recycler_view_accompagnements.setVisibility(View.VISIBLE);
    }
}
