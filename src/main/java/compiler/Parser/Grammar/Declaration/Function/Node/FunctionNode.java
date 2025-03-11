package compiler.Parser.Grammar.Declaration.Function.Node;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class FunctionNode extends ASTNode {

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
