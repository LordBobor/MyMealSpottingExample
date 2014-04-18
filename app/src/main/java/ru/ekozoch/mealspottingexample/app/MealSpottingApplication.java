package ru.ekozoch.mealspottingexample.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ekozoch on 19.04.14.
 */
public class MealSpottingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Meal.class);

        Parse.initialize(this, "O1UuFdAM5M79ZZsTjELXFYFkyQHEs13GhPGHSmZO", "hQEMKNHDZL4Jx013eq3L4TYUusy5GqYN3Zn3ozMh");

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
