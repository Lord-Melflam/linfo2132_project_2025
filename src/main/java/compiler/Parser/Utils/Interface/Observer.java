package compiler.Parser.Utils.Interface;

import compiler.Lexer.Symbol;

public interface Observer {

  void update(Symbol CurrentSymbol);
}