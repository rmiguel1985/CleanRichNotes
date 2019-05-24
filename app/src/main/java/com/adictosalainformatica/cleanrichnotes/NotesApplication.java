package com.adictosalainformatica.cleanrichnotes;

import android.os.StrictMode;

import com.adictosalainformatica.cleanrichnotes.base.BaseApplication;
import com.adictosalainformatica.cleanrichnotes.base.dagger.DaggerNotesComponent;
import com.adictosalainformatica.cleanrichnotes.base.dagger.NotesComponent;
import com.adictosalainformatica.cleanrichnotes.base.dagger.NotesModule;
import com.adictosalainformatica.cleanrichnotes.utils.ConnectivityHelper;

import timber.log.Timber;

public class NotesApplication extends BaseApplication {
    private static NotesComponent daggerNotesComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeLogging();
        initializeDagger();
        initializeConnectivity();
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
     * Initialize diagnostic tools
     *
     */
    @Override
    protected void initializeDiagnosticTools(){
        if (isDebugBuild()) {
            final StrictMode.ThreadPolicy strictModeThreadPolicy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyDeath()
                    .build();
            StrictMode.setThreadPolicy(strictModeThreadPolicy);
        }
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
