package fr.sushi.app.ui.checkout.paiement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adyen.core.PaymentRequest;
import com.adyen.core.interfaces.HttpResponseCallback;
import com.adyen.core.interfaces.PaymentDataCallback;
import com.adyen.core.interfaces.PaymentRequestListener;
import com.adyen.core.models.Payment;
import com.adyen.core.models.PaymentRequestResult;
import com.adyen.core.utils.AsyncHttpClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.sushi.app.R;
import fr.sushi.app.databinding.FragmentPaiementBinding;
import fr.sushi.app.ui.adressPicker.AddressPickerActivity;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.payment.Global.Constants;
import fr.sushi.app.util.PermissionUtil;


public class PaiementFragment extends Fragment implements OnMapReadyCallback {
    private FragmentPaiementBinding binding;
    private GoogleMap mGoogleMap;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    private BottomSheetDialog dialog;


    public PaiementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paiement, container, false);
        View view = binding.getRoot();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), AddressPickerActivity.class));
            }
        });


        binding.layoutCarteBancaire.setOnClickListener((View v) ->{
            binding.tvTitle.setText("Paiement par carte");
            binding.layoutPaymentOption.setVisibility(View.VISIBLE);
            binding.ivPaymentOption.setImageResource(R.drawable.ic_card);
            binding.tvPaymentOptionTitle.setText("Visa");
            binding.tvPaymentOptionBody.setText("Se terminant par 0076");
        });

        binding.layoutCarteBancaireAuLivreur.setOnClickListener((View v)->{
            binding.tvTitle.setText("Paiement par carte à la livraison");
            binding.layoutPaymentOption.setVisibility(View.VISIBLE);
            binding.ivPaymentOption.setImageResource(R.drawable.ic_wallet);
            binding.tvPaymentOptionTitle.setText("Monnaie à prévoir");
            binding.tvPaymentOptionBody.setText("0,00 €");

        });

        binding.layoutTicketRestaurant.setOnClickListener((View v) ->{
            binding.tvTitle.setText("Paiement en espèce/ticket restaurant");
            binding.layoutPaymentOption.setVisibility(View.GONE);
            showDialog();
        });
        return view;
    }

    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_to_deliveryman, null);

        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button buttonSpecifyAnAmount=bottomSheet.findViewById(R.id.buttonSpecifyAnAmount);
        Button buttonNoChange=bottomSheet.findViewById(R.id.buttonNoChange);

        buttonSpecifyAnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDialogSpecifyAmount();
            }
        });

        buttonNoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                binding.tvTitle.setText("Paiement en espèce/ticket restaurant");
                binding.layoutPaymentOption.setVisibility(View.VISIBLE);
                binding.ivPaymentOption.setImageResource(R.drawable.ic_wallet);
                binding.tvPaymentOptionTitle.setText("Monnaie à prévoir");
                binding.tvPaymentOptionBody.setText("0,00 €");
            }
        });

    }

    private void showDialogSpecifyAmount() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_specify_an_amount, null);

        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView tvClose=bottomSheet.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            mGoogleMap = googleMap;
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.custom_style_json));
            if (!success) {
                Log.e("MapFragment", "Style parsing failed.");
            }

            List<SearchPlace> currentSearchPlace = PlaceUtil.getSearchPlace();
            if(!currentSearchPlace.isEmpty()) {
                SearchPlace latestSearchPlace = currentSearchPlace.get(0);
                if(latestSearchPlace.getLat() != 0.0 && latestSearchPlace.getLng() != 0.0){
                    LatLng latLng = new LatLng(latestSearchPlace.getLat(), latestSearchPlace.getLng());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    //markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    //move map camera
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
            }
            //checkPermissionAndPrepareClient();
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*@SuppressLint("MissingPermission")
    private void checkPermissionAndPrepareClient() {
        @SuppressLint("RestrictedApi")
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (PermissionUtil.init(getActivity()).request(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
           // mGoogleMap.setMyLocationEnabled(true);
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();

        List<SearchPlace> currentSearchPlace = PlaceUtil.getSearchPlace();
        if(!currentSearchPlace.isEmpty()) {
            SearchPlace latestSearchPlace = currentSearchPlace.get(0);
            binding.tvCountryCode.setText(latestSearchPlace.getPostalCode()+" "+latestSearchPlace.getCity());
            binding.tvAddress.setText(latestSearchPlace.getAddress());
            if(!TextUtils.isEmpty(latestSearchPlace.getTime())) {
                String time = latestSearchPlace.getTime().replace(":", "h");
                binding.tvDeliveryTime.setText("Livraison prévue pour " + time);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }*/
    }

   /* private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            }
        }
    };*/


}
