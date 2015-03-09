package fr.fteychene.gradlejug.service.impl;

import fr.fteychene.gradejug.service.TodoItemService;
import fr.fteychene.gradlejug.model.TodoItem;
import fr.fteychene.gradlejug.repository.dao.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fteychen on 09/03/2015.
 */
@Service
public class TodoItemServiceImpl implements TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Override
    public Iterable<TodoItem> findAll() {
        return todoItemRepository.findAll();
    }

    @Override
    public TodoItem save(TodoItem newTodoItem) {
        return todoItemRepository.save(newTodoItem);
    }

    @Override
    public Iterable<TodoItem> save(Iterable<TodoItem> newTodoItems) {
        return todoItemRepository.save(newTodoItems);
    }

    @Override
    public void delete(Long id) {
        todoItemRepository.delete(id);
    }
}
