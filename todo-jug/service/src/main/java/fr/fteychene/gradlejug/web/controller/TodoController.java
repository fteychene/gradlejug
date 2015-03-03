package fr.fteychene.gradlejug.web.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fr.fteychene.gradlejug.model.TodoItem;
import fr.fteychene.gradlejug.repository.dao.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TodoItem> findAll() {
        return Lists.newArrayList(todoItemRepository.findAll());
    }

    @RequestMapping(value = "/put", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TodoItem save(@RequestBody TodoItem newTodoItem) {
        Preconditions.checkNotNull(newTodoItem);
        return todoItemRepository.save(newTodoItem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(value = "id") Long id) {
        Preconditions.checkNotNull(id);
        todoItemRepository.delete(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void update(@RequestBody TodoItem updateItem) {
        Preconditions.checkNotNull(updateItem);
        Preconditions.checkNotNull(updateItem.getId());
        todoItemRepository.save(updateItem);
    }

    @RequestMapping(value = "/markAll/{completed}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TodoItem> markAll(@PathVariable(value = "completed") boolean completed) {
        Preconditions.checkNotNull(completed);
        Iterable<TodoItem> items = todoItemRepository.findAll();
        items.forEach((item) -> item.setCompleted(completed));
        todoItemRepository.save(items);

        return Lists.newArrayList(items);
    }

    public TodoItemRepository getTodoItemRepository() {
        return todoItemRepository;
    }

    public void setTodoItemRepository(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }
}