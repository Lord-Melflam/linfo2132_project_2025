package compiler.Semantic.Types.Statement;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ReturnError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Type;

public class CheckReturn {

  public void checkReturn(MainNode mainNode, Type functionTable)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    CheckExpressionType checkExpressionType = new CheckExpressionType();
    String type = checkExpressionType.checkExpressionType(mainNode.getChildren(), functionTable);
    if (!type.equals(functionTable.getReturnType())) {
      throw new ReturnError(mainNode.getLine());
    }
  }
}
