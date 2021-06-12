package com.winphyoethu.twoappstudiotest.data.model;

import androidx.annotation.Nullable;

public class UrlModel {

    private String urlName;
    private String url;
    private String urlImage;
    private boolean isSelected = false;

    public UrlModel(String urlName, String url, String urlImage, boolean isSelected) {
        this.urlName = urlName;
        this.url = url;
        this.urlImage = urlImage;
        this.isSelected = isSelected;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof UrlModel) {
            UrlModel foreignUrlModel = ((UrlModel) obj);
            return this.urlName.equals(foreignUrlModel.urlName) && this.url.equals(foreignUrlModel.url) && this.urlImage.equals(foreignUrlModel.urlImage) && this.isSelected == foreignUrlModel.isSelected;
        }
        return false;
    }
}
