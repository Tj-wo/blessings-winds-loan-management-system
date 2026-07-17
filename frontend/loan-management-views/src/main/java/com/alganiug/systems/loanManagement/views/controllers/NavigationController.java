package com.alganiug.systems.loanManagement.views.controllers;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "navigationController")
@SessionScoped
public class NavigationController implements Serializable {

    private static final long serialVersionUID = 1L;

    public String dashboard() {
        return LoanManagementHyperLinks.DASHBOARD;
    }

    public String loans() {
        return LoanManagementHyperLinks.LOANS_VIEW;
    }

    public String employees() {
        return LoanManagementHyperLinks.EMPLOYEES_VIEW;
    }

    public String companies() {
        return LoanManagementHyperLinks.COMPANIES_VIEW;
    }

    public String payroll() {
        return LoanManagementHyperLinks.PAYROLL_BATCHES_VIEW;
    }

    public String reports() {
        return LoanManagementHyperLinks.REPORTS;
    }

    public String profile() {
        return LoanManagementHyperLinks.PROFILE;
    }

    public String login() {
        return LoanManagementHyperLinks.LOGIN;
    }
}
