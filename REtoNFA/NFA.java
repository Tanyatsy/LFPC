import java.util.*;

public class NFA {
    String start;
    Set<String> ends;
    Map<String, Map<Character,List<String>>> transitions;

    NFA(String[] ss, String[] ts) {
        ends = new TreeSet<>();
        transitions = new TreeMap<>();

        // States
        for (String v : ss) {
            String[] pieces = v.split(",");
            if (pieces.length>1) {
                if (pieces[1].equals("S")) start = pieces[0];
                else if (pieces[1].equals("E")) ends.add(pieces[0]);
            }
        }

        // Transitions
        for (String e : ts) {
            String[] pieces = e.split(",");
            String from = pieces[0],
                    to = pieces[1];
            if (!transitions.containsKey(from)) transitions.put(from, new TreeMap<>());
            for (int i=2; i<pieces.length; i++) {
                char c = pieces[i].charAt(0);
                if (!transitions.get(from).containsKey(c)) transitions.get(from).put(c, new ArrayList<String>());
                transitions.get(from).get(c).add(to);
            }
        }

        System.out.println("start:"+start);
        System.out.println("end:"+ends);
        System.out.println("transitions:"+transitions);
    }

    public boolean match(String s) {

        Set<String> currStates = new TreeSet<String>();
        currStates.add(start);
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            Set<String> nextStates = new TreeSet<String>();

            for (String state : currStates)
                if (transitions.get(state).containsKey(c))
                    nextStates.addAll(transitions.get(state).get(c));
            if (nextStates.isEmpty()) return false;
            currStates = nextStates;
        }

        for (String state : currStates) {
            if (ends.contains(state)) return true;
        }
        return false;
    }


    public void test(String name, String[] inputs) {
        System.out.println("***" + name);
        for (String s : inputs)
            System.out.println(s + ":" + match(s));
    }

    public static void main(String[] args) {


        String[] ss = { "S,S", "F", "D", "X,E" };
        String[] ts = { "S,S,b", "S,F,a", "F,F,b", "F,D,c", "F,X,a", "D,S,c", "D,X,a" };
        NFA nfa2 = new NFA(ss, ts);

        String[] testT = {"abbca","babbca","accaa",""};
        nfa2.test("testT", testT);

    }
}