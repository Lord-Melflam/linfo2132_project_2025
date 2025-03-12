package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class GenericNode<T> extends ASTNode {

  private String name;
  private String value;

  public GenericNode(String name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public String toString() {
    return name + " (" + value + ")";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
