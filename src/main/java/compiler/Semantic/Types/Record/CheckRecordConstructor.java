package compiler.Semantic.Types.Record;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.RecordError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.LinkedList;

public class CheckRecordConstructor {


  public String checkRecord(MainNode node, Type table)
      throws ScopeError, GenericError, TypeError, ArgumentError, OperatorError {
    GenericNode<?> recordName = (GenericNode<?>) node.getChildren().getFirst();
    Type record = null;
/*
    if (table.getCurrent().containsKey(recordName.getValue())) {
*/
    record = new Utils().lookInSymbolsTable(table, recordName.getValue(), recordName.getLine());
    for (ASTNode child : node.getChildren()) {
      if (child.getName().equals("Parameters")) {
        MainNode mainNode = (MainNode) child;
        LinkedList<String> types = new LinkedList<>();
        getSymbols(mainNode, types, table, "", (LinkedList<Type>) record.getParameters().clone());
        /*todo maybe add record const value in the table maybe same for the function*/
      }
    }
    /*} else {
      throw new ScopeError(
          "Semantic exception: Scope error: the requested element is not accessible in the current context"
              + "on line " + "{line}." + recordName.getValue() + " is not declared");
    }*/
    return record.getReturnType();
  }

  public void getSymbols(MainNode mainNode, LinkedList<String> args, Type table, String type,
      LinkedList<Type> functionArgsType)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    for (ASTNode child : mainNode.getChildren()) {
      getSymbolsImpl(child, true, args, table, type, functionArgsType);
    }
    if (!functionArgsType.isEmpty()) {
      throw new RecordError(Integer.toString(mainNode.getLine()),
          functionArgsType.pop().getName());
    }
  }

  private void getSymbolsImpl(ASTNode node, boolean isLast, LinkedList<String> list, Type table,
      String type, LinkedList<Type> functionArgsType)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    if (node == null) {
      return;
    }

    if (node instanceof GenericNode<?>) {
      list.add(((GenericNode<?>) node).getValue());
      Type value = functionArgsType.pop();
      if (!value.getReturnType().equals(type)) {
        throw new ArgumentError(value.getReturnType(), type,
            String.valueOf(((GenericNode<?>) node).getLine()));
      }

    } else if (node instanceof MainNode mainNode) {
      LinkedList<ASTNode> children = mainNode.getChildrenList();
      CheckExpressionType checkExpressionType = new CheckExpressionType();

      if (mainNode.getName().equals("ArrayInitialization")) {
        GenericNode<?> genericNode = (GenericNode<?>) children.pop();
        if (genericNode.getName().equals("Keyword") && genericNode.getValue().equals("array")) {
          String value = checkExpressionType.checkExpressionType(children.pop(), table);
          children.pop();
          if (!value.equals("int") && !value.equals("float")) {
            throw new ArgumentError(Integer.toString(genericNode.getLine()));
           /* throw new GenericError(
                "Semantic Exception: Array error: the size provided is not an int type on line "
                    + genericNode.getLine());*/
          }
          genericNode = (GenericNode<?>) children.pop();
          String typeSpecifier = genericNode.getValue() + "[]";
          if (!functionArgsType.peek().getReturnType().equals(typeSpecifier)) {
            throw new ArgumentError(Integer.toString(genericNode.getLine()));
            /*throw new GenericError("Semantic Exception: Array error:  Not the same type expected {"
                + functionArgsType.peek().getReturnType() + "} actual " + "{" + typeSpecifier
                + "} on line "
                + genericNode.getLine());*/
          }
          functionArgsType.pop();
          if (!children.isEmpty()) {
            children.pop();
          }
        }
      }
      if (!children.isEmpty()) {
        String value = checkExpressionType.checkExpressionType(children, table);
        getSymbolsImpl(children.getFirst(), 0 == children.size() - 1, list, table, value,
            functionArgsType);
      }
    }
  }

}
