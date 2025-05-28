package edu.az.example.web.travelplanning.repository;


import edu.az.example.web.travelplanning.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    @Query("select u from User u where lower( u.name )= lower(?1)")
    List<User> findByName(String name);


    @Query("select u from User u where u.age = ?1")
    List<User> findAllByAge(Integer age);


    @Query("select u from User u where lower(u.gender) = lower(?1)")
    List<User> findAllByGender(String gender);
}
