package ca.ulaval.ima.mp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Restaurant {
    static public String ID = "id";
    static public String NAME = "name";
    static public String REVIEW_COUNT = "review_count";
    static public String DISTANCE = "distance";
    static public String IMAGE = "image";
    static public String REVIEW_AVERAGE = "review_average";
    static public String OPENING_HOURS = "opening_hours";
    static public String OPENING_HOUR = "opening_hour";
    static public String CLOSING_HOUR = "closing_hour";
    static public String CREATOR = "creator";
    static public String DAY = "day";
    static public String DATE = "date";
    static public String STARS = "stars";
    static public String FIRST_NAME = "first_name";
    static public String LAST_NAME = "last_name";
    static public String COMMENT = "comment";
    static public String PHONE_NUMBER = "phone_number";
    static public String WEBSITE = "website";
    static public String REVIEWS = "reviews";

    public String id;
    public String name;
    public String distance;
    public String reviewCount;
    public String image;
    public float reviewAverage;
    public Map<String, OpeningHour> openingHours = new HashMap<>();
    public ArrayList<Review> reviews;
    public String phoneNumber;
    public String website;

    Restaurant(JSONObject data) throws JSONException {
        this.id = data.getString(ID);
        this.name = data.getString(NAME);
        this.distance = data.getString(DISTANCE);
        this.image = data.getString(IMAGE);
        this.reviewCount = data.getString(REVIEW_COUNT);
        this.phoneNumber = data.getString(PHONE_NUMBER);
        this.website = data.getString(WEBSITE);
        this.reviewAverage = (float)data.getDouble(REVIEW_AVERAGE);
        this.reviews = new ArrayList<>();
        JSONArray jsonReviews = data.getJSONArray(REVIEWS);
        for (int i = 0; i < jsonReviews.length(); i++) {
            reviews.add(new Review(jsonReviews.getJSONObject(i)));
        }
        JSONArray jsonOpeningHours = data.getJSONArray(OPENING_HOURS);
        for (int i = 0; i < jsonOpeningHours.length(); i++) {
            JSONObject openingHour = jsonOpeningHours.getJSONObject(i);
            this.openingHours.put(openingHour.getString(DAY), new OpeningHour(openingHour));
        }
    }

    static public String MONDAY = "MON";
    static public String TUESDAY = "TUE";
    static public String WEDNESDAY= "WED";
    static public String THURSDAY= "THU";
    static public String FRIDAY= "FRI";
    static public String SATURDAY= "SAT";
    static public String SUNDAY = "SUN";

    class OpeningHour {
        public String id;
        public String opening_hour;
        public String closing_hour;
        public String day;

        OpeningHour(JSONObject jsonData) throws JSONException {
            this.id = jsonData.getString(ID);
            this.opening_hour = jsonData.getString(OPENING_HOUR);
            if (this.opening_hour != "null")
                this.opening_hour = this.opening_hour.substring(0, 5);
            this.closing_hour = jsonData.getString(CLOSING_HOUR);
            if (this.closing_hour != "null")
                this.closing_hour = this.closing_hour.substring(0, 5);
            this.day = jsonData.getString(DAY);
        }
    }

    public static class Review {
        public String id;
        public Creator creator;
        public String stars;
        public String image;
        public String comment;
        public String date;

        Review(JSONObject data) throws JSONException {
            this.id = data.getString(ID);
            this.stars = data.getString(STARS);
            this.image = data.getString(IMAGE);
            this.comment = data.getString(COMMENT);
            this.date = data.getString(DATE);
            this.creator = new Creator(data.getJSONObject(CREATOR));

        }
        class Creator {
            public String firstName;
            public String lastName;
            Creator(JSONObject jsonCreator) throws JSONException {
                this.firstName = jsonCreator.getString(FIRST_NAME);
                this.lastName = jsonCreator.getString(LAST_NAME);
            }

        }
    }
}

