package com.pstu.dtl.client.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * Чтобы заработало нужно задать размер для .uploadButton и .uploadButton BUTTON
 */
public abstract class AbstractFileUploadButton extends Composite {

    static int i = 0;

    public class SubmitFile {
        String name;
        long size; // в байах

        public SubmitFile(String name, long size) {
            super();
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return name + ":" + size;
        }

    }

    class UploadForm extends FormPanel {
        public final FileUpload fileChooser = new FileUpload();
        final int id = i++;

        public UploadForm() {
            setAction(submitUrl);
            setEncoding(FormPanel.ENCODING_MULTIPART);
            setMethod(FormPanel.METHOD_POST);
            add(fileChooser);

            fileChooser.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event) {
                    if (fileChooser.getFilename() != null && !fileChooser.getFilename().isEmpty())
                        AbstractFileUploadButton.this.submit(id, getFileInfo());
                }
            });

            addSubmitCompleteHandler(new SubmitCompleteHandler() {
                public void onSubmitComplete(SubmitCompleteEvent event) {
                    AbstractFileUploadButton.this.onCompleteSubmit(id, getFileInfo(), event.getResults());
                    UploadForm.this.setStyleName("used");
                    UploadForm.this.removeFromParent();
                }
            });

            fileChooser.setName("file");
            fileChooser.getElement().getStyle().setWidth(120, Unit.PX);
            fileChooser.getElement().getStyle().setLineHeight(0, Unit.PX);
            fileChooser.getElement().getStyle().setCursor(Cursor.POINTER);
            setMultiple(multiple);
            this.getElement().getStyle().setMargin(0, Unit.PX);
        }

        public void setMultiple(boolean multiple) {
            if (multiple)
                fileChooser.getElement().setPropertyString("multiple", "multiple");
            else
                fileChooser.getElement().removeAttribute("multiple");
        }

        public List<SubmitFile> getFileInfo() {
            List<SubmitFile> res = new ArrayList<SubmitFile>();
            String s = getFilesInfo(fileChooser.getElement());
            String[] fileAndSizeList = s.split("\\|");
            for (String string : fileAndSizeList) {
                if (string.length() > 0) {
                    res.add(new SubmitFile(string.split(":")[0], Long.parseLong(string.split(":")[1])));
                }
            }
            return res;
        }
    }

    private final String submitUrl;
    // private Button button = new Button();
    protected FlowPanel panel = new FlowPanel();
    protected UploadForm form;

    private boolean multiple = true;

    private boolean enabled = true;
    protected Panel customPanel;

    /**
     * Не прятать эту кнопку до тех пор пока загрузка не завершится (а то не
     * отработает перехватчик завершения)
     */
    public AbstractFileUploadButton(String submitUrl) {
        this.submitUrl = submitUrl;
        form = new UploadForm();
        form.fileChooser.setEnabled(enabled);
        panel.add(form);
        panel.getElement().getStyle().setOverflow(Overflow.HIDDEN);
        initWidget(panel);
        setStyleName("uploadButton");
    }

    public AbstractFileUploadButton(String submitUrl, Panel myPanel, String style) {
        this(submitUrl);
        this.customPanel = myPanel;
        panel.add(myPanel);
        setStyleName(style);

    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
        form.setMultiple(multiple);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        form.fileChooser.setEnabled(enabled);
    }

    private void submit(int submitId, List<SubmitFile> files) {
        if (onBeforeSubmit(submitId, files)) {
            UploadForm oldForm = form;
            form.setVisible(false);
            form = new UploadForm();
            form.getElement().getStyle().setMargin(0, Unit.PX);
            form.fileChooser.setEnabled(enabled);
            panel.add(form);

            onStartSubmit(submitId, files);

            oldForm.submit();
        }
    }

    protected abstract void onStartSubmit(int submitId, List<SubmitFile> files);

    /**
     * Данные поехали на сервер
     * 
     * @param submitId - идентефикатор отправления
     * @param files - файлы которые отправили на сервер
     * @return true - если начать отправку файлов, false - если отменить
     */
    protected abstract boolean onBeforeSubmit(int submitId, List<SubmitFile> files);

    /**
     * Завершилась обработка данных на сервере
     * 
     * @param submitId - идентефикатор отправления
     * @param files - файлы которые отправили на сервер
     * @param result - ответ сервера
     */
    protected abstract void onCompleteSubmit(int submitId, List<SubmitFile> files, String result);

    private native String getFilesInfo(Element input) /*-{
                                                      var s = "";
                                                      if (input.files != null)
                                                      for ( var i = 0; i < input.files.length; i++) {
                                                      s = s + input.files[i].name + ":" + input.files[i].size + "|";
                                                      }
                                                      return s;
                                                      }-*/;

    // private static native void clickOnInputFile(Element elem) /*-{
    // elem.click();
    // }-*/;
}
