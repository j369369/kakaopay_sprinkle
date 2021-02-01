package com.kakaopay.sprinkle.service;


public interface SprinkleGetService {

    /**
     * 받기
     *
     * @param token  뿌리기 토큰
     * @param userId 사용자 아이디
     * @param roomId 대화방 아이디
     * @return 받은 금액
     */
    long sprinkleGet(String token, long userId, String roomId);


}
