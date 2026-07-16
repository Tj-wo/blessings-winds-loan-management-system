package com.alganiug.systems.loanManagement.models;

import com.alganiug.systems.loanManagement.models.constants.RecordStatus;

import com.alganiug.systems.loanManagement.models.audit.AuditLog;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.loan.*;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import com.alganiug.systems.loanManagement.models.security.Permission;
import com.alganiug.systems.loanManagement.models.security.PermissionConstants;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.RolePermission;
import com.alganiug.systems.loanManagement.models.security.SystemPermission;
import com.alganiug.systems.loanManagement.models.security.SystemRole;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.models.settings.SystemSetting;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityMappingsTest {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.driver_class", "org.h2.Driver")
                .applySetting("hibernate.connection.url", "jdbc:h2:mem:loan_models;MODE=MySQL;DB_CLOSE_DELAY=-1")
                .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop").applySetting("hibernate.show_sql", "false")
                .build();

        MetadataSources sources = new MetadataSources(registry);
        Arrays.asList(Company.class, Employee.class, Permission.class, Role.class, RolePermission.class, User.class, LoanProduct.class, Loan.class,
                LoanApproval.class, Disbursement.class, RepaymentScheduleItem.class, Repayment.class,
                PenaltyCharge.class, PayrollDeductionBatch.class, PayrollDeduction.class, Document.class,
                Notification.class, AuditLog.class, SystemSetting.class).forEach(sources::addAnnotatedClass);
        sessionFactory = sources.buildMetadata().buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null)
            sessionFactory.close();
        if (registry != null)
            StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void allEntitiesProduceAValidSchema() {
        assertNotNull(sessionFactory);
        assertFalse(sessionFactory.getMetamodel().getEntities().isEmpty());
        assertTrue(sessionFactory.getMetamodel().getEntities().size() >= 19);
    }

    @Test
    void baseEntityCreatesUuidAuditFieldsAndSupportsSoftDeletion() {
        Company company = new Company();
        company.setCompanyCode("CMP-001");
        company.setName("Test Employer");
        company.setRegistrationNumber("REG-001");
        company.setPhysicalAddress("Kampala");
        company.setContactPerson("Test Contact");
        company.setContactEmail("test@alganiug.com");
        company.setContactPhone("+256700000001");
        company.setOnboardingCode("join-test-employer-0001");

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(company);
            session.getTransaction().commit();
        }

        assertNotNull(company.getId());
        assertNotNull(company.getCreatedAt());
        assertNotNull(company.getUpdatedAt());
        assertEquals(RecordStatus.ACTIVE, company.getRecordStatus());

        company.setRecordStatus(RecordStatus.DELETED);
        assertEquals(RecordStatus.DELETED, company.getRecordStatus());
    }

    @Test
    void permissionConstantsAreAnnotatedAndUnique() throws IllegalAccessException {
        Set<String> codes = new HashSet<>();
        int permissionCount = 0;

        for (Field field : PermissionConstants.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != String.class) {
                continue;
            }

            SystemPermission permission = field.getAnnotation(SystemPermission.class);
            assertNotNull(permission);
            assertFalse(permission.name().trim().isEmpty());
            assertFalse(permission.description().trim().isEmpty());
            assertTrue(codes.add((String) field.get(null)));
            permissionCount++;
        }

        assertEquals(79, permissionCount);
    }
    @Test
    void roleConstantsAreAnnotatedAndUnique() throws IllegalAccessException {
        Set<String> roles = new HashSet<>();
        int roleCount = 0;

        for (Field field : RoleConstants.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != String.class) {
                continue;
            }

            SystemRole role = field.getAnnotation(SystemRole.class);
            assertNotNull(role);
            assertFalse(role.name().trim().isEmpty());
            assertFalse(role.description().trim().isEmpty());
            assertTrue(roles.add((String) field.get(null)));
            roleCount++;
        }

        assertEquals(4, roleCount);
    }}
