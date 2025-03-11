package compiler.Parser.Grammar.Assignment.Node;

import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;

public class ArrayAccessNode {

  private IdentifierNode identifierNode;
  private SpecialSymbolNode specialSymbolNodeOpen;
  private ExpressionNode expressionNode;
  private SpecialSymbolNode specialSymbolNodeClose;
  private AssignmentExpressionNode assignmentExpressionNode;

  public ArrayAccessNode(AssignmentExpressionNode assignmentExpressionNode,
      IdentifierNode identifierNode, SpecialSymbolNode specialSymbolNodeOpen,
      ExpressionNode expressionNode, SpecialSymbolNode specialSymbolNodeClose) {
    this.assignmentExpressionNode = assignmentExpressionNode;
    this.identifierNode = identifierNode;
    this.specialSymbolNodeOpen = specialSymbolNodeOpen;
    this.expressionNode = expressionNode;
    this.specialSymbolNodeClose = specialSymbolNodeClose;
  }
}
