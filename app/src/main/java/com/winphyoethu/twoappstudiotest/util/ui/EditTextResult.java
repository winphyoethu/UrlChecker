package com.winphyoethu.twoappstudiotest.util.ui;

public class EditTextResult {

    private boolean isValid = false;
    private String result = "";

    public EditTextResult(boolean isValid, String result) {
        this.isValid = isValid;
        this.result = result;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getResult() {
        return result;
    }
}
