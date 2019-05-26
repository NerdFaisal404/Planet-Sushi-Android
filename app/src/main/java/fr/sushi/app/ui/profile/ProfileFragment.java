package fr.sushi.app.ui.profile;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileItemModel;
import fr.sushi.app.databinding.FragmentProfileBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.base.ItemClickListener;
import fr.sushi.app.ui.carddetails.SusuCardDetailsActivity;
import fr.sushi.app.ui.editprofile.EditProfileActivity;
import fr.sushi.app.ui.main.MainActivity;
import fr.sushi.app.ui.profileaddress.ProfileAddressActivity;

public class ProfileFragment extends BaseFragment implements ItemClickListener<ProfileItemModel> {
    private FragmentProfileBinding mBinding;
    private ProfileAdapter mAdapter;
    String index = "0";

    private String[] itemName = {"Mes informations", "Adresses", /*"Paiement", "Mes commandes",*/ "Fidélité"};
    private int[] itemIcon = {R.drawable.icon_user2x, R.drawable.icon_home2x,/* R.drawable.icon_payment2x,
            R.drawable.icon_order2x,*/ R.drawable.ic_loyality};

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }


    @Override
    protected void startUI() {
        // getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mBinding = (FragmentProfileBinding) getViewDataBinding();

        initRecyclerView();

        String userName = SharedPref.read(PrefKey.USER_NAME, "");

        String totalQuantity = SharedPref.read(PrefKey.FIDELITY_TOTAL_QUANTITY, "");
        String amount = SharedPref.read(PrefKey.FIDELITY_AMOUNT, "");
        String quantity = SharedPref.read(PrefKey.FIDELITY_QUANTITY, "");


        if (TextUtils.isEmpty(totalQuantity)) {
            mBinding.groupTopView.setVisibility(View.GONE);
        } else {
            mBinding.groupTopView.setVisibility(View.VISIBLE);
            mBinding.textViewPoint.setText(quantity);
            mBinding.textViewPointLeft.setText(totalQuantity + " pts manquants");
            showPointValue("150");
        }

        mBinding.textViewName.setText(userName);

        setClickListener(mBinding.imageViewSettings);
    }

    @Override
    protected void stopUI() {

    }


    private void setProgressAnimate(int progressTo) {

        ObjectAnimator animation = ObjectAnimator.ofInt(mBinding.progressBar, "progress", progressTo);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.image_view_settings) {
            SharedPref.write(PrefKey.IS_LOGINED, false);
            ((MainActivity) getActivity()).gotoEmptyProfilePage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
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
        for (int i = 0; i < 3; i++) {
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
        } else if (item.getItemName().equals(itemName[2])) {
            startActivity(new Intent(getActivity(), SusuCardDetailsActivity.class));
        }
    }

    private void showPointValue(String totalPoint) {

        int currentPoint = Integer.parseInt(totalPoint);

        if (currentPoint > 0 && currentPoint < 151) {
            mBinding.imageViewCard.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.card_pink));
            mBinding.tvMember.setText("Membre Pink");
            mBinding.progressBar.setProgressDrawable(getActivity().getResources().getDrawable(R.drawable.drawable_pink_progress));
            mBinding.progressBar.setMax(151);
            setProgressAnimate(currentPoint);
            index ="0";
        } else if (currentPoint > 150 && currentPoint < 451) {
            mBinding.imageViewCard.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.card_gold));
            mBinding.tvMember.setText("Membre Gold");
            mBinding.progressBar.setProgressDrawable(getActivity().getResources().getDrawable(R.drawable.drawable_gold_progress));
            mBinding.progressBar.setMax(451);
            setProgressAnimate(currentPoint);
            index ="1";
        } else if (currentPoint > 450) {
            mBinding.imageViewCard.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.card_black));
            mBinding.tvMember.setText("Membre Black");
            mBinding.progressBar.setProgressDrawable(getActivity().getResources().getDrawable(R.drawable.drawable_black_progress));
            mBinding.progressBar.setMax(100);
            setProgressAnimate(100);
            index ="2";
        }

        mBinding.imageViewCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),SusuCardDetailsActivity.class);
            intent.putExtra(IntentKey.KEY_IS_CARD_POSITION,index);
            startActivity(intent);
        });

        mBinding.textViewProgressDetails.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),SusuCardDetailsActivity.class);
            intent.putExtra(IntentKey.KEY_IS_CARD_POSITION,index);
            startActivity(intent);
        });

    }
}
