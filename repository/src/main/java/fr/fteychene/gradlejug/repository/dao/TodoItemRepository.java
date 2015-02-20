package fr.fteychene.gradlejug.repository.dao;

import fr.fteychene.gradlejug.model.TodoItem;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by fteychene on 06/02/2015.
 */
public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {

}
