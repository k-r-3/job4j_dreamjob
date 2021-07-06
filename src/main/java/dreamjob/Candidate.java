package dreamjob;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class Candidate {
    private final CopyOnWriteArrayList<Integer> vacancyId = new CopyOnWriteArrayList<>();
    private final int id;
    private final String name;
    private final String resume;

    public Candidate(int id, String name, String resume) {
        this.id = id;
        this.name = name;
        this.resume = resume;
    }

    public String getResume() {
        return resume;
    }

    public void accept(ConcurrentHashMap<Integer, String> vacancies, Predicate<String> pred) {
        for (Map.Entry<Integer, String> vacancy : vacancies.entrySet()) {
            if (pred.test(vacancy.getValue())) {
                vacancyId.add(vacancy.getKey());
            }
        }
    }
}
