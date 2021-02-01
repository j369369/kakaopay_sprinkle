package com.kakaopay.sprinkle.domain.sprinkleGet;

import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sprinkleGet")
public class SprinkleGet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "getId")
    private Long getId;

    // 뿌리기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkleId")
    @Setter
    private Sprinkle sprinkle;

    // 유저 아이디
    @Column(name = "userId")
    @Setter
    private Long userId;

    // 받은 금액
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Version
    private Integer version;

    // 생성일시
    @Column(name = "createdAt")
    @Setter
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public SprinkleGet(Long amount) {
        this.amount = amount;
    }


    public boolean isNotGet() {
        return userId == null;
    }
    public boolean isAlreadyGet() {
        return userId != null;
    }
}
