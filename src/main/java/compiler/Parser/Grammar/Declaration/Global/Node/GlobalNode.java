package compiler.Parser.Grammar.Declaration.Global.Node;

import compiler.Parser.AST.AssignmentNode;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.AST.TypeSpecifierNode;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class GlobalNode extends ASTNode {

  /* TODO */
  private IdentifierNode identifierNode;
  private TypeSpecifierNode typeSpecifierNode;
  private ExpressionNode expressionNode;
  private SpecialSymbolNode specialSymbolNode;
  private AssignmentNode assignmentNode = new AssignmentNode();

  public GlobalNode(IdentifierNode identifierNode,/* TypeSpecifierNode typeSpecifierNode,*/
      ExpressionNode expressionNode, SpecialSymbolNode specialSymbolNode) {
    this.identifierNode = identifierNode;
/*
    this.typeSpecifierNode = typeSpecifierNode;
*/
    this.assignmentNode = new AssignmentNode();
    this.expressionNode = expressionNode;
    this.specialSymbolNode = specialSymbolNode;
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
