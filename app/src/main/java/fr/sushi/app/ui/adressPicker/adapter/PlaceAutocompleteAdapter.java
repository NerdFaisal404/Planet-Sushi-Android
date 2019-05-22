package fr.sushi.app.ui.adressPicker.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.BaseAddress;
import fr.sushi.app.data.model.ProfileAddressModel;

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder> implements Filterable {

    public interface PlaceAutoCompleteInterface {
        public void onPlaceClick(BaseAddress address);
    }

    Context mContext;
    PlaceAutoCompleteInterface mListener;
    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    ArrayList<BaseAddress> mResultList;

    private GoogleApiClient mGoogleApiClient;

    private LatLngBounds mBounds;

    private int layout;

    private AutocompleteFilter mPlaceFilter;


    public PlaceAutocompleteAdapter(Context context, GoogleApiClient googleApiClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        this.mContext = context;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
        this.mListener = (PlaceAutoCompleteInterface) mContext;
    }

    /*
    Clear List items
     */
    public void clearList() {
        if (mResultList != null && mResultList.size() > 0) {
            mResultList.clear();
        }
    }


    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    public void addItems(ArrayList<BaseAddress> addressList){
         mResultList = addressList;
         notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (!TextUtils.isEmpty(constraint)) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                } else {
                    Log.d("PlaceSearch", "own place call");
                    String addressJson = SharedPref.read(PrefKey.USER_ADDRESS, "");
                    List<ProfileAddressModel> addressList = GsonHelper.on().convertJsonToNormalAddress(addressJson);
                    if (mResultList == null) {
                        mResultList = new ArrayList<>();
                    } else {
                        mResultList.clear();
                    }
                    mResultList.addAll(addressList);

                    results.values = mResultList;
                    results.count = mResultList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();

                }
            }
        };
        return filter;
    }

    private ArrayList<BaseAddress> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            Log.i("", "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
//                Toast.makeText(mContext, "Error contacting API: " + status.toString(),
//                        Toast.LENGTH_SHORT).show();
                Log.e("", "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i("", "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getFullText(STYLE_BOLD)));
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }
        Log.e("", "Google API client is not connected for autocomplete query.");
        return null;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.adapter_palce_auto_complete, viewGroup, false);
        PlaceViewHolder mPredictionHolder = new PlaceViewHolder(convertView);
        return mPredictionHolder;
    }


    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, final int i) {
        BaseAddress address = mResultList.get(i);
        if (address instanceof PlaceAutocomplete) {
            mPredictionHolder.mAddress.setText(((PlaceAutocomplete) address).description);
        } else if (address instanceof ProfileAddressModel) {
            mPredictionHolder.mAddress.setText(((ProfileAddressModel) address).getLocation());
        }

        mPredictionHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlaceClick(address);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public BaseAddress getItem(int position) {
        return mResultList.get(position);
    }

    /*
    View Holder For Trip History
     */
    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        //        CardView mCardView;
        public RelativeLayout mParentLayout;
        public TextView mAddress;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (RelativeLayout) itemView.findViewById(R.id.predictedRow);
            mAddress = (TextView) itemView.findViewById(R.id.address);
        }

    }


    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete extends BaseAddress {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }
}