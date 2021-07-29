package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    Collection<City> findAllCity();

    Map<Integer, String> cityOfCandidate();

    void save(Post post);

    void save(Candidate candidate);

    void save(User user);

    Post findById(int id);

    Candidate findCandById(int id);

    User findByEmail(String email);

}
