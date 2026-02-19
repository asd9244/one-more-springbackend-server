package com.board.one_more_project.domain.spice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity // JPA가 관리하는 엔티티임을 선언 (ORM 매핑 대상)
@Table(name = "spices") // 매핑할 DB 테이블 명시
@Getter // Lombok: Getter 메서드 생성 (불변성 확보를 위해 Setter는 지양)
@NoArgsConstructor // JPA 스펙 준수를 위한 기본 생성자 (Protected/Public)
public class Spice {

    @Id // Primary Key 매핑
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PostgreSQL SERIAL 전략 사용
    private Long id;

    @Column(nullable = false, unique = true) // Not Null, Unique 제약조건 설정
    private String name;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    // 엔티티 생명주기 이벤트: 영속화(Persist) 전 실행
    @PrePersist
    public void onPrePersist() {
        this.createdAt = OffsetDateTime.now();
    }
}