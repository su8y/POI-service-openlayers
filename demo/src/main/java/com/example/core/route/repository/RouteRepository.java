package com.example.core.route.repository;

import com.example.core.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long>, SqlRouteRepository {
    @Modifying
    @Query("delete from Route r where r.id= :id and r.member.id like :memberId ")
    void deleteByIdAndMemberId(@Param("id") Long id, @Param("memberId") String memberId);
    Optional<Route> findByIdAndMemberId(Long id, String memberId);
}
