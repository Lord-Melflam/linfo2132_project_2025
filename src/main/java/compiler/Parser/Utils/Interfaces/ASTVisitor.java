package compiler.Parser.Utils.Interfaces;

import compiler.Parser.ASTNode.MainNode;

public interface ASTVisitor {

  void visit(MainNode constantNode);

}
