package fr.sushi.app.ui.checkout.accompagnements;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sushi.app.R;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.FragmentAccompagnementsBinding;
import fr.sushi.app.ui.base.ItemClickListener;
import fr.sushi.app.ui.checkout.PaymentMethodCheckoutActivity;
import fr.sushi.app.ui.checkout.adapter.AccomplishmentAdapter;
import fr.sushi.app.ui.checkout.adapter.BaguettesAdapter;
import fr.sushi.app.ui.checkout.adapter.BoissonAdapter;
import fr.sushi.app.ui.checkout.adapter.DessertAdapter;
import fr.sushi.app.ui.checkout.adapter.SaucesAdapter;
import fr.sushi.app.ui.checkout.adapter.WasbiGingerAdapter;
import fr.sushi.app.ui.checkout.commade.model.AccompagnementResponse;
import fr.sushi.app.ui.checkout.commade.model.ChopsticksItem;
import fr.sushi.app.ui.checkout.commade.model.DessertsItem;
import fr.sushi.app.ui.checkout.commade.model.DrinksItem;
import fr.sushi.app.ui.checkout.commade.model.FreeSaucesItem;
import fr.sushi.app.ui.checkout.commade.model.FreeWasabiGingerItem;
import fr.sushi.app.ui.checkout.commade.model.PayingSaucesItem;
import fr.sushi.app.ui.checkout.commade.model.PayingWasabiGingerItem;
import fr.sushi.app.ui.checkout.commade.model.Sauces;
import fr.sushi.app.ui.checkout.commade.model.UpsellItem;
import fr.sushi.app.ui.checkout.commade.model.Wasbi;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.SideProduct;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompagnementsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AccompagnementsFragment.class.getSimpleName();
    private AccompagnementViewModel accompagnementViewModel;
    private RecyclerView recycler_view_accompagnements;
    private AccompagnementsAdapter adapter;
    private RelativeLayout rlSauces, rlAccompagnements, rlBoissons, rlDesserts, rlWasbi, rlBaguettes;
    private RelativeLayout rlCountForSauces, rlCountForAccompagnements, rlCountForBoissons, rlCountForDesserts, rlCountForWasbi, rlCountForBaguettes;
    private TextView tvCountSauces, tvCountAccompagnements, tvCountBoissons, tvCountDesserts, tvCountWasbi, tvCountBaguettes;
    private TextView tvSauces, tvAccompagnements, tvBoissons, tvDesserts, tvWasbi, tvBaguettes;
    private FragmentAccompagnementsBinding binding;

    private int selectedAccompagnements;
    private static final int SAUCES = 1;
    private static final int ACCOMPAGNEMENTS = 2;
    private static final int BOISSONS = 3;
    private static final int DESSERTS = 4;
    private static final int WASBI = 5;
    private static final int BAGUETTES = 6;
    private int countSauces = 0, countAccompagnements = 0, countBoissons = 0, countDesserts = 0, countWasbi = 0, countBauettes = 0;

    private AccompagnementResponse accompagnementResponse;

    private int freeSideProductCount;

    private static AccompagnementsFragment accompagnementsFragment;

    public static AccompagnementsFragment on() {
        if (accompagnementsFragment == null) {
            accompagnementsFragment = new AccompagnementsFragment();
        }
        return accompagnementsFragment;
    }

    /*private AccompagnementsFragment() {
        // Required empty public constructor
    }*/


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


        rlAccompagnements = view.findViewById(R.id.rlAccompagnements);
        rlAccompagnements.setOnClickListener(this);
        rlCountForAccompagnements = view.findViewById(R.id.rlCountForAccompagnements);
        tvCountAccompagnements = view.findViewById(R.id.tvCountAccompagnements);
        tvAccompagnements = view.findViewById(R.id.tvAccompagnements);

        rlBoissons = view.findViewById(R.id.rlBoissons);
        rlBoissons.setOnClickListener(this);
        rlCountForBoissons = view.findViewById(R.id.rlCountForBoissons);
        tvCountBoissons = view.findViewById(R.id.tvCountBoissons);
        tvBoissons = view.findViewById(R.id.tvBoissons);

        rlDesserts = view.findViewById(R.id.rlDesserts);
        rlDesserts.setOnClickListener(this);
        rlCountForDesserts = view.findViewById(R.id.rlCountForDesserts);
        tvCountDesserts = view.findViewById(R.id.tvCountDesserts);
        tvDesserts = view.findViewById(R.id.tvDesserts);

        rlWasbi = view.findViewById(R.id.rlWasbi);
        rlWasbi.setOnClickListener(this);
        rlCountForWasbi = view.findViewById(R.id.rlCountForWasbi);
        tvCountWasbi = view.findViewById(R.id.tvCountWasbi);
        tvWasbi = view.findViewById(R.id.tvWasbi);

        rlBaguettes = view.findViewById(R.id.rlBaguettes);
        rlBaguettes.setOnClickListener(this);
        rlCountForBaguettes = view.findViewById(R.id.rlCountForBaguettes);
        tvCountBaguettes = view.findViewById(R.id.tvCountBaguettes);
        tvBaguettes = view.findViewById(R.id.tvBaguettes);
        freeSideProductCount = ((PaymentMethodCheckoutActivity) getActivity()).getFreeSaucesCount();
        String items = "" + freeSideProductCount;

        String subTitle = "Choisissez vos <font color=\"#EA148A\">" + items + " sauce(s) gratuite(s),</font>" + " wasabi, gingembre et baguettes.\n Compléter votre commande d'un accompagnement, d'une boisson ou d'un dessert. Sauce(s) offertes";


        binding.tvSubtitle.setText(Html.fromHtml(subTitle), TextView.BufferType.SPANNABLE);
        recycler_view_accompagnements = view.findViewById(R.id.recycler_view_accompagnements);

        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );


    }

    @Override
    public void onResume() {
        super.onResume();
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
                    ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                } else {
                    accompagnementResponse = new Gson().fromJson(responseObject.toString(), AccompagnementResponse.class);

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
        switch (view.getId()) {
            case R.id.rlSauces:
                handleSauce();
                showSaucesData();
                break;
            case R.id.rlAccompagnements:
                handleAccompanements();
                showAccomplishmentData();
                break;
            case R.id.rlBoissons:
                handleBoissons();
                showBoissonData();
                break;
            case R.id.rlDesserts:
                handleDesserts();
                showDessertData();
                break;
            case R.id.rlWasbi:
                handleWasbi();
                showWasibData();
                break;
            case R.id.rlBaguettes:
                handleBaguettes();
                showBaguettesData();
                break;

        }
    }

    SaucesAdapter saucesAdapter;
    WasbiGingerAdapter wasbiGingerAdapter;

    private void showSaucesData() {
        if (accompagnementResponse == null) return;
        binding.recyclerViewAccompagnements.setVisibility(View.VISIBLE);
        saucesAdapter = new SaucesAdapter(getActivity());
        binding.recyclerViewAccompagnements.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewAccompagnements.setAdapter(saucesAdapter);
        List<FreeSaucesItem> payingSaucesItemList =
                new ArrayList<>(accompagnementResponse.getResponse().getFreeSauces());
        List<Sauces> convertedList = new ArrayList<Sauces>(payingSaucesItemList);
        saucesAdapter.addItem(convertedList);
        saucesAdapter.setItemClickListener(saucesItemClickListener);
    }

    private void showAccomplishmentData() {
        if (accompagnementResponse == null) return;
        binding.recyclerViewAccompagnements.setVisibility(View.VISIBLE);
        AccomplishmentAdapter accomplishmentAdapter = new AccomplishmentAdapter(getActivity());
        binding.recyclerViewAccompagnements.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewAccompagnements.setAdapter(accomplishmentAdapter);
        List<UpsellItem> upsellItems = new ArrayList<>(accompagnementResponse.getResponse().getUpsell());
        accomplishmentAdapter.addItem(upsellItems);
        accomplishmentAdapter.setItemClickListener(accomplishmentClickListener);
    }

    private void showBoissonData() {
        if (accompagnementResponse == null) return;
        binding.recyclerViewAccompagnements.setVisibility(View.VISIBLE);
        BoissonAdapter boissonAdapter = new BoissonAdapter(getActivity());
        binding.recyclerViewAccompagnements.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewAccompagnements.setAdapter(boissonAdapter);
        List<DrinksItem> drinksItems = new ArrayList<>(accompagnementResponse.getResponse().getDrinks());
        boissonAdapter.addItem(drinksItems);
        boissonAdapter.setItemClickListener(boissonItemClickListener);
    }

    private void showDessertData() {
        if (accompagnementResponse == null) return;
        DessertAdapter dessertAdapter = new DessertAdapter(getActivity());
        binding.recyclerViewAccompagnements.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewAccompagnements.setAdapter(dessertAdapter);
        List<DessertsItem> dessertsItems = new ArrayList<>(accompagnementResponse.getResponse().getDesserts());
        dessertAdapter.addItem(dessertsItems);
        dessertAdapter.setItemClickListener(dessertItemClickListener);
    }

    private void showWasibData() {
        if (accompagnementResponse == null) return;
        binding.recyclerViewAccompagnements.setVisibility(View.VISIBLE);
        wasbiGingerAdapter = new WasbiGingerAdapter(getActivity());
        binding.recyclerViewAccompagnements.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewAccompagnements.setAdapter(wasbiGingerAdapter);
        List<FreeWasabiGingerItem> wasbiList =
                new ArrayList<>(accompagnementResponse.getResponse().getFreeWasabiGinger());
        List<Wasbi> convertedList = new ArrayList<>(wasbiList);
        wasbiGingerAdapter.addItem(convertedList);


        wasbiGingerAdapter.setItemClickListener(wasbiItemClickListener);
    }

    private void showBaguettesData() {
        if (accompagnementResponse == null) return;
        binding.recyclerViewAccompagnements.setVisibility(View.VISIBLE);
        BaguettesAdapter baguettesAdapter = new BaguettesAdapter(getActivity());
        binding.recyclerViewAccompagnements.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewAccompagnements.setAdapter(baguettesAdapter);
        List<ChopsticksItem> chopsticksItems = new ArrayList<>(accompagnementResponse.getResponse().getChopsticks());
        baguettesAdapter.addItem(chopsticksItems);
        baguettesAdapter.setItemClickListener(baguettesItemClickListener);
    }

    private List<PayingSaucesItem> payingSaucesItems = new ArrayList<>();
    private List<FreeSaucesItem> freeSaucesItemList = new ArrayList<>();
    private List<UpsellItem> accomplishmentitemList = new ArrayList<>();
    private List<DrinksItem> boissonItemList = new ArrayList<>();
    private List<DessertsItem> dessertItemList = new ArrayList<>();
    private List<PayingWasabiGingerItem> payingWasbiItemClicList = new ArrayList<>();
    private List<FreeWasabiGingerItem> freeWasbiItemClicList = new ArrayList<>();
    private List<ChopsticksItem> baguettesItemList = new ArrayList<>();

    private Map<String, FreeSaucesItem> freeSaucesMap = new HashMap<>();
    private Map<String, PayingSaucesItem> payingSaucesMap = new HashMap<>();
    private Map<String, UpsellItem> accomplishmentitemMap = new HashMap<>();
    private Map<String, DrinksItem> boissonItemMap = new HashMap<>();
    private Map<String, DessertsItem> dessertItemMap = new HashMap<>();
    private Map<String, PayingWasabiGingerItem> payingWasbiItemClicMap = new HashMap<>();
    private Map<String, FreeWasabiGingerItem> freeWasbiItemClicMap = new HashMap<>();
    private Map<String, ChopsticksItem> baguettesItemMap = new HashMap<>();

    private Map<Integer, Integer> freeSauces = new HashMap<>();
    private Map<Integer, Integer> freeWasbi = new HashMap<>();

    private ItemClickListener<Sauces> saucesItemClickListener = new ItemClickListener<Sauces>() {

        @Override
        public void onItemClick(View view, Sauces item, int position) {

            if (view.getId() == R.id.imgViewPlus) {
                countSauces++;
                if (item instanceof FreeSaucesItem) {
                    FreeSaucesItem fItem = (FreeSaucesItem) item;

                    fItem.selectCount = fItem.selectCount + 1;
                    freeSaucesItemList.add(fItem);
                    freeSaucesMap.put(fItem.getIdProduct(), fItem);

                    saucesAdapter.notifyDataSetChanged();
                    freeSauces.put(position, fItem.selectCount);

                    if (freeSaucesItemList.size() >= freeSideProductCount) {
                        //load paid item
                        List<PayingSaucesItem> list = new ArrayList<>(accompagnementResponse.getResponse().getPayingSauces());

                        for (Map.Entry<Integer, Integer> mapInt : freeSauces.entrySet()) {
                            int kay = mapInt.getKey();
                            int value = mapInt.getValue();
                            PayingSaucesItem saucesItem = list.get(kay);
                            saucesItem.selectCount = value;
                        }
                        List<Sauces> convertedList = new ArrayList<Sauces>(list);
                        saucesAdapter.addPaidItems(convertedList);

                    }
                } else {
                    PayingSaucesItem pItem = (PayingSaucesItem) item;
                    pItem.selectCount = pItem.selectCount + 1;
                    payingSaucesItems.add((PayingSaucesItem) item);
                    payingSaucesMap.put(pItem.getIdProduct(), pItem);

                    saucesAdapter.notifyDataSetChanged();
                    freeSauces.put(position, pItem.selectCount);
                }
            } else {
                countSauces--;
                if (item instanceof FreeSaucesItem) {
                    FreeSaucesItem fItem = (FreeSaucesItem) item;
                    freeSaucesItemList.remove(fItem);
                    fItem.selectCount = fItem.selectCount - 1;
                    if (fItem.selectCount == 0) {
                        freeSaucesMap.remove(fItem.getIdProduct());
                    } else {
                        freeSaucesMap.put(fItem.getIdProduct(), fItem);
                    }

                    saucesAdapter.notifyDataSetChanged();
                    freeSauces.put(position, fItem.selectCount);
                } else {
                    PayingSaucesItem pItem = (PayingSaucesItem) item;
                    pItem.selectCount = pItem.selectCount - 1;
                    saucesAdapter.notifyDataSetChanged();
                    freeSauces.put(position, pItem.selectCount);

                    if (payingSaucesItems.isEmpty()) {
                        //load free item
                        List<FreeSaucesItem> list = new ArrayList<>(accompagnementResponse.getResponse().getFreeSauces());
                        for (Map.Entry<Integer, Integer> mapInt : freeSauces.entrySet()) {
                            int kay = mapInt.getKey();
                            int value = mapInt.getValue();
                            FreeSaucesItem saucesItem = list.get(kay);
                            saucesItem.selectCount = value;
                        }
                        List<Sauces> convertedList = new ArrayList<Sauces>(list);
                        saucesAdapter.addPaidItems(convertedList);
                        FreeSaucesItem freeItem = freeSaucesItemList.remove(0);

                        if (freeItem.selectCount == 0) {
                            freeSaucesMap.remove(freeItem.getIdProduct());
                        } else {
                            freeItem.selectCount = freeItem.selectCount - 1;
                            freeSaucesMap.put(freeItem.getIdProduct(), freeItem);
                        }
                    } else {
                        payingSaucesItems.remove(pItem);

                        if (pItem.selectCount == 0) {
                            payingSaucesMap.remove(pItem.getIdProduct());
                        } else {
                            payingSaucesMap.put(pItem.getIdProduct(), pItem);
                        }
                    }
                }
            }

            binding.tvCountSauces.setText(String.valueOf(countSauces));
            if (countSauces > 0) {
                binding.rlCountForSauces.setVisibility(View.VISIBLE);
            } else {
                binding.rlCountForSauces.setVisibility(View.GONE);
            }
            calculatePrice();
        }


        @Override
        public void onItemClick(View view, Sauces item) {
        }
    };

    private ItemClickListener<Wasbi> wasbiItemClickListener = new ItemClickListener<Wasbi>() {


        @Override
        public void onItemClick(View view, Wasbi item, int position) {

            if (view.getId() == R.id.imgViewPlus) {
                countWasbi++;
                if (item instanceof FreeWasabiGingerItem) {
                    FreeWasabiGingerItem fItem = (FreeWasabiGingerItem) item;

                    fItem.selectCount = fItem.selectCount + 1;
                    freeWasbiItemClicList.add(fItem);
                    freeWasbiItemClicMap.put(fItem.getIdProduct(), fItem);
                    wasbiGingerAdapter.notifyDataSetChanged();
                    freeWasbi.put(position, fItem.selectCount);

                    if (freeWasbiItemClicList.size() >= freeSideProductCount) {
                        //load paid item
                        List<PayingWasabiGingerItem> list = new ArrayList<>(accompagnementResponse.getResponse().getPayingWasabiGinger());

                        for (Map.Entry<Integer, Integer> mapInt : freeWasbi.entrySet()) {
                            int kay = mapInt.getKey();
                            int value = mapInt.getValue();
                            PayingWasabiGingerItem saucesItem = list.get(kay);
                            saucesItem.selectCount = value;
                        }
                        List<Wasbi> convertedList = new ArrayList<Wasbi>(list);
                        wasbiGingerAdapter.addPaidItems(convertedList);

                    }
                } else {
                    PayingWasabiGingerItem pItem = (PayingWasabiGingerItem) item;
                    pItem.selectCount = pItem.selectCount + 1;
                    payingWasbiItemClicList.add((PayingWasabiGingerItem) item);
                    payingWasbiItemClicMap.put(pItem.getIdProduct(), pItem);
                    wasbiGingerAdapter.notifyDataSetChanged();
                    freeWasbi.put(position, pItem.selectCount);
                }
            } else {
                countWasbi--;
                if (item instanceof FreeWasabiGingerItem) {
                    FreeWasabiGingerItem fItem = (FreeWasabiGingerItem) item;

                    freeWasbiItemClicList.remove(fItem);

                    fItem.selectCount = fItem.selectCount - 1;

                    if (fItem.selectCount == 0) {
                        freeWasbiItemClicMap.remove(fItem.getIdProduct());
                    } else {
                        freeWasbiItemClicMap.put(fItem.getIdProduct(), fItem);
                    }
                    wasbiGingerAdapter.notifyDataSetChanged();
                    freeWasbi.put(position, fItem.selectCount);
                } else {
                    PayingWasabiGingerItem pItem = (PayingWasabiGingerItem) item;
                    pItem.selectCount = pItem.selectCount - 1;

                    wasbiGingerAdapter.notifyDataSetChanged();
                    freeWasbi.put(position, pItem.selectCount);

                    if (payingWasbiItemClicList.isEmpty()) {
                        //load free item
                        List<FreeWasabiGingerItem> list =
                                new ArrayList<>(accompagnementResponse.getResponse().getFreeWasabiGinger());
                        for (Map.Entry<Integer, Integer> mapInt : freeWasbi.entrySet()) {
                            int kay = mapInt.getKey();
                            int value = mapInt.getValue();
                            FreeWasabiGingerItem saucesItem = list.get(kay);
                            saucesItem.selectCount = value;
                        }
                        List<Wasbi> convertedList = new ArrayList<Wasbi>(list);
                        wasbiGingerAdapter.addPaidItems(convertedList);
                        FreeWasabiGingerItem freeItem = freeWasbiItemClicList.remove(0);

                        freeItem.selectCount = freeItem.selectCount - 1;

                        if (freeItem.selectCount <= 0) {
                            freeWasbiItemClicMap.remove(freeItem.getIdProduct());
                        } else {
                            freeWasbiItemClicMap.put(freeItem.getIdProduct(), freeItem);
                        }

                    } else {
                        payingWasbiItemClicList.remove(pItem);
                        if (pItem.selectCount <= 0) {
                            payingWasbiItemClicMap.remove(pItem.getIdProduct());
                        } else {
                            payingWasbiItemClicMap.put(pItem.getIdProduct(), pItem);
                        }
                    }
                }
            }

            binding.tvCountWasbi.setText(String.valueOf(countWasbi));
            if (countWasbi > 0) {
                binding.rlCountForWasbi.setVisibility(View.VISIBLE);
            } else {
                binding.rlCountForWasbi.setVisibility(View.GONE);
            }
            calculatePrice();
        }

        @Override
        public void onItemClick(View view, Wasbi item) {

        }
    };

    private ItemClickListener<UpsellItem> accomplishmentClickListener = (view, item) -> {
        if (view.getId() == R.id.imgViewPlus) {
            countAccompagnements += 1;
            accomplishmentitemList.add(item);
            accomplishmentitemMap.put(item.getIdProduct(), item);

        } else {
            if (countAccompagnements > 0) {
                countAccompagnements -= 1;
                accomplishmentitemList.remove(0);
                if (item.selectCount == 0) {
                    accomplishmentitemMap.remove(item.getIdProduct());
                } else {
                    accomplishmentitemMap.put(item.getIdProduct(), item);
                }

            }
        }

        binding.tvCountAccompagnements.setText(String.valueOf(countAccompagnements));
        if (countAccompagnements > 0) {
            binding.rlCountForAccompagnements.setVisibility(View.VISIBLE);
        } else {
            binding.rlCountForAccompagnements.setVisibility(View.GONE);
        }
        calculatePrice();
    };

    private ItemClickListener<DrinksItem> boissonItemClickListener = (view, item) -> {
        if (view.getId() == R.id.imgViewPlus) {
            countBoissons += 1;
            boissonItemList.add(item);
            boissonItemMap.put(item.getIdProduct(), item);
        } else {
            if (countBoissons > 0) {
                countBoissons -= 1;
                boissonItemList.remove(0);
                if (item.selectCount == 0) {
                    boissonItemMap.remove(item.getIdProduct());
                } else {
                    boissonItemMap.put(item.getIdProduct(), item);
                }
            }
        }
        binding.tvCountBoissons.setText(String.valueOf(countBoissons));
        if (countBoissons > 0) {
            binding.rlCountForBoissons.setVisibility(View.VISIBLE);
        } else {
            binding.rlCountForBoissons.setVisibility(View.GONE);
        }
        calculatePrice();
    };

    private ItemClickListener<DessertsItem> dessertItemClickListener = (view, item) -> {
        if (view.getId() == R.id.imgViewPlus) {
            countDesserts += 1;
            dessertItemList.add(item);
            dessertItemMap.put(item.getIdProduct(), item);
        } else {
            if (countDesserts > 0) {
                countDesserts -= 1;
                dessertItemList.remove(0);
                if (item.selectCount == 0) {
                    dessertItemMap.remove(item.getIdProduct());
                } else {
                    dessertItemMap.put(item.getIdProduct(), item);
                }
            }
        }

        binding.tvCountDesserts.setText(String.valueOf(countDesserts));
        if (countDesserts > 0) {
            binding.rlCountForDesserts.setVisibility(View.VISIBLE);
        } else {
            binding.rlCountForDesserts.setVisibility(View.GONE);
        }
        calculatePrice();
    };


    private ItemClickListener<ChopsticksItem> baguettesItemClickListener = (view, item) -> {
        if (view.getId() == R.id.imgViewPlus) {
            countBauettes += 1;
            baguettesItemList.add(item);
            baguettesItemMap.put(item.getIdProduct(), item);
        } else {
            if (countBauettes > 0) {
                countBauettes -= 1;
                baguettesItemList.remove(0);
                if (item.selectCount == 0) {
                    baguettesItemMap.remove(item.getIdProduct());
                } else {
                    baguettesItemMap.put(item.getIdProduct(), item);
                }
            }
        }
        tvCountBaguettes.setText(String.valueOf(countBauettes));
        if (countBauettes > 0) {
            rlCountForBaguettes.setVisibility(View.VISIBLE);
        } else {
            rlCountForBaguettes.setVisibility(View.GONE);
        }
        calculatePrice();
    };

    private void calculatePrice() {
        double priceSideProducts = 0.0;

        SideProduct sideProduct = null;
        List<SideProduct> sideProducts = new ArrayList<>();

        for (Map.Entry<String, PayingSaucesItem> item : payingSaucesMap.entrySet()) {
            PayingSaucesItem payingSaucesItem = item.getValue();
            priceSideProducts += Double.parseDouble(payingSaucesItem.getPriceTtc()) * payingSaucesItem.selectCount;
            sideProduct = new SideProduct(payingSaucesItem.getIdProduct(), "" + payingSaucesItem.selectCount);
            sideProducts.add(sideProduct);
        }

        for (Map.Entry<String, UpsellItem> item : accomplishmentitemMap.entrySet()) {
            UpsellItem upsellItem = item.getValue();
            priceSideProducts += Double.parseDouble(upsellItem.getPriceTtc()) * upsellItem.selectCount;
            sideProduct = new SideProduct(upsellItem.getIdProduct(), "" + upsellItem.selectCount);
            sideProducts.add(sideProduct);
        }
        for (Map.Entry<String, DrinksItem> item : boissonItemMap.entrySet()) {
            DrinksItem drinksItem = item.getValue();
            priceSideProducts += Double.parseDouble(drinksItem.getPriceTtc()) * drinksItem.selectCount;
            sideProduct = new SideProduct(drinksItem.getIdProduct(), "" + drinksItem.selectCount);
            sideProducts.add(sideProduct);
        }
        for (Map.Entry<String, DessertsItem> item : dessertItemMap.entrySet()) {
            DessertsItem dessertsItem = item.getValue();
            priceSideProducts += Double.parseDouble(dessertsItem.getPriceTtc()) * dessertsItem.selectCount;
            sideProduct = new SideProduct(dessertsItem.getIdProduct(), "" + dessertsItem.selectCount);
            sideProducts.add(sideProduct);
        }
        for (Map.Entry<String, PayingWasabiGingerItem> item : payingWasbiItemClicMap.entrySet()) {
            PayingWasabiGingerItem payingWasabiGingerItem = item.getValue();
            priceSideProducts += Double.parseDouble(payingWasabiGingerItem.getPriceTtc()) * payingWasabiGingerItem.selectCount;
            sideProduct = new SideProduct(payingWasabiGingerItem.getIdProduct(), "" + payingWasabiGingerItem.selectCount);
            sideProducts.add(sideProduct);
        }
        for (Map.Entry<String, ChopsticksItem> item : baguettesItemMap.entrySet()) {
            ChopsticksItem chopsticksItem = item.getValue();
            priceSideProducts += Double.parseDouble(chopsticksItem.getPriceTtc()) * chopsticksItem.selectCount;
            sideProduct = new SideProduct(chopsticksItem.getIdProduct(), "" + chopsticksItem.selectCount);
            sideProducts.add(sideProduct);
        }

        /*for(FreeSaucesItem item : freeSaucesItemList){
            sideProduct = new SideProduct(item.getIdProduct(), "" + item.selectCount);
            Log.d("Aziz_item", "sauce" + item.selectCount);
            sideProducts.add(sideProduct);
        }
        for (FreeWasabiGingerItem item : freeWasbiItemClicList) {
            sideProduct = new SideProduct(item.getIdProduct(), "" + item.selectCount);
            Log.d("Aziz_item", "wasabi" + item.selectCount);
            sideProducts.add(sideProduct);
        }*/

        for (Map.Entry<String, FreeSaucesItem> item : freeSaucesMap.entrySet()) {
            FreeSaucesItem freeSaucesItem = item.getValue();
            sideProduct = new SideProduct(freeSaucesItem.getIdProduct(), "" + freeSaucesItem.selectCount);
            sideProducts.add(sideProduct);
        }

        for (Map.Entry<String, FreeWasabiGingerItem> item : freeWasbiItemClicMap.entrySet()) {
            FreeWasabiGingerItem freeSaucesItem = item.getValue();
            sideProduct = new SideProduct(freeSaucesItem.getIdProduct(), "" + freeSaucesItem.selectCount);
            sideProducts.add(sideProduct);
        }

        DataCacheUtil.addSideProducts(sideProducts);
        ((PaymentMethodCheckoutActivity) getActivity()).setPriceWithSideProducts(priceSideProducts);
    }


    private void handleSauce() {

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.colorWhite)
        );


        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );


        selectedAccompagnements = 1;
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


    private void handleAccompanements() {
        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.colorWhite)
        );

        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        selectedAccompagnements = 2;
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


    private void handleBaguettes() {
        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.colorWhite)
        );

        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        selectedAccompagnements = 6;

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
        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.colorWhite)
        );
        selectedAccompagnements = 5;

        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

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
        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.colorWhite)
        );
        selectedAccompagnements = 4;

        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

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
        DrawableCompat.setTint(
                binding.imgViewBoissons.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.colorWhite)
        );

        DrawableCompat.setTint(
                binding.imgViewSauces.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewAccompagnements.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewWasbi.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewDesserts.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        DrawableCompat.setTint(
                binding.imgViewBaguettes.getDrawable(),
                ContextCompat.getColor(getActivity(), R.color.color_627588)
        );

        selectedAccompagnements = 3;
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


    public void clearPreviousSelectedItem() {
        freeSaucesMap = new HashMap<>();
        payingSaucesMap.clear();
        accomplishmentitemMap.clear();
        boissonItemMap.clear();
        dessertItemMap.clear();
        payingWasbiItemClicMap.clear();
        freeWasbiItemClicMap.clear();
        baguettesItemMap.clear();

        payingSaucesItems.clear();
        freeSaucesItemList.clear();
        accomplishmentitemList.clear();
        boissonItemList.clear();
        dessertItemList.clear();
        payingWasbiItemClicList.clear();
        freeWasbiItemClicList.clear();
        baguettesItemList.clear();
        freeSauces.clear();
        freeWasbi.clear();
        countSauces = 0;
        countAccompagnements = 0;
        countBoissons = 0;
        countDesserts = 0;
        countWasbi = 0;
        countBauettes = 0;

        if (binding != null)
            binding.recyclerViewAccompagnements.setVisibility(View.GONE);

    }
}
