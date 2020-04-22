package io.todo.api.repository.impl;

import io.todo.api.entity.Task;
import io.todo.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.todo.api.repository.query.TaskQueries.FIND_ALL;
import static io.todo.api.repository.query.TaskQueries.FIND_FILTERED;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final EntityManager em;

    @Autowired
    public TaskRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public UUID persist(Task task) {
        em.persist(task);
        return task.getId();
    }

    @Override
    public Optional<List<Task>> findAll(String username, Integer page, Integer size) {
        final String queryString = FIND_ALL.getQueryString();
        TypedQuery<Task> query = em.createQuery(queryString, Task.class);
        query.setParameter("username", username);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        try {
            List<Task> resultList = query.getResultList();
            return Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<List<Task>> findInProgress(String username, Integer page, Integer size) {
        return findFiltered(username, page, size, false);
    }

    @Override
    public Optional<List<Task>> findCompleted(String username, Integer page, Integer size) {
        return findFiltered(username, page, size, true);

    }

    @Override
    public Task findById(UUID id) {
        return em.find(Task.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    private Optional<List<Task>> findFiltered(String username, Integer page, Integer size, boolean inProgress) {
        final String queryString = FIND_FILTERED.getQueryString();
        TypedQuery<Task> query = em.createQuery(queryString, Task.class);
        query.setParameter("username", username);
        query.setParameter("completed", inProgress);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        try {
            List<Task> resultList = query.getResultList();
            return Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


}
