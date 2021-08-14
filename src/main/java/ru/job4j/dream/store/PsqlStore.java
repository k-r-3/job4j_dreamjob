package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties config = new Properties();
        try (BufferedReader in = new BufferedReader(new FileReader("db.properties"))) {
            config.load(in);
        } catch (IOException e) {
            LOG.debug("db.properties read exception", e);
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
            LOG.debug("find all posts exception", e);
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
            LOG.debug("find all candidates exception", e);
        }
        return candidates;
    }

    @Override
    public Collection<City> findAllCity() {
        List<City> cityList = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM city")
        ) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    City city = new City();
                    city.setId(result.getInt("id"));
                    city.setName(result.getString("name"));
                    cityList.add(city);
                }
            }
        } catch (Exception e) {
            LOG.debug("find all city exception", e);
        }
        return cityList;
    }

    @Override
    public Map<Integer, String> cityOfCandidate() {
        Map<Integer, String> map = new HashMap<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT cand.id, c.name FROM city c"
                     + " JOIN candidate cand"
                     + " ON c.id = cand.city_id")
        ) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    map.putIfAbsent(id, name);
                }
            }
        } catch (SQLException e) {
            LOG.debug("select candidate city exception", e);
        }
        return map;
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

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private User create(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("INSERT INTO users(name, email, pass) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stat.setString(1, user.getName());
            stat.setString(2, user.getEmail());
            stat.setString(3, user.getPassword());
            stat.execute();
            try (ResultSet id = stat.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            Set<ConstraintViolation<String>> set = new HashSet<>();
            throw new ConstraintViolationException("пользователь с email "
                    + user.getEmail() + " уже существует", set);
        }
        return user;
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("INSERT INTO post(name, descr, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
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
            LOG.debug("create post exception", e);
        }
        return post;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("INSERT INTO candidate(name, city_id) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stat.setString(1, candidate.getName());
            stat.setInt(2, candidate.getCityId());
            stat.execute();
            try (ResultSet id = stat.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.debug("create candidate exception", e);
        }
        return candidate;
    }

    private City create(City city) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("INSERT INTO city(name, cand_id) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stat.setString(1, city.getName());
            stat.setInt(2, city.getId());
            stat.execute();
            try (ResultSet id = stat.getGeneratedKeys()) {
                if (id.next()) {
                    city.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.debug("create city exception", e);
        }
        return city;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            stat.setInt(1, id);
            try (ResultSet item = stat.executeQuery()) {
                if (item.next()) {
                    post = new Post(item.getInt("id"),
                            item.getString("name"),
                            item.getString("descr"),
                            item.getString("created"));
                }
            }
        } catch (Exception e) {
            LOG.debug("find by ID post exception", e);
        }
        return post;
    }

    public Candidate findCandById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            stat.setInt(1, id);
            try (ResultSet item = stat.executeQuery()) {
                if (item.next()) {
                    candidate = new Candidate(item.getInt("id"),
                            item.getString("name"));
                    candidate.setPhoto(item.getString("photo"));
                    candidate.setCityId(item.getInt("city_id"));
                }
            }
        } catch (Exception e) {
            LOG.debug("find by ID candidates exception", e);
        }
        return candidate;
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement("SELECT * FROM users WHERE email = ?")
        ) {
            stat.setString(1, email);
            try (ResultSet item = stat.executeQuery()) {
                if (item.next()) {
                    user = new User();
                    String name = item.getString("name");
                    user.setId(item.getInt("id"));
                    user.setName(name);
                    user.setEmail(item.getString("email"));
                    user.setPassword(item.getString("pass"));
                }
            }
        } catch (Exception e) {
            LOG.debug("find by email user exception", e);
        }
        return user;
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement(
                     "UPDATE users "
                             + "SET name = ?, email = ?, pass = ? "
                             + "WHERE id =" + user.getId())
        ) {
            stat.setString(1, user.getName());
            stat.setString(2, user.getEmail());
            stat.setString(3, user.getPassword());
            stat.execute();
        } catch (Exception e) {
            LOG.debug("update user exception", e);
        }
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
        } catch (Exception e) {
            LOG.debug("update post exception", e);
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement stat = cn.prepareStatement(
                     "UPDATE candidate "
                             + "SET name = ?, "
                             + "photo = ?, "
                             + "city_id = ? "
                             + "WHERE id = ?")
        ) {
            stat.setString(1, candidate.getName());
            stat.setString(2, candidate.getPhoto());
            stat.setInt(3, candidate.getCityId());
            stat.setInt(4, candidate.getId());
            stat.execute();
        } catch (Exception e) {
            LOG.debug("update candidate exception", e);
        }
    }
}
