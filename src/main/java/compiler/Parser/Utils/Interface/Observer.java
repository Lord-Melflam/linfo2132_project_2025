package compiler.Parser.Utils.Interface;

import compiler.Parser.Utils.Position;

public interface Observer {

  void update(Position CurrentPosition);
}