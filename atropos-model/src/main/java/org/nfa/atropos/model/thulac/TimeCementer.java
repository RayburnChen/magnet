package org.nfa.atropos.model.thulac;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 黏结阿拉伯数字与时间量词.
 */
public class TimeCementer {

    private static final Set<Character> TIME_UNITS = new HashSet<>(Arrays.asList(
            '年', '月', '日', '号', '时', '点', '分', '秒')
    );

    public static void cement(List<SegItem> segItems) {
        for (int i = segItems.size() - 1; i > 0; i--) {
            String word = segItems.get(i).word;
            if (word.length() == 1 && TIME_UNITS.contains(word.charAt(0))) {
                SegItem segItem = segItems.get(i - 1);
                if (isNumber(segItem.word)) {
                    segItems.set(i - 1,
                            new SegItem(segItem.word + segItems.remove(i).word, "t")
                    );
                    i--;
                }
            }
        }
    }

    /**
     * 一个词是否为阿拉伯数字组成.
     *
     * @param word 词.
     * @return 布尔值
     */
    private static boolean isNumber(String word) {
        for (char ch : word.toCharArray()) {
            if (!CharUtils.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }
}
