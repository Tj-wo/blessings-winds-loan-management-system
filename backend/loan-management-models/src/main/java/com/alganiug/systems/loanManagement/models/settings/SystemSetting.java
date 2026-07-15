package com.alganiug.systems.loanManagement.models.settings;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.SettingValueType;
import javax.persistence.*;

@Entity
@Table(name = "system_settings", uniqueConstraints = @UniqueConstraint(name = "uk_setting_key", columnNames = "setting_key"))
public class SystemSetting extends BaseEntity {
    @Column(name = "setting_key", nullable = false, length = 120)

    private String settingKey;

    @Lob
    @Column(name = "setting_value", nullable = false)

    private String settingValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false, length = 20)

    private SettingValueType valueType = SettingValueType.STRING;

    @Column(name = "category", nullable = false, length = 80)

    private String category;

    @Column(name = "description", length = 500)

    private String description;

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String v) {
        settingKey = v;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String v) {
        settingValue = v;
    }

    public SettingValueType getValueType() {
        return valueType;
    }

    public void setValueType(SettingValueType v) {
        valueType = v;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String v) {
        category = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }
}
