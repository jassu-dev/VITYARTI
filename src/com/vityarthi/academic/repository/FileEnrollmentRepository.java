package com.vityarthi.academic.repository;

import com.vityarthi.academic.model.Enrollment;
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
import java.util.stream.Collectors;

/**
 * File-backed persistent implementation of EnrollmentRepository.
 */
public class FileEnrollmentRepository implements Repository<Enrollment, String> {

    private final Map<String, Enrollment> storage = new ConcurrentHashMap<>();
    private final File dataFile;

    public FileEnrollmentRepository() {
        String dataDir = ConfigManager.getInstance().getDataDirectory();
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.dataFile = new File(dir, "enrollments.dat");
        loadFromFile();
    }

    public FileEnrollmentRepository(String customFilePath) {
        this.dataFile = new File(customFilePath);
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        loadFromFile();
    }

    @Override
    public Enrollment save(Enrollment entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }
        storage.put(entity.getEnrollmentId(), entity);
        flush();
        return entity;
    }

    @Override
    public Optional<Enrollment> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    public List<Enrollment> findByStudentId(String studentId) {
        if (studentId == null) return List.of();
        return storage.values().stream()
                .filter(e -> e.getStudentId().equalsIgnoreCase(studentId.trim()))
                .collect(Collectors.toList());
    }

    public List<Enrollment> findByCourseCode(String courseCode) {
        if (courseCode == null) return List.of();
        return storage.values().stream()
                .filter(e -> e.getCourseCode().equalsIgnoreCase(courseCode.trim()))
                .collect(Collectors.toList());
    }

    public Optional<Enrollment> findByStudentAndCourse(String studentId, String courseCode) {
        if (studentId == null || courseCode == null) return Optional.empty();
        return storage.values().stream()
                .filter(e -> e.getStudentId().equalsIgnoreCase(studentId.trim())
                        && e.getCourseCode().equalsIgnoreCase(courseCode.trim())
                        && e.getStatus() != Enrollment.Status.DROPPED)
                .findFirst();
    }

    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null) return false;
        boolean removed = storage.remove(id) != null;
        if (removed) {
            flush();
        }
        return removed;
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) return false;
        return storage.containsKey(id);
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
            System.err.println("Warning: Could not flush enrollments to storage: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void loadFromFile() {
        if (!dataFile.exists() || dataFile.length() == 0) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            List<Enrollment> list = (List<Enrollment>) ois.readObject();
            storage.clear();
            for (Enrollment e : list) {
                storage.put(e.getEnrollmentId(), e);
            }
        } catch (Exception e) {
            System.err.println("Notice: Initializing fresh enrollment database. (" + e.getMessage() + ")");
        }
    }
}
