package com.alganiug.systems.loanManagement.views.dialogs;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;

import java.io.Serializable;

public abstract class FormPresenter<T extends BaseEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected T model;
    protected boolean editing;

    public abstract void persist();

    public abstract void save();

    public abstract void resetModal();

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
        if (model == null) {
            resetModal();
        } else {
            setFormProperties();
        }
    }

    public void setFormProperties() {
        editing = true;
    }

    public boolean isEditing() {
        return editing;
    }
}
