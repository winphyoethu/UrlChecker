package com.winphyoethu.twoappstudiotest;

import com.winphyoethu.twoappstudiotest.util.UrlRegex;
import com.winphyoethu.twoappstudiotest.util.ui.EditTextResult;

import org.junit.Test;

public class RegexTest {

    UrlRegex urlRegex = new UrlRegex();

    @Test
    public void isValidRegex() {

        EditTextResult result = urlRegex.checkUrl("https://www.facebook.com");



    }

}
