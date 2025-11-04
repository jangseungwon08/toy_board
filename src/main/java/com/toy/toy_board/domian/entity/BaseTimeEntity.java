package com.toy.toy_board.domian.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter //getCreatedAt같은 메서드 사용할 수 있게 해줌
@MappedSuperclass 
//테이블과 직접 매핑되지 않고 자식 클래스에게 매핑 정보만 제공
@EntityListeners(AuditingEntityListener.class)
//이 클래스 또는 자식 클래스의 변경사항을 감지(listen)하는 의미이다.
//@CreatedDate와 @LastModifiedDate가 붙은 필드에 시간을 자동으로 수정하거나 채워줌
public class BaseTimeEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
