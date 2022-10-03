package datafacades;

import entities.CityInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public class CityInfoFacade implements IDataFacade<CityInfo, String>{

    private static CityInfoFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CityInfoFacade() {}

    public static CityInfoFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CityInfoFacade();
        }
        return instance;
    }
    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CityInfo create(String zipCode, String city) {
        CityInfo cityInfo = new CityInfo(zipCode,city);
        create(cityInfo);
        return cityInfo;
    }
    @Override
    public CityInfo create(CityInfo cityInfo) {
        executeInsideTransaction((em) -> {
            em.persist(cityInfo);
        });
        return cityInfo;    //TODO: errorhandling, in general all methods needs
    }

    @Override
    public CityInfo getById(String zipCode) {
        CityInfo ci =  executeWithClose((em) -> em.find(CityInfo.class, zipCode));
        if (ci == null)
            throw new EntityNotFoundException("The CityInfo entity with zipCode: "+zipCode+" Was not found");
        return ci;
    }

    @Override
    public List<CityInfo> getAll() {
        return executeWithClose((em) -> {
            TypedQuery<CityInfo> query = em.createQuery("SELECT ci FROM CityInfo ci", CityInfo.class);
            List<CityInfo> cityInfos = query.getResultList();
            return cityInfos;
        });
    }

    @Override
    public CityInfo update(CityInfo cityInfo) {
        if (cityInfo.getZipCode() == null)
            throw new IllegalArgumentException("No CityInfo can be updated when zipCode is missing");
        CityInfo check = executeWithClose((em) -> em.find(CityInfo.class, cityInfo.getZipCode()));
        if (check == null)
            throw new IllegalArgumentException("No CityInfo with the zipCode: "+cityInfo.getZipCode());

        executeInsideTransaction((em) -> {
            em.merge(cityInfo);
        });
        return cityInfo;
    }

    @Override
    public void delete(String zipCode) {
        executeInsideTransaction((em) -> {
            CityInfo ci =  em.find(CityInfo.class, zipCode);
            if (ci == null)
                throw new EntityNotFoundException("The CityInfo entity with zipCode: "+zipCode+" Was not found");
            em.remove(ci);
        });
    }
}
