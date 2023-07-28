package com.example.core.repository;

import com.example.core.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
//    Optional<Member> findById(String username);
}
