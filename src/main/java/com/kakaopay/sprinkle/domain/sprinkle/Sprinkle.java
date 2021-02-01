package com.kakaopay.sprinkle.domain.sprinkle;

import com.kakaopay.sprinkle.domain.sprinkleGet.SprinkleGet;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sprinkle")
public class Sprinkle {

    // 뿌리기 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprinkleId")
    private Long sprinkleId;

    // 유저 아이디
    @Column(name = "userId", nullable = false)
    private Long userId;

    // 대화방 아이디
    @Column(name = "roomId", nullable = false)
    private String roomId;

    // token
    @Column(name = "token", nullable = false, length = 3)
    private String token;

    // 뿌리기 금액
    @Column(name = "amount", nullable = false)
    private Long amount;

    // 뿌리기 갯수
    @Column(name = "totalCount", nullable = false)
    private Integer totalCount;

    // 생성일시
    @Column(name = "createdAt")
    @Setter
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "sprinkle", cascade = CascadeType.ALL)
    private List<SprinkleGet> sprinkleGets = new ArrayList<>();

    public void addSprinkleGet(SprinkleGet sprinkleGet) {
        sprinkleGets.add(sprinkleGet);
        sprinkleGet.setSprinkle(this);
    }

    @Builder
    public Sprinkle(long userId, String roomId, String token, long amount, int totalCount) {
        this.userId = userId;
        this.roomId = roomId;
        this.token = token;
        this.amount = amount;
        this.totalCount = totalCount;
    }
}
