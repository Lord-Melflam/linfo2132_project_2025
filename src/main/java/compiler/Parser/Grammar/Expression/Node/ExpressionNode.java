package compiler.Parser.Grammar.Expression.Node;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class ExpressionNode extends ASTNode {

  private List<ASTNode> children;

  public ExpressionNode(ASTNode... nodes) {
    children = new ArrayList<>();
    for (ASTNode node : nodes) {
      if (node != null) {
        children.add(node);
      }
    }
  }

  public List<ASTNode> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    StringBuilder results = new StringBuilder("[");
    for (ASTNode node : children) {
      if (node != null) {
        results.append(node);
      }
    }
    results.append("]");
    return results.toString();
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
