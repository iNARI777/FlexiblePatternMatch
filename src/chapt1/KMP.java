package chapt1;

public class KMP {

    private int[] setNext(String pattern) {
        int[] next = new int[pattern.length()];
        next[0] = -1;

        char[] patChar = pattern.toCharArray();

        int index = -1;
        for(int i = 1; i < next.length; i++) {
            while(index > -1 && patChar[i] != patChar[index + 1])
                index = next[index];

            if(patChar[i] == patChar[index + 1])
                next[i] = ++index;

            next[i] = index;
        }

        return next;
    }

    public boolean match(String content, String pattern) {
        int[] next = setNext(pattern);
        int indexNext = 0;
        for(int i = 0; i < content.length() - pattern.length(); i++) {
            if(content.charAt(i) == pattern.charAt(indexNext)) {
                indexNext++;
            } else {
                while(indexNext > 0 && content.charAt(i) != pattern.charAt(indexNext))
                    indexNext = next[indexNext - 1] + 1;
            }
            if(indexNext == pattern.length() - 1) return true;
        }

        return false;
    }

    public static void main(String[] args) {
        boolean b = new KMP().match("a string abc in content abcabcabda", "abc");
        System.out.println(b);
    }
}
