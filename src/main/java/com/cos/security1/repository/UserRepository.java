package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 를 상속하면 자동 컴포넌트 스캔됨.
// CRUD  함수를 내장하고 있음
public interface UserRepository extends JpaRepository<User,Integer> {

    public User findByUsername(String username);
}
