package ca.ulaval.ima.mp.dummy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static void addItem(JSONObject item) throws JSONException {
        DummyItem restaurant = new DummyItem(item);
        ITEMS.add(restaurant);
        ITEM_MAP.put(restaurant.id, restaurant);
    }

    public static void clearItems() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        static public String ID = "id";
        static public String NAME = "name";
        static public String REVIEW_COUNT = "review_count";
        static public String DISTANCE = "distance";
        static public String IMAGE = "image";
        static public String REVIEW_AVERAGE = "review_average";
        static public String CUISINE = "cuisine";

        public final String id;
        public final String name;
        public final String distance;
        public final String cuisine;
        public final String reviewCount;
        public final String image;
        public final float reviewAverage;

        public DummyItem(JSONObject restaurant) throws JSONException {
            this.id = restaurant.getString(ID);
            this.name = restaurant.getString(NAME);
            this.distance = restaurant.getString(DISTANCE);
            this.image = restaurant.getString(IMAGE);
            this.reviewCount = restaurant.getString(REVIEW_COUNT);
            this.reviewAverage = (float)restaurant.getDouble(REVIEW_AVERAGE);
            this.cuisine = restaurant.getJSONArray(CUISINE).getJSONObject(0).getString("name");
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
