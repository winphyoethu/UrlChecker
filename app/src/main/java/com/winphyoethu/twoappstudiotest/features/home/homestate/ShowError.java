package com.winphyoethu.twoappstudiotest.features.home.homestate;

public class ShowError implements HomeState {

    private String errorMessage;

    public ShowError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
