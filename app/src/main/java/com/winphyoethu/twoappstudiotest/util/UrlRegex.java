package com.winphyoethu.twoappstudiotest.util;

import com.winphyoethu.twoappstudiotest.util.ui.EditTextResult;

import kotlin.text.Regex;

public class UrlRegex {

    public EditTextResult checkUrl(String input) {
        Regex regex = new Regex("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
        if (input.isEmpty()) {
            return new EditTextResult(false, "URL must not be empty");
        } else if (!regex.matches(input)) {
            return new EditTextResult(false, "Invalid URL");
        } else {
            return new EditTextResult(true, "Valid URL");
        }
    }

}
