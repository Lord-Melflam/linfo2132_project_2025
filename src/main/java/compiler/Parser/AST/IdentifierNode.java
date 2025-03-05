package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class IdentifierNode extends ASTNode {

  String name;

  public IdentifierNode(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Identifier(" + name + ")";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
