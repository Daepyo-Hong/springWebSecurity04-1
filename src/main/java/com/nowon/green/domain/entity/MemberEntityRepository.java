package com.nowon.green.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberEntityRepository  extends JpaRepository<MemberEntity,Long> {
    //JpaRepository를 상속하면 기본 JPA 메서드를 사용할 수 있음
    //사용자가 정의하는 쿼리 메서드 영역 ( JPQL로 @Query("쿼리문")으로 직접 지정하거나 메서드생성규칙에 맞게 만들면 됨)
    Optional<MemberEntity> findByEmail(String username);  //Email 이 유니크속성이기 때문에 결과값은 무조건 1개 또는 null

}
