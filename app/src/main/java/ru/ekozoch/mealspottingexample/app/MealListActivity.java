package ru.ekozoch.mealspottingexample.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Arrays;

public class MealListActivity extends ListActivity {

    private ParseQueryAdapter<Meal> mainAdapter;
    private FavoriteMealAdapter favoritesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setClickable(false);

        mainAdapter = new ParseQueryAdapter<Meal>(this, Meal.class);
        mainAdapter.setTextKey("title");
        mainAdapter.setImageKey("photo");

        // Subclass of ParseQueryAdapter
        favoritesAdapter = new FavoriteMealAdapter(this);

        // Default view is all meals
        setListAdapter(mainAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_meal_list, menu);
        return true;
    }

    /*
     * Posting meals and refreshing the list will be controlled from the Action
     * Bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh: {
                updateMealList();
                break;
            }

            case R.id.action_favorites: {
                showFavorites();
                break;
            }

            case R.id.action_new: {
                newMeal();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMealList() {
        mainAdapter.loadObjects();
        setListAdapter(mainAdapter);
    }

    private void showFavorites() {
        favoritesAdapter.loadObjects();
        setListAdapter(favoritesAdapter);
    }

    private void newMeal() {
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // If a new post has been added, update
            // the list of posts
            updateMealList();
        }
    }


    public class FavoriteMealAdapter extends ParseQueryAdapter<Meal> {

        public FavoriteMealAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<Meal>() {
                public ParseQuery<Meal> create() {
                    // Here we can configure a ParseQuery to display
                    // only top-rated meals.
                    ParseQuery query = new ParseQuery("Meal");
                    query.whereContainedIn("rating", Arrays.asList("5", "4"));
                    query.orderByDescending("rating");
                    return query;
                }
            });
        }

        @Override
        public View getItemView(Meal meal, View v, ViewGroup parent) {

            if (v == null) {
                v = View.inflate(getContext(), R.layout.item_list_favorites, null);
            }

            super.getItemView(meal, v, parent);

            ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.icon);
            ParseFile photoFile = meal.getParseFile("photo");
            if (photoFile != null) {
                mealImage.setParseFile(photoFile);
                mealImage.loadInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        // nothing to do
                    }
                });
            }

            TextView titleTextView = (TextView) v.findViewById(R.id.text1);
            titleTextView.setText(meal.getTitle());
            TextView ratingTextView = (TextView) v
                    .findViewById(R.id.favorite_meal_rating);
            ratingTextView.setText(meal.getRating());
            return v;
        }

    }

}
