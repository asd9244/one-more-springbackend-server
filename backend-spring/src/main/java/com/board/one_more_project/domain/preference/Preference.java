package com.board.one_more_project.domain.preference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "preferences") // 매핑될 DB 테이블 이름
@Getter
@NoArgsConstructor
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 타입 (DB에서 자동 생성)
    private Long id;

    @Column(nullable = false)
    private String category; // STYLE, TASTE, CONDITION

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist // 엔티티가 영속화(저장)되기 전에 실행되는 로직
    public void onPrePersist() {
        // DB의 TIMESTAMP WITH TIME ZONE 타입에 맞추기 위해 OffsetDateTime 사용
        this.createdAt = OffsetDateTime.now();
    }
}