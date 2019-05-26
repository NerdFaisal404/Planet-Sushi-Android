package fr.sushi.app.ui.shop;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.ligl.android.widget.iosdialog.IOSDialog;

import java.util.ArrayList;
import java.util.Timer;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.model.BaseAddress;
import fr.sushi.app.databinding.ActivityMapAutoCompletePlaceBinding;
import fr.sushi.app.misc.Constants;
import fr.sushi.app.ui.adressPicker.adapter.PlaceAutocompleteAdapter;
import fr.sushi.app.util.DataCacheUtil;
import timber.log.Timber;

public class MapAutoCompletePlaceActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private GoogleApiClient mGoogleApiClient;
    ActivityMapAutoCompletePlaceBinding binding;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    private PlaceAutocompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_auto_complete_place);
        setUpAdapter();
        binding.tvClose.setOnClickListener(v -> finish());
    }


    void setUpAdapter() {
        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("FR").build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        mAdapter = new PlaceAutocompleteAdapter(this,
                mGoogleApiClient, BOUNDS, filter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                if (mGoogleApiClient.isConnected()) {
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

    @Override
    public void onPlaceClick(BaseAddress address) {
        if (address != null) {
            if (address instanceof PlaceAutocompleteAdapter.PlaceAutocomplete) {
                try {
                    final String placeId = String.valueOf(((PlaceAutocompleteAdapter.PlaceAutocomplete) address).placeId);
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                            .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getCount() == 1) {
                                Place place = places.get(0);
                                finish(place.getLatLng().latitude, place.getLatLng().longitude);
                                //FrequentFunctions.hideKeyBoard(ChooseLocation.this, rootLayout);
                            /*selectedPlace = places;
                            String coordinates = selectedPlace.get(0).getLatLng().latitude + "," + selectedPlace.get(0).getLatLng().longitude;*/
                            } else {
                                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.blank, R.anim.top_to_bottom);
    }

    private void finish(double latitude, double longitude) {
        Timber.v("Latitude, Longitude = %f, %f", latitude, longitude);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.LATITUDE, latitude);
        returnIntent.putExtra(Constants.LONGITUDE, longitude);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
