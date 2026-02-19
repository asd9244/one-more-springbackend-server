package com.board.one_more_project.domain.ingredient.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity // DB 테이블과 연결됨
@Table(name = "ingredients") // 'ingredients' 테이블과 매핑됨
@Getter
@NoArgsConstructor
// ORM(Object-Relational Mapping): 클레스를 DB테이블로 매핑한다. JPA가 이 객체를 보고 DB쿼리를 자동 생성함.
public class Ingredient {

    @Id // PK (Primary Key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL (Auto Increment)
    private Long id;

    @Column(nullable = false, unique = true) // NULL 불가, 중복 불가
    private String name; // 재료 이름 (예: 돼지고기, 양파)

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    // 데이터가 저장되기 전에 실행되는 로직
    @PrePersist
    public void onPrePersist() {
        this.createdAt = OffsetDateTime.now();
    }

}