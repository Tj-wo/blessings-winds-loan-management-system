package com.alganiug.systems.loanManagement.views.security.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.security.PermissionService;
import com.alganiug.systems.loanManagement.core.services.security.RolePermissionService;
import com.alganiug.systems.loanManagement.core.services.security.RoleService;
import com.alganiug.systems.loanManagement.models.security.Permission;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RolePermission;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean(name = "roleFormDialog")
@ViewScoped
public class RoleFormDialog extends DialogForm<Role> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{roleServiceImpl}")
    private RoleService service;

    @ManagedProperty(value = "#{permissionServiceImpl}")
    private PermissionService permissionService;

    @ManagedProperty(value = "#{rolePermissionServiceImpl}")
    private RolePermissionService rolePermissionService;

    private List<Permission> availablePermissions = Collections.emptyList();
    private List<Permission> selectedPermissions = new ArrayList<>();
    private Map<Object, Boolean> permissionSelections = new LinkedHashMap<>();

    public RoleFormDialog() {
        super(LoanManagementHyperLinks.ROLE_FORM_DIALOG, 800, 650);
        resetModal();
    }

    @Override
    protected RoleService getService() {
        return service;
    }

    @Override
    public void persist() {
        model = service.saveInstance(model);
        selectedPermissions = availablePermissions.stream()
                .filter(permission -> Boolean.TRUE.equals(permissionSelections.get(permission.getId())))
                .collect(Collectors.toCollection(ArrayList::new));
        rolePermissionService.synchronize(model, selectedPermissions);
    }

    @Override
    public void setFormProperties() {
        super.setFormProperties();
        loadPermissions();
        selectedPermissions = rolePermissionService.getByRole(model).stream().map(RolePermission::getPermission)
                .collect(Collectors.toCollection(ArrayList::new));
        preparePermissionSelections();
    }

    @Override
    public void resetModal() {
        model = new Role();
        editing = false;
        selectedPermissions = new ArrayList<>();
        loadPermissions();
        preparePermissionSelections();
    }

    private void loadPermissions() {
        availablePermissions = permissionService == null ? Collections.emptyList()
                : permissionService.getAllInstances();
    }

    private void preparePermissionSelections() {
        permissionSelections = new LinkedHashMap<>();
        for (Permission permission : availablePermissions) {
            permissionSelections.put(permission.getId(), selectedPermissions.contains(permission));
        }
    }

    public Map<Object, Boolean> getPermissionSelections() {
        return permissionSelections;
    }

    public List<Permission> getAvailablePermissions() {
        return availablePermissions;
    }

    public List<Permission> getSelectedPermissions() {
        return selectedPermissions;
    }

    public void setSelectedPermissions(List<Permission> selectedPermissions) {
        this.selectedPermissions = selectedPermissions;
    }

    public void setService(RoleService service) {
        this.service = service;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void setRolePermissionService(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }
}
