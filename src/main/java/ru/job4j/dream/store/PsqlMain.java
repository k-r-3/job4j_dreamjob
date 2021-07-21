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
        User boris = new User();
        boris.setName("Boris");
        boris.setEmail("e@mail.com");
        boris.setPassword("****");
        store.save(boris);
        User ivan = new User();
        ivan.setName("Ivan");
        ivan.setEmail("e@mail.com");
        ivan.setPassword("****");
        store.save(ivan);
        for (User user : store.findAllUsers()) {
            System.out.println(user);
        }
    }
}
