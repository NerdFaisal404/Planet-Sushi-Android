package fr.sushi.app.ui.home.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.databinding.AdapterPalceAutoCompleteBinding;
import fr.sushi.app.databinding.FramentHomeBinding;
import fr.sushi.app.databinding.ItemRecentSearchLocationBinding;
import fr.sushi.app.ui.adressPicker.AdressPickerActivity;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.base.BaseViewHolder;
import fr.sushi.app.ui.base.ItemClickListener;
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

    @Override
    protected int getLayoutId() {
        return R.layout.frament_home;
    }

    @Override
    protected void startUI() {
        binding = (FramentHomeBinding) getViewDataBinding();
        binding.layoutAddress.setOnClickListener(this::onClick);
        //  StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 20, null);
        observeData();

        /*boolean isImporterPressed = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
        boolean isLivraison = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);

        if (PlaceUtil.getCurrentSearchPlace() != null && PlaceUtil.getCurrentSearchPlace().getAddress() != null) {
            if (isLivraison) {
                binding.destinationTv.setText("Livraison ou " + PlaceUtil.getCurrentSearchPlace().getAddress());
            } else if (isImporterPressed) {
                binding.destinationTv.setText("À emporter ou " + PlaceUtil.getCurrentSearchPlace().getAddress());
            } else {
                binding.destinationTv.setText(PlaceUtil.getCurrentSearchPlace().getAddress());


            }
        }*/
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

        boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
        boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
        if (isLivarsion) {
            showImporter();
        } else if (isExporter) {
            showExporter();
        }
    }

    private void initListener() {
        binding.layoutSignup.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreateAccountActivity.class)));

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
            Log.e("Configdata", "config =" + restuarentsResponse.getResponse().size());
        });

        mHomeViewModel.getHomeConfigLiveData().observe(this, homeConfigResponse -> {
            Log.e("Configdata", "config =" + homeConfigResponse.getResponse().getImgBaseUrl());
            this.homeConfigurationData = homeConfigResponse;
            this.imageBaseUrl = homeConfigResponse.getResponse().getImgBaseUrl();
            this.homeSlidesItemList = homeConfigResponse.getResponse().getHomeSlides();
            showImageWithDelay();
        });

        mHomeViewModel.getShopList();
        mHomeViewModel.getHomeConfigData();


    }

    private void showImageWithDelay() {

        if (homeSlidesItemList == null) return;
        String path = imageBaseUrl + homeSlidesItemList.get(nextImageIndex).getPicture();
        Log.e("Configdata", "path =" + path);
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


  /*  void findViews(View view) {
        LinearLayout linearLayoutLivrasion = view.findViewById(R.id.linearLayoutLivrasion);
        linearLayoutLivrasion.setOnClickListener(this);

    }*/

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.layoutAddress:
                startActivityForResult(new Intent(getActivity(), AdressPickerActivity.class), PALACE_SEARCH_ACTION);
                getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);
                break;

           /* case R.id.linearLayoutLivrasion:
                if (PlaceUtil.getCurrentSearchPlace() != null && PlaceUtil.getCurrentSearchPlace().getAddress() != null) {
                    showBottomDialog(true);
                } else {
                    showBottomDialog(false);
                }
                break;

            case R.id.radioButtonEmporter:
                SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, true);
                SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, false);
                viewDivider.setVisibility(View.VISIBLE);
                buttonAddAddres.setVisibility(View.VISIBLE);
                break;

            case R.id.radioButtonLivraison:
                SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, false);
                SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, true);
                viewDivider.setVisibility(View.VISIBLE);
                buttonAddAddres.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonAddAddres:
                dialog.dismiss();
                startActivityForResult(new Intent(getActivity(), AdressPickerActivity.class), PALACE_SEARCH_ACTION);
                getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);

                break;

            case R.id.textViewModifier:
                dialog.dismiss();
                boolean isImporterPressed = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
                boolean isLivraison = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);

                if (isLivraison) {
                    showSavedAddressBottomSheet();
                } else if (isImporterPressed) {
                    showAddressBottomSheet();
                }

                break;*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == PALACE_SEARCH_ACTION) {
            if (data != null) {
                SearchPlace searchPlace = data.getParcelableExtra(SearchPlace.class.getName());
                boolean isImporterPressed = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
                boolean isLivraison = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);

                if (isLivraison) {
                    binding.destinationTv.setText("Livraison ou " + searchPlace.getAddress());
                } else if (isImporterPressed) {
                    binding.destinationTv.setText("À emporter ou " + searchPlace.getAddress());
                }
                PlaceUtil.saveCurrentPlace(searchPlace);
            }

        }
        showBottomDialog(true);*/
    }

    void showBottomDialog(boolean showAddressLayout) {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_bottom_sheet_pickup_delivery, null);
        RadioButton radioButtonLivraison = bottomSheet.findViewById(R.id.radioButtonLivraison);
        RadioButton radioButtonEmporter = bottomSheet.findViewById(R.id.radioButtonEmporter);
        buttonAddAddres = bottomSheet.findViewById(R.id.buttonAddAddres);
        TextView textViewModifier = bottomSheet.findViewById(R.id.textViewModifier);
        TextView recentAddress = bottomSheet.findViewById(R.id.recent_addr_tv);
        viewDivider = bottomSheet.findViewById(R.id.view_divider);
        textViewModifier.setOnClickListener(this);
        linearLayoutAddress = bottomSheet.findViewById(R.id.linearLayoutAddress);
        if (showAddressLayout) {

            viewDivider.setVisibility(View.VISIBLE);
            boolean isImporterPressed = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
            boolean isLivraison = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);

            if (isLivraison) {
                radioButtonLivraison.setChecked(true);
                radioButtonEmporter.setChecked(false);
            } else if (isImporterPressed) {
                radioButtonLivraison.setChecked(false);
                radioButtonEmporter.setChecked(true);
            }

            linearLayoutAddress.setVisibility(View.VISIBLE);
            buttonAddAddres.setVisibility(View.VISIBLE);
            buttonAddAddres.setText("VALIDER");
            recentAddress.setText(PlaceUtil.getCurrentSearchPlace().getAddress());
        } else {
            linearLayoutAddress.setVisibility(View.GONE);
        }

        radioButtonLivraison.setOnClickListener(this);
        radioButtonEmporter.setOnClickListener(this);
        buttonAddAddres.setOnClickListener(this);
        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    void showSavedAddressBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_recent_search_address_list, null);
        RecyclerView recyclerView = bottomSheet.findViewById(R.id.address_rv);
        TextView tvSearchAddress = bottomSheet.findViewById(R.id.tvNewAddress);
        tvSearchAddress.setOnClickListener(v -> {
            dialog.dismiss();
            startActivityForResult(new Intent(getActivity(), AdressPickerActivity.class), PALACE_SEARCH_ACTION);
            getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);
        });
        SavedAddressAdapter addressAdapter = new SavedAddressAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(addressAdapter);

        List<SearchPlace> searchPlaces = PlaceUtil.getSavedPlaces();

        addressAdapter.addItem(searchPlaces);
        addressAdapter.setItemClickListener(itemClickListener);
        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    void showAddressBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_address_list, null);
        RecyclerView recyclerView = bottomSheet.findViewById(R.id.address_rv);
        TextView tvClose = bottomSheet.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(v -> dialog.dismiss());
        ShopAddressAdapter addressAdapter = new ShopAddressAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(addressAdapter);

        List<ResponseItem> itemList = restuarentsResponse.getResponse();

        addressAdapter.addItem(itemList);
        addressAdapter.setItemClickListener(itemClickListener);
        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    private ItemClickListener<SearchPlace> itemClickListener = new ItemClickListener<SearchPlace>() {
        @Override
        public void onItemClick(View view, SearchPlace item) {
            dialog.dismiss();
            binding.destinationTv.setText(item.getAddress());
            PlaceUtil.saveCurrentPlace(item);
        }
    };

    public static class SavedAddressAdapter extends BaseAdapter<SearchPlace> {

        @Override
        public boolean isEqual(SearchPlace left, SearchPlace right) {
            return false;
        }

        @Override
        public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
            return new PlaceHolder(inflate(parent, R.layout.item_recent_search_location));
        }

        private class PlaceHolder extends BaseViewHolder<SearchPlace> {
            private ItemRecentSearchLocationBinding binding;

            public PlaceHolder(ViewDataBinding viewDataBinding) {
                super(viewDataBinding);
                binding = (ItemRecentSearchLocationBinding) viewDataBinding;
                binding.recentAddrTv.setOnClickListener(this);
            }

            @Override
            public void bind(SearchPlace item) {
                if (item.getAddress().equalsIgnoreCase(PlaceUtil.getCurrentSearchPlace().getAddress())) {
                    binding.imageViewTick.setVisibility(View.VISIBLE);
                } else {
                    binding.imageViewTick.setVisibility(View.GONE);
                }
                binding.recentAddrTv.setText(item.getAddress());
            }

            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, getItem(getAdapterPosition()));
            }
        }
    }


    public static class ShopAddressAdapter extends BaseAdapter<ResponseItem> {

        @Override
        public boolean isEqual(ResponseItem left, ResponseItem right) {
            return false;
        }

        @Override
        public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
            return new PlaceHolder(inflate(parent, R.layout.adapter_palce_auto_complete));
        }

        private class PlaceHolder extends BaseViewHolder<ResponseItem> {
            private AdapterPalceAutoCompleteBinding binding;

            public PlaceHolder(ViewDataBinding viewDataBinding) {
                super(viewDataBinding);
                binding = (AdapterPalceAutoCompleteBinding) viewDataBinding;
                binding.address.setOnClickListener(this);
            }

            @Override
            public void bind(ResponseItem item) {
                binding.address.setText(item.getAddress());
            }

            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, getItem(getAdapterPosition()));
            }
        }
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
