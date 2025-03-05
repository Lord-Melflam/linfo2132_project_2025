package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class LiteralNode extends ASTNode {

  Object value;

  public LiteralNode(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return " Literal ";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
