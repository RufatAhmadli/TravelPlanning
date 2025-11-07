package edu.az.example.web.travelplanning;

import java.time.LocalDateTime;
import edu.az.example.web.travelplanning.model.entity.BaseEntity;

public interface TestAuditable {
    default void assignAuditFields(Object entity) {

        if (entity instanceof BaseEntity auditable) {
            auditable.setCreatedAt(LocalDateTime.now());
            auditable.setUpdatedAt(LocalDateTime.now());
            auditable.setCreatedBy("test-user");
            auditable.setUpdatedBy("test-user");
        }
    }
}
