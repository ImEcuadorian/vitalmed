package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    private final HistoryRepository historyRepository;

    public ClinicalHistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public CompletableFuture<ClinicalHistory> addHistory(ClinicalHistory history) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                historyRepository.save(history);
                return history;
            } catch (Exception e) {
                throw new RuntimeException("Failed to add clinical history", e);
            }
        });
    }

    @Override
    public CompletableFuture<Optional<ClinicalHistory>> getHistoryById(String historyId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return historyRepository.findById(historyId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve clinical history", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<ClinicalHistory>> getAllHistories() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return historyRepository.findAll();
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve all clinical histories", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<ClinicalHistory>> getHistoryByPatient(String patientId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return historyRepository.findByPatientId(patientId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve clinical history for patient", e);
            }
        });
    }

    @Override
    public CompletableFuture<ClinicalHistory> upsertHistory(ClinicalHistory history) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<ClinicalHistory> existingHistoryOpt = historyRepository.findById(String.valueOf(history.id()));
                if (existingHistoryOpt.isPresent()) {
                    ClinicalHistory existingHistory = existingHistoryOpt.get();
                    ClinicalHistory newClinicalHistory = updateWith(history);
                    historyRepository.save(newClinicalHistory);
                    return existingHistory;
                } else {
                    historyRepository.save(history);
                    return history;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to upsert clinical history", e);
            }
        });
    }

    private ClinicalHistory updateWith(ClinicalHistory history) {
        return ClinicalHistory.builder().id(history.id())
                .patientId(history.patientId())
                .age(history.age())
                .sex(history.sex())
                .updatedAt(Instant.now())
                .build();
    }

    @Override
    public CompletableFuture<ClinicalHistory> updateHistory(String historyId, ClinicalHistory history) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<ClinicalHistory> existingHistoryOpt = historyRepository.findById(historyId);
                if (existingHistoryOpt.isPresent()) {
                    ClinicalHistory updatedHistory = updateWith(history);
                    historyRepository.save(updatedHistory);
                    return updatedHistory;
                } else {
                    throw new NoSuchElementException("Clinical history not found with ID: " + historyId);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to update clinical history", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteHistory(String historyId) {
        return CompletableFuture.runAsync(() -> {
            try {
                Optional<ClinicalHistory> existingHistoryOpt = historyRepository.findById(historyId);
                if (existingHistoryOpt.isPresent()) {
                    historyRepository.delete(historyId);
                } else {
                    throw new NoSuchElementException("Clinical history not found with ID: " + historyId);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete clinical history", e);
            }
        });
    }
}
