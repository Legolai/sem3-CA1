package datafacades;

import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonFacade implements IDataFacade<Person, Integer> {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    public static PersonFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Person create(String email, String firstName, String lastName, Address address, Set<Phone> phones, List<String> hobbies) {
        Person person = new Person();
        person.setEmail(email);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        phones.forEach(phone -> phone.setPerson(person));
        person.setPhones(phones);
        person.setHobbies(executeWithClose(em -> {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE h.name IN :hobbies", Hobby.class);
            query.setParameter("hobbies", hobbies);
            return query.getResultStream().collect(Collectors.toSet());
        }));
        return create(person);
    }

    public Person create(String email, String firstName, String lastName, Address address, Set<Phone> phones, Set<Hobby> hobbies) {
        Person person = new Person();
        person.setEmail(email);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        phones.forEach(phone -> phone.setPerson(person));
        person.setPhones(phones);
        person.setHobbies(hobbies);
        return create(person);
    }

    @Override
    public Person create(Person person) {
        executeInsideTransaction(em -> em.persist(person));
        return person;
    }

    @Override
    public Person getById(Integer id) {
        return executeWithClose(em -> em.find(Person.class, id));
    }


    public Person getByPhoneNumber(String phoneNumber) {
        return executeWithClose(em -> {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.phones ph where ph.number = :number", Person.class);
            query.setParameter("number", phoneNumber);
            return query.getSingleResult();
        });
    }

    public List<Person> getAllByHobby(String hobbyName) {
        return executeWithClose(em -> {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h where h.name = :hobby", Person.class);
            query.setParameter("hobby", hobbyName);
            return query.getResultList();
        });
    }

    public List<Person> getAllByCityInfo(String zipCode) {
        return executeWithClose(em -> {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address a JOIN a.cityInfo c WHERE c.zipCode = :zipCode", Person.class);
            query.setParameter("zipCode", zipCode);
            return query.getResultList();
        });
    }

    @Override
    public List<Person> getAll() {
        return executeWithClose(em -> {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            return query.getResultList();
        });
    }

    @Override
    public Person update(Person person) {
        executeInsideTransaction(em -> {
            Person toUpdate = em.find(Person.class, person.getId());
            if (toUpdate == null ) throw new EntityNotFoundException();
            toUpdate.setFirstName(person.getFirstName());
            toUpdate.setLastName(person.getLastName());
            toUpdate.setEmail(person.getEmail());
            toUpdate.setAddress(person.getAddress());
            toUpdate.setHobbies(person.getHobbies());
            toUpdate.setPhones(person.getPhones());
            em.merge(person);
        });
        return person;
    }

    @Override
    public void delete(Integer id) {
        executeInsideTransaction(em -> {
            Person p = em.find(Person.class, id);
            if (p == null) throw new EntityNotFoundException();
            em.remove(p);
        });
    }
}
