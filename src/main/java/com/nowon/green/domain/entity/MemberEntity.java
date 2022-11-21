package com.nowon.green.domain.entity;

import com.nowon.green.security.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SequenceGenerator(name = "g_s_member", sequenceName = "s_member", allocationSize = 1)
@Table(name = "j_member")
@Entity //JPA에서는 물리DB를 대신해서 Entity로 처리합니다.
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "g_s_member", strategy = GenerationType.SEQUENCE)
    @Column(name = "mno")
    private long memberNo;

    @Column(unique = true, nullable = false)// unique not null
    private String email;
    @Column(nullable = false)//not null
    private String pass;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String nickName;

    private boolean social;     //true(1) or false(0) 디폴트는 false(0)

    @Builder.Default        // new HashSet<>(); 값이 디폴트로 적용됩니다
    @Enumerated(EnumType.STRING) //enum인 경우 지정하지 않으면 EnumType.ORDINAL 숫자(0부터)로 저장됨, 확장성을위해 문자로..
    @CollectionTable(name = "mem_role")
    @ElementCollection(fetch = FetchType.EAGER) //즉시로딩
    //임베디드 속성은 식별자란 개념이 없음, 항상 부모와 함께 저장,삭제 되므로 cascade.ALL 기본적용(변경불가)
    //1:N 별도의 테이블(MemberRole)로 생성됩니다.
    private Set<MemberRole> roles = new HashSet<>();

    public MemberEntity addRole(MemberRole role){
        roles.add(role);
        return this;
    }
    public MemberEntity removeRole(MemberRole role){
        roles.remove(role);
        return this;
    }

}
