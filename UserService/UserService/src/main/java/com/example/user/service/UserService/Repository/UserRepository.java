package com.example.user.service.UserService.Repository;

import com.example.user.service.UserService.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

}
