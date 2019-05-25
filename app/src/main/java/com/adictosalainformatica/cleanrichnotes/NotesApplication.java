package com.adictosalainformatica.cleanrichnotes;

import com.adictosalainformatica.cleanrichnotes.base.BaseApplication;
import com.adictosalainformatica.cleanrichnotes.base.dagger.DaggerNotesComponent;
import com.adictosalainformatica.cleanrichnotes.base.dagger.NotesComponent;
import com.adictosalainformatica.cleanrichnotes.base.dagger.NotesModule;
import com.adictosalainformatica.cleanrichnotes.utils.ConnectivityHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

import timber.log.Timber;

public class NotesApplication extends BaseApplication {
    private static NotesComponent daggerNotesComponent;
    private static FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeLogging();
        initializeDagger();
        initializeConnectivity();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        daggerNotesComponent = null;
    }

    /**
     * Initialize logging only for debug builds
     *
     */
    @Override
    protected void initializeLogging() {
        if (isDebugBuild()) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    /**
     * Initialize ConnectivityHelper helper Class
     *
     */
    @Override
    protected void initializeConnectivity() {
        ConnectivityHelper.setContext(this);
    }

    /**
     * Initialize Dagger
     *
     */
    @Override
    protected void initializeDagger() {
        daggerNotesComponent = DaggerNotesComponent.builder()
                .notesModule(new NotesModule(this))
                .build();
        daggerNotesComponent.inject(this);
    }

    /**
     * Check build type
     *
     * @return true if is a debug build, otherwise false
     */
    private boolean isDebugBuild(){
        return BuildConfig.DEBUG;
    }

    /**
     * Get DaggerComponent
     *
     * @return NotesComponent daggerNotesComponent
     */
    public static NotesComponent getDaggerComponent(){
        return daggerNotesComponent;
    }
}
