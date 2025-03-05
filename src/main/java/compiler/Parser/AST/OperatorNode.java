package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class OperatorNode extends ASTNode {

  String name;

  public OperatorNode(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Operator";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
