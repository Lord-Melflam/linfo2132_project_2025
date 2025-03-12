package compiler.Parser.Utils.Interface;

import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;

public interface ASTVisitor {

  void visit(MainNode constantNode);

  /*void visit(MainNode recordNode);

  void visit(MainNode functionNode);

  void visit(MainNode statementNode);
*/
}
