package fr.sushi.app.ui.home;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import fr.sushi.app.data.local.SharedPref;

public class PlaceUtil {
    private static final String SEARCH_PLACE = "recent";
    private static final String CURRENT_SEARCH_PLACE = "current";
    private static final String KEY_PLACE_NAME = "n";
    private static final String KEY_PLACE_LAT = "lt";
    private static final String KEY_PLACE_LNG = "lg";

    public static void saveCurrentPlace(SearchPlace place) {
        String existingPlace = SharedPref.read(SEARCH_PLACE, "");
        String jsonString = convertToJsonString(place);

        if (TextUtils.isEmpty(existingPlace)) {
            SharedPref.write(SEARCH_PLACE, jsonString);
        } else {
            String afterRemove = removeExistingPlace(existingPlace, place.getAddress());
            existingPlace = jsonString + "@" + afterRemove;
            SharedPref.write(SEARCH_PLACE, existingPlace);
        }

        SharedPref.write(CURRENT_SEARCH_PLACE, jsonString);
    }

    private static String convertToJsonString(SearchPlace place) {
        try {
            JSONObject jo = new JSONObject();
            jo.put(KEY_PLACE_NAME, place.getAddress());
            jo.put(KEY_PLACE_LAT, place.getLat());
            jo.put(KEY_PLACE_LNG, place.getLng());
            return jo.toString();
        } catch (JSONException e) {
        }
        return null;
    }

    private static String removeExistingPlace(String existing, String current) {
        String[] placeArray = existing.split("@");
        List<String> existPlaceList = new LinkedList<String>(Arrays.asList(placeArray));
        boolean isExist = false;
        for (int i = 0; i < existPlaceList.size(); i++) {
            if (existPlaceList.get(i).contains(current)) {
                existPlaceList.remove(i);
                isExist = true;
            }
        }

        if (isExist) {
            if (existPlaceList.size() > 0) {
                String jsonString = existPlaceList.get(0);
                for (int i = 1; i < existPlaceList.size(); i++) {
                    jsonString = jsonString + "@" + existPlaceList.get(i);
                }
                return jsonString;
            }
        } else {
            return existing;
        }

        return null;
    }

    public static List<SearchPlace> getSavedPlaces(){
        String existingPlace = SharedPref.read(SEARCH_PLACE, "");
        List<SearchPlace> searchPlaces = new ArrayList<>();
        if(!TextUtils.isEmpty(existingPlace)){
            String[] existingPlaces = existingPlace.split("@");
            for (String item : existingPlaces) {
                SearchPlace place = parsePlace(item);
                if(place != null){
                    searchPlaces.add(place);
                }
            }
        }
        return searchPlaces;
    }

    /**
     * Return current searched place
     *
     * @return
     */
    public static SearchPlace getCurrentSearchPlace(){
        String currentPlace = SharedPref.read(CURRENT_SEARCH_PLACE, "");
        if(!TextUtils.isEmpty(currentPlace)){
            return parsePlace(currentPlace);
        }
        return null;
    }

    private static SearchPlace parsePlace(String jsonString){
        try {
            JSONObject jo = new JSONObject(jsonString);

            String name = jo.getString(KEY_PLACE_NAME);
            double lat = jo.getDouble(KEY_PLACE_LAT);
            double lng = jo.getDouble(KEY_PLACE_LNG);

            SearchPlace searchPlace = new SearchPlace(name, lat, lng);
            return searchPlace;
        }catch (JSONException e){}
        return null;
    }

}
