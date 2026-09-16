package com.vityarthi.academic.repository;

import com.vityarthi.academic.model.User;
import com.vityarthi.academic.util.ConfigManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File-backed persistent implementation of UserRepository.
 * Uses ConcurrentHashMap for O(1) in-memory lookups and Java Serialization for durability.
 */
public class FileUserRepository implements Repository<User, String> {

    private final Map<String, User> storage = new ConcurrentHashMap<>();
    private final File dataFile;

    public FileUserRepository() {
        String dataDir = ConfigManager.getInstance().getDataDirectory();
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.dataFile = new File(dir, "users.dat");
        loadFromFile();
    }

    public FileUserRepository(String customFilePath) {
        this.dataFile = new File(customFilePath);
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        loadFromFile();
    }

    @Override
    public User save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        storage.put(entity.getUserId().toLowerCase(), entity);
        flush();
        return entity;
    }

    @Override
    public Optional<User> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id.toLowerCase()));
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return storage.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null) return false;
        boolean removed = storage.remove(id.toLowerCase()) != null;
        if (removed) {
            flush();
        }
        return removed;
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) return false;
        return storage.containsKey(id.toLowerCase());
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public synchronized void flush() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(new ArrayList<>(storage.values()));
        } catch (IOException e) {
            System.err.println("Warning: Could not flush users to storage: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void loadFromFile() {
        if (!dataFile.exists() || dataFile.length() == 0) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            List<User> list = (List<User>) ois.readObject();
            storage.clear();
            for (User u : list) {
                storage.put(u.getUserId().toLowerCase(), u);
            }
        } catch (Exception e) {
            System.err.println("Notice: Initializing fresh user database. (" + e.getMessage() + ")");
        }
    }
}
