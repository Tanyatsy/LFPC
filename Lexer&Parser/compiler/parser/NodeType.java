package compiler.parser;

import compiler.lexer.TokenType;

// NodeType allows us to discern between SyntaxNodes. This allows us to
// perform reductions more easily in Parser.java 

public enum NodeType {

  // Terminals - These are the types of Nodes that represent leaf symbols in the language.

  Comparison         ("Comparison"),
  BooleanOp          ("Boolean Operator"),
  NumberOp           ("Number Operator"),
  String             ("String"),
  Assignment         ("Assignment"),
  Control            ("Control"),
  IO                 ("IO"),
  Integer            ("Integer"),
  Halt               ("Halt"),
  UserDefinedName    ("User Defined Name"),
  Procedure          ("Procedure"),
  Grouping           ("Grouping"),
  ShortString        ("Short String"),

  // Non Terminals

  INSTR ("INSTR");



  public final String name;

  private NodeType(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

  // Translates between Token types and node types. Used for the "leaves"
  // of the tree. This code is not integral to the parsing logic, and it only
  // serves to translate between a Token to a Syntax Node in a a tree.

  public static NodeType fromTokenType(TokenType token) {
    NodeType coercedType = IO;
    switch(token) {
      case Comparison       : coercedType = NodeType.Comparison;      break;
      case BooleanOp        : coercedType = NodeType.BooleanOp;       break;         
      case NumberOp         : coercedType = NodeType.NumberOp;        break;    
      case String           : coercedType = NodeType.String;          break;        
      case Assignment       : coercedType = NodeType.Assignment;      break;       
      case Control          : coercedType = NodeType.Control;         break;        
      case IO               : coercedType = NodeType.IO;              break;        
      case Integer          : coercedType = NodeType.Integer ;        break;        
      case UserDefinedName  : coercedType = NodeType.UserDefinedName; break;
      case Procedure        : coercedType = NodeType.Procedure;       break;          
      case Grouping         : coercedType = NodeType.Grouping;        break;           
      case ShortString      : coercedType = NodeType.ShortString ;    break;
    }
    return coercedType;
  }
}