package com.microsoft.xuetang.bean.cache;

/**
 * Created by jiash on 8/9/2016.
 */
public class WebFeatureEntity {
    private String title = null;
    private String imageUrl = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean equals(Object other) {
        if(!(other instanceof WebFeatureEntity)) {
            return false;
        }

        WebFeatureEntity otherEntiry = (WebFeatureEntity) other;

        if((title == null && otherEntiry.title == null) || (title != null && title.equals(otherEntiry.title))) {
            if((imageUrl == null && otherEntiry.imageUrl == null) || (imageUrl != null && imageUrl.equals(otherEntiry.imageUrl))) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Title: ")
                .append(title)
                .append(" ImageUrl: ")
                .append(imageUrl);

        return stringBuffer.toString();
    }
}
