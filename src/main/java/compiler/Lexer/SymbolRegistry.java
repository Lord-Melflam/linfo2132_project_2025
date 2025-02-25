package compiler.Lexer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

class SymbolRegistry {

  private final ArrayList<Symbol> symbols;

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
    final String filename = "src/main/java/compiler/Lexer/Symbols";
    try {
      File directory = new File(filename);
      if (directory.isDirectory()) {
        String[] files = directory.list();

        assert files != null;
        for (String className : files) {
          Class<?> clazz = Class.forName("compiler.Lexer.Symbols." + className.split("\\.")[0]);
          Symbol symbol = (Symbol) clazz.getDeclaredConstructor().newInstance();
          symbols.add(symbol);
        }
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
