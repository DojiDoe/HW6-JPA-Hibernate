package com.cursor.repository;

import com.cursor.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository  {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User upsert(User user){
        return entityManager.merge(user);
    }

    @Transactional
    public void deleteById(Long id){
        entityManager.remove(getById(id));
    }

    public List<User> getAll() {
        return  entityManager.createQuery("select user from User user").getResultList();
    }

    public User getById(Long id){
        return  entityManager.find(User.class,id);
    }

    public List<User> getByEmail(String email) {
        Query query = entityManager.createQuery("select user from User user WHERE user.email=:email");
        query.setParameter("email", email);
        return query.getResultList();
    }
}
