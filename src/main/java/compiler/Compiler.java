/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package compiler;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Parser.Parser;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Compiler {

  public static void main(String[] args) {
    try (Reader reader = new FileReader(args[1])) {
      Lexer lexer = new Lexer(reader);
   /*   while (lexer.hasNext()) {
        System.out.println(lexer.getNextSymbol());
      }*/
      Parser parser = new Parser(lexer);
    } catch (IOException | NotASCIIException | UnrecognisedTokenException | ParserException e) {
      throw new RuntimeException(e);
    }
  }
}