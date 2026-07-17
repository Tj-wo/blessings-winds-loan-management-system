package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.base.BaseEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.UUID;

public abstract class EntityConverter<T extends BaseEntity> implements Converter<T> {

    private final String serviceExpression;
    private final Class<? extends GenericService> serviceType;

    protected EntityConverter(String serviceExpression, Class<? extends GenericService> serviceType) {
        this.serviceExpression = serviceExpression;
        this.serviceType = serviceType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            GenericService<T> service = (GenericService<T>) context.getApplication().evaluateExpressionGet(context,
                    serviceExpression, serviceType);
            return service.getInstanceById(UUID.fromString(value)).orElse(null);
        } catch (IllegalArgumentException exception) {
            throw new ConverterException("Invalid record identifier", exception);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, T value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
