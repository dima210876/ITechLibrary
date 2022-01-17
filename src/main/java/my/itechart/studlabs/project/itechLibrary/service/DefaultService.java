package my.itechart.studlabs.project.itechLibrary.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface DefaultService<T>
{
    List<T> findAll();

    int getCountOfPages();

    T findById(long id);

    T create(T entity);

    T update(T entity);

    default List<T> checkOptionalList(Optional<List<T>> entities) { return entities.orElseGet(ArrayList::new); }
}
