package fr.fteychene.gradlejug.repository.integration.dao;

import com.google.common.collect.Lists;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.fteychene.gradlejug.model.TodoItem;
import fr.fteychene.gradlejug.repository.config.RepositoryConfiguration;
import fr.fteychene.gradlejug.repository.dao.TodoItemRepository;
import fr.fteychene.gradlejug.repository.integration.DbSetupCommonOperation;
import fr.fteychene.gradlejug.repository.integration.config.DerbyDatabaseConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by fteychen on 06/02/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DerbyDatabaseConfiguration.class, RepositoryConfiguration.class})
public class DerbyTodoItemRepositoryTest {

    @Autowired
    private TodoItemRepository repository;

    @Autowired
    private DataSource datasource;

    @Before
    public void before() {
        Operation operation = sequenceOf(
                DbSetupCommonOperation.DELETE_ALL,
                insertInto("todo")
                        .columns("id", "name", "completed")
                        .values(1L, "TODO1 Test", true)
                        .build()
        );

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(datasource), operation);
        dbSetup.launch();
    }

    @Test
    public void testFind() throws Exception {
        TodoItem item = repository.findOne(1L);
        assertEquals((Long) 1L, item.getId());
        assertEquals("TODO1 Test", item.getName());
        assertEquals(true, item.isCompleted());
    }

    @Test
    public void testFindAll() throws Exception {
        List<TodoItem> items = Lists.newArrayList(repository.findAll());
        assertEquals(1, items.size());
        assertEquals((Long) 1L, items.get(0).getId());
        assertEquals("TODO1 Test", items.get(0).getName());
        assertEquals(true, items.get(0).isCompleted());
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(1L);
        TodoItem itemActual = repository.findOne(1L);
        assertNull(itemActual);
    }
}
