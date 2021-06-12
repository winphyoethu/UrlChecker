package com.winphyoethu.twoappstudiotest.features.home.homestate;

import com.winphyoethu.twoappstudiotest.data.model.UrlModel;

import java.util.List;

public class UpdateList implements HomeState {

    private final List<UrlModel> urlList;
    private final int urlCount;

    public UpdateList(List<UrlModel> urlList, int urlCount) {
        this.urlList = urlList;
        this.urlCount = urlCount;
    }

    public List<UrlModel> getUrlList() {
        return urlList;
    }

    public int getUrlCount() {
        return urlCount;
    }
}
