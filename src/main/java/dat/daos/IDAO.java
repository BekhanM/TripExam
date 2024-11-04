package dat.daos;

import java.util.List;

public interface IDAO<T> {
    void create(T dto);
    List<T> getAll();
    T getById(Integer id);
    void update(T dto);
    void delete(Integer id);
}
