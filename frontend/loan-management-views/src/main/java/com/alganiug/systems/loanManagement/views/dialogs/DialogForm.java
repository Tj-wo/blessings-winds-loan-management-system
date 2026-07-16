package com.alganiug.systems.loanManagement.views.dialogs;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import org.primefaces.PrimeFaces;

import javax.faces.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public abstract class DialogForm<T extends BaseEntity> extends FormPresenter<T> {

    private static final long serialVersionUID = 1L;

    private String name;
    private int width;
    private int height;

    protected DialogForm(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    protected abstract GenericService<T> getService();

    public void show(ActionEvent actionEvent) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", height);
        options.put("contentWidth", width);
        options.put("widgetVar", name);
        PrimeFaces.current().dialog().openDynamic(name, options, null);
    }

    @Override
    public void persist() {
        model = getService().saveInstance(model);
    }

    @Override
    public void save() {
        try {
            persist();
            MessageComposer.info("Action successful", "The record was saved successfully");
            hide();
        } catch (RuntimeException exception) {
            MessageComposer.error("Action failed", exception.getMessage());
        }
    }

    public void hide() {
        PrimeFaces.current().dialog().closeDynamic(model);
    }

    @Override
    public void setFormProperties() {
        editing = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
