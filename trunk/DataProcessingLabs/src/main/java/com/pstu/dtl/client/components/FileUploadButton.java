package com.pstu.dtl.client.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.shared.dto.FileDto;

public abstract class FileUploadButton extends AbstractFileUploadButton {

    Widget panel = null;

    public FileUploadButton() {
        super(Site.getAddFileServletUrl());
        addStyleName("btn add");
    }

    public FileUploadButton(Panel panel, String style) {
        super(Site.getAddFileServletUrl(), panel, style);
    }

    @Override
    protected void onCompleteSubmit(int submitId, List<SubmitFile> files, String result) {
        System.out.println("PhotoEditWidget.onSubmitComplete " + submitId + "   " + files + "   " + result);
        ArrayList<FileDto> out = new ArrayList<FileDto>();
        if (result.contains("<h2>HTTP ERROR: 500</h2>")) {
            onSubmitError(result);
        }
        else {
            for (String fileParamsStr : result.split(":fsep;")) {
                String[] fileParamsArr = fileParamsStr.split(":psep;");
                Long id = Long.parseLong(fileParamsArr[0]);
                String name = fileParamsArr[1];
                Long size = Long.parseLong((fileParamsArr[2] == null) ? "0" : fileParamsArr[2]);
                if (id != null) {
                    FileDto file = new FileDto(id, name, size);
                    out.add(file);
                }
            }

            onSubmitComplete(submitId, out);
        }
    }

    @Override
    protected boolean onBeforeSubmit(int submitId, List<SubmitFile> files) {
        System.out.println("PhotoEditWidget.onBeforeSubmit " + submitId + "   " + files);
        return true;
    }

    @Override
    protected void onStartSubmit(int submitId, List<SubmitFile> files) {
        this.setEnabled(false);
        hideButton();
        super.customPanel.clear();
        super.customPanel.getElement().appendChild(Site.loadingElement);
    }

    private void hideButton() {
        this.form.fileChooser.getElement().getStyle().setDisplay(Display.NONE);
    }

    private void showButton() {
        this.form.fileChooser.getElement().getStyle().clearDisplay();
    }

    protected void onSubmitComplete(int submitId, List<FileDto> files) {
        this.setEnabled(true);
        showButton();
        Site.loadingElement.removeFromParent();
        if (panel != null) {
            panel.removeFromParent();
            panel = null;
        }
    }

    protected void onSubmitError(String error) {
        Site.handleError(new Error("Не удалось сохранить файлы"));
        Site.loadingElement.removeFromParent();
        this.setEnabled(true);
        showButton();
        if (panel != null) {
            panel.removeFromParent();
            panel = null;
        }
    }

}
