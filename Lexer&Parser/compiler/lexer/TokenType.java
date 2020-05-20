package compiler.lexer;

public enum TokenType {

  Comparison         ("Comparison"),
  BooleanOp          ("Boolean Operator"),
  NumberOp           ("Number Operator"),
  String             ("String"),
  Assignment         ("Assignment"),
  Control            ("Control"),
  IO                 ("IO"),
  Integer            ("Integer"),
  UserDefinedName    ("User Defined Name"),
  Procedure          ("Procedure"),
  Grouping           ("Grouping"),
  ShortString        ("Short String");

  public final String name;

  private TokenType(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
}
