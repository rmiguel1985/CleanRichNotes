package com.adictosalainformatica.cleanrichnotes.base.dagger;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotesModule {
    private final Application application;

    public NotesModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    public ExecutorService providesExecutor() { return Executors.newSingleThreadExecutor(); }
}
