package com.king.bankbackend.utils;

import com.king.bankbackend.constant.CardConstant;

import java.util.Random;

public class BankCardIdGenerator {

    /**
     * 生成随机银行卡号
     *
     * @return 格式为"10103576xxxxxxxx"的16位银行卡号
     */
    public static String generate() {
        int randomPartLength = CardConstant.CARD_ID_LENGTH - CardConstant.DEFAULT_CARD_ID_PREFIX.length();
        return CardConstant.DEFAULT_CARD_ID_PREFIX + generateRandomNumbers(randomPartLength);
    }

    /**
     * 生成随机银行卡号
     *
     * @return 格式为"10103576xxxxxxxx"的16位银行卡号
     */
    public static String generateCardId() {
        // 计算需要生成的随机部分长度
        int randomPartLength = CardConstant.CARD_ID_LENGTH - CardConstant.DEFAULT_CARD_ID_PREFIX.length();

        // 生成随机数字部分
        String randomPart = generateRandomNumbers(randomPartLength);

        // 组合前缀和随机部分
        return CardConstant.DEFAULT_CARD_ID_PREFIX + randomPart;
    }

    /**
     * 生成指定长度的随机数字字符串
     *
     * @param length 需要生成的随机数字长度
     * @return 随机数字字符串
     */
    private static String generateRandomNumbers(int length) {
        if (length <= 0) {
            return "";
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0-9的随机数字
        }

        return sb.toString();
    }

}