package compiler.Parser.Utils.Interfaces;

import compiler.Parser.Utils.Position;

public interface Observer {

  void updatePosition(Position CurrentPosition);

}