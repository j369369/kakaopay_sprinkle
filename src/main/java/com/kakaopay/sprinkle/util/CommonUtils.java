package com.kakaopay.sprinkle.util;

import com.kakaopay.sprinkle.constants.CommonConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CommonUtils {

    public long[] dvdAmt(long amount, int count, int option) {
        long[] result = new long[count];

        // 최소 금액 1원씩
        if (option == 1){
            for (int i = 0; i < count - 1; i++) {
                // 최대범위 = 금액-나눌인원 +1
                result[i] = RandomUtils.nextLong(1, amount-(count-i)+1);
                amount -= result[i];
            }
            result[count - 1] = amount;
        }
        // 0원 허용
        else {
            Random r = new Random();
            for (int i = 0; i < count - 1; i++) {
                result[i] = r.nextInt((int) amount);
                amount-=result[i];
                if (amount == 1) break;
            }
            result[count-1] = amount;
        }

        return result;
    }

    public String generateToken(){
        return RandomStringUtils.randomAlphabetic(CommonConstants.TOKEN_LENGTH);
    };
}
