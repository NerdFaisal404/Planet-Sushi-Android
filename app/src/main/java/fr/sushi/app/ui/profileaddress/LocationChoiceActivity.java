package fr.sushi.app.ui.profileaddress;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.sushi.app.R;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.databinding.ActivityLocationChoiceBinding;
import fr.sushi.app.ui.adressPicker.adapter.PlaceAutocompleteAdapter;
import fr.sushi.app.ui.base.BaseActivity;

public class LocationChoiceActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private ActivityLocationChoiceBinding binding;
    private PlaceAutocompleteAdapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Geocoder mGeocoder;
    private boolean isFromAdd;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_choice;
    }


    @Override
    protected void startUI() {
        binding = (ActivityLocationChoiceBinding) getViewDataBinding();

        parseIntent();

        initGoogleClient();

        mGeocoder = new Geocoder(this, Locale.FRANCE);

        setUpAdapter();

        binding.ivClose.setOnClickListener(view -> finish());
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        try {
                            if (places.getCount() == 1) {
                                LatLng latLng = places.get(0).getLatLng();
                                List<Address> addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                String zipCode = addresses.get(0).getPostalCode();
                                String city = addresses.get(0).getLocality();
                                String address = addresses.get(0).getThoroughfare();
                                Log.e("Place_cliec", "code =" + zipCode);
                                Log.e("Place_cliec", "city =" + city);
                                Log.e("Place_cliec", "address =" + address);

                                if (isFromAdd) {
                                    Intent intent = new Intent(LocationChoiceActivity.this, AddressAddActivity.class);
                                    intent.putExtra(IntentKey.KEY_IS_CREATE_ADDRESS, true);
                                    intent.putExtra(IntentKey.ADDRESS, address);
                                    intent.putExtra(IntentKey.CITY, city);
                                    intent.putExtra(IntentKey.ZIP_CODE, zipCode);
                                    startActivity(intent);
                                } else {
                                    CommonUtility.LOCATION = address;
                                    CommonUtility.CITY = city;
                                    CommonUtility.ZIP_CODE = zipCode;
                                }

                                finish();
                                // viewModel.setDeliveryAddress(address, zipCode, city);
                            } else {
                                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(IntentKey.IS_FROM_ADD_REQUEST)) {
            isFromAdd = intent.getBooleanExtra(IntentKey.IS_FROM_ADD_REQUEST, false);
        }
    }

    private void setUpAdapter() {
        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("FR").build();
        mAdapter = new PlaceAutocompleteAdapter(this,
                mGoogleApiClient, BOUNDS, filter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.bg_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        binding.recyclerView.addItemDecoration(horizontalDecoration);
        binding.recyclerView.setAdapter(mAdapter);


        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (i2 > 0) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        binding.recyclerView.setAdapter(mAdapter);
                    }
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
