package compiler.lexer;

public class Token {

    private int lineNumber;
    private TokenType type;
    private String value;

    public Token(TokenType type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String toString() {

      String returnString = "TYPE: ";
      returnString += type.toString() + "\nVALUE: ";
      returnString += value;
      returnString += "\nLINE: ";
      returnString += lineNumber; 
      return returnString;
    }
}
