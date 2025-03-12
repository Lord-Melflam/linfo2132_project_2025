package compiler.Parser.Utils;

import compiler.Parser.AST.RootNode;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ASTNodeProcessor implements ASTVisitor {

  private RootNode root;

  public ASTNodeProcessor() {
    this.root = new RootNode();
  }

  public RootNode getRoot() {
    return root;
  }


  public void printTree(String prefix) {
    for (MainNode child : root.getNodes()) {
      if (child != null) {
        System.out.println(prefix + child.getName());
        for (ASTNode child2 : child.getChildren()) {
          if (child2 != null) {
            System.out.println(" " + child2.toString());
          }
        }
      }
    }
  }

  @Override
  public void visit(MainNode constantNode) {
    root.addNode(constantNode);
  }

/*

  @Override
  public void visit(MainNode recordNode) {
    root.addNode(recordNode);
  }

  @Override
  public void visit(MainNode functionNode) {
    root.addNode(functionNode);
  }

  @Override
  public void visit(MainNode statementNode) {
    root.addNode(statementNode);
  }
*/

}

