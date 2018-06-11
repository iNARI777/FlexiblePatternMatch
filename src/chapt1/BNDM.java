package algorithms;

public class BNDM {
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
            long status = -1, last = pattern.length();
            for(int j = pattern.length() - 1; j >= 0; j--) {
                status = status & bitmaps[content.charAt(i + j)];
                if(status == 0) break;
                if((status & target) != 0) last = j;
                status <<= 1;
            }
            if(status != 0) return true;
            i += last;
        }
        return false;
    }

    public static void main(String[] args) {
        String pattern = "abcab";
//        String content = "bcabcab";
        String content = "abcaabcbcaabcab";
        System.out.println(new BNDM().hasPattern(content, pattern));
    }
}
