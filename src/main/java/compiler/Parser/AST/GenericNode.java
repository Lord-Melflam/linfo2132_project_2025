package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class GenericNode<T> extends ASTNode {

  String nodeName;

  public GenericNode(String name, ASTNode... nodes) {
    this.nodeName = name;
  }

  @Override
  public String toString() {
    return nodeName + (" + name + ");
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
