package compiler.Parser.Utils.Interfaces;

import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;

/*
public interface ASTVisitor {

  void visit(MainNode constantNode);

  void visit(MainNode node, boolean bool);

  void visit(GenericNode<?> node);
}
*/

public interface ASTVisitor<T> {

  void visit(MainNode constantNode);

  T visitMainNode(MainNode node) throws OperatorError;

  T visitGenericNode(GenericNode<?> node) throws OperatorError;
}
