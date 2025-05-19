package compiler.Semantic;

import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

public class ExtractMainNodeVisitor implements ASTVisitor<MainNode> {

  @Override
  public MainNode visitGenericNode(GenericNode<?> node) {
    return null;
  }

  @Override
  public void visit(MainNode constantNode) {

  }

  @Override
  public MainNode visitMainNode(MainNode node) {
    return node;
  }
}
