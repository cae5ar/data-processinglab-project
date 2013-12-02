package com.pstu.dtl.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

public class CustomImage extends Composite {

    private SimplePanel wraper = new SimplePanel();
    private Image image = new Image();

    private int width = -1;
    private int height = -1;
    private boolean loaded = false;
    boolean verticalCenter = false;
    boolean horizontalCenter = false;

    public CustomImage() {
        this("", -1, -1);
    }

    public CustomImage(String url, int width, int height) {
        super();
        initWidget(wraper);
        wraper.setStyleName("image-container");
        wraper.setWidget(image);

        image.addLoadHandler(new LoadHandler() {
            public void onLoad(LoadEvent event) {
                loaded = true;
                updateImageSize();
            }
        });
        setAlign(true, true);
        setPixelSize(width, height);
        setUrl(url);
    }

    public void setAlt(String altText) {
        image.setAltText(altText);
    }

    public void setAlign(boolean verticalCenter, boolean horizontalCenter) {
        setVerticalAlign(verticalCenter);
        setHorizontalAlign(horizontalCenter);
    }

    public void setVerticalAlign(boolean verticalCenter) {
        this.verticalCenter = verticalCenter;
        updateImageSize();
    }

    public void setHorizontalAlign(boolean horizontalCenter) {
        this.horizontalCenter = horizontalCenter;
        updateImageSize();
    }

    public void setUrl(String url) {
        String previousUrl = image.getUrl();
        if (previousUrl == null
                || (!previousUrl.equalsIgnoreCase(url) && !previousUrl.equalsIgnoreCase(GWT.getHostPageBaseURL() + url))) {
            loaded = false;
            image.setVisible(false);
            image.setUrl(url);
        }
    }

    @Deprecated
    @Override
    public void setWidth(String width) {
        assert false : "derecated method setWidth() in GoodImage class. Use setPixelWidth()";
    }

    @Deprecated
    @Override
    public void setHeight(String height) {
        assert false : "derecated method setHeight() in GoodImage class. Use setPixelHeight()";
    }

    @Override
    public void setSize(String width, String height) {
        assert false : "derecated method setSize() in GoodImage class. Use setPixelSize()";
    }

    @Override
    public void setPixelSize(int width, int height) {
        setPixelWidth(width);
        setPixelHeight(height);
    }

    public void setPixelWidth(int width) {
        if (width >= 0)
            wraper.setWidth(width + "px");
        else
            wraper.setWidth("");
        this.width = width;
        updateImageSize();
    }

    public void setPixelHeight(int height) {
        if (height >= 0)
            wraper.setHeight(height + "px");
        else
            wraper.setHeight("");
        this.height = height;
        updateImageSize();
    }

    private void updateImageSize() {
        if (loaded) {
            image.setVisible(true);
            int w = image.getWidth();
            int h = image.getHeight();

            if (width < 0 && height < 0) {
                image.setWidth("");
                image.setHeight("");
            }

            if (width < 0 && height >= 0) {
                image.setWidth("");
                image.setHeight(height + "px");
            }

            if (width >= 0 && height < 0) {
                image.setHeight("");
                image.setWidth(width + "px");
            }

            if (width >= 0 && height >= 0) {
                double cw = ((double) width / w);
                double ch = ((double) height / h);
                if (ch >= cw) {
                    int recalculatedImageHeight = (int) (h * cw);
                    if (verticalCenter)
                        image.getElement().getStyle().setPaddingTop((height - recalculatedImageHeight) / 2, Unit.PX);
                    image.setHeight(recalculatedImageHeight + "px");
                    image.setWidth(width + "px");
                }
                else {
                    int recalculatedImageWidth = (int) (w * ch);
                    if (horizontalCenter)
                        image.getElement().getStyle().setPaddingLeft((width - recalculatedImageWidth) / 2, Unit.PX);
                    image.setWidth(recalculatedImageWidth + "px");
                    image.setHeight(height + "px");
                }
            }
        }
    }
}
