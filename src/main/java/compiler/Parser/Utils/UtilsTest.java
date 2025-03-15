package compiler.Parser.Utils;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.Symbols.Generic;
import compiler.Parser.Parser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class UtilsTest {

  static public Lexer tokenizeFile(String filePath)
      throws IOException, NotASCIIException, UnrecognisedTokenException {
    try (Reader reader = new FileReader(filePath)) {
      return new Lexer(reader);
    }
  }

  static public Lexer getLexer(String input)
      throws IOException, NotASCIIException, UnrecognisedTokenException {
    StringReader reader = new StringReader(input);
    return new Lexer(reader);
  }

  static public Parser getLexerFilePath(String filePath)
      throws IOException, NotASCIIException, UnrecognisedTokenException, ParserException {
    return new Parser(tokenizeFile(filePath));
  }

  static public Parser getLexerInput(String filePath)
      throws IOException, NotASCIIException, UnrecognisedTokenException, ParserException {
    return new Parser(getLexer(filePath));
  }

  static public List<Symbol> loadExpectedTokens(String filePath) throws IOException {
    List<Symbol> tokens = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty() || line.equals("<StartFile>") || line.equals("<EndFile>")) {
          continue;
        }

        if (!line.startsWith("<") || !line.endsWith(">")) {
          continue;
        }

        line = line.substring(1, line.length() - 1);
        String[] parts = line.split(",", 2);

        if (parts.length < 1) {
          throw new IllegalArgumentException("Erreur de parsing dans : " + line);
        }

        String type = parts[0];
        String value = parts[1];

        tokens.add(new Generic(type, value));
      }
    }
    return tokens;
  }

}
