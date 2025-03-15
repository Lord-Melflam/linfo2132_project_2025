package compiler.Lexer;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Lexer.Symbols.EndFile;
import compiler.Lexer.Symbols.StartFile;
import compiler.Lexer.Symbols.UnrecognisedToken;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

public class Lexer implements Iterator<Symbol> {

  @Getter
  private final LinkedList<Symbol> symbols = new LinkedList<Symbol>();
  private final SymbolRegistry symbolRegistry;
  private int line = 1;
  private int column = 0;
  private final List<UnrecognisedToken> unrecognisedTokens = new ArrayList<>();

  private LinkedList<Symbol> allSymbolsClone;

  public Lexer(Reader input) throws IOException, NotASCIIException, UnrecognisedTokenException {
    this.symbols.add(new StartFile());
    symbolRegistry = new SymbolRegistry();
    symbolRegistry.loadSymbols();
    parseSymbols(input);
  }

  public Symbol getNextSymbol() throws UnrecognisedTokenException {
    checkForErrors();
    if (!hasNext()) {
      return null;
    }
    return next();
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
      column++;
    }

    if (!word.isEmpty()) {
      column++;
      String longestMatch = symbolRegistry.getSymbolType(word.toString());
      if (longestMatch != null) {
        if (notAdd(longestMatch)) {
          symbols.add(createSymbols(longestMatch, word.toString()));
        }
        symbols.add(new EndFile(line));
        deepCopySymbols(symbols);
        return;
      }
      UnrecognisedToken unrecognised = new UnrecognisedToken(line, word.substring(0, 1));
      unrecognisedTokens.add(unrecognised);
      symbols.add(unrecognised);
      checkForErrors();
    }
  }

  public void checkForErrors() throws UnrecognisedTokenException {
    if (!unrecognisedTokens.isEmpty()) {
      for (UnrecognisedToken token : unrecognisedTokens) {
        throw new UnrecognisedTokenException(token.getToken(),
            Integer.toString(token.getLine_number()));
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
  @SuppressWarnings("unchecked")
  public <T> T createSymbols(String symbolName, String value) {
    try {
      Class<?> clazz = Class.forName("compiler.Lexer.Symbols." + symbolName);
      java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(String.class,
          int.class);
      return (T) constructor.newInstance(value, line);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void deepCopySymbols(LinkedList<Symbol> originalList) {
    LinkedList<Symbol> clonedList = new LinkedList<>();
    for (Symbol symbol : originalList) {
      clonedList.add(symbol.clone());
    }
    allSymbolsClone = clonedList;
  }


  public LinkedList<Symbol> getAllSymbolsClone() {
    return allSymbolsClone;
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
      column = 0;
    }
  }
}
