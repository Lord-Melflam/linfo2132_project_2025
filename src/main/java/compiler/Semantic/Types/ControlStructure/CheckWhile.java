package compiler.Semantic.Types.ControlStructure;

import compiler.Exceptions.Semantic.MissingConditionError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractMainNodeVisitor;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Statement.CheckStatement;
import compiler.Semantic.Types.Type;
import java.util.HashMap;

public class CheckWhile {

  public void checkWhile(MainNode mainNode, Type functionTable) throws OperatorError {
    MainNode whileNode = null;
    boolean cond = false;
    MainNode whileStatement = null;

    for (ASTNode astNode : mainNode.getChildrenList()) {
      if (astNode.getName().contains("Expression")) {
        whileNode = (MainNode) astNode;
        cond = checkCond((MainNode) astNode, functionTable);
      }
      if (astNode.getName().equals("Statements")) {
        whileStatement = (MainNode) astNode;
      }
    }
    if (cond) {
      HashMap<String, Type> typeHashMap = new HashMap<>();
      Type whileType = new Type("While", "While", functionTable, null, null, typeHashMap);
//      GenericNode<?> identifier = (GenericNode<?>) whileNode.getChildrenList().pop();
//      if (identifier.getName().equals("Identifier")) {
//        whileType.getCurrent().put(identifier.getValue(),
//            new Type("WhileCond", identifier.getValue(), functionTable, "bool", null,
//                whileType.getCurrent()));
//      }
      CheckStatement checkStatement = new CheckStatement();
      if (whileStatement != null && !whileStatement.getChildrenList().isEmpty()) {
        for (ASTNode node : whileStatement.getChildrenList()) {
          MainNode mainNode1 = node.accept(new ExtractMainNodeVisitor());
          if (mainNode1 != null) {
            checkStatement.checkBlock((MainNode) node, whileType);
          }
        }
      }

    } else {
      assert whileNode != null;
      throw new MissingConditionError(whileNode.getLine());
    }
  }

  public boolean checkCond(MainNode astNode, Type functionTable) throws OperatorError {
    //functionTable.getCurrent().put();
    CheckExpressionType checkExpressionType = new CheckExpressionType();
    String type = checkExpressionType.checkExpressionType(astNode.getChildren(), functionTable);
    return type.equals("bool");
  }
}
