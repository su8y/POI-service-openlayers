package com.example.core.repository;

import com.example.core.model.POI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class CustomPOIRepositoryImpl implements CustomPOIRepository {

    private final EntityManager em;

    @Override
    public Page<POI> findBySearchParam(POISearchParam searchParam) {
//        List<POI> resultList = em.createQuery("select p from POI p ,Category c where p.category.categoryCode = c.categoryCode and " +
//                        "within(p.coordinates,:area)", POI.class)
        List<POI> resultList = em.createQuery("select p from POI p where within(p.coordinates,:area) = true", POI.class)
                .setParameter("area", searchParam.getPolygon())
//                .setFirstResult((int) searchParam.getPageable().getOffset())
//                .setMaxResults(searchParam.getPageable().getPageSize())
                .getResultList();

//        Integer total = em.createQuery("select count(p.id) from POI p ,Category c where p.category.categoryCode = c.categoryCode and " +
//                        "within(p.coordinates,:area)", Integer.class)
//                .setParameter("area", searchParam.getSpace().getCoordinates())
//                .getSingleResult();


        return new PageImpl<>(resultList, searchParam.getPageable(), 100);
    }
}
