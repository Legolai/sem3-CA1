package datafacades;

import entities.Hobby;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HobbyFacade implements IDataFacade<Hobby, String> {
    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Hobby create(String name, String description, String category, String type) {
        Hobby hobby = new Hobby();
        hobby.setName(name);
        hobby.setDescription(description);
        hobby.setCategory(category);
        hobby.setType(type);
        return create(hobby);
    }

    @Override
    public Hobby create(Hobby hobby) {
        executeInsideTransaction(em -> em.persist(hobby));
        return hobby;
    }

    @Override
    public Hobby getById(String name) {
        Hobby hobby = executeWithClose(em -> em.find(Hobby.class, name));
        if (hobby == null)
            throw new EntityNotFoundException("Hobby called " + name + " does not exists!");
        return hobby;
    }

    @Override
    public List<Hobby> getAll() {
        return executeWithClose(em -> {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h", Hobby.class);
            return query.getResultList();
        });
    }

    @Override
    public Hobby update(Hobby hobby) {
        if (getById(hobby.getName()) == null) throw new EntityNotFoundException();
        executeInsideTransaction(em -> em.merge(hobby));
        return hobby;
    }

    @Override
    public void delete(String name) {
        executeInsideTransaction(em -> {
            Hobby hobby = em.find(Hobby.class, name);
            if (hobby == null) throw new EntityNotFoundException("Hobby called " + name + " does not exists!");
            em.remove(hobby);
        });
    }

    public Map<String, Integer> getCountOfAllMembers() {
        return executeWithClose(em -> {
            TypedQuery<Object[]> query = em.createQuery("SELECT h.name, COUNT(h.people) FROM Hobby h GROUP BY h.name", Object[].class);
            return query.getResultList().stream().collect(Collectors.toMap(o -> String.valueOf(o[0]), o -> (int) Long.parseLong(String.valueOf(o[1]))));
        });
    }
    public Integer getCountOfMembersForHobby(String name) {
        return getCountOfAllMembers().get(name);
    }
}
