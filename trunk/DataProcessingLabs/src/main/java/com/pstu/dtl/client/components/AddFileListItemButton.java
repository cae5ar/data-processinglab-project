package com.pstu.dtl.client.components;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.pstu.dtl.shared.dto.FileDto;

public abstract class AddFileListItemButton extends Composite {

    private FlexTable table = new FlexTable();
    private FileUploadButton addButton = new FileUploadButton(table, "afb-box") {
        @Override
        protected void onSubmitComplete(int submitId, List<FileDto> files) {
            super.onSubmitComplete(submitId, files);
            AddFileListItemButton.this.onSubmitComplete(submitId, files);
        }
    };

    protected abstract void onSubmitComplete(int submitId, List<FileDto> files);

    public AddFileListItemButton() {
        initWidget(addButton);
        table.setStyleName("afb-table");
        table.setHTML(0, 0, "<i class='icon-plus'></i>");
        table.getFlexCellFormatter().setStyleName(0, 1, "afb-table-text");
        setLabel("Загрузить новый документ");

    }

    public void setMultiple(boolean multiple) {
        addButton.setMultiple(multiple);
    }

    public void setLabel(String text) {
        table.setHTML(0, 1, text);
    }
}
