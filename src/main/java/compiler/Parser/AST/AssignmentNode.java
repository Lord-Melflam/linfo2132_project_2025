package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class AssignmentNode extends ASTNode {

  String name;

  public AssignmentNode() {
    this.name = "=";
  }

  @Override
  public String toString() {
    return "Operator";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
