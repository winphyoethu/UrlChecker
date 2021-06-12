package com.winphyoethu.twoappstudiotest.features.home.homestate;

import com.winphyoethu.twoappstudiotest.data.model.UrlModel;

import java.util.ArrayList;
import java.util.List;

public class ShowList implements HomeState {

    private final List<UrlModel> urlList;

    public ShowList(List<UrlModel> urlList) {
        this.urlList = urlList;
    }

    public List<UrlModel> getUrlList() {
        return urlList;
    }
}
