package com.alganiug.systems.loanManagement.models.security;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(name = "uk_permission_code", columnNames = "code"))
public class Permission extends BaseEntity {

    @Column(name = "code", nullable = false, length = 100)

    private String code;

    @Column(name = "name", nullable = false, length = 150)

    private String name;

    @Column(name = "description", length = 255)

    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
