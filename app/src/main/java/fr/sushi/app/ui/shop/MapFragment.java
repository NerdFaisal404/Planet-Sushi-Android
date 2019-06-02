package fr.sushi.app.ui.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.data.model.restuarents.TodaySchedulesItem;
import fr.sushi.app.databinding.FragmentMapBinding;
import fr.sushi.app.databinding.ListEachRowShopsBinding;
import fr.sushi.app.misc.Constants;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.shop.map.DataListener;
import fr.sushi.app.ui.shop.map.GetNearbyPlacesData;
import fr.sushi.app.ui.shop.map.MapItem;
import fr.sushi.app.ui.shop.viewmodel.MapViewModel;
import fr.sushi.app.util.PermissionUtil;
import fr.sushi.app.util.Utils;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    private static final int REQUEST_ADDRESS = 201;

    private FragmentMapBinding binding;
    private GoogleMap mGoogleMap;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    private MapViewModel mapViewModel;
    private RestuarentsResponse restuarentsResponse;
    private List<ResponseItem> mapItemList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void startUI() {
        binding = (FragmentMapBinding) getViewDataBinding();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        observeData();

        binding.etSearchContacts.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), MapAutoCompletePlaceActivity.class), REQUEST_ADDRESS);
        });

        binding.ivMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng;
                binding.ivMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker_around_me_pink));
                if (mLastLocation != null) {
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                } else {
                    latLng = new LatLng(48.864716, 2.349014);
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void stopUI() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_ADDRESS != requestCode) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            double latitude = data.getDoubleExtra(Constants.LATITUDE, 0);
            double longitude = data.getDoubleExtra(Constants.LONGITUDE, 0);
            showSearchLocation(latitude, longitude);
        } else {
            //todo error message to user

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            mGoogleMap = googleMap;
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.custom_style_json));
            if (!success) {
                Log.e("MapFragment", "Style parsing failed.");
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    int index = (int) marker.getTag();
                    binding.viewpager.setCurrentItem(index);
                    return false;
                }
            });

            checkPermissionAndPrepareClient();
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("MissingPermission")
    private void checkPermissionAndPrepareClient() {
        @SuppressLint("RestrictedApi")
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (PermissionUtil.init(getActivity()).request(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            this.mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.setMyLocationEnabled(true);
        }
        if(!isLocationEnable()){
            //Place current location marker
            LatLng latLng = new LatLng(48.864716, 2.349014);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_small));
            //mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    private boolean isLocationEnable() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0 && mLastLocation == null) {
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
                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_small));
                //mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            }
        }
    };

    private void observeData() {

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getShopList();

        mapViewModel.getRestuarentListMutableLiveData().observe(this, restuarentsResponse -> {
            mapItemList = restuarentsResponse.getResponse();
            loadMapItem();
            setupViewPager();
            Log.e("Map_item", "Item count =" + restuarentsResponse.getResponse().size());
        });

    }

    //private ClusterManager<ResponseItem> mClusterManager;

    private void loadMapItem() {

      /*  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapItemList.get(0).getPosition(), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<ResponseItem>(getActivity(), mGoogleMap);

        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        mGoogleMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.addItems(mapItemList);
        mClusterManager.cluster();*/
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        // mGoogleMap.setOnCameraIdleListener(mClusterManager);
        //mGoogleMap.setOnMarkerClickListener(mClusterManager);

        for (int i = 0; i < mapItemList.size(); i++) {
            ResponseItem item = mapItemList.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(item.getLat(), item.getLng());
            markerOptions.position(latLng);
            //markerOptions.title(item.getName());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_small));
            Marker marker = mGoogleMap.addMarker(markerOptions);
            marker.setTag(i);
            //move map camera
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }

    private void setupViewPager() {
        MapPagerAdapter pagerAdapter = new MapPagerAdapter();
        PageListener pageListener = new PageListener();
        binding.viewpager.addOnPageChangeListener(pageListener);
        binding.viewpager.setAdapter(pagerAdapter);
        //mViewPager.setPageTransformer(true, new ZoomPageTransformer());
        binding.viewpager.setCurrentItem(0);
        binding.viewpager.setPageMargin(20);
        binding.viewpager.setClipToPadding(false);
        binding.viewpager.setPadding(20, 0, 80, 0);
    }

    private void showSearchLocation(double latitude, double longitude) {
        showSearchMarker(latitude, longitude);
        float zoom = mGoogleMap.getCameraPosition().zoom;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom);

        mGoogleMap.animateCamera(update);
    }

    private void gotoLocation(double latitude, double longitude) {
        addMarker(latitude, longitude);
        float zoom = mGoogleMap.getCameraPosition().zoom;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom);

        mGoogleMap.animateCamera(update);
    }

    private void addMarker(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_big));

        if (moveMarker != null) {
            moveMarker.remove();
        }

        moveMarker = mGoogleMap.addMarker(markerOptions);

        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    private void showSearchMarker(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_search_location));

        if (moveMarker != null) {
            moveMarker.remove();
        }

        moveMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }


    /* Pager for bottom location card according to map marker */
    private int currentPage;
    private Marker moveMarker;

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            currentPage = position;
            binding.ivMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_around_me_black));
            if (currentPage == 0) {
                binding.viewpager.setPadding(20, 0, 80, 0);
            } else {
                binding.viewpager.setPadding(80, 0, 80, 0);
            }
            ResponseItem item = mapItemList.get(currentPage);
            gotoLocation(item.getLat(), item.getLng());
            Log.e("Selected_page", "Page no =" + position);
        }
    }

    private class MapPagerAdapter extends PagerAdapter implements View.OnClickListener {
        ResponseItem responseItem;

        @Override
        public int getCount() {
            return mapItemList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ListEachRowShopsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                    R.layout.list_each_row_shops, container, false);
            responseItem = mapItemList.get(position);
            Location location = mLastLocation;
            String distance = Utils.getDistance(location, responseItem.getLat(), responseItem.getLng());
            binding.tvDistance.setText(distance);
            binding.tvAddressOne.setText(responseItem.getAddress());
            binding.tvAddressOne.setText(responseItem.getPostcode() + " " + responseItem.getCity());
            binding.tvName.setText(responseItem.getName());

            String currentDay = Utils.getDay();
            String itemDay = "";
            if (!TextUtils.isEmpty(currentDay)) {
                try {
                    if (currentDay.equalsIgnoreCase("1")) {
                        itemDay = responseItem.getSchedules().getDayOne().getSchedule();
                    } else if (currentDay.equalsIgnoreCase("2")) {
                        itemDay = responseItem.getSchedules().getDayTwo().getSchedule();
                    } else if (currentDay.equalsIgnoreCase("3")) {
                        itemDay = responseItem.getSchedules().getDayThree().getSchedule();
                    } else if (currentDay.equalsIgnoreCase("4")) {
                        itemDay = responseItem.getSchedules().getDayFour().getSchedule();
                    } else if (currentDay.equalsIgnoreCase("5")) {
                        itemDay = responseItem.getSchedules().getDayFive().getSchedule();
                    } else if (currentDay.equalsIgnoreCase("6")) {
                        itemDay = responseItem.getSchedules().getDaySix().getSchedule();
                    } else if (currentDay.equalsIgnoreCase("7")) {
                        itemDay = responseItem.getSchedules().getDaySeven().getSchedule();
                    }

                } catch (Exception e) {

                }

            }

            if (!TextUtils.isEmpty(itemDay)) {
                binding.layoutDate.setVisibility(View.VISIBLE);

                binding.tvOpeningTime.setText(itemDay);
            } else {
                binding.layoutDate.setVisibility(View.GONE);
            }

            //  binding.tvOpeningTime.setText(responseItem.ge);
            binding.imgViewPhoneCall.setOnClickListener(this::onClick);
            container.addView(binding.getRoot());
            return binding.getRoot();
        }


        //Roman Code
        private void setTime(ListEachRowShopsBinding binding, ResponseItem responseItem) {
            StringBuilder sb = new StringBuilder();
            List<TodaySchedulesItem> schedulesItems = responseItem.getTodaySchedules();
            int lastIndex = schedulesItems.size() - 1;
            for (int i = lastIndex; i >= 0; i--) {
                TodaySchedulesItem item = schedulesItems.get(i);
                String debTime = item.getTimeDeb();
                String debTimeWithoutSec = debTime.substring(0, debTime.length() - 3);
                String endTime = item.getTimeEnd();
                String endTimeWithoutSec = endTime.substring(0, endTime.length() - 3);
                sb.append(debTimeWithoutSec + "-" + endTimeWithoutSec + "/");
            }
            sb.deleteCharAt(sb.lastIndexOf("/"));
            binding.tvOpeningTime.setText(sb);
        }


        @Override
        public void onClick(View v) {
            showDialogForCallShop(responseItem);
        }
    }

    private void showDialogForCallShop(ResponseItem responseItem) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_dialog_call_shop);

        TextView tvCall = dialog.findViewById(R.id.tvCall);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        TextView tvPhoneNo = dialog.findViewById(R.id.tvPhoneNo);

        tvPhoneNo.setText(responseItem.getPhone());
        tvCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + responseItem.getPhone()));
            getActivity().startActivity(intent);
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
