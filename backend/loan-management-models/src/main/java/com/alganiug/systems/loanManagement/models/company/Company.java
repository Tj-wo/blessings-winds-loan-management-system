package com.alganiug.systems.loanManagement.models.company;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.CompanyStatus;

import javax.persistence.*;

@Entity
@Table(name = "companies", uniqueConstraints = {
        @UniqueConstraint(name = "uk_company_code", columnNames = "company_code"),
        @UniqueConstraint(name = "uk_company_registration", columnNames = "registration_number"),
        @UniqueConstraint(name = "uk_company_onboarding", columnNames = "onboarding_code") })
public class Company extends BaseEntity {

    @Column(name = "company_code", length = 30)

    private String companyCode;

    @Column(name = "name", nullable = false, length = 150)

    private String name;

    @Column(name = "registration_number", length = 80)

    private String registrationNumber;

    @Column(name = "physical_address", nullable = false, length = 255)

    private String physicalAddress;

    @Column(name = "contact_person", nullable = false, length = 120)

    private String contactPerson;

    @Column(name = "contact_email", nullable = false, length = 150)

    private String contactEmail;

    @Column(name = "contact_phone", nullable = false, length = 30)

    private String contactPhone;

    @Column(name = "onboarding_code", nullable = false, length = 120)

    private String onboardingCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)

    private CompanyStatus status = CompanyStatus.ACTIVE;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String v) {
        companyCode = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String v) {
        registrationNumber = v;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String v) {
        physicalAddress = v;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String v) {
        contactPerson = v;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String v) {
        contactEmail = v;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String v) {
        contactPhone = v;
    }

    public String getOnboardingCode() {
        return onboardingCode;
    }

    public void setOnboardingCode(String v) {
        onboardingCode = v;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus v) {
        status = v;
    }
}
