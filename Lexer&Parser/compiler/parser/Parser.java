package compiler.parser;

import java.util.ArrayList;
import compiler.lexer.Token;

public class Parser {
    
    private SyntaxTree tree = new SyntaxTree();
    private ParseStack stack = new ParseStack();

    public void parse(ArrayList<Token> tokens) {

        // For every token in our list, shift this token 
        // (that is:add it to the stack), and then try to reduce.


        for (Token t : tokens) {
            shift(t);
            reduce();
        }

    }

    public void shift(Token t) {
        stack.push(new SyntaxNode(t));
    }

    // This is the reduce function from the literature. At the moment it only
    // pops the top 3 to check if it can reduce to INSTR. In the final function,
    // the reduce method will have to do a lot more checking (for different stack
    // portions of size x, and for different types of syntax nodes/production rules). 

    public boolean reduce() {
        boolean reduceMade = false;
        if (stack.size() >= 3) {
            ArrayList<SyntaxNode> test = stack.peek(3);
            if (reduceINSTR(test)) {
                stack.pop(3);
                stack.push(new SyntaxNode(NodeType.INSTR));
            }
        }
        return reduceMade;
    }

    public boolean reduceINSTR(ArrayList<SyntaxNode> test) {
        return test.get(0).getType() == NodeType.Integer && 
                test.get(1).getType() == NodeType.Assignment && 
                    test.get(2).getType() == NodeType.UserDefinedName;
    }

    public SyntaxTree getTree() { return tree; }
    
}
