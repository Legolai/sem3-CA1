package datafacades;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IDataFacade<T, I> {
    EntityManager getEntityManager();

    T create(T t);
    T getById(I id); //throws EntityNotFoundException;
    List<T> getAll();
    T update(T t); // throws EntityNotFoundException;
    void delete(I id); // throws EntityNotFoundException;

    default  <R> R executeWithClose(Function<EntityManager, R> action) {
        EntityManager em = getEntityManager();
        R result = action.apply(em);
        em.close();
        return result;
    }
    default void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }
}
