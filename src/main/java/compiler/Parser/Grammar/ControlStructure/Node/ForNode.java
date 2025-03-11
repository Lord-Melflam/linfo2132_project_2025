package compiler.Parser.Grammar.ControlStructure.Node;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ForNode extends ASTNode {

  String i;

  public ForNode(String i) {
    this.i = i;
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
