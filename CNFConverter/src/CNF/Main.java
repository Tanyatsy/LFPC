package CNF;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GrammarRules grammar = new GrammarRules();
//Write absolute path here!!
            if (grammar.importFile("src/grammar.txt")) {
                System.out.println(grammar.toString());

            grammar.printGrammar();

            // Check recursive initial symbol
            if (grammar.verifyInitialRecursion()) {
                grammar.removeInitialRecusion();
            } else
                System.out.println("No initial recursive rule");

            // Check Labda Symbols
            if (grammar.verifyLambdaRule()) {
                grammar.removeLambda();
            } else
                System.out.println("No Lambda rule");

            // Check chain rule
            if (grammar.verifyChainRule()) {
                grammar.removeChainRule();
            } else
                System.out.println("No chain rule");


            grammar.printGrammar();
        }

    }

}
