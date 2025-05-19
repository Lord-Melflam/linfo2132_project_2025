package compiler.Semantic.Types.Function;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Function {

  HashMap<String, BuiltIn> builtInFunctionArgs = new HashMap<>();

  public Function() {
    builtInFunctionArgs.put("writeInt",
        new BuiltIn("writeInt", null, new LinkedList<>(List.of("int"))));
    builtInFunctionArgs.put("writeFloat",
        new BuiltIn("writeFloat", null, new LinkedList<>(List.of("float"))));

    builtInFunctionArgs.put("write", new BuiltIn("write", null, null));
    builtInFunctionArgs.put("writeln", new BuiltIn("writeln", null, null));
  }

  public String checkFunction(MainNode function, Type table)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    int paramsNbr = 0;
    String functionIdentifier = "";
    Type type1 = null;
    for (ASTNode astNode : function.getChildren()) {
      if (astNode.getName().equals("Identifier")) {
        GenericNode<?> node = (GenericNode<?>) astNode;
        functionIdentifier = node.getValue();
      }

      if (astNode.getName().equals("Parameters")) {
        MainNode params = (MainNode) astNode;
        LinkedList<String> type = new LinkedList<>();
        type1 = new Utils().lookInSymbolsTable(table, functionIdentifier, params.getLine());
        getSymbols((MainNode) astNode, type, table, "", type1.getParameters());
      }
    }
    assert type1 != null;
    return type1.getReturnType();
  }


  public void getSymbols(MainNode mainNode, LinkedList<String> args, Type table, String type,
      LinkedList<Type> functionArgsType)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    for (ASTNode child : mainNode.getChildren()) {
      getSymbolsImpl(child, true, args, table, type, functionArgsType);
    }
    if (!functionArgsType.isEmpty()) {
      throw new ArgumentError(mainNode.getLine(), table.getParameters().size(), args.size());
    }
  }

  private void getSymbolsImpl(ASTNode node, boolean isLast, LinkedList<String> list,
      Type table, String type, LinkedList<Type> functionArgsType)
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
      List<ASTNode> children = mainNode.getChildren();
      CheckExpressionType checkExpressionType = new CheckExpressionType();
      String value = checkExpressionType.checkExpressionType(children, table);
      getSymbolsImpl(children.getFirst(), 0 == children.size() - 1, list, table, value,
          functionArgsType);
    }
  }
}
