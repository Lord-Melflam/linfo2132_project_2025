package compiler.Parser.Grammar.Expression.Node;

import compiler.Parser.AST.OperatorNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class BinaryOperator extends ASTNode {

  /*
    ExpressionNode left;
  */
  OperatorNode operator;
/*
  ExpressionNode right;
*/

  public BinaryOperator(/*ExpressionNode left,*/ OperatorNode operator/*, ExpressionNode right*/) {
/*
    this.left = left;
*/
    this.operator = operator;
/*
    this.right = right;
*/
  }

  @Override
  public String toString() {
    return operator.toString();
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
