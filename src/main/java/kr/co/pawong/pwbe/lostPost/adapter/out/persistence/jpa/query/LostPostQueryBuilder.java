package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.query;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostSearchRequest;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class LostPostQueryBuilder {

    @PersistenceContext
    private EntityManager em;

    public Page<LostPostEntity> buildFilterQuery(Pageable pageable, LostPostSearchRequest request) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        // content 조회 쿼리
        CriteriaQuery<LostPostEntity> cq = cb.createQuery(LostPostEntity.class);
        Root<LostPostEntity> root = cq.from(LostPostEntity.class);

        // 조건 만드는 객체 (where)
        List<Predicate> preds = new ArrayList<>();
        if (request.getType() != null) {
            preds.add(cb.equal(root.get("postType"), request.getType()));
        }
        if (request.getUpKindCds() != null && !request.getUpKindCds().isEmpty()) {
            preds.add(root.get("upKindCd").in(request.getUpKindCds()));
        }
        if (request.getSexCd() != null) {
            preds.add(cb.equal(root.get("sexCd"), request.getSexCd()));
        }
        // regions 부분 일치 검색으로 변경
        if (request.getRegions() != null && !request.getRegions().isEmpty()) {
            List<Predicate> regionPreds = new ArrayList<>();
            for (String region : request.getRegions()) {
                regionPreds.add(cb.like(root.get("location"), "%" + region + "%"));
            }
            preds.add(cb.or(regionPreds.toArray(new Predicate[0])));
        }

        cq.where(preds.toArray(new Predicate[0]));

        // 정렬 적용 (order by)
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(o -> {
                Path<?> path = root.get(o.getProperty());
                orders.add(o.isAscending() ? cb.asc(path) : cb.desc(path));
            });
            cq.orderBy(orders);
        }

        TypedQuery<LostPostEntity> query = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        List<LostPostEntity> content = query.getResultList();

        // 2) total count 쿼리
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<LostPostEntity> countRoot = countCq.from(LostPostEntity.class);

        List<Predicate> countPreds = new ArrayList<>();
        if (request.getType() != null) {
            countPreds.add(cb.equal(countRoot.get("postType"), request.getType()));
        }
        if (request.getUpKindCds() != null && !request.getUpKindCds().isEmpty()) {
            countPreds.add(countRoot.get("upKindCd").in(request.getUpKindCds()));
        }
        if (request.getSexCd() != null) {
            countPreds.add(cb.equal(countRoot.get("sexCd"), request.getSexCd()));
        }
        // count query에도 regions LIKE 조건 추가
        if (request.getRegions() != null && !request.getRegions().isEmpty()) {
            List<Predicate> countRegionPreds = new ArrayList<>();
            for (String region : request.getRegions()) {
                countRegionPreds.add(cb.like(countRoot.get("location"), "%" + region + "%"));
            }
            countPreds.add(cb.or(countRegionPreds.toArray(new Predicate[0])));
        }

        countCq.select(cb.count(countRoot))
                .where(countPreds.toArray(new Predicate[0]));
        Long total = em.createQuery(countCq).getSingleResult();

        // 3) PageImpl 으로 묶어서 반환
        return new PageImpl<>(content, pageable, total);
    }
}