package compiler.Parser.Grammar.Deallocation.Node;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class FreeNode extends ASTNode {

  String i;

  public FreeNode(String i) {
    this.i = i;
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }

}
