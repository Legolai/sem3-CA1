package datafacades;

import entities.Person;
import entities.Phone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PhoneFacade implements IDataFacade<Phone, String> {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PhoneFacade() {
    }


    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PhoneFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Phone create(String number, String description, Integer personId) {
        Person person = executeWithClose(em -> em.find(Person.class, personId));
        Phone phone = new Phone(number, description, person);

        return create(phone);
    }

    @Override
    public Phone create(Phone phone) {
        executeInsideTransaction(em -> {
            em.persist(phone);
        });
        return phone;
    }

    @Override
    public Phone getById(String number) {
        return executeWithClose(em -> {
            Phone phone = em.find(Phone.class, number);
            if (phone == null)
                throw new EntityNotFoundException("No Phone can be found with the number:" + number);

            return phone;
        });
    }

    @Override
    public List<Phone> getAll() {
        return executeWithClose(em -> {
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p", Phone.class);
            return query.getResultList();
        });
    }

    @Override
    public Phone update(Phone phone) {

        if (phone.getNumber() == null)
            throw new IllegalArgumentException("No Phone can be updated when phone is missing");

        boolean numberFound = executeWithClose(em -> em.find(Phone.class, phone.getNumber()) == null);
        if (numberFound)
            throw new IllegalArgumentException("No Phone number found with number:" + phone.getNumber());

        executeInsideTransaction(em -> em.merge(phone));

        return phone;
    }

    @Override
    public void delete(String number) {
         executeInsideTransaction(em -> {
            if (number == null)
                throw new IllegalArgumentException("No Phone can be updated when number is missing");

            Phone phone = em.find(Phone.class, number);

            if (phone == null)
                throw new EntityNotFoundException("No Phone can be found with the number:" + number);

            em.remove(phone);
        });
    }
}
