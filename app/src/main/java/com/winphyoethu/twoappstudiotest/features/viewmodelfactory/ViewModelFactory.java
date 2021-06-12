package com.winphyoethu.twoappstudiotest.features.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.winphyoethu.twoappstudiotest.data.ApiService;
import com.winphyoethu.twoappstudiotest.features.home.HomeViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ApiService apiService;

    public ViewModelFactory(ApiService apiService) {
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(HomeViewModel.class)) {
            return (T) new HomeViewModel(apiService);
        }
        return null;
    }

}
