package datafacades;

import entities.Hobby;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public class HobbyFacade implements IDataFacade<Hobby> {
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
    public <String> Hobby getById(String name) {
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
    public <String> void delete(String name) {
        executeInsideTransaction(em -> {
            Hobby hobby = em.find(Hobby.class, name);
            if (hobby == null) throw new EntityNotFoundException("Hobby called " + name + " does not exists!");
            em.remove(hobby);
        });
    }
}
