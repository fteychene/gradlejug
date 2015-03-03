package fr.fteychene.gradlejug.model;

import javax.persistence.*;

/**
 * Created by fteychene on 05/02/2015.
 */
@Entity
@Table(name = "TODO")
public class TodoItem implements Comparable<TodoItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean completed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public int compareTo(TodoItem toDoItem) {
        return this.getId().compareTo(toDoItem.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoItem todoItem = (TodoItem) o;

        if (completed != todoItem.completed) return false;
        if (!id.equals(todoItem.id)) return false;
        if (!name.equals(todoItem.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                '}';
    }
}
