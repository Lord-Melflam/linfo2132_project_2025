package compiler.Lexer;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer implements Iterator<Symbol> {

  private final List<Symbol> symbols;
  private int index;
  private final SymbolRegistry symbolRegistry;
  private int line;

  public Lexer(Reader input) throws IOException {
    this.symbols = new ArrayList<>();
    this.index = 0;
    this.line = 1;
    symbolRegistry = new SymbolRegistry();
    symbolRegistry.loadSymbols();
    parseSymbols(input);
  }

  public Symbol getNextSymbol() {
    if (!hasNext()) {
      return null;
    }
    return next();
  }


  @Override
  public boolean hasNext() {
    return index < symbols.size();
  }

  @Override
  public Symbol next() {
    return symbols.get(index++);
  }

  /**
   * parseSymbols Description - Reads the input file and associates each word with symbols and adds
   * them to a list.
   *
   * @param reader - File to read
   */
  private void parseSymbols(Reader reader) throws IOException {
    StringBuilder word = new StringBuilder();
    int nextChar;

    while ((nextChar = reader.read()) != -1) {
      char ch = (char) nextChar;
      if (!Symbol.isAscii(ch)) {
        System.err.println("Not ASCII: " + ch);
        continue;
      }
      word.append(ch);
      List<String> symbolSet = symbolRegistry.getSymbolTypeList(word.toString());

      if (symbolSet.isEmpty()) {
        if (!word.isEmpty()) {
          String longestMatch = symbolRegistry.getSymbolType(word.substring(0, word.length() - 1));
          if (longestMatch != null) {
            symbols.add(createSymbols(longestMatch, word.substring(0, word.length() - 1)));
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
        symbols.add(createSymbols(longestMatch, word.toString()));
      }
    }
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
      return (Symbol) constructor.newInstance(value, count_line(symbolName));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public int count_line(String symbolName) {
    if (symbolName.equals("NewLine")) {
      line++;
    }
    return line;
  }
}
