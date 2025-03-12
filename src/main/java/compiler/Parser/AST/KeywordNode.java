package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class KeywordNode extends ASTNode {

  String name;

  public KeywordNode(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "KeywordNode(" + name + ")";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
