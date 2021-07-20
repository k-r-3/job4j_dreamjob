package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties config = new Properties();
        try (BufferedReader in = new BufferedReader(new FileReader("db.properties"))) {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.setDriverClassName(config.getProperty("jdbc.driver"));
        pool.setUrl(config.getProperty("jdbc.url"));
        pool.setUsername(config.getProperty("jdbc.username"));
        pool.setPassword(config.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        try {
            Class.forName(config.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static final class Lazy {
        private static final PsqlStore INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    posts.add(new Post(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("descr"),
                            result.getString("created")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Candidate candidate = new Candidate(
                            result.getInt("id"),
                            result.getString("name"));
                    candidate.setPhoto(result.getString("photo"));
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("INSERT INTO post(name, descr, created) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stat.setString(1, post.getName());
            stat.setString(2, post.getDescription());
            stat.setString(3, post.getCreated());
            stat.execute();
            try (ResultSet id = stat.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stat.setString(1, candidate.getName());
            stat.execute();
            try (ResultSet id = stat.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("SELECT * FROM post WHERE id =" + id)
        ) {
            try (ResultSet item = stat.executeQuery()) {
                if (item.next()) {
                    post = new Post(item.getInt("id"),
                            item.getString("name"),
                            item.getString("descr"),
                            item.getString("created"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public Candidate findCandById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("SELECT * FROM candidate WHERE id =" + id)
        ) {
            try (ResultSet item = stat.executeQuery()) {
                if (item.next()) {
                    candidate = new Candidate(item.getInt("id"),
                            item.getString("name"));
                    candidate.setPhoto(item.getString("photo"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement(
                     "UPDATE post "
                             + "SET name = ?, descr = ?, created = ? "
                             + "WHERE id =" + post.getId())
        ) {
            stat.setString(1, post.getName());
            stat.setString(2, post.getDescription());
            stat.setString(3, post.getCreated());
            stat.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement(
                     "UPDATE candidate "
                             + "SET name = ?, "
                             + "photo = ?"
                             + "WHERE id =" + candidate.getId())
        ) {
            stat.setString(1, candidate.getName());
            stat.setString(2, candidate.getPhoto());
            stat.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
