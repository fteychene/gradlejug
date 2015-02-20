package fr.fteychene.gradlejug.tool

import fr.fteychene.gradlejug.model.TodoItem;

class TodoTool {

    void groovyPrint(TodoItem item) {
        println "TodoItem [ id : $item.id, nom : $item.nom, completed = $item.completed ]";
    }

}