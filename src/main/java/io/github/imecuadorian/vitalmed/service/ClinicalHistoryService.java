package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;

public interface ClinicalHistoryService {
    ClinicalHistory addHistory(ClinicalHistory history);
    Optional<ClinicalHistory> getHistoryById(String historyId);
    List<ClinicalHistory> getAllHistories();
    List<ClinicalHistory> getHistoryByPatient(String patientId);
    ClinicalHistory upsertHistory(ClinicalHistory history);
    ClinicalHistory updateHistory(String historyId, ClinicalHistory history);
    void deleteHistory(String historyId);
}
