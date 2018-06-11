package algorithms;

public class BNDM {
    /*
    为每个字符生成一个bitmap用来标识其在pattern中出现的位置，因为是放在
    long类型的变量中，所以要求pattern的长度不能大于64
    */
    private long[] setBitmaps(String pattern) {
        long[] bitmaps = new long[256];

        for(int i = pattern.length() - 1, j = 0; i >= 0; i--, j++) {
            bitmaps[pattern.charAt(i)] |= 1 << j;
        }

        return bitmaps;
    }

    public boolean hasPattern(String content, String pattern) {
        long[] bitmaps = setBitmaps(pattern);
        long target = 1 << pattern.length() - 1;

        for (int i = 0; i <= content.length() - pattern.length(); ) {
            long status = -1, step = pattern.length();  // status是当前的匹配状态（初始为全1），step是下次窗口移动的距离（初始为pattern长度）
            for(int j = pattern.length() - 1; j >= 0; j--) {
                status = status & bitmaps[content.charAt(i + j)];   // 可以得到当前窗口中的content是否有pattern中存在的子字符串
                if(status == 0) break;
                if((status & target) != 0) step = j;    // 当前遍历到的状态在pattern的开头（即当前content的后缀为pattern的一个前缀），要减小step
                status <<= 1;                           // 准备下一次状态改变
            }
            if(status != 0) return true;
            i += step;
        }
        return false;
    }
}
