package com.alganiug.systems.loanManagement.core.services.employee;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.employee.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentService extends GenericService<Document> {

    List<Document> getDocumentsForEmployee(UUID employeeId);
}
