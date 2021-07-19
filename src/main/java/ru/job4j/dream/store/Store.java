package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {
    private static final Store INST = new Store();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
    private final static AtomicInteger POST_ID = new AtomicInteger(3);
    private final static AtomicInteger CANDIDATE_ID = new AtomicInteger(3);

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Learner Assistant, Java Backend",
                LocalDateTime.now().format(pattern)));
        posts.put(2, new Post(2, "Middle Java Job", "Java Application Developer - Intermediate",
                LocalDateTime.now().format(pattern)));
        posts.put(3, new Post(3, "Senior Java Job", "Are you a passionate Java Engineer with"
                + " experience building systems in a fast-paced agile environment?", LocalDateTime.now().format(pattern)));
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

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        post.setCreated(LocalDateTime.now().format(pattern));
        posts.put(post.getId(), post);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public Candidate findCandById(int id) {
        return candidates.get(id);
    }
}
