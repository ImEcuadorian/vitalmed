package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.util.*;

public interface HistoryRepository extends CRUDRepository<String, ClinicalHistory> {
    List<ClinicalHistory> findByPatientId(String patientId);

    void upsert(ClinicalHistory history);
}
