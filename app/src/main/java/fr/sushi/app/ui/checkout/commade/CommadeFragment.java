package fr.sushi.app.ui.checkout.commade;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.databinding.FragmentCommadeBinding;
import fr.sushi.app.ui.checkout.PaymentMethodCheckoutActivity;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem;
import fr.sushi.app.util.Utils;
import fr.sushi.app.util.swipanim.ItemTouchHelperExtension;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommadeFragment extends Fragment implements CommadeAdapter.Listener {
    private FragmentCommadeBinding binding;
    private List<MyCartProduct> selectedProducts = new ArrayList<>();
    private LinearLayoutManager itemViewLayoutManager;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;
    private CommadeViewModel commadeViewModel;
    private double totalPrice;


    public CommadeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commade, container, false);
        View view = binding.getRoot();
        initView();
        observeData();

        return view;
    }

    private void observeData() {

        commadeViewModel = ViewModelProviders.of(this).get(CommadeViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        int minimumPrice = (int) this.totalPrice;
        if (!SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false) && minimumPrice < 25) {
            binding.tvMinAmount.setVisibility(View.VISIBLE);
        } else {
            binding.tvMinAmount.setVisibility(View.GONE);
        }
    }

    CommadeAdapter commadeAdapter;

    private void initView() {

        //selectedProducts = MenuPrefUtil.getSaveItems();
        selectedProducts = DBManager.on().getAllProducts();
        itemViewLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvCartItem.setLayoutManager(itemViewLayoutManager);

        commadeAdapter = new CommadeAdapter(getActivity(), selectedProducts, this);
        mCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(binding.rvCartItem);
        commadeAdapter.setItemTouchHelperExtension(mItemTouchHelper);
        binding.rvCartItem.setAdapter(commadeAdapter);

        setTotalPrice();
    }

    private void setTotalPrice() {

        totalPrice = 0.0;

        for (MyCartProduct item : selectedProducts) {
            totalPrice = totalPrice + (Double.parseDouble(item.getPriceHt()) * item.getItemCount());
        }

        List<CrossSellingSelectedItem> sellingSelectedItems = DBManager.on().getAllCrossSellingItems();

        Log.e("Side_products", "Products count =" + sellingSelectedItems.size());
        for (CrossSellingSelectedItem item : sellingSelectedItems) {
            if (!item.isFree()) {
                totalPrice = totalPrice + Double.valueOf(item.getProductPrice());
            }
        }

        binding.totalPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
        binding.deliveryFeeTv.setText("0.95€");
        totalPrice = totalPrice + 0.95;

        binding.totalPriceWithFee.setText(Utils.getDecimalFormat(totalPrice) + "€");

        ((PaymentMethodCheckoutActivity) getActivity()).setTotalPrice(totalPrice);
    }

    @Override
    public void onItemDeselect(MyCartProduct item, int index) {
        selectedProducts.remove(index);
        commadeAdapter.notifyDataSetChanged();
        DBManager.on().removeProduct(item);
        DBManager.on().deleteSelectedItemById(item.getProductId());
        setTotalPrice();
        //MenuPrefUtil.removeItem(item);
        if (selectedProducts.size() == 0) {
            getActivity().finish();
        }
    }
}
