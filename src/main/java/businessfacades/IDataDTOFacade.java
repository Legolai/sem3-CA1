package businessfacades;

import java.util.List;

public interface IDataDTOFacade<T> {

    T create(T t);
    <P> T getById(P p); //throws EntityNotFoundException;
    List<T> getAll();
    T update(T t); // throws EntityNotFoundException;
    <P> void delete(P p); // throws EntityNotFoundException;

}
