package compiler.Parser.Grammar.Assignment.Node;

import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;

public class RecordAccessNode {

  private IdentifierNode identifierNode;
  private SpecialSymbolNode specialSymbolNodeOpen;
  private IdentifierNode identifierNode2;
  private AssignmentExpressionNode assignmentExpressionNode;

  public RecordAccessNode(AssignmentExpressionNode assignmentExpressionNode,
      IdentifierNode identifierNode, SpecialSymbolNode specialSymbolNodeOpen,
      IdentifierNode identifierNode2) {
    this.assignmentExpressionNode = assignmentExpressionNode;
    this.identifierNode = identifierNode;
    this.specialSymbolNodeOpen = specialSymbolNodeOpen;
    this.identifierNode2 = identifierNode2;
  }
}
