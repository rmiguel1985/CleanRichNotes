package com.adictosalainformatica.cleanrichnotes.features.image_rest.api;

import com.adictosalainformatica.cleanrichnotes.features.image_rest.entities.RestImages;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.adictosalainformatica.cleanrichnotes.utils.Constants.API_KEY;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.CX;

public interface ApiInterface {
    @GET("v1?safe=high&start=1&searchType=image&imgSize=medium&key="+API_KEY+"&cx="+CX)
    Call<RestImages> getImages(@Query("q") String searchString);
}
