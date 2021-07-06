package dreamjob;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class HR {
    private final ConcurrentHashMap<Integer, String> vacancies = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Candidate> candidates = new CopyOnWriteArrayList<>();
    private final AtomicInteger id = new AtomicInteger();

    public void toPublish(String vacancy) {
        vacancies.putIfAbsent(id.incrementAndGet(), vacancy);
    }

    public ConcurrentHashMap<Integer, String> getVacancies() {
        ConcurrentHashMap<Integer, String> copy = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, String> vacancy : vacancies.entrySet()) {
            copy.putIfAbsent(vacancy.getKey(), vacancy.getValue());
        }
        return copy;
    }

    public void accept(Candidate candidate, Predicate<String> pred) {
        if (pred.test(candidate.getResume())) {
            candidates.add(candidate);
        }
    }
}
