package com.winphyoethu.twoappstudiotest.data;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    @GET
    public Observable<Response<ResponseBody>> findUrl(@Url String url);

}
