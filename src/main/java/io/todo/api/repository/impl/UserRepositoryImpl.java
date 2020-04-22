package io.todo.api.repository.impl;

import io.todo.api.entity.User;
import io.todo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

import static io.todo.api.repository.query.UserQueries.FETCH_TASKS;
import static io.todo.api.repository.query.UserQueries.IF_EXISTS;
import static io.todo.api.repository.query.UserQueries.LOAD_BY_USERNAME;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager em;

    @Autowired
    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public void persist(User user) {
        em.persist(user);
    }


    @Override
    public boolean ifExists(String username, String email) {
        final String queryString = IF_EXISTS.getQueryString();
        TypedQuery<User> query = em.createQuery(queryString, User.class);
        query.setParameter("username", username);
        query.setParameter("email", email);
        query.setLockMode(LockModeType.PESSIMISTIC_READ);

        try {
            query.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<User> findById(String username) {
        User user = em.find(User.class, username, LockModeType.PESSIMISTIC_READ);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> loadByUsername(String username) {
        final String queryString = LOAD_BY_USERNAME.getQueryString();
        TypedQuery<User> query = em.createQuery(queryString, User.class);
        query.setParameter("username", username);

        try {
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public User fetchTaskList(String username) {
        final String queryString = FETCH_TASKS.getQueryString();
        TypedQuery<User> query = em.createQuery(queryString, User.class);
        query.setParameter("username", username);

        // query.getSingleResult will never throw NoResultException,
        // since method can only be invoked if user exists/authenticated/authorized
        return query.getSingleResult();
    }
}
