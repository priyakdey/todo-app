package io.todo.api.repository.impl;

import io.todo.api.entity.User;
import io.todo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import java.util.Optional;

import static io.todo.api.repository.impl.UserQueries.*;

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
}
