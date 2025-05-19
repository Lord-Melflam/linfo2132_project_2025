package compiler.Semantic;

public class SymbolTable {

  SymbolTable previousTable; // link to previous table

  SymbolTable(SymbolTable prev) {
    previousTable = prev;
  }
}
