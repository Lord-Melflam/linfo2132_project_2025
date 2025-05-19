package compiler.Semantic.Types;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Semantic.Types.Expression.CheckExpressionType;

public class CheckArray {

  public String checkArray(MainNode mainNode, Type table)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    /*todo*/
    if (mainNode.getChildren().size() == 4) {
      MainNode size = (MainNode) mainNode.getChildren().get(1);
      CheckExpressionType checkExpressionType = new CheckExpressionType();
      String arraySizeType = checkExpressionType.checkExpressionType(size.getChildren(), table);
      if (!arraySizeType.equals("int") && !arraySizeType.equals("float")) {
        throw new GenericError(
            "Semantic Exception: Array error: the size provided is not an int type on line "
                + mainNode.getLine());
      }
      return "array";
    }
    throw new GenericError(
        "Semantic Exception: Array error: the size contains more than 1 expression on line "
            + mainNode.getLine());
  }
}
