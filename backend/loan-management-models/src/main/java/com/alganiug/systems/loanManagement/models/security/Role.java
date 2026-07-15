package com.alganiug.systems.loanManagement.models.security;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "uk_role_name", columnNames = "name"))
public class Role extends BaseEntity {
    @Column(name = "name", nullable = false, length = 50)

    private String name;

    @Column(name = "description", length = 255)

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }
}
