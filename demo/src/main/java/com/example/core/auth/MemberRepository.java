package com.example.core.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {
//    Optional<Member> findById(String username);
}
