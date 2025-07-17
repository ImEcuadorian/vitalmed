package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;
import java.util.concurrent.*;

public interface ClinicalHistoryService {
    CompletableFuture<ClinicalHistory> addHistory(ClinicalHistory history);
    CompletableFuture<Optional<ClinicalHistory>> getHistoryById(String historyId);
    CompletableFuture<List<ClinicalHistory>> getAllHistories();
    CompletableFuture<List<ClinicalHistory>> getHistoryByPatient(String patientId);
    CompletableFuture<ClinicalHistory> upsertHistory(ClinicalHistory history);
    CompletableFuture<ClinicalHistory> updateHistory(String historyId, ClinicalHistory history);
    CompletableFuture<Void> deleteHistory(String historyId);
}
