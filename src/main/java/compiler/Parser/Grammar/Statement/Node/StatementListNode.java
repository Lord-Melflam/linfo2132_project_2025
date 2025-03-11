package compiler.Parser.Grammar.Statement.Node;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;
import java.util.ArrayList;

public class StatementListNode extends ASTNode {

  private ArrayList<ASTNode> astNode;

  public StatementListNode(ASTNode... astNodes) {
    this.astNode = new ArrayList<>();

    for (ASTNode node : astNodes) {
      if (node != null) {
        this.astNode.add(node);
      }
    }
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
