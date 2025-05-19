package compiler.Semantic.Types.ControlStructure;

import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractGenericVisitor;
import compiler.Semantic.ExtractMainNodeVisitor;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Statement.CheckStatement;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.HashMap;

public class CheckFor {

  public void checkFor(MainNode mainNode, Type functionTable) throws OperatorError {
    ExtractGenericVisitor visitor = new ExtractGenericVisitor();
    String identifier = "";
    String typeSpecifier = "";
    Type typeIdentifier = null;
    int cntExp = 1;
    while (!mainNode.getChildrenList().isEmpty() && !mainNode.getChildrenList().peek().getName()
        .equals("Statements")) {

      ASTNode node = mainNode.getChildrenList().pop();
      GenericNode<?> child = node.accept(visitor);
      if (child != null) {

        if (child.getName().equals("Identifier")) {
          identifier = child.getValue();
          try {
            typeIdentifier = new Utils().lookInSymbolsTable(functionTable, identifier,
                child.getLine());
            if (!typeIdentifier.getReturnType().equals("int") && !typeIdentifier.getReturnType()
                .equals("float")) {
              throw new GenericError(
                  "The first element of a for loop must be an int on line " + child.getLine());
            }
          } catch (Exception e) {

          }
        }
        if (child.getName().equals("TypeSpecifier")) {
          typeSpecifier = child.getValue();
          if (!typeSpecifier.equals("int") && !typeSpecifier.equals("float")) {
            throw new GenericError(
                "The first element of a for loop must be an int on line " + child.getLine());

          }
        }
      }
      if (mainNode != null && !mainNode.getChildrenList().isEmpty()) {
        if (mainNode.getChildrenList().peek().getName().equals("Expression")) {
          cntExp++;
          //expressions.add((MainNode) mainNode.getChildrenList().peek());
          CheckExpressionType checkExpressionType = new CheckExpressionType();
          String type = checkExpressionType.checkExpressionType(mainNode.getChildrenList().peek(),
              functionTable);
          if (!type.equals("int") && !type.equals("float")) {
            throw new GenericError(
                "The " + cntExp + " element of a for loop must be an int on line "
                    + child.getLine());
          }

        }
      }
    }
    CheckStatement checkStatement = new CheckStatement();
    HashMap<String, Type> typeHashMap = new HashMap<>();
    typeHashMap.put(identifier,
        new Type("forIdentifier", identifier, functionTable, "int", null, typeHashMap));
    Type ifType = new Type("forBlock", "forBlock", functionTable, null, null, typeHashMap);
    if (mainNode != null && !mainNode.getChildrenList().isEmpty()) {
      MainNode forBlock = (MainNode) mainNode.getChildrenList().getFirst();
      for (ASTNode node : forBlock.getChildrenList()) {
        MainNode mainNode1 = node.accept(new ExtractMainNodeVisitor());
        if (mainNode1 != null) {
          checkStatement.checkBlock((MainNode) node, ifType);
        }
      }
    }
  }
}
