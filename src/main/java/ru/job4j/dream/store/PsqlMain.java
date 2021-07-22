package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.time.LocalDate;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job", ""));
        Post job = new Post(1, "Java Job", "We are looking for Java developer", LocalDate.now().toString());
        store.save(job);
        System.out.println("Vacancies :");
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " "
                    + post.getName() + " "
                    + post.getDescription() + " "
                    + post.getCreated());
        }
        Candidate junior = new Candidate(1, "Java junior");
        store.save(new Candidate(0, "Java junior"));
        junior.setPhoto("1.png");
        store.save(junior);
        System.out.println("Candidates :");
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " "
                    + candidate.getName() + " "
                    + candidate.getPhoto());
        }
    }
}
