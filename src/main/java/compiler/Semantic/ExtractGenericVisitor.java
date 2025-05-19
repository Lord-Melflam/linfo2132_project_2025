package compiler.Semantic;

import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

public class ExtractGenericVisitor implements ASTVisitor<GenericNode<?>> {

  @Override
  public GenericNode<?> visitGenericNode(GenericNode<?> node) {
    return node;
  }

  @Override
  public void visit(MainNode constantNode) {

  }

  @Override
  public GenericNode<?> visitMainNode(MainNode node) {
    return null;
  }
}

