package chapt1;

public class ShiftAnd {

    private long[] setBitmap(String pattern) {
        long[] bitmaps = new long[256];

        for(int i = 0; i < pattern.length(); i++) {
            char charac = pattern.charAt(i);
            bitmaps[charac] = bitmaps[charac] | (1 << i);
        }
        return bitmaps;
    }

    public boolean hasPattern(String content, String pattern) {
        if(pattern.length() > 64)
            throw new UnsupportedOperationException("Pattern's length beyonds a word's length");

        long[] bitmaps = setBitmap(pattern);

        long target = 1 << (pattern.length() - 1);
        long mask = 0;
        for(int i = 0; i < content.length(); i++) {
            mask = ((mask << 1) | 1 ) & bitmaps[content.charAt(i)];     //Core process
            if((mask | target) == target) return true;      // Don't use "mask == target"!
        }
        return false;
    }
}
