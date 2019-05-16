package fr.sushi.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import java.lang.ref.WeakReference;
import java.util.Stack;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.ui.objectbox.MyObjectBox;
import io.objectbox.BoxStore;
import timber.log.Timber;

public class SushiApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private Stack<Activity> activityStack;
    private WeakReference<Activity> currentActivity;

    private static volatile SushiApp instance;

    private static Context context;

    private boolean splashVisited;

    public static synchronized SushiApp getInstance() {
        return instance;
    }
    private static BoxStore boxStore;
    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(this);
        instance = this;
        activityStack = new Stack<>();
       // LebonbonFontSetting.getInstance(this);
        SharedPref.on(getApplicationContext());
        //boxStore = MyObjectBox.builder().androidContext(this).build();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static Context getContext(){
        return context;
    }

    public static BoxStore getBoxStore(){
        return boxStore;
    }



    public Activity getCurrentActivity() {
        return currentActivity.get();
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = new WeakReference<>(currentActivity);
    }

    private void clearActivityReferences(Activity callbackActivity) {
        Activity currActivity = getCurrentActivity();
        if (callbackActivity.equals(currActivity))
            setCurrentActivity(null);
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityStack.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        setCurrentActivity(activity);

    }

    @Override
    public void onActivityPaused(Activity activity) {
        clearActivityReferences(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityStack.remove(activity);

    }

    public boolean isSplashVisited() {
        return splashVisited;
    }

    public void setSplashVisited(boolean splashVisited) {
        this.splashVisited = splashVisited;
    }
}

