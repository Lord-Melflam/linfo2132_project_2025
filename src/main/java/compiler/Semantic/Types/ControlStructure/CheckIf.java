package compiler.Semantic.Types.ControlStructure;

import compiler.Exceptions.Semantic.MissingConditionError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Function.Function;
import compiler.Semantic.Types.Statement.CheckStatement;
import compiler.Semantic.Types.Type;
import java.util.HashMap;

public class CheckIf {

  public void check(MainNode block, Type functionTable) throws OperatorError {
    MainNode ifNode = null;
    boolean cond = false;
    MainNode ifStatement = null;
    MainNode elseStatement = null;

    for (ASTNode astNode : block.getChildrenList()) {
      if (astNode.getName().contains("Expression")) {
        ifNode = (MainNode) astNode;
        cond = checkCond((MainNode) astNode, functionTable);
      } else if (astNode.getName().equals("FunctionCall")) {
        Function function = new Function();
        String typeCond = function.checkFunction((MainNode) astNode, functionTable);
        cond = typeCond.equals("bool");
      }
      if (astNode.getName().equals("Statements")) {
        if (ifStatement == null) {
          ifStatement = (MainNode) astNode;
        } else {
          elseStatement = (MainNode) astNode;
        }
      }
    }
    if (cond) {
      HashMap<String, Type> typeHashMap = new HashMap<>();
      Type ifType = new Type("If", "If", functionTable, null, null, typeHashMap);
      //assert ifNode != null;
      //GenericNode<?> identifier = (GenericNode<?>) ifNode.getChildrenList().pop();
//      if (identifier.getName().equals("Identifier")) {
//        ifType.getCurrent().put(identifier.getValue(),
//            new Type("IfCond", identifier.getValue(), functionTable, "bool", null,
//                ifType.getCurrent()));
//      }
      CheckStatement checkStatement = new CheckStatement();
      assert ifStatement != null;
      for (ASTNode node : ifStatement.getChildrenList()) {
        if (!node.getName().equals("Punctuation")) {
          checkStatement.checkBlock((MainNode) node, ifType);
        }
      }
      if (elseStatement != null) {
        for (ASTNode node : elseStatement.getChildrenList()) {
          if (!node.getName().equals("Punctuation")) {
            checkStatement.checkBlock((MainNode) node, ifType);
          }
        }
      }
    } else {
      assert ifNode != null;
      throw new MissingConditionError(ifNode.getLine());
    }
  }

  public boolean checkCond(MainNode astNode, Type functionTable) throws OperatorError {
    CheckExpressionType checkExpressionType = new CheckExpressionType();
    String type = checkExpressionType.checkExpressionType(astNode.getChildren(), functionTable);
    return type.equals("bool");
  }
}
