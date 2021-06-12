package com.winphyoethu.twoappstudiotest.util;

import com.winphyoethu.twoappstudiotest.data.model.UrlModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class UrlMapper {

    public UrlModel getWebsiteUrlFromString(Response<ResponseBody> response) {
        String url = response.raw().request().url().toString();
        Document doc = null;
        try {
            doc = Jsoup.parse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = doc.select("meta");
        Elements imgElements = doc.select("img");

        String imageUrl = "";
        if (!elements.isEmpty()) {
            for (Element element : elements) {
                if (element.attr("property").equals("og:image")) {
                    imageUrl = element.attr("content");
                }
            }
        }
        if (imageUrl.isEmpty() && !imgElements.isEmpty()) {
            for (Element imgElement : imgElements) {
                if (imgElement.toString().contains("http")) {
                    imageUrl = imgElement.attr("src");
                }
            }
        }

        return new UrlModel(doc.title(), url, imageUrl, false);
    }

}
