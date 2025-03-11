package compiler.Parser.Grammar.Assignment.Node;

import compiler.Parser.AST.AssignmentNode;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class AssignmentExpressionNode extends ASTNode {

  private IdentifierNode identifierNode;
  private AssignmentExpressionNode arrayAccessNode;
  private AssignmentExpressionNode recordAccessNode;
  private final AssignmentNode assignmentNode;
  private final ExpressionNode expressionNode;
  private final SpecialSymbolNode specialSymbolNode;


  @Override
  public void accept(ASTVisitor visitor) {

  }
}
