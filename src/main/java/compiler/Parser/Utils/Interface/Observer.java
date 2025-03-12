package compiler.Parser.Utils.Interface;

import compiler.Lexer.Symbol;
import compiler.Parser.Utils.Position;

public interface Observer {

  void updatePosition(Position CurrentPosition);

  void updateSymbol(Symbol CurrentSymbol);

}