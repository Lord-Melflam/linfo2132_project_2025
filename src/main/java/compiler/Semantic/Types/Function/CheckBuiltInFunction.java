package compiler.Semantic.Types.Function;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Lexer.Symbols.TypeSpecifier;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.CheckArray;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CheckBuiltInFunction {

  HashMap<String, BuiltIn> builtInFunctionArgs = new HashMap<>();

  public CheckBuiltInFunction() {
    builtInFunctionArgs.put("readInt", new BuiltIn("readInt", "int", new LinkedList<>()));
    builtInFunctionArgs.put("readFloat", new BuiltIn("readFloat", "float", new LinkedList<>()));
    builtInFunctionArgs.put("readString", new BuiltIn("readString", "string", new LinkedList<>()));

    builtInFunctionArgs.put("!", new BuiltIn("!", "bool", new LinkedList<>(List.of("bool"))));
    builtInFunctionArgs.put("len",
        new BuiltIn("len", "int", new LinkedList<>(List.of("string", "array"))));
    builtInFunctionArgs.put("floor",
        new BuiltIn("floor", "int", new LinkedList<>(List.of("float"))));
    builtInFunctionArgs.put("chr", new BuiltIn("chr", "string", new LinkedList<>(List.of("int"))));
    builtInFunctionArgs.put("min",
        new BuiltIn("min", "int", new LinkedList<>(List.of("int", "int"))));
    builtInFunctionArgs.put("max",
        new BuiltIn("max", "int", new LinkedList<>(List.of("int", "int"))));

  }

  public String checkBuiltInFunction(LinkedList<ASTNode> children, Type table)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    GenericNode<?> child = (GenericNode<?>) children.peek();
    LinkedList<String> type = new LinkedList<>();
    if (builtInFunctionArgs.containsKey(child.getValue())) {
      BuiltIn builtIn = builtInFunctionArgs.get(child.getValue());
      int paramsNbr = 0;
      for (ASTNode astNode : children) {
        if (astNode.getName().equals("Parameters")) {
          MainNode params = (MainNode) astNode;
          getSymbols(params, type, table);
          paramsNbr = params.getChildren().size();
          checkArg(type, paramsNbr, builtIn, params.getLine());
        }
      }
      if (builtIn.getReturnType() == null) {
        return "void";
      }
      if ((builtIn.getName().equals("min") || builtIn.getName().equals("max")) && type.getFirst()
          .equals("float") && type.getLast().equals("float")) {
        return "float";
      }
      return builtIn.getReturnType();
    } else {
      checkWriteBuiltIn(children, table);
    }
    return "";
  }

  public void checkWriteBuiltIn(LinkedList<ASTNode> children, Type table) throws OperatorError {
    HashMap<String, BuiltIn> builtInFunctionArgs = new HashMap<>();
    builtInFunctionArgs.put("writeInt",
        new BuiltIn("writeInt", null, new LinkedList<>(List.of("int"))));
    builtInFunctionArgs.put("writeFloat",
        new BuiltIn("writeFloat", null, new LinkedList<>(List.of("float"))));

    builtInFunctionArgs.put("write", new BuiltIn("write", null, null));
    builtInFunctionArgs.put("writeln", new BuiltIn("writeln", null, null));
    builtInFunctionArgs.put("printType", new BuiltIn("printType", null, null));

    GenericNode<?> child = (GenericNode<?>) children.pop();

    if (builtInFunctionArgs.containsKey(child.getValue())) {
      BuiltIn builtIn = builtInFunctionArgs.get(child.getValue());
      int paramsNbr = 0;
      for (ASTNode astNode : children) {
        if (astNode.getName().equals("Parameters")) {
          MainNode params = (MainNode) astNode;
          LinkedList<String> type = new LinkedList<>();
          getSymbols(params, type, table);
          paramsNbr = params.getChildren().size();
          if (!builtIn.getName().equals("write") && !builtIn.getName().equals("writeln")
              && !builtIn.getName().equals("printType")) {
            checkArg(type, paramsNbr, builtIn, params.getLine());
          } else {
            if (paramsNbr != 1 && paramsNbr != 0) {
              throw new ArgumentError(
                  "Semantic exception: Argument error: incorrect number of arguments – expected {0 or 1}, received {"
                      + paramsNbr + "}" + " on line " + params.getLine());
            }
            if (paramsNbr == 0) {
              if (type.isEmpty()) {
                return;
              }
              throw new ArgumentError(
                  "Semantic exception: Argument error: incorrect number of arguments – expected {0 or 1}, received {"
                      + paramsNbr + "}" + " on line " + params.getLine());
            }
            if (!TypeSpecifier.TYPE_SPECIFIERS.contains(type.getFirst()) && !builtIn.getName()
                .equals("printType")) {
              throw new ArgumentError(
                  "Semantic exception: Argument error: Parameter need to be a primitive type on line "
                      + params.getLine());
            }

          }
        }
      }
    }
  }

  private void checkArg(LinkedList<String> type, int paramsNbr, BuiltIn builtIn, int line)
      throws ArgumentError, TypeError {
    int size = builtIn.getArgs().size();
    if (builtIn.getName().equals("len")) {
      size -= 1;
    }
    if (type.size() == paramsNbr) {

      if (size != paramsNbr) {
        throw new ArgumentError(line, size, paramsNbr);
      }
      for (int i = 0; i < type.size(); i++) {
        String node = type.get(i);
        if (builtIn.getName().equals("len")) {
          System.out.println();
          if (!new TypeSpecifier().isArrayTypeSpecifier(node) && !node.equals("string")) {
            throw new ArgumentError(line, size, paramsNbr);
          }
          /*if (!builtIn.getArgs().contains(node)) {
            if (!node.contains("float") && !builtIn.getArgs().contains("int")) {
              throw new ArgumentError(line, size, paramsNbr);
            }
          }*/

        } else {
          if (!builtIn.getArgs().get(i).equals(node)) {
            if ((builtIn.getName().equals("min") || builtIn.getName().equals("max") && node.equals(
                "float"))) {
              continue;
            }
            throw new ArgumentError(line, size, paramsNbr);

          }
        }
      }
    } else {
      throw new ArgumentError(line, size, paramsNbr);
    }
  }

  public void getSymbols(MainNode mainNode, LinkedList<String> args, Type table)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    for (ASTNode child : mainNode.getChildren()) {
      getSymbolsImpl(child, true, args, table);
    }
  }

  private void getSymbolsImpl(ASTNode node, boolean isLast, LinkedList<String> list, Type table)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    if (node == null) {
      return;
    }

    if (node instanceof GenericNode<?>) {
    } else if (node instanceof MainNode mainNode) {
      LinkedList<ASTNode> children = mainNode.getChildrenList();
      if (mainNode.getName().equals("ArrayInitialization")) {
        CheckArray checkArray = new CheckArray();
        String value = checkArray.checkArray(mainNode, table);
        list.add(value);
        return;
      } else if (mainNode.getName().equals("FunctionCall")) {
        CheckExpressionType checkExpressionType = new CheckExpressionType();
        String value = checkExpressionType.checkExpressionType(children.getFirst(), table);
        GenericNode<?> genericNode = (GenericNode<?>) children.pop();
        Type type = new Utils().lookInSymbolsTable(table, genericNode.getValue(),
            genericNode.getLine());
        LinkedList<ASTNode> childrenList = ((MainNode) mainNode.getChildrenList()
            .getFirst()).getChildrenList();
        if (((MainNode) mainNode.getChildrenList().getFirst()).getChildrenList().size()
            != type.getParameters().size()) {
          throw new ArgumentError("m");
        }
        for (int i = 0; i < childrenList.size(); i++) {
          ASTNode astNode = childrenList.get(i);
          String valueArg = checkExpressionType.checkExpressionType(astNode, table);
          if (!valueArg.equals(type.getParameters().get(i).getReturnType())) {
            throw new ArgumentError("h");

          }

        }
        list.add(value);
        return;
      } else if (mainNode.getName().equals("FieldAccess")) {
        String name = ((GenericNode<?>) ((MainNode) mainNode.getChildrenList()
            .getFirst()).getChildrenList().getFirst()).getValue();
        String reType = new Utils().lookInSymbolsTable(table, name, mainNode.getLine())
            .getReturnType();
        Type type = new Utils().lookInSymbolsTable(table, reType, mainNode.getLine());
        String attName = ((GenericNode<?>) mainNode.getChildrenList().get(2)).getValue();
        Type att = type.getParameters().stream().filter(a -> a.getName().equals(attName))
            .findFirst().get();
        list.add(att.getReturnType());
        return;
      }
      CheckExpressionType checkExpressionType = new CheckExpressionType();
      String value = checkExpressionType.checkExpressionType(children, table);
      list.add(value);
      /*}*/
    }
  }
}
