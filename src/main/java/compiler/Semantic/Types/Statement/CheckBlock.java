package compiler.Semantic.Types.Statement;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.HashMap;
import java.util.LinkedList;

public class CheckBlock {

  public void checkBlock(Type table, LinkedList<ASTNode> children)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    HashMap<String, Type> functionTable = new HashMap<>();
    String identifier = "";
    String typeSpecifier = null;
    LinkedList<Type> functionArgTypes = new LinkedList<>();
    while (!children.isEmpty() && !children.peek().getName().equals("FunctionBlock")) {
      GenericNode<?> child = null;
      if (children.peek() instanceof GenericNode<?>) {
        child = (GenericNode<?>) children.pop();
      } else if (children.peek() instanceof MainNode) {
        MainNode mainNode = (MainNode) children.pop();
        if (mainNode.getName().equals("Parameters")) {
          new Utils().getSymbols(mainNode, functionArgTypes, table);
        }
        children.pop();
        continue;
      }
      assert child != null;
      if (child.getName().equals("Identifier")) {
        identifier = child.getValue();
        if (table.getCurrent().containsKey(identifier)) {
          throw new ArgumentError(Integer.toString(child.getLine()));
        }
      }
      if (child.getName().equals("TypeSpecifier")) {
        typeSpecifier = child.getValue();
      }
    }
    Type functionType = new Type("Function", identifier, null, typeSpecifier, functionArgTypes,
        table.getCurrent());
    table.getCurrent().put(identifier, functionType);
    functionTable.put(identifier, functionType);
    for (Type type : functionArgTypes) {
      functionTable.put(type.getName(),
          new Type("Function_Argument", type.getName(), table, type.getReturnType(), null,
              table.getCurrent()));
    }
    Type functionTableSymbol = new Type("Function", identifier, table, typeSpecifier,
        functionArgTypes,
        functionTable);

    CheckStatement checkStatement = new CheckStatement();
    MainNode mainNode = (MainNode) children.pop();

    checkStatement.checkStatement(mainNode.getChildren(), functionTableSymbol);

  }
}
