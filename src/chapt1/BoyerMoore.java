package chapt1;

public class BoyerMoore {
    /*
    坏字符原则：建立一个字符查找表，该表记录的是所有字符在pattern中出现的最右的位置，如果没出现过则为-1..
     */
    private int[] processBCR(String pattern) {
        final int ALPHABET_SIZE = 256;  // ASCII
        int[] table = new int[ALPHABET_SIZE];

        for(int i = 0; i < table.length; i++) {
            table[i] = -1;
        }

        for(int i = 0; i < pattern.length(); i++) {
            table[pattern.charAt(i)] = i;
        }
        return table;
    }

    /*
    好后缀原则使用的表，table[i]代表如果比较窗口中的第i个字符不匹配，后缀pattern[i+1..n]在pattern中
    当前匹配位置右边开始的与该后缀相同的子串开始的位置-1.该位置就是需要与当前不匹配字符对齐的位置。
     */
    private int[] processGSR(String pattern) {
        int[] table = new int[pattern.length()];
        // 情况1：没有好后缀与之匹配
        for(int i = 0; i < table.length; i++) {
            table[i] = -1;
        }
        int[] suff = suffix(pattern);
        // 情况2：好后缀的后缀是pattern的前缀
        for(int i = 0; i < pattern.length(); i++) {
            if(suff[i] == i + 1)
                for(int j = i; j < pattern.length() - i - 1; j++)
                    table[j] = -1;
        }
        // 情况3：好后缀在pattern中间
        for(int i = 0; i < pattern.length() - 1; i++) {
            table[pattern.length() - 1 - suff[i]] = i - suff[i];
        }
        return table;
    }

    /*
    suff[i]代表pattern[0..i]和pattern[0..n]（n为pattern长度）的最大公共后缀长度，用了比较暴力的方法，好像可以简化。
    以后再说吧233！
     */
    private int[] suffix(String pattern) {
        int[] table = new int[pattern.length()];
        for(int i = pattern.length() - 1; i >= 0; i--) {
            int length = 0;
            while(i - length >= 0 && pattern.charAt(i - length) == pattern.charAt(pattern.length() - 1 - length))
                length++;
            table[i] = length;
        }
        return table;
    }

    /*
    使用Boyer-Moore算法判断content中是否有pattern
     */
    public boolean hasPattern(String content, String pattern) {
        if(content == null || pattern == null || content.length() == 0 || pattern.length() == 0)
            throw new IllegalArgumentException();

        int[] bcTable = processBCR(pattern);
        int[] gsTable = processGSR(pattern);

        for(int i = 0; i <= content.length() - pattern.length(); ) {
            int step = 1;
            for(int j = pattern.length() - 1; j >= 0; j--) {
                if(content.charAt(i + j) != pattern.charAt(j)) {
                    int position = Math.min(bcTable[content.charAt(i + j)], gsTable[j]);
                    step = j - position;
                    break;
                }
                if(j == 0) return true;
            }
            i += step;
        }
        return false;
    }
}
