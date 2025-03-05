package compiler.Parser.Grammar.Expression;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class PrimaryOperator extends ASTNode {

  private List<ASTNode> children;

/*
  public PrimaryOperator(ASTNode node) {
    this.node = node;
  }
*/

  public PrimaryOperator(ASTNode... nodes) {
    this.children = new ArrayList<>();
    for (ASTNode node : nodes) {
      this.children.add(node);
    }
  }

  public List<ASTNode> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    StringBuilder results = new StringBuilder();
    for (ASTNode node : children) {
      results.append(node.toString());
    }
    return results.toString();
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
