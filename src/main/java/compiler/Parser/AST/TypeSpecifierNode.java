package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class TypeSpecifierNode extends ASTNode {

  private String type;

  public TypeSpecifierNode(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "TypeSpecifier (" + type + ')';
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
