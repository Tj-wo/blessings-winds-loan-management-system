package com.alganiug.systems.loanManagement.views.dialogs;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import org.primefaces.PrimeFaces;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DialogForm<T extends BaseEntity> extends FormPresenter<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(DialogForm.class.getName());

    private String name;
    private int width;
    private int height;

    protected DialogForm(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    protected abstract GenericService<T> getService();

    public String show() {
        openDialog(null);
        return null;
    }

    public String edit(BaseEntity selectedModel) {
        openDialog(selectedModel);
        return null;
    }

    private void openDialog(BaseEntity selectedModel) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", height);
        options.put("contentWidth", width);

        Map<String, java.util.List<String>> parameters = new HashMap<>();
        if (selectedModel != null && selectedModel.getId() != null) {
            parameters.put("entityId", Collections.singletonList(selectedModel.getId().toString()));
        }
        PrimeFaces.current().dialog().openDynamic(name, options, parameters);
    }

    @PostConstruct
    public void initializeDialogModel() {
        String entityId = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("entityId");
        if (entityId == null || entityId.trim().isEmpty()) {
            return;
        }
        try {
            getService().getInstanceById(UUID.fromString(entityId)).ifPresent(entity -> {
                model = entity;
                setFormProperties();
            });
        } catch (IllegalArgumentException exception) {
            LOGGER.log(Level.WARNING, "Invalid dialog entity id: " + entityId, exception);
        }
    }

    @Override
    public void persist() {
        model = getService().saveInstance(model);
    }

    @Override
    public void save() {
        try {
            persist();
            LOGGER.log(Level.INFO, "Saved {0} with id {1}",
                    new Object[]{model.getClass().getSimpleName(), model.getId()});
            hide();
        } catch (RuntimeException exception) {
            String entityName = model == null ? "record" : model.getClass().getSimpleName();
            LOGGER.log(Level.SEVERE, "Failed to save " + entityName, exception);
            MessageComposer.error("Unable to save " + entityName, rootMessage(exception));
        }
    }

    public void hide() {
        PrimeFaces.current().dialog().closeDynamic(model);
    }

    public void cancel() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    private String rootMessage(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }

        String message = rootCause.getMessage();
        return message == null || message.trim().isEmpty()
                ? "The operation failed. Check the server log for details."
                : message;
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