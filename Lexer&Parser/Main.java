import compiler.lexer.*;
import compiler.parser.*;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;

class Main {

  // Helper method to neaten error messages

  public static void fatalError(String message) {
    System.out.println(message);
    System.exit(1);
  }

  public static void main(String[] args) {



    // Create an instance of the Lexer class.
    Lexer lex = new Lexer();

    // Create an instance of the Parser class
    Parser parser = new Parser();

    // Create String from file.
    StringBuilder sb = new StringBuilder();
    SyntaxNode sn = new SyntaxNode();


    try {
      FileReader fileReader = new FileReader("test.txt");
      BufferedReader reader = new BufferedReader(fileReader);
      String line;
      int lineNumber = 1;

      while ((line = reader.readLine()) != null) {
        lex.scan(line, lineNumber);
        lineNumber++;
      }
      fileReader.close();

    } catch (Exception e) { fatalError("There was a problem reading the file"); }

    for (Token token:lex.getTokens()) {
      System.out.println(token);
      System.out.println("");
      sn.buildTree(token);
    }
     sn.printTree();
  
    parser.parse(lex.getTokens());
  }
}
