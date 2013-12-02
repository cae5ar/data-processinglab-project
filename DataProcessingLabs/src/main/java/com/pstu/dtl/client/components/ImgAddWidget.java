package com.pstu.dtl.client.components;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.shared.dto.FileDto;

public class ImgAddWidget extends FlowPanel {

    private CustomImage img = new CustomImage();
    private FlowPanel imgBlock = new FlowPanel();
    private FlowPanel buttonsBlock = new FlowPanel();
    private SimplePanel resultPanel = new SimplePanel();

    private FileUploadButton photoCover = new FileUploadButton(resultPanel, "resultPanel") {
        @Override
        protected void onSubmitComplete(int submitId, List<FileDto> files) {
            super.onSubmitComplete(submitId, files);
            setImageInfo(files.get(0));
            setFileName(files.get(0).getName() + "<br>(загрузить новый документ)");
        }
    };

    private Btn imgClear = new Btn(Site.messages.msgDelete());

    private FileDto file = null;

    public ImgAddWidget() {
        setStyleName("row img-edit");
        imgBlock.addStyleName("img-block span2");
        add(imgBlock);
        buttonsBlock.addStyleName("buttons-block span3");
        add(buttonsBlock);

        img.setAlt(Site.messages.msgImgPlace());
        img.setAlign(true, true);
        imgBlock.add(img);

        photoCover.setStyleName("image-input");
        photoCover.setMultiple(false);
        buttonsBlock.add(photoCover);
        buttonsBlock.add(imgClear);

        imgClear.addStyleName(EButtonStyle.WARNING.toString());
        imgClear.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setImageInfo(null);
                resultPanel.clear();
            }
        });
        resultPanel.getElement().getStyle().setMarginBottom(3, Unit.PX);
        setImageInfo(null);
    }

    public void setImageInfo(FileDto file) {
        this.file = file;
        if (file != null) {
            img.setUrl(Site.getGetFileServletUrl("file", file.getId()));
            setFileName(file.getName());
            imgClear.setVisible(true);
        }
        else {
            img.setUrl("img/attache_document.png");
            imgClear.setVisible(false);
        }
    }

    public FileDto getImageId() {
        return file;
    }

    public void setFileName(String name) {
        resultPanel.setWidget(new HTML(name));
    }
}
