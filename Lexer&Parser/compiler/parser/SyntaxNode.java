package compiler.parser;

import compiler.lexer.Lexer;
import compiler.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class SyntaxNode {

    private NodeType type;
    private ArrayList<SyntaxNode> children;
    private String value;
    private Lexer lexer = new Lexer();
    private String currentControl;
    private String currentProcedure;
    private List<String> list = new ArrayList<>();

    public SyntaxNode() {

    }


    // Some logic to make printing the tree more presentable :)


    public SyntaxNode(NodeType type) {
        this.type = type;
    }

    public SyntaxNode(Token token) {
        this.type = NodeType.fromTokenType(token.getType());
        addChildren(new SyntaxNode(getType()));
    }
    // A function that adds n children to a node

    public void buildTree(Token token) {
        if (token.getType().name.equals("Procedure")) {
            list.add("Root is: " + token.getValue());
            currentProcedure = token.getValue();
        }
        if (currentProcedure != null && lexer.matchProcedure(currentProcedure) && token.getType().name.equals("User Defined Name")) {
            list.add("{" + currentProcedure + "} child is: (" + token.getValue() + ")");
        }
        if (currentControl != null)
            currentProcedure = null;

        if (token.getType().name.equals("Control") || token.getType().name.equals("IO")) {
            currentControl = token.getValue();
            list.add("Roots child is: {" + token.getValue() + "}");
        }

        if (currentControl != null && lexer.matchControl(currentControl) && token.getType().name.equals("User Defined Name")) {
            list.add("{" + currentControl + "} child is: (" + token.getValue() + ")");
        }
        if (currentControl != null && lexer.matchIO(currentControl) && token.getType().name.equals("User Defined Name")) {
            list.add("{" + currentControl + "} child is: (" + token.getValue() + ")");
        }
    }


    // Constructor

    public void printTree() {
        System.out.println("Syntax tree:\n");
        for (String str : list
        ) {
            System.out.println(str);
        }
    }

    public void addChildren(SyntaxNode... newChildren) {
        if (children == null) {
            children = new ArrayList<>();
        }
        for (SyntaxNode child : newChildren) {
            children.add(child);
        }
    }

    public NodeType getType() {
        return type;
    }
}