package compiler.Lexer;

import compiler.Exceptions.NotASCIIException;
import compiler.Exceptions.UnrecognisedTokenException;
import compiler.Lexer.Symbols.EndFile;
import compiler.Lexer.Symbols.StartFile;
import compiler.Lexer.Symbols.UnrecognisedToken;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer implements Iterator<Symbol> {

  private final ArrayDeque<Symbol> symbols;
  private final SymbolRegistry symbolRegistry;
  private int line;

  public Lexer(Reader input) throws IOException, NotASCIIException, UnrecognisedTokenException {
    this.symbols = new ArrayDeque<Symbol>();
    this.symbols.add(new StartFile());
    this.line = 1;
    symbolRegistry = new SymbolRegistry();
    symbolRegistry.loadSymbols();
    parseSymbols(input);
  }

  public Symbol getNextSymbol() {
    if (!hasNext()) {
      return null;
    }
    Symbol next = next();
    System.out.println(next);
/*
        System.out.println(nextSymbol + "/" + s.getLine_number());
*/
    return next;
  }


  @Override
  public boolean hasNext() {
    return !symbols.isEmpty();
  }

  @Override
  public Symbol next() {
    return symbols.pollFirst();
  }

  /**
   * parseSymbols Description - Reads the input file and associates each word with symbols and adds
   * them to a list.
   *
   * @param reader - File to read
   */
  private void parseSymbols(Reader reader)
      throws IOException, NotASCIIException, UnrecognisedTokenException {
    StringBuilder word = new StringBuilder();
    int nextChar;

    while ((nextChar = reader.read()) != -1) {
      char ch = (char) nextChar;
      if (!Symbol.isAscii(ch)) {
        symbols.add(new UnrecognisedToken(line, Character.toString(ch)));
        throw new NotASCIIException(Character.toString(ch), Integer.toString(line));
      }
      word.append(ch);
      List<String> symbolSet = symbolRegistry.getSymbolTypeList(word.toString());

      if (symbolSet.isEmpty()) {
        if (!word.isEmpty()) {
          String longestMatch = symbolRegistry.getSymbolType(word.substring(0, word.length() - 1));
          if (longestMatch != null) {
            if (notAdd(longestMatch)) {
              symbols.add(createSymbols(longestMatch, word.substring(0, word.length() - 1)));
            }
            word = new StringBuilder().append(word.charAt(word.length() - 1));
          }
        } else {
          word.setLength(0);
        }
      }
    }

    if (!word.isEmpty()) {
      String longestMatch = symbolRegistry.getSymbolType(word.toString());
      if (longestMatch != null) {
        if (notAdd(longestMatch)) {
          symbols.add(createSymbols(longestMatch, word.toString()));
          symbols.add(new EndFile(line));
          return;
        }
      }
    }
    symbols.add(new UnrecognisedToken(line, word.substring(0, 1)));
    throw new UnrecognisedTokenException(word.substring(0, 1), Integer.toString(line));
  }

  /**
   * createSymbols Description - Creates new symbol object
   *
   * @param symbolName class name
   * @param value      word associated to the symbol
   * @return the new symbol object
   */
  public Symbol createSymbols(String symbolName, String value) {
    try {
      Class<?> clazz = Class.forName("compiler.Lexer.Symbols." + symbolName);
      java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(String.class,
          int.class);
      return (Symbol) constructor.newInstance(value, line);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean notAdd(String longestMatch) {
    ArrayList<String> Symbols = new ArrayList<>(
        List.of("Comment", "NewLine", "Tabulation", "Space"));
    countLine(longestMatch);
    return !Symbols.contains(longestMatch);
  }

  public void countLine(String symbolName) {
    if (symbolName.equals("NewLine")) {
      line++;
    }
  }
}
