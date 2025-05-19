package compiler.Semantic.Types.Expression;


import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Lexer.Symbols.Literal;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractGenericVisitor;
import compiler.Semantic.ExtractMainNodeVisitor;
import compiler.Semantic.Types.Function.CheckBuiltInFunction;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.LinkedList;
import java.util.List;

/*
public class CheckExpressionType {

  public String checkExpressionType(List<ASTNode> nodes, Type symbolTable)
      throws TypeException, ArgumentError, GenericError, ScopeError {
    if (nodes == null || nodes.isEmpty()) {
      return "Unknown";
    }

    ASTNode firstNode = nodes.get(0);

    if (firstNode instanceof GenericNode<?> genericNode) {
      String nodeValue = genericNode.getValue();

      if (genericNode.getName().equals("Identifier")) {
        return new Utils().lookInSymbolsTable(symbolTable, nodeValue).getReturnType();
      }

      if (genericNode.getName().equals("Literal")) {
        Literal literal = new Literal();
        if (literal.isFloat(nodeValue)) {
          return "float";
        }
        if (literal.isInt(nodeValue)) {
          return "int";
        }
        if (literal.isString(nodeValue)) {
          return "string";
        }
        if (literal.isArray(nodeValue)) {
          return "array";
        }
        return "bool";
      }
      if (genericNode.getName().equals("BuiltInFunction")) {
        CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();
        return checkBuiltInFunction.checkBuiltInFunction((LinkedList<ASTNode>) nodes, symbolTable);
      }
    }

    if (nodes.size() == 3 && nodes.get(1) instanceof GenericNode<?>) {
      String leftType = checkExpressionType(List.of(nodes.get(0)), symbolTable);
      String rightType = checkExpressionType(List.of(nodes.get(2)), symbolTable);
      String operator = ((GenericNode<?>) nodes.get(1)).getValue().toString();

      if ("&& ||".contains(operator)) {
        if (!leftType.equals("bool") || !rightType.equals("bool")) {
          throw new RuntimeException(
              "Erreur: Opération logique '" + operator + "' entre " + leftType + " et "
                  + rightType);
        }
        return "bool";
      }

      if ("== != < > <= >=".contains(operator)) {
        if (!leftType.equals("int") || !rightType.equals("int")) {
          throw new RuntimeException(
              "Erreur: Comparaison entre types invalides: " + leftType + " et " + rightType);
        }
        return "bool";
      }

      if ("+/-*%".contains(operator)) {
        if (operator.equals("+")) {
          if (leftType.equals("string") || rightType.equals("string")) {
            return "string";
          }
        }
        if (leftType.equals("string") || rightType.equals("string")) {
          throw new RuntimeException(
              "Erreur: Opération arithmétique invalide entre " + leftType + " et " + rightType);
        }
        if ((!leftType.equals("int") && !leftType.equals("float")) ||
            (!rightType.equals("int") && !rightType.equals("float"))) {
          throw new RuntimeException(
              "Erreur: Opération arithmétique invalide entre " + leftType + " et " + rightType);
        }
        if (leftType.equals("float") || rightType.equals("float")) {
          return "float";
        }
        return "int";
      }
    }

    if (nodes.size() == 2 && nodes.get(0) instanceof GenericNode<?>) {
      return checkExpressionType(List.of(nodes.get(1)), symbolTable);
    }

    if (firstNode instanceof MainNode mainNode) {
      return checkExpressionType(mainNode.getChildren(), symbolTable);
    }

    return "Unknown";
  }


  private boolean isZero(ASTNode node) {
    if (node instanceof GenericNode<?>) {
      String value = ((GenericNode<?>) node).getValue();
      return value.equals("0") || value.equals("0.0");
    }
    return false;
  }
}

 */
public class CheckExpressionType {

  ExtractMainNodeVisitor extractMainNodeVisitor = new ExtractMainNodeVisitor();
  ExtractGenericVisitor extractGenericVisitor = new ExtractGenericVisitor();

  public CheckExpressionType() {
  }

  public String checkExpressionType(ASTNode node, Type symbolTable)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {

    if (node == null) {
      return "Unknown";
    }

    // ✅ Si c’est un GenericNode
    GenericNode<?> genericNode = node.accept(extractGenericVisitor);
    if (genericNode != null) {
      String nodeValue = genericNode.getValue();
      String nodeName = genericNode.getName();

      return switch (nodeName) {
        case "Identifier" ->
            new Utils().lookInSymbolsTable(symbolTable, nodeValue, genericNode.getLine())
                .getReturnType();
        case "Literal" -> {
          Literal literal = new Literal();
          if (literal.isFloat(nodeValue)) {
            yield "float";
          }
          if (literal.isInt(nodeValue)) {
            yield "int";
          }
          if (literal.isString(nodeValue)) {
            yield "string";
          }
          if (literal.isArray(nodeValue)) {
            yield "array";
          }
          yield "bool";
        }
        case "BuiltInFunction" -> {
          CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();
          yield checkBuiltInFunction.checkBuiltInFunction(new LinkedList<>(List.of(node)),
              symbolTable);
        }
        default -> "Unknown";
      };
    }

    // ✅ Si c’est un MainNode, on descend dans ses enfants
    MainNode mainNode = node.accept(extractMainNodeVisitor);
    if (mainNode.getName().equals("BuiltInFunctionCall")) {
      CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();
      return checkBuiltInFunction.checkBuiltInFunction(mainNode.getChildrenList(), symbolTable);
    } else if (mainNode.getName().equals("FieldAccess")) {
      String name = ((GenericNode<?>) ((MainNode) mainNode.getChildrenList()
          .getFirst()).getChildrenList().getFirst()).getValue();
      String reType = new Utils().lookInSymbolsTable(symbolTable, name, mainNode.getLine())
          .getReturnType();
      Type type = new Utils().lookInSymbolsTable(symbolTable, reType, mainNode.getLine());
      String attName = ((GenericNode<?>) mainNode.getChildrenList().get(2)).getValue();
      Type att = type.getParameters().stream().filter(a -> a.getName().equals(attName)).findFirst()
          .get();
      System.out.println();
      return att.getReturnType();
    } else {
      if (mainNode != null) {
        return checkExpressionType(mainNode.getChildren(), symbolTable);
      }
    }
    return "Unknown";
  }

  public String checkExpressionType(List<ASTNode> nodes, Type symbolTable)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {

    if (nodes == null || nodes.isEmpty()) {
      return "Unknown";
    }

    ASTNode firstNode = nodes.get(0);

    if (size(nodes) == 1) {
      return checkExpressionType(firstNode, symbolTable);
    }

    // Cas binaire : opérateurs entre 3 noeuds
    if (size(nodes) == 3 && nodes.get(1) instanceof GenericNode<?>) {
      String leftType = checkExpressionType(nodes.get(0), symbolTable);
      String rightType = checkExpressionType(nodes.get(2), symbolTable);
      String operator = ((GenericNode<?>) nodes.get(1)).getValue().toString();
      if (leftType.contains("[]")) {
        leftType = leftType.substring(0, leftType.length() - 2);
      }
      if (rightType.contains("[]")) {
        rightType = rightType.substring(0, rightType.length() - 2);
      }
      if ("&& ||".contains(operator)) {
        if (!leftType.equals("bool") || !rightType.equals("bool")) {
          throw new OperatorError(((GenericNode<?>) nodes.get(1)).getLine());
        }
        return "bool";
      }

      if ("== != < > <= >=".contains(operator)) {
        if (!(leftType.equals("int") || leftType.equals("float")) || !(rightType.equals("int")
            || rightType.equals("float"))) {
          throw new OperatorError(((GenericNode<?>) nodes.get(1)).getLine());
        }
        return "bool";
      }

      if ("+-*/%".contains(operator)) {
        if (operator.equals("+") && (leftType.equals("string") || rightType.equals("string"))) {
          return "string";
        }
        if (leftType.equals("string") || rightType.equals("string")) {
          throw new OperatorError(((GenericNode<?>) nodes.get(1)).getLine());
        }
        /* *//*todo use float for mixed expression*//*
        if ((!leftType.equals("int") && !leftType.equals("float")) || (!rightType.equals("int")
            && !rightType.equals("float"))) {
          throw new OperatorError(((GenericNode<?>) nodes.get(1)).getLine());
        }*/
        if (leftType.equals("float") || rightType.equals("float")) {
          return "float";
        }
        return "int";
      }
    }

    // Cas unaires, exemple : négation ou parenthèses
/*
    if (nodes.size() == 2 && nodes.get(0) instanceof GenericNode<?>) {
*/
    if (size(nodes) == 2 && nodes.get(0) instanceof GenericNode<?>) {

      return checkExpressionType(nodes.get(1), symbolTable);
    }

    // Si le 1er noeud est un MainNode, on descend
    MainNode mainNode = firstNode.accept(extractMainNodeVisitor);
    if (mainNode != null) {
      return checkExpressionType(mainNode.getChildren(), symbolTable);
    }

    return "Unknown";
  }


  public int size(List<ASTNode> nodes) {
    int counter = 0;
    for (ASTNode astNode : nodes) {
      if (!astNode.getName().equals("SpecialSymbol")) {
        counter++;
      }
    }
    return counter;
  }

  /*todo dont forget*/
  private boolean isZero(ASTNode node) {
    if (node instanceof GenericNode<?>) {
      String value = ((GenericNode<?>) node).getValue().toString();
      return value.equals("0") || value.equals("0.0");
    }
    return false;
  }
}


