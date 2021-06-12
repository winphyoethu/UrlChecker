package com.winphyoethu.twoappstudiotest.data;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ProvideRetrofit {

    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://www.google.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
