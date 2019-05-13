package fr.sushi.app.ui.profile;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileItemModel;
import fr.sushi.app.databinding.FragmentProfileBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.base.ItemClickListener;
import fr.sushi.app.ui.editprofile.EditProfileActivity;
import fr.sushi.app.ui.main.MainActivity;
import fr.sushi.app.ui.profileaddress.ProfileAddressActivity;

public class ProfileFragment extends BaseFragment implements ItemClickListener<ProfileItemModel> {
    private FragmentProfileBinding mBinding;
    private ProfileAdapter mAdapter;

    private String[] itemName = {"Mes informations", "Mes adresses", "Paiement", "Mes commandes", "Fidélité"};
    private int[] itemIcon = {R.drawable.icon_user2x, R.drawable.icon_home2x, R.drawable.icon_payment2x,
            R.drawable.icon_order2x, R.drawable.icon_loyalty2x};

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }


    @Override
    protected void startUI() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mBinding = (FragmentProfileBinding) getViewDataBinding();

        initRecyclerView();

        String userName = SharedPref.read(PrefKey.USER_NAME, "");

        mBinding.textViewName.setText(userName);

        setClickListener(mBinding.imageViewSettings);

    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.image_view_settings) {
            SharedPref.write(PrefKey.IS_LOGINED, false);
            ((MainActivity) getActivity()).gotoEmptyProfilePage();
        }
    }

    private void initRecyclerView() {
        mBinding.recyclerViewProfile.setHasFixedSize(true);
        mBinding.recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProfileAdapter();
        mAdapter.setItemClickListener(this);
        mBinding.recyclerViewProfile.setAdapter(mAdapter);

        List<ProfileItemModel> itemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            itemList.add(new ProfileItemModel(itemName[i], itemIcon[i]));
        }
        mAdapter.clear();
        mAdapter.addItems(itemList);
    }

    @Override
    public void onItemClick(View view, ProfileItemModel item) {
        if (item.getItemName().equals(itemName[0])) {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        } else if (item.getItemName().equals(itemName[1])) {
            startActivity(new Intent(getActivity(), ProfileAddressActivity.class));
        }
    }
}
