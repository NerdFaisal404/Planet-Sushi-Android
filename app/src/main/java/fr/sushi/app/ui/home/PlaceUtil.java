package fr.sushi.app.ui.home;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import fr.sushi.app.data.local.SharedPref;

public class PlaceUtil {
    private static final String CURRENT_SEARCH_PLACE = "current-place";
    private static String SPLITER = "-@-";

    public static void saveCurrentPlace(SearchPlace place) {
        Gson gson = new Gson();
        if (place == null) return;

        String existingPlace = SharedPref.read(CURRENT_SEARCH_PLACE, "");

        String currentJson = gson.toJson(place);

        if (TextUtils.isEmpty(existingPlace)) {
            SharedPref.write(CURRENT_SEARCH_PLACE, currentJson);
        } else {
            String[] existingItem = existingPlace.split(SPLITER);

            String newString = "";
            if (existingItem.length == 1) {
                newString = existingPlace + SPLITER + currentJson;
            } else {
                newString = existingItem[0] + SPLITER + currentJson;
            }
            SharedPref.write(CURRENT_SEARCH_PLACE, newString);
        }
    }

    public static List<SearchPlace> getSearchPlace() {
        List<SearchPlace> searchPlaceList = new ArrayList<>();
        String existingPlace = SharedPref.read(CURRENT_SEARCH_PLACE, "");

        if (TextUtils.isEmpty(existingPlace))
            return searchPlaceList;


        String[] splitedItem = existingPlace.split(SPLITER);


        for (String item : splitedItem) {
            SearchPlace place = new Gson().fromJson(item, SearchPlace.class);
            searchPlaceList.add(place);
        }
        return searchPlaceList;
    }

    public static void saveCurrentPlaceInFirstPosition(SearchPlace searchPlace) {
        Gson gson = new Gson();
        if (searchPlace == null) return;

        String existingPlace = SharedPref.read(CURRENT_SEARCH_PLACE, "");

        String currentJson = gson.toJson(searchPlace);

        if (TextUtils.isEmpty(existingPlace)) {
            SharedPref.write(CURRENT_SEARCH_PLACE, currentJson);
        } else {
            String[] existingItem = existingPlace.split(SPLITER);

            String newString = "";
            if (existingItem.length == 1) {
                newString = currentJson;
            } else {
                newString = currentJson + SPLITER + existingItem[1];
            }
            SharedPref.write(CURRENT_SEARCH_PLACE, newString);
        }
    }
}
