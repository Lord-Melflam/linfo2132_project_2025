package compiler.Lexer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class SymbolRegistry {

  private final ArrayList<Symbol> symbols;
  private final List<String> SYMBOLS_NAME = new ArrayList<>(List.of(
      "Keyword", "Literal", "SpecialSymbol", "TypeSpecifier", "NewLine", "Tabulation", "Space",
      "Operator", "Assignment", "Comment", "BuiltInFunction", "Record", "Identifier"
  ));

  public SymbolRegistry() {
    this.symbols = new ArrayList<>();
  }

  public String getSymbolType(String token) {
    for (Symbol symbol : symbols) {
      if (symbol.matches(token)) {
        return symbol.getName();
      }
    }
    return null;
  }

  public ArrayList<String> getSymbolTypeList(String token) {
    ArrayList<String> result = new ArrayList<>();
    for (Symbol symbol : symbols) {
      if (symbol.matches(token)) {
        if (!symbol.getName().equals("Space")) {
          result.add(symbol.getName());
        }
      }
    }
    return result;
  }

  public void loadSymbols() {
    try {
      for (String className : SYMBOLS_NAME) {
        Class<?> clazz = Class.forName("compiler.Lexer.Symbols." + className);
        Symbol symbol = (Symbol) clazz.getDeclaredConstructor().newInstance();
        symbols.add(symbol);
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
