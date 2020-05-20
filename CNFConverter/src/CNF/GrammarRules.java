package CNF;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarRules {
    public ArrayList<String> grammarRules = new ArrayList<String>();

    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }

    public boolean importFile(String fileName) throws IOException {
        try {
            File fileIn = new File(fileName);
            Reader IN = new FileReader(fileIn);
            LineNumberReader reader = new LineNumberReader(IN);

            while (reader.ready()) {
                this.grammarRules.add(reader.readLine().concat(" "));
            }
            reader.close();
            IN.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not Found !!!: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyInitialRecursion() {

		return this.grammarRules.get(0).indexOf("S", 2) > 1;
    }

    public void removeInitialRecusion() {
        String strAux = this.grammarRules.get(0).replaceFirst("S ->", "Q ->");
        grammarRules.add(0, strAux);
    }

    public boolean verifyLambdaRule() {
        for (String i : grammarRules) {
            if (i.contains(".")) {
                return true;
            }

        }
        return false;
    }

    public void removeLambdaRule() {
        ArrayList<String> whos_have = new ArrayList<String>();
        ArrayList<String> whos_have_in = new ArrayList<String>();

// Check which rules have lambda'
        for (String i : this.grammarRules) {
            if (i.contains(" . ")) {
                whos_have.add(i.substring(0, 1));
            }
        }

        // Check which rules have connection with lambda rules
        for (String i : this.grammarRules) {
            for (String j : whos_have) {
                if (i.contains(" " + j + " ")) {
                    whos_have_in.add(i.substring(0, 1));
                }
            }
        }

        // Add rules do not exist to Array whos_have
        for (String x : whos_have_in) {
            if (!whos_have.contains(x))
                whos_have.add(x);
        }

        // Separate to the rules
        for (String i : this.grammarRules) {
            for (String j : whos_have) {
                if (i.substring(0, 1).compareTo(j) == 0) {
                    String[] parses;
                    parses = i.split("\\|");
                    String current_rule = "";
                    for (int k = 0; k < parses.length; k++) {

                        if (parses[k].substring(0, 1).compareTo(" ") == 0) {
                            whos_have_in.add(current_rule.concat(parses[k]));
                        } else {
                            current_rule = current_rule.concat(parses[k]
                                    .substring(0, 4));
                            whos_have_in.add(parses[k]);
                        }
                    }

                }
            }
        }

// Check if any of the separate rules are related to the lambda rule
        ArrayList<String> created = new ArrayList<String>();
        for (String separate_rules : whos_have_in) {
            ArrayList<Integer> amount = new ArrayList<Integer>();
            for (String rules_lambda : whos_have) {
                for (int i = 0; i < separate_rules.length(); i++) {
                    if (separate_rules.charAt(i) == rules_lambda.charAt(0)) {
                        amount.add(i);
                    }

                }

                if (amount.size() == 1) {
                    created.add(separate_rules.replace(rules_lambda, ""));
                } else if (amount.size() > 1) {
                }
            }
            for (String string_i : created) {
                System.out.println("created: " + string_i);
            }
        }

        for (String string : whos_have) {
            System.out.println(string);
        }

        for (String string : whos_have_in) {
            System.out.println(string);
        }

    }

    public boolean verifyChainRule() {
        for (String i : grammarRules) {
            if (i.matches(".*[|] [A-Z] [|].*")) {
                return true;
            }
        }
        return false;
    }

    private boolean searchTerm(String term) {
        for (String string : this.grammarRules) {
            if (string.contains(term)) {
                return true;
            }
        }
        return false;
    }

    public void removeChainRule() {

        ArrayList<String> grammarRules_in = new ArrayList<String>();

        Pattern p = Pattern.compile(" [A-Z] ");
        Matcher m;
        // I go through all the rules of grammar
        int index = 0;
        String string_i_in = "";
        for (String string_i : grammarRules) {

// Check if any pattern rules belong to the string_i rule
            m = p.matcher(string_i);

            string_i_in = "";
            // Check if it really has chain rules, if it is a false positive
            while (m.find()) {
                string_i_in = string_i;
// Find the index that matches the pattern
                int idx_start = indexOf(Pattern.compile(" [A-Z] "),
                        string_i_in);
                char rule = string_i_in.charAt(idx_start + 1);

                // Remove the rule that has a chain
                string_i_in = string_i_in.replaceAll(" \\| " + rule, "");
                string_i_in = string_i_in.replaceAll(rule + " \\| ", "");
                // System.out.println("Blank rule: " + string_i_in);

// Insert new rules for the "deleted chain rule"
                for (String string_concat : grammarRules) {
                    if (string_concat.substring(0, 1).startsWith("" + rule
					)) {
                        string_i_in = string_i_in.concat("|"
                                + string_concat.substring(4
						));
                    }
                }

                string_i = string_i_in;
                m = p.matcher(string_i);
            }
            if (string_i_in != null) {
                grammarRules_in.add(index, string_i_in);
            }
//			}
            index++;
        }
        index = 0;
        for (String idx : grammarRules_in) {
            if (!idx.isEmpty()) {
                this.grammarRules.remove(index);
                this.grammarRules.add(index, idx);
            }
            index++;
        }
    }

    public void removeTermRule() {
        ArrayList<String> terms = new ArrayList<String>();
        ArrayList<String> prevs = new ArrayList<String>();
        LinkedHashSet<String> remove = new LinkedHashSet<String>();

        Pattern p = Pattern.compile(" [a-z]* ");
        Matcher m;

// Fill in the first terminals to find out which rules have them
        for (String rules : this.grammarRules) {
            m = p.matcher(rules);
            if (m.find()) {
                terms.add(rules.substring(0, 1)); // add terminal rule
            }
        }

        System.out.println("First Term: " + terms.toString());


        do {
            // Clear ArrayList Prevs, to avoid duplicates
            prevs.clear();
            prevs.addAll(terms);

            for (String grammar_rule : this.grammarRules) {
                for (String rule_term : prevs) {
                    if (rule_term.compareTo(grammar_rule.substring(0, 1)) != 0) {
                        p = Pattern.compile(" [a-z]*".concat(rule_term
                                .concat(" ")));
                        m = p.matcher(grammar_rule);
                        if (m.find())
                            terms.add(grammar_rule.substring(0, 1));
                        p = Pattern.compile(" ".concat(rule_term).concat(
                                "[a-z]* "));
                        m = p.matcher(grammar_rule);
                        if (m.find())
                            terms.add(grammar_rule.substring(0, 1));
                    }
                }
            }

            // remove repeat elements of ArrayList term
            remove.clear();
            remove.addAll(terms);
            terms.clear();
            terms.addAll(remove);

        } while (!prevs.equals(terms));

        // Remove the useless ( no terminal conections ) rules
        prevs.clear(); // used to new grammar ( in )
        for (String rule_terms : terms) {
            for (String grammar : this.grammarRules) {
                if (grammar.substring(0, 1).compareTo(rule_terms) == 0) {
                    prevs.add(grammar);
                    System.out.println("Grammar add in final term: " + grammar);
                }
            }
        }

        //Clear e add all rules, except the rules with no conections terminals
        this.grammarRules.clear();
        this.grammarRules.addAll(prevs);
    }

    public void removeReachRule() {
        ArrayList<String> reach = new ArrayList<String>();
        ArrayList<String> new_ = new ArrayList<String>();
        ArrayList<String> prev = new ArrayList<String>();

        //Cause grammar starts with Q
        for (String grammar : grammarRules) {
            if ((grammar.substring(1, 0).compareTo("Q") == 0)) {

            }
        }
        //if not start Q, then start S
        if (reach.size() == 0) {
            for (String grammar : grammarRules) {
                if ((grammar.substring(1, 0).compareTo("S") == 0)) {

                }
            }
        }

        do {
            // clear new_ and add Reach-Prev
            new_.clear();
            new_.addAll(reach);
            new_.removeAll(prev);

            prev.clear();
            prev.addAll(reach);


        } while (!reach.equals(prev));
    }


    // returns the variable for a given rule index
    public String returnVariable(int indice) {
        String str = "";
        StringTokenizer st = new StringTokenizer(grammarRules.get(indice), "-");
        str += st.nextToken().trim();
        return str;
    }

    public Integer temLambda() {

        for (String s : this.grammarRules) {
            if (s.contains(".")) {

                return this.grammarRules.indexOf(s);
            }
        }
        System.out.println("No Lambda Rules");
        return null;
    }


    // maybe change the name to nullable variables
    public void removeLambda() {
        boolean initialLambda = false;
        ArrayList<Integer> PREV = new ArrayList<>();
        ArrayList<Integer> NULL = new ArrayList<>();

        while (this.temLambda() != null) {
            PREV.clear();
            NULL.clear();
            NULL.add(this.temLambda());
            String generatingLambda = "";
            // for each lambda
            do {
                // PREV = NULL
                for (Integer i : NULL) {
                    if (!PREV.contains(i)) {
                        PREV.add(i);
                    }
                }
                System.out.println("The NULL is: " + NULL.toString());
                System.out.println("PREV is: " + PREV.toString());

                for (String a : this.grammarRules) {

                    StringTokenizer st = new StringTokenizer(a, "->");
                    st.nextToken();
                    String in = st.nextToken().trim();

                    if (a.contains(".")) {
                        generatingLambda += returnVariable(grammarRules.indexOf(a)) + "#";
                    }

                    for (Integer x : PREV) {
                        if (in.contains(returnVariable(x))) {
                            if (!NULL.contains(grammarRules.indexOf(a))) {
                                NULL.add(grammarRules.indexOf(a));
                            }
                        }
                    }
                }
                System.out.println("The NULL is: " + NULL.toString());
                System.out.println("PREV is: " + PREV.toString());

            } while (!PREV.equals(NULL));

            //If the initial symbol is NULL then S ->.
            if (NULL.contains(0)) {
                String str = grammarRules.get(0);
                ArrayList<String> productions = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(str, "->|");
                st.nextToken();
                while (st.hasMoreTokens()) {
                    productions.add(st.nextToken().trim());
                }
                for (String s : productions) {
                    if (s.equals(s.toUpperCase())) {
                        for (Integer i : NULL) {
                            if (s.contains(returnVariable(i))) {
                                for (int x = 0; x < s.length(); x++) {
                                    String y = "";
                                    y += s.charAt(x);
                                    if (generatingLambda.contains(y)) {
                                        initialLambda = true;
                                    }
                                }
                            }
                        }

                    }
                }
            }

            System.out.println("Initial symbol with lambda: " + initialLambda);
            String variable = "";
            ArrayList<String> projections = new ArrayList<>();
            for (Integer i : NULL) {

                String str = "";
                str += grammarRules.get(i);

                // Take the first position of NULL which is the one that already has lambda
                if (NULL.indexOf(i) == 0) {
                    StringTokenizer st = new StringTokenizer(str, "->|");
                    variable += st.nextToken().trim();
                    while (st.hasMoreTokens()) {
                        String in = st.nextToken().trim();
                        if (!in.contains(".")) {
                            projections.add(in);
                        }
                    }
                    for (int x = 0; x < projections.size(); x++) {
                        String proj = projections.get(x);
                        String projNew = "";
                        if (proj.contains(variable)) {
                            for (int z = 0; z < proj.length(); z++) {
                                if (proj.charAt(z) == variable.charAt(0)) {
                                    String in = "";
                                    in += proj.substring(0, z);
                                    projNew += proj.substring(z + 1);
                                    if (!projections.contains(projNew)
                                            && projNew.length() != 0) {
                                        projections.add(projNew);
                                    }
                                    projNew = in;

                                } else {
                                    projNew += proj.charAt(z);
                                }

                                if (!projections.contains(projNew)
                                        && projNew.length() != 0) {
                                    projections.add(projNew);
                                }

                            }
                        }
                    }
                    StringBuffer strBff = new StringBuffer();
                    strBff.append(variable);
                    strBff.append(" -> ");
                    int x = 1;
                    for (String s : projections) {
                        strBff.append(s);
                        if (x < projections.size()) {

                            strBff.append(" | ");
                        }
                        x++;
                    }
                    grammarRules.set(i, strBff.toString());
                    projections.clear();

                } else {
                    String v = "";
                    StringTokenizer st = new StringTokenizer(str, "->|");
                    v += st.nextToken().trim();
                    while (st.hasMoreTokens()) {
                        String in = st.nextToken().trim();
                        projections.add(in);
                    }
                    for (int x = 0; x < projections.size(); x++) {
                        String proj = projections.get(x);
                        String projNew = "";
                        if (proj.contains(variable)) {
                            for (int z = 0; z < proj.length(); z++) {
                                if (proj.charAt(z) == variable.charAt(0)) {
                                    String aux = "";
                                    aux += proj.substring(0, z);
                                    projNew += proj.substring(z + 1);
                                    if (!projections.contains(projNew)
                                            && projNew.length() != 0) {
                                        projections.add(projNew);
                                    }
                                    projNew = aux;

                                } else {
                                    projNew += proj.charAt(z);
                                }

                                if (!projections.contains(projNew)
                                        && projNew.length() != 0) {
                                    projections.add(projNew);
                                }

                            }
                        }
                    }

                    StringBuffer strBff = new StringBuffer();
                    strBff.append(v);
                    strBff.append(" -> ");
                    int x = 1;
                    for (String s : projections) {
                        strBff.append(s);
                        if (x < projections.size()) {
                            strBff.append(" | ");
                        }
                        x++;
                    }
                    grammarRules.set(i, strBff.toString());
                    projections.clear();

                }
            }
        }
        if (initialLambda == true) {
            String str = "";
            str = grammarRules.get(0);
            str += "| . ";
            grammarRules.set(0, str);
        }
    }

    @Override
    public String toString() {
        return "GrammarRules [grammarRules=" + grammarRules + "]";
    }

    public void printGrammar() {
        System.out.println("\n============================================\n");
        for (String i : grammarRules) {
            System.out.println(i);
        }
        System.out.println("\n============================================\n");
    }

}
