package businessfacades;

import java.util.List;

public interface IDataDTOFacade<T, I> {

    T create(T t);
    T getById(I id); //throws EntityNotFoundException;
    List<T> getAll();
    T update(T t); // throws EntityNotFoundException;
    void delete(I id); // throws EntityNotFoundException;

}
