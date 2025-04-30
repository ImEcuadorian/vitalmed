package io.github.imecuadorian.vitalmed.repository;

import io.github.imecuadorian.library.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public abstract class FileRepository<K, V> implements Repository<K, V> {

    private static final String BASE_FOLDER = "vitalmed";
    protected final Files fileManager;
    protected final Logger logger;

    protected FileRepository(String fileName, Logger logger) {
        this.logger = logger;

        try {
            Files baseFolder = new Files(BASE_FOLDER);
            baseFolder.createFile(FileType.DIRECTORY);
        } catch (IOException e) {
            logger.severe("Error creating base folder 'data': " + e.getMessage());
        }

        String fullPath = BASE_FOLDER + File.separator + fileName;
        this.fileManager = new Files(fullPath);

        try {
            fileManager.createFile(FileType.FILE);
        } catch (IOException e) {
            logger.severe("Error creating file '" + fullPath + "': " + e.getMessage());
        }
    }

    @Override
    public void save(V entity) {
        try {
            fileManager.writeFile(serialize(entity), false);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Entity saved: %s", getId(entity)));
            }
        } catch (IOException e) {
            logger.severe("Failed to save entity: " + e.getMessage());
        }
    }

    @Override
    public List<V> findAll() {
        List<V> entities = new ArrayList<>();
        try {
            String content = fileManager.readFile();
            String[] lines = content.split(System.lineSeparator());
            for (String line : lines) {
                if (!line.isBlank()) {
                    entities.add(deserialize(line));
                }
            }
        } catch (IOException e) {
            logger.severe("Failed to read file: " + e.getMessage());
        }
        return entities;
    }

    @Override
    public Optional<V> findById(K id) {
        return findAll().stream()
                .filter(entity -> getId(entity).equals(id))
                .findFirst();
    }

    @Override
    public void update(K id, V entity) {
        List<V> all = findAll();
        try {
            List<String> updatedLines = new ArrayList<>();
            for (V e : all) {
                if (getId(e).equals(id)) {
                    updatedLines.add(serialize(entity));
                } else {
                    updatedLines.add(serialize(e));
                }
            }
            fileManager.writeFile(String.join(System.lineSeparator(), updatedLines), true);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Entity updated: %s", id));
            }
        } catch (IOException e) {
            logger.severe("Failed to update entity: " + e.getMessage());
        }
    }

    @Override
    public void delete(K id) {
        List<V> all = findAll();
        all.removeIf(entity -> getId(entity).equals(id));
        try {
            List<String> updatedLines = new ArrayList<>();
            for (V e : all) {
                updatedLines.add(serialize(e));
            }
            fileManager.writeFile(String.join(System.lineSeparator(), updatedLines), true);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Entity deleted: %s", id));
            }
        } catch (IOException e) {
            logger.severe("Failed to delete entity: " + e.getMessage());
        }
    }

    protected abstract String serialize(V entity);
    protected abstract V deserialize(String line);
    protected abstract K getId(V entity);
}
