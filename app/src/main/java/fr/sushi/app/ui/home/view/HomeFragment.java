package fr.sushi.app.ui.home.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.databinding.FramentHomeBinding;
import fr.sushi.app.ui.adressPicker.AdressPickerActivity;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.createaccount.CreateAccountActivity;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.home.data.HomeConfigurationData;
import fr.sushi.app.ui.home.data.HomeSlidesItem;
import fr.sushi.app.ui.home.viewmodel.HomeViewModel;
import fr.sushi.app.util.HandlerUtil;
import fr.sushi.app.util.PicassoUtil;

public class HomeFragment extends BaseFragment {
    private Button buttonAddAddres;
    private BottomSheetDialog dialog;
    private LinearLayout linearLayoutAddress;
    private View viewDivider;
    private final int PALACE_SEARCH_ACTION = 100;
    private FramentHomeBinding binding;
    private HomeViewModel mHomeViewModel;
    private RestuarentsResponse restuarentsResponse;

    private HomeConfigurationData homeConfigurationData;
    private volatile int nextImageIndex = 0;
    private String imageBaseUrl;
    private List<HomeSlidesItem> homeSlidesItemList;

    private  List<SearchPlace> recentSearchPlace;

    private List<CategoriesItem> categoriesItems = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.frament_home;
    }

    @Override
    protected void startUI() {
        binding = (FramentHomeBinding) getViewDataBinding();
        binding.layoutAddress.setOnClickListener(this::onClick);
        binding.addressOne.setOnClickListener(this::onClick);
        binding.addressOneTwo.setOnClickListener(this::onClick);
        observeData();

        initListener();


    }

    @Override
    public void onResume() {
        super.onResume();

        if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
            binding.layoutSignup.setVisibility(View.GONE);
            binding.layoutRecentAddress.setVisibility(View.VISIBLE);
        } else {
            binding.layoutSignup.setVisibility(View.VISIBLE);
            binding.layoutRecentAddress.setVisibility(View.GONE);
        }

        recentSearchPlace = PlaceUtil.getSearchPlace();

        if(!recentSearchPlace.isEmpty()){

            if(recentSearchPlace.size() == 1){
                binding.addressOne.setVisibility(View.VISIBLE);
                binding.addressOneTwo.setVisibility(View.GONE);
                SearchPlace place = recentSearchPlace.get(0);
                binding.recentAddrTv.setText(place.getPostalCode()+" "+place.getCity());
                binding.tvAddresTwo.setText(place.getAddress());

            }else {
                binding.addressOne.setVisibility(View.VISIBLE);
                binding.addressOneTwo.setVisibility(View.VISIBLE);

                SearchPlace place = recentSearchPlace.get(0);
                binding.recentAddrTv.setText(place.getPostalCode()+" "+place.getCity());
                binding.tvAddresTwo.setText(place.getAddress());

                SearchPlace place2 = recentSearchPlace.get(1);
                binding.recentAddrTvTwo.setText(place2.getPostalCode()+" "+place2.getCity());
                binding.tvAddresTwoText.setText(place2.getAddress());

            }
        }else {
            binding.addressOne.setVisibility(View.GONE);
            binding.addressOneTwo.setVisibility(View.GONE);
        }

        boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
        boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
        if (isLivarsion) {
            showImporter();
        } else if (isExporter) {
            showExporter();
        }
    }

    private void initListener() {
        binding.layoutSignup.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                CreateAccountActivity.class)));

        binding.tvDelivery.setOnClickListener(v -> {
            showBottomSheet();
        });
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // findViews(view);
    }

    private void observeData() {

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        mHomeViewModel.getRestuarentListMutableLiveData().observe(this, restuarentsResponse -> {
            this.restuarentsResponse = restuarentsResponse;
            //Log.e("Configdata", "config =" + restuarentsResponse.getResponse().size());
        });

        mHomeViewModel.getHomeConfigLiveData().observe(this, homeConfigResponse -> {
            //Log.e("Configdata", "config =" + homeConfigResponse.getResponse().getImgBaseUrl());
            this.homeConfigurationData = homeConfigResponse;
            this.imageBaseUrl = homeConfigResponse.getResponse().getImgBaseUrl();
            this.homeSlidesItemList = homeConfigResponse.getResponse().getHomeSlides();
            showImageWithDelay();
        });

        mHomeViewModel.getShopList();
        mHomeViewModel.getHomeConfigData();

        mHomeViewModel.getFoodMenuListMutableLiveData().observe(this, foodMenuResponse -> {
            //this.foodMenuResponse = foodMenuResponse;
            categoriesItems = foodMenuResponse.getResponse().getCategories();
        });


    }

    private void showImageWithDelay() {

        if (homeSlidesItemList == null) return;
        String path = imageBaseUrl + homeSlidesItemList.get(nextImageIndex).getPicture();
        //Log.e("Configdata", "path =" + path);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PicassoUtil.loadImage(path, binding.topLayout);
            }
        });

        if (homeSlidesItemList.size() > 1) {
            HandlerUtil.postBackground(delayRunnable, 5000);
        }
    }

    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            nextImageIndex++;
            if (nextImageIndex >= homeSlidesItemList.size()) {
                nextImageIndex = 0;
            }
            showImageWithDelay();

        }
    };


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.layoutAddress:
                startActivityForResult(new Intent(getActivity(), AdressPickerActivity.class), PALACE_SEARCH_ACTION);
                getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);
                break;
            case R.id.addressOne:
                Toast.makeText(getActivity(),"Item 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addressOneTwo:
                Toast.makeText(getActivity(),"Item 2", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }



    private void showBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_liversion_botton_sheet, null);

        LinearLayout liversionView = bottomSheet.findViewById(R.id.livrasion);
        LinearLayout aemporterView = bottomSheet.findViewById(R.id.aemporter);
        liversionView.setOnClickListener(view -> {
            dialog.dismiss();
            showImporter();
            Intent intent = new Intent(getActivity(), AdressPickerActivity.class);
            SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, true);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, false);
            startActivity(intent);

        });

        aemporterView.setOnClickListener(view -> {
            dialog.dismiss();
            showExporter();
            Intent intent = new Intent(getActivity(), AdressPickerActivity.class);
            SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, false);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, true);
            startActivity(intent);
        });

        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showImporter() {
        binding.tvDelivery.setText("Livraison");
        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_delivery);
        Drawable rightImage = getResources().getDrawable(R.drawable.ic_down_arrow);
        binding.tvDelivery.setCompoundDrawablesWithIntrinsicBounds(img, null, rightImage, null);
    }

    private void showExporter() {
        binding.tvDelivery.setText("A emporter");
        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_pickup);
        Drawable rightImage = getResources().getDrawable(R.drawable.ic_down_arrow);
        binding.tvDelivery.setCompoundDrawablesWithIntrinsicBounds(img, null, rightImage, null);
    }

}
