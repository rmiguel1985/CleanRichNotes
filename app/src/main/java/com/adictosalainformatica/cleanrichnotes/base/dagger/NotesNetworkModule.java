package com.adictosalainformatica.cleanrichnotes.base.dagger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ruben on 26/06/17.
 */

@Module
public class NotesNetworkModule {

    private String baseUrl;
    private static final long TIMEOUT_CONNECT_MS = 8000;
    private static final long TIMEOUT_READ_MS = 8000;

    // Constructor needs one parameter to instantiate.
    public NotesNetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ_MS, TimeUnit.MILLISECONDS);

        /*if (BuildConfig.DEBUG) {
            //OKHHTP LOGGER
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(interceptor);
        }*/

        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}