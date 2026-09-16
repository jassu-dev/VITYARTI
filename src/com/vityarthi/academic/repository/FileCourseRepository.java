package com.vityarthi.academic.repository;

import com.vityarthi.academic.model.Course;
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
 * File-backed persistent implementation of CourseRepository.
 */
public class FileCourseRepository implements Repository<Course, String> {

    private final Map<String, Course> storage = new ConcurrentHashMap<>();
    private final File dataFile;

    public FileCourseRepository() {
        String dataDir = ConfigManager.getInstance().getDataDirectory();
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.dataFile = new File(dir, "courses.dat");
        loadFromFile();
    }

    public FileCourseRepository(String customFilePath) {
        this.dataFile = new File(customFilePath);
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        loadFromFile();
    }

    @Override
    public Course save(Course entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        storage.put(entity.getCourseCode().toUpperCase(), entity);
        flush();
        return entity;
    }

    @Override
    public Optional<Course> findById(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(storage.get(code.trim().toUpperCase()));
    }

    public List<Course> findByDepartment(String dept) {
        if (dept == null) return List.of();
        return storage.values().stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(dept.trim()))
                .collect(Collectors.toList());
    }

    public List<Course> findByInstructorId(String instructorId) {
        if (instructorId == null) return List.of();
        return storage.values().stream()
                .filter(c -> c.getInstructorId().equalsIgnoreCase(instructorId.trim()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(String code) {
        if (code == null) return false;
        boolean removed = storage.remove(code.trim().toUpperCase()) != null;
        if (removed) {
            flush();
        }
        return removed;
    }

    @Override
    public boolean existsById(String code) {
        if (code == null) return false;
        return storage.containsKey(code.trim().toUpperCase());
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
            System.err.println("Warning: Could not flush courses to storage: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void loadFromFile() {
        if (!dataFile.exists() || dataFile.length() == 0) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            List<Course> list = (List<Course>) ois.readObject();
            storage.clear();
            for (Course c : list) {
                storage.put(c.getCourseCode().toUpperCase(), c);
            }
        } catch (Exception e) {
            System.err.println("Notice: Initializing fresh course database. (" + e.getMessage() + ")");
        }
    }
}
