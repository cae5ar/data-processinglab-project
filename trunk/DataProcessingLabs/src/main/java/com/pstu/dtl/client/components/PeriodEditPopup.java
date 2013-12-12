package com.pstu.dtl.client.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.pstu.dtl.client.SimpleAsyncCallback;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.client.components.AlertDialogBox.EAlertType;
import com.pstu.dtl.shared.dto.PeriodDto;

public class PeriodEditPopup extends CustomPopup {

    public interface PeriodSaveHandler {
        public void save(List<PeriodDto> list, PeriodEditPopup sender);
    }

    private FlowPanel panel = new FlowPanel();
    private ScrollPanel scroll = new ScrollPanel(panel);

    private FlowPanel periodEditInputsPanel = new FlowPanel();
    private Map<CustomTextBox, Long> periodsMap = new LinkedHashMap<CustomTextBox, Long>();
    private Btn cancel = new Btn("Отменить", EButtonStyle.DEFAULT, new ClickHandler() {
        public void onClick(ClickEvent event) {
            PeriodEditPopup.this.hide();
        }
    });
    private PeriodSaveHandler handler = null;
    private Btn saveBtn = new Btn("Сохранить изменения", EButtonStyle.SUCCESS, new ClickHandler() {
        public void onClick(ClickEvent event) {
            saveAllChanges();
        }
    });
    private KeyDownHandler enterPressHandler = new KeyDownHandler() {
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) saveAllChanges();
        }
    };

    public PeriodEditPopup(PeriodSaveHandler handler) {
        super();
        addStyleName("period-edit-popup");
        setHandler(handler);
        setHeader("Редатирование периодов");
        Btn addButton = new Btn("<span class='glyphicon glyphicon-plus'></span><span style='margin-left:10px'>"
                + "Добавить элемент</span>", EButtonStyle.PRIMARY, new ClickHandler() {
            public void onClick(ClickEvent event) {
                createItem(new PeriodDto());
            }
        });
        SimplePanel bp = new SimplePanel(addButton);
        bp.addStyleName("row without-margin");
        periodEditInputsPanel.addStyleName("input");
        panel.add(periodEditInputsPanel);
        modalBody.add(bp);
        modalBody.add(scroll);
        scroll.addStyleName("period-edit-scroll");
        modalFooter.addStyleName("text-left");
        modalFooter.add(saveBtn);
        modalFooter.add(cancel);
        reset();
    }

    private void reset() {
        Site.service.getAllPeriods(new SimpleAsyncCallback<Map<Long, String>>() {
            public void onSuccess(Map<Long, String> result) {
                periodEditInputsPanel.clear();
                periodsMap.clear();
                generateContent(result);
            }
        });
    }

    private void generateContent(Map<Long, String> result) {
        for (Entry<Long, String> entrySet : result.entrySet()) {
            createItem(new PeriodDto(entrySet.getKey(), entrySet.getValue()));
        }
    }

    private void createItem(final PeriodDto dto) {
        final FlowPanel itemPanel = new FlowPanel();
        itemPanel.addStyleName("row without-margin period-edit-block");
        final CustomTextBox text = new CustomTextBox();
        text.addLabelStyleName("advanced-label empty");
        text.addInputStyleName("advanced-input");
        text.setValue(dto.getName());
        Btn remove = new Btn("<span class='glyphicon glyphicon-minus'></span>", EButtonStyle.LINK);
        DOM.sinkEvents(remove.getElement(), Event.ONCLICK);
        DOM.setEventListener(remove.getElement(), new EventListener() {
            public void onBrowserEvent(Event event) {
                if (dto.getId() != null) {
                    Site.service.deletePeriod(dto.getId(), new SimpleAsyncCallback<Void>() {
                        public void onSuccess(Void result) {
                            periodsMap.remove(text);
                            itemPanel.removeFromParent();
                            AlertDialogBox.showDialogBox("OK!", "Период успешно удален", EAlertType.SUCCESS);
                        }
                    });
                }
                else {
                    periodsMap.remove(text);
                    itemPanel.removeFromParent();
                }
            }
        });
        remove.addStyleName("period-remove-btn");
        text.setHelpBlock(remove.getElement());
        itemPanel.add(text);
        periodsMap.put(text, dto.getId());
        periodEditInputsPanel.add(itemPanel);
        text.getTextBox().addKeyDownHandler(enterPressHandler);
        text.setFocus();

    }

    protected void saveAllChanges() {
        if (handler != null) {
            List<PeriodDto> list = new ArrayList<PeriodDto>();
            for (Entry<CustomTextBox, Long> entrySet : periodsMap.entrySet()) {
                if (entrySet.getKey().getValue() != null && !entrySet.getKey().getValue().isEmpty())
                    list.add(new PeriodDto(entrySet.getValue(), entrySet.getKey().getValue()));
            }
            handler.save(list, PeriodEditPopup.this);
        }

    }

    public void setHandler(PeriodSaveHandler handler) {
        this.handler = handler;
    }
}
