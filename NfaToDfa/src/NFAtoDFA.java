import java.io.IOException;
import java.util.*;

class Nfa {
    private Integer startState;
    private Integer exitState;    // This is the unique accept state
    private Map<Integer, List<Transition>> trans;

    public Nfa(Integer startState, Integer exitState) {
        this.startState = startState;
        this.exitState = exitState;
        trans = new HashMap<Integer, List<Transition>>();
        if (!startState.equals(exitState))
            trans.put(exitState, new LinkedList<Transition>());
    }

    static Map<Set<Integer>, Map<String, Set<Integer>>>
    compositeDfaTrans(Integer s0, Map<Integer, List<Transition>> trans) {
        Set<Integer> S0 = epsilonClose(Collections.singleton(s0), trans);
        LinkedList<Set<Integer>> worklist = new LinkedList<Set<Integer>>();
        worklist.add(S0);
        // The transition relation of the DFA
        Map<Set<Integer>, Map<String, Set<Integer>>> res =
                new HashMap<Set<Integer>, Map<String, Set<Integer>>>();
        while (!worklist.isEmpty()) {
            Set<Integer> S = worklist.removeFirst();
            if (!res.containsKey(S)) {
                // The S -lab-> T transition relation being constructed for a given S
                Map<String, Set<Integer>> STrans = new HashMap<String, Set<Integer>>();
                // For all s in S, consider all transitions s -lab-> t
                for (Integer s : S) {
                    // For all non-epsilon transitions s -lab-> t, add t to T
                    for (Transition tr : trans.get(s)) {
                        if (tr.lab != null) {            // Already a transition on lab
                            Set<Integer> toState;
                            if (STrans.containsKey(tr.lab))
                                toState = STrans.get(tr.lab);
                            else {                         // No transitions on lab yet
                                toState = new HashSet<Integer>();
                                STrans.put(tr.lab, toState);
                            }
                            toState.add(tr.target);
                        }
                    }
                }
                // Epsilon-close all T such that S -lab-> T, and put on worklist
                HashMap<String, Set<Integer>> STransClosed =
                        new HashMap<String, Set<Integer>>();
                for (Map.Entry<String, Set<Integer>> entry : STrans.entrySet()) {
                    Set<Integer> Tclose = epsilonClose(entry.getValue(), trans);
                    STransClosed.put(entry.getKey(), Tclose);
                    worklist.add(Tclose);
                }
                res.put(S, STransClosed);
            }
        }
        return res;
    }

    static Set<Integer> epsilonClose(Set<Integer> S,
                                     Map<Integer, List<Transition>> trans) {
        LinkedList<Integer> worklist = new LinkedList<Integer>(S);
        Set<Integer> res = new HashSet<Integer>(S);
        while (!worklist.isEmpty()) {
            Integer s = worklist.removeFirst();
            for (Transition tr : trans.get(s)) {
                if (tr.lab == null && !res.contains(tr.target)) {
                    res.add(tr.target);
                    worklist.add(tr.target);
                }
            }
        }
        return res;
    }

    static Map<Set<Integer>, Integer> mkRenamer(Collection<Set<Integer>> states) {
        Map<Set<Integer>, Integer> renamer = new HashMap<Set<Integer>, Integer>();
        for (Set<Integer> k : states)
            renamer.put(k, renamer.size());
        return renamer;
    }

    static Map<Integer, Map<String, Integer>> rename(Map<Set<Integer>, Integer> renamer, Map<Set<Integer>, Map<String, Set<Integer>>> trans) {
        Map<Integer, Map<String, Integer>> newtrans =
                new HashMap<Integer, Map<String, Integer>>();
        for (Map.Entry<Set<Integer>, Map<String, Set<Integer>>>
                entry : trans.entrySet()) {
            Set<Integer> k = entry.getKey();
            Map<String, Integer> newktrans = new HashMap<String, Integer>();
            for (Map.Entry<String, Set<Integer>> tr : entry.getValue().entrySet())
                newktrans.put(tr.getKey(), renamer.get(tr.getValue()));
            newtrans.put(renamer.get(k), newktrans);
        }
        return newtrans;
    }

    static Set<Integer> acceptStates(Set<Set<Integer>> states,
                                     Map<Set<Integer>, Integer> renamer,
                                     Integer exit) {
        Set<Integer> acceptStates = new HashSet<Integer>();
        for (Set<Integer> state : states)
            if (state.contains(exit))
                acceptStates.add(renamer.get(state));
        return acceptStates;
    }

    public void addTrans(Integer s1, String lab, Integer s2) {
        List<Transition> s1Trans;
        if (trans.containsKey(s1))
            s1Trans = trans.get(s1);
        else {
            s1Trans = new LinkedList<Transition>();
            trans.put(s1, s1Trans);
        }
        s1Trans.add(new Transition(lab, s2));
    }

    public String toString() {
        return "NFA start=" + startState + " exit=" + exitState + "\n" + trans;
    }

    public Dfa toDfa() {
        Map<Set<Integer>, Map<String, Set<Integer>>> cDfaTrans
                = compositeDfaTrans(startState, trans);
        Set<Integer> cDfaStart
                = epsilonClose(Collections.singleton(startState), trans);
        Set<Set<Integer>> cDfaStates = cDfaTrans.keySet();
        Map<Set<Integer>, Integer> renamer = mkRenamer(cDfaStates);
        Map<Integer, Map<String, Integer>> simpleDfaTrans
                = rename(renamer, cDfaTrans);
        Integer simpleDfaStart = renamer.get(cDfaStart);
        Set<Integer> simpleDfaAccept
                = acceptStates(cDfaStates, renamer, exitState);
        return new Dfa(simpleDfaStart, simpleDfaAccept, simpleDfaTrans);
    }

    public static class Transition {
        String lab;
        Integer target;

        public Transition(String lab, Integer target) {
            this.lab = lab;
            this.target = target;
        }

        public String toString() {
            return "-" + lab + "-> " + target;
        }
    }
}

class Dfa {
    private Integer startState;
    private Set<Integer> acceptStates;
    private Map<Integer, Map<String, Integer>> trans;

    public Dfa(Integer startState,
               Set<Integer> acceptStates,
               Map<Integer, Map<String, Integer>> trans) {
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.trans = trans;
    }

    public Integer getStart() {
        return startState;
    }

    public Set<Integer> getAccept() {
        return acceptStates;
    }

    public Map<Integer, Map<String, Integer>> getTrans() {
        return trans;
    }

    public String toString() {
        return "DFA start=" + startState + "\naccept=" + acceptStates
                + "\n" + trans;
    }

}

class NFAtoDFA {
    public static void main(String[] args)
            throws IOException {
        buildAndShow("dfa1.dot");
    }

    public static void buildAndShow(String filename)
            throws IOException {

        if (filename == "dfa1.dot") {

            Nfa nfa2 = new Nfa(0, 2);
            nfa2.addTrans(0, "B", 0);
            nfa2.addTrans(0, "A", 1);
            nfa2.addTrans(1, "C", 1);
            nfa2.addTrans(1, "A", 2);
            nfa2.addTrans(3, "A", 1);
            nfa2.addTrans(3, "A", 3);
            nfa2.addTrans(2, "A", 3);

            System.out.println(nfa2);
            Dfa dfa2 = nfa2.toDfa();
            System.out.println(dfa2);
            System.out.println("Writing DFA graph to file " + filename);
            System.out.println();
        }
    }
}

