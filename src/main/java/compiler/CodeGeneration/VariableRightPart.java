package compiler.CodeGeneration;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.LinkedList;
import java.util.List;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class VariableRightPart {

  private Table table;
  private String variableName;
  LocalIndexAllocator allocator;
  Label start, end;

  public VariableRightPart(Table table, String variableName, LocalIndexAllocator allocator,
      Label start, Label end) {
    this.table = table;
    this.variableName = variableName;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public LinkedList<String> checkVariableRightPart2(MethodVisitor cw, ASTNode expression)
      throws OperatorError {
    MainNode mainNode = (MainNode) expression;
    for (ASTNode astNode : mainNode.getChildrenList()) {
      switch (mainNode.getName()) {
        case "FunctionCall" -> {
          FunctionCall function = new FunctionCall(table, allocator, start, end);
          Table index = function.checkFunction(cw, mainNode);
          return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
        }
     /* case "Expression" -> {
        ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
            allocator, start, end);
        Table index = expressionCodeGenerator.checkExpression(expression, cw);
        return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
      }*/

        case "Identifier" -> {
          GenericNode<?> identifierNode = (GenericNode<?>) astNode;
          Table index = Utils.searchIdentifier(cw, table, identifierNode.getValue(), false, false);
          return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
        }
        case "Literal" -> {
          GenericNode<?> literalNode = (GenericNode<?>) astNode;
          return new LinkedList<>(List.of("Literal", literalNode.getValue()));
        }
        case "BuiltInFunctionCall" -> {
          BuiltInFunctionCall builtInFunctionCall = new BuiltInFunctionCall(table, variableName,
              allocator, start, end);
          Table index = builtInFunctionCall.CheckBuiltInFunctionCall(cw, expression, false);
          //GenericNode<?> genericNode = (GenericNode<?>) astNode;
          GenericNode<?> genericNode = (GenericNode<?>) mainNode.getChildrenList().getFirst();
          if (!genericNode.getValue().contains("write")) {
            return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));

          }
        }

        case "Record" -> {
          RecordCall recordCall = new RecordCall(table, allocator, start, end);
          int index = recordCall.checkConstructor(cw, mainNode, variableName);
        }
      }
    }
    return null;
  }

  public LinkedList<String> checkVariableRightPart(MethodVisitor cw, ASTNode expression,
      Boolean isConstructor)
      throws OperatorError {
    MainNode mainNode = (MainNode) expression;
    //for (ASTNode astNode : mainNode.getChildrenList()) {
    System.out.println(mainNode.getName());
    switch (mainNode.getName()) {
      case "FunctionCall" -> {
        FunctionCall function = new FunctionCall(table, allocator, start, end);
        Table index = function.checkFunction(cw, mainNode);
        if (index.index == -1) {
          return new LinkedList<>(
              List.of("Identifier", Integer.toString(index.index), index.name));
        }
        return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
      }
      case "Expression" -> {
        ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
            allocator, start, end);
        Table index = expressionCodeGenerator.checkExpression(expression, cw, isConstructor);
        if (index.index == -1) {
          return new LinkedList<>(
              List.of("Identifier", Integer.toString(index.index), index.name));
        }
        return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
      }

     /* case "Identifier" -> {
        GenericNode<?> identifierNode = (GenericNode<?>) astNode;
        Table index = Utils.searchIdentifier(cw, table, identifierNode.getValue(), false);
        return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
      }
      case "Literal" -> {
        GenericNode<?> literalNode = (GenericNode<?>) astNode;
        return new LinkedList<>(List.of("Literal", literalNode.getValue()));
      }*/
      case "BuiltInFunctionCall" -> {
        BuiltInFunctionCall builtInFunctionCall = new BuiltInFunctionCall(table, variableName,
            allocator, start, end);
        Table index = builtInFunctionCall.CheckBuiltInFunctionCall(cw, expression, isConstructor);
        //GenericNode<?> genericNode = (GenericNode<?>) astNode;
        GenericNode<?> genericNode = (GenericNode<?>) mainNode.getChildrenList().getFirst();
        if (!genericNode.getValue().contains("write")) {
          if (index.index == -1) {
            return new LinkedList<>(
                List.of("Identifier", Integer.toString(index.index), index.name));
          }
          return new LinkedList<>(List.of("Identifier", Integer.toString(index.index)));
        }
      }

      case "RecordConstructorCall" -> {
        RecordCall recordCall = new RecordCall(table, allocator, start, end);
        int index = recordCall.checkConstructor(cw, mainNode, variableName);
      }
    }
    // }
    return null;
  }
}
