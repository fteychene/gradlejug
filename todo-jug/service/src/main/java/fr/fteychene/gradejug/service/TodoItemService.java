package fr.fteychene.gradejug.service
        ;

import fr.fteychene.gradlejug.model.TodoItem;

import java.util.Iterator;

/**
 * Created by fteychen on 09/03/2015.
 */
public interface TodoItemService {

    Iterable<TodoItem> findAll();

    TodoItem save(TodoItem newTodoItem);

    Iterable<TodoItem> save(Iterable<TodoItem> newTodoItem);

    void delete(Long id);
}
