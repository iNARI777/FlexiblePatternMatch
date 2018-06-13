package chapt1;

import java.util.HashMap;
import java.util.Map;

public class BOM {
    /*
    一个状态节点，provider记录前置状态（与当前状态不相邻，但是进入状态的条件与当前状态相同的最近的一个状态。
    比如对于announce来说，其状态有0-a->1-n->2-n->3-o->4-u->5-n->6-c->7-e->8，中6号状态的前置状态就是3号状态。
    provider的作用可以在addStatus函数中看到。
    trans是一个哈希表，用来保存当前状态可以转移到哪些状态，转移条件是map中的Character，转移状态是map中的Status。
     */
    private class Status {
        Status provider = null;                         // 代替供给函数，默认为空，只有状态机的第一个节点是空
        Map<Character, Status> trans = new HashMap<>(); // 保存状态的所有转移（外转移及内转移）
    }

    /*
    有限无环自动机，自动机生成的时候会自动生成0号状态。两个指针head和tail分别指向0号状态和当前自动机中的最后
    一个状态。
    自动机有m=pattern.length()+1个状态，内部转移m个（前后相邻两状态之间的转移为内部转移），外部转移最多m-1个。
     */
    private class Automata {
        Status head = new Status(); // 自动机的第一个节点是默认起始状态
        Status tail = head;
    }

    /*
    * createAutomata的功能函数，用来向自动机(Factor Oracle)中添加一个新状态。
    * current的前置状态由current之前的一个状态previous求得，分为三种情况。
    */
    private Automata addStatus(Automata automata, Character c) {
        Status current = new Status();

        // 添加前一节点到current的内部转移
        Status previous = automata.tail;
        previous.trans.put(c, current);

        // 设置current的provider
        Status preProvider = previous.provider;

        // 1. 前一个状态的provider不为空，且这个provider没有到当前状态的转移的时候
        while(preProvider != null && !preProvider.trans.containsKey(c)) {
            preProvider.trans.put(c, current);
            preProvider = preProvider.provider;
        }
        if(preProvider == null) {                   // 2. 证明前一个节点是0号状态，把其provider设为0号状态
            current.provider = automata.head;
        } else {
            current.provider = preProvider.trans.get(c);    // 3. 前一个状态的provider不为空，且这个provider有到当前状态的转移的时候
        }

        automata.tail = current;        // 记得把当前状态添加到状态机末尾
        return automata;
    }

    private Automata createAutomata(String pattern) {
        Automata automata = new Automata();
        for(int i = pattern.length() - 1; i >= 0; i--) {
            addStatus(automata, pattern.charAt(i));
        }
        return automata;
    }

    public boolean hasPattern(String content, String pattern) {
        Automata automata = createAutomata(pattern);

        for(int i = 0; i <= content.length() - pattern.length();) {
            Status current = automata.head;
            int j = pattern.length();
            while(j > 0 && current != null) {
                current = current.trans.get(content.charAt(i + j - 1));
                j--;
            }
            if(current != null) {
                return true;
            }
            i += j + 1;
        }
        return false;
    }
}
