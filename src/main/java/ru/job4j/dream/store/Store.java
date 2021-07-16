package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private static final Store INST = new Store();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Learner Assistant, Java Backend",
                LocalDateTime.now().format(pattern)));
        posts.put(2, new Post(2, "Middle Java Job", "Java Application Developer - Intermediate",
                LocalDateTime.now().format(pattern)));
        posts.put(3, new Post(3, "Senior Java Job", "Are you a passionate Java Engineer with"
                +" experience building systems in a fast-paced agile environment?", LocalDateTime.now().format(pattern)));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }
}
