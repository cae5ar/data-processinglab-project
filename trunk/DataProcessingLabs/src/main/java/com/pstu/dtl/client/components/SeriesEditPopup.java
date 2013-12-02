package com.pstu.dtl.client.components;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.client.components.CustomDialogBox.EAlertType;
import com.pstu.dtl.shared.dto.SeriesDto;

public class SeriesEditPopup extends CustomPopup {

    public interface ISaveClickHandler {
        void click(SeriesEditPopup sender, SeriesDto bean);
    }

    private FlowPanel panel = new FlowPanel();
    private ScrollPanel scroll = new ScrollPanel(panel);
    private Map<Long, CustomTextBox> valueInputs = new LinkedHashMap<Long, CustomTextBox>();
    private CustomTextBox name;
    private SeriesDto item;
    private Btn cancel = new Btn("Отменить", EButtonStyle.DEFAULT);
    private Btn save = new Btn("Сохранить", EButtonStyle.PRIMARY);
    private ISaveClickHandler handler;

    public SeriesEditPopup(SeriesDto dto, Map<Long, String> periods, ISaveClickHandler handler) {
        super();
        this.handler = handler;
        setHeader(dto != null ? "Редатировать ряд" : "Добавить ряд");
        item = dto != null ? dto : new SeriesDto();
        scroll.addStyleName("series-edit-scroll");
        panel.addStyleName("row without-margin");
        modalBody.add(scroll);
        modalFooter.add(cancel);
        modalFooter.add(save);
        save.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                saveAction();
            }

        });
        ClickHandler closeHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                SeriesEditPopup.this.hide();
            }
        };
        close.addClickHandler(closeHandler);
        cancel.addClickHandler(closeHandler);
        generateContent(periods);

    }

    private void saveAction() {
        boolean commit = true;
        String nameValue = name.getValue();
        if (nameValue != null && !nameValue.isEmpty()) {
            item.setName(nameValue);
            name.setErrorStyle(false);
        }
        else {
            name.setErrorStyle(true);
            commit = false;
        }
        commit = getAllValues();
        if (commit && handler != null) {
            handler.click(SeriesEditPopup.this, item);
        }
        else {
            CustomDialogBox.showDialogBox(new CustomDialogBox("Внимание", "Все поля должны быть верно заполнены", EAlertType.WARNING));
        }
    }

    protected boolean getAllValues() {
        boolean commit = true;
        for (Entry<Long, CustomTextBox> entry : valueInputs.entrySet()) {
            CustomTextBox input = entry.getValue();
            String textValue = input.getValue();
            if (textValue != null && !textValue.isEmpty()) {
                Double value = null;
                try {
                    value = Double.parseDouble(textValue.replace(',', '.'));
                    item.getValues().put(entry.getKey(), value);
                    input.setErrorStyle(false);
                }
                catch (Exception e) {
                    input.setErrorStyle(true);
                    commit = false;
                }
            }
            else {
                input.setErrorStyle(true);
                commit = false;
            }
        }
        return commit;
    }

    private void generateContent(Map<Long, String> periods) {
        name = new CustomTextBox();
        KeyDownHandler enterPressHandler = new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) saveAction();
            }
        };
        name.getTextBox().addKeyDownHandler(enterPressHandler);
        name.setValue(item.getName());
        name.setCaption("Наименование ряда");
        name.addLabelStyleName("advanced-label");
        name.addInputStyleName("advanced-input");
        panel.add(name);
        for (Entry<Long, String> e : periods.entrySet()) {
            CustomTextBox input = new CustomTextBox();
            input.setCaption(e.getValue());
            input.setValue((item.getValues().get(e.getKey()) == null) ? "0.0" : item.getValues().get(e.getKey()).toString());
            input.addLabelStyleName("advanced-label");
            input.addInputStyleName("advanced-input");
            input.getTextBox().addKeyDownHandler(enterPressHandler);
            panel.add(input);
            valueInputs.put(e.getKey(), input);
        }
    }

    public void setHandler(ISaveClickHandler handler) {
        this.handler = handler;
    }
}
