package compiler.Semantic.Types.Assignment;

import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Lexer.Symbols.Record;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractGenericVisitor;
import compiler.Semantic.ExtractMainNodeVisitor;
import compiler.Semantic.Types.Constant.CheckConstantType;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.LinkedList;

public class CheckAssignment {

  private LinkedList<Type> recordCreation = new LinkedList<>();

  public CheckAssignment() {
  }

  public LinkedList<Type> getRecordCreation() {
    return recordCreation;
  }

  public void setRecordCreation(LinkedList<Type> recordCreation) {
    this.recordCreation = recordCreation;
  }

  public void checkAssignment(MainNode mainNode, Type functionTable) throws OperatorError {
    if (mainNode.getChildrenList().isEmpty()) {
      return;
    }
    CheckExpressionType checkExpressionType = new CheckExpressionType();
    CheckConstantType checkConstantType = new CheckConstantType();

    GenericNode<?> genericNode = (GenericNode<?>) ((MainNode) mainNode.getChildrenList()
        .getFirst()).getChildrenList().getFirst();
    Type type = null;
    try {
      type = new Utils().lookInSymbolsTable(functionTable, genericNode.getValue(),
          genericNode.getLine());
    } catch (Exception e) {
    }

    if (type == null) {
      LinkedList<String> strings = lookInSymbolsTable(functionTable, genericNode.getValue(),
          genericNode.getLine());
      System.out.println();
      type = new Type("", "", null, strings.peek(), null, null);

    }
    Type record = new Utils().lookInSymbolsTable(functionTable, type.getReturnType(),
        genericNode.getLine());
    int index = 0;
    String lastType = "";
    while (!mainNode.getChildrenList().peek().getName().equals("RecordAssignment")) {
      MainNode node = mainNode.getChildrenList().peek().accept(new ExtractMainNodeVisitor());

      if (mainNode.getChildrenList().peek().getName().contains("Array")) {
        for (ASTNode astNode : node.getChildrenList()) {
          if (astNode.getName().equals("Identifier")) {
            GenericNode<?> genericNode1 = astNode.accept(new ExtractGenericVisitor());
            for (Type arg : record.getParameters()) {
              if (arg.getName().equals(genericNode1.getValue())) {
                lastType = arg.getReturnType().substring(0, arg.getReturnType().length() - 2);
                if (!arg.getReturnType().contains("[]")) {
                  throw new TypeError(genericNode1.getLine());
                }
                if (!mainNode.getChildrenList().isEmpty()) {
                  mainNode.getChildrenList().pop();
                }
                // checkAssignment(mainNode, functionTable);
                break;
              }
            }
          }
          if (astNode.getName().contains("Expression")) {
            String sizeType = checkExpressionType.checkExpressionType(astNode, functionTable);
            if (!sizeType.equals("int") && !sizeType.equals("float")) {
              throw new TypeError(String.valueOf(astNode.getLine()), "int", sizeType);
            }
          }
        }
      } else {
        System.out.println();
        for (ASTNode astNode : node.getChildrenList()) {
          if (astNode.getName().equals("Identifier")) {
            GenericNode<?> genericNode1 = astNode.accept(new ExtractGenericVisitor());
            for (Type arg : record.getParameters()) {
              if (arg.getName().equals(genericNode1.getValue())) {
                lastType = arg.getReturnType();
                if (new Record().matches(lastType)) {
                  if (!mainNode.getChildrenList().isEmpty()) {
                    mainNode.getChildrenList().pop();
                  }
                  //checkAssignment(mainNode, functionTable);
                }
                break;
              }
            }
          }
        }

      }
      index++;
      if (!mainNode.getChildrenList().isEmpty()) {
        mainNode.getChildrenList().pop();
      }
    }
    System.out.println();
    for (ASTNode astNode : ((MainNode) mainNode.getChildrenList().getFirst()).getChildrenList()) {
      if (astNode.getName().equals("Expression")) {
        String assignmentType = checkExpressionType.checkExpressionType(astNode, functionTable);
        if (!assignmentType.equals(lastType)) {
          throw new TypeError(String.valueOf(astNode.getLine()), lastType, assignmentType);
        }
      }
    }
  }

  public LinkedList<String> lookInSymbolsTable(Type symbolTable, String nodeValue, int line)
      throws ScopeError {
    Type temp = symbolTable;
    while (temp.getPrevious() != null) {
      if (!temp.getReccordAtt().isEmpty()) {
        LinkedList<String> recordName = temp.findRecordName(nodeValue);
        if (!recordName.isEmpty()) {
          return recordName;
        }
      }
      temp = temp.getPrevious();
    }
    if (!temp.getReccordAtt().isEmpty()) {
      LinkedList<String> recordName = temp.findRecordName(nodeValue);
      if (!recordName.isEmpty()) {
        return recordName;
      }
    }
    throw new ScopeError(nodeValue, line);
  }

  public void checkArrayAssignment(MainNode mainNode, Type functionTable) throws OperatorError {
    if (mainNode.getChildrenList().isEmpty()) {
      return;
    }
    CheckExpressionType checkExpressionType = new CheckExpressionType();
    CheckConstantType checkConstantType = new CheckConstantType();

    GenericNode<?> genericNode = (GenericNode<?>) ((MainNode) mainNode.getChildrenList()
        .getFirst()).getChildrenList().getFirst();
    Type record = new Utils().lookInSymbolsTable(functionTable, genericNode.getValue(),
        genericNode.getLine());
    if (!record.getReturnType().contains("[]")) {
      throw new TypeError(genericNode.getLine());
    }
    while (!mainNode.getChildrenList().peek().getName().equals("RecordAssignment")) {
      MainNode node = mainNode.getChildrenList().peek().accept(new ExtractMainNodeVisitor());
      if (mainNode.getChildrenList().peek().getName().contains("RecordArrayAttribute")) {
        for (ASTNode astNode : node.getChildrenList()) {
          if (astNode.getName().contains("Expression")) {
            String sizeType = checkExpressionType.checkExpressionType(astNode, functionTable);
            if (!sizeType.equals("int") && !sizeType.equals("float")) {
              throw new TypeError(String.valueOf(astNode.getLine()), "int", sizeType);
            }
          }
        }
      }
      if (!mainNode.getChildrenList().isEmpty()) {
        mainNode.getChildrenList().pop();
      }
    }
    /*String assignmentvType = checkConstantType.checkAssignment(
        ((MainNode) mainNode.getChildrenList().getFirst()).getChildrenList(),
        functionTable);*/

    for (ASTNode astNode : ((MainNode) mainNode.getChildrenList().getFirst()).getChildrenList()) {
      MainNode node = astNode.accept(new ExtractMainNodeVisitor());
      if (node != null) {
        String assignmentType = checkConstantType.checkAssignment(node,
            functionTable);

        if (!record.getReturnType().contains(assignmentType)) {
          throw new TypeError(
              record.getReturnType(),
              assignmentType, String.valueOf(astNode.getLine()));
        }
      }
    }
  }
}
/*
public void checkAssignment(MainNode mainNode, Type functionTable) throws OperatorError {
  if (mainNode.getChildrenList().isEmpty()) {
    return;
  }

  if (type.getSymbolTypeName().equals("Array") && genericNode.getValue()
      .equals(TokenType.LBRACKET.getValue())) {
    String sizeType = checkExpressionType.checkExpressionType(mainNode.getChildrenList().pop(),
        functionTable);
    if (!sizeType.equals("int")) {
      throw new GenericError(
          "Semantic Exception: Array error: the size provided is not an int type on line "
              + genericNode.getLine());
    }

    while (!mainNode.getChildrenList().isEmpty() && !mainNode.getChildrenList().peek().getName()
        .equals("Expression")) {
      mainNode.getChildrenList().pop();
    }
    if (mainNode.getChildrenList().peek().getName().equals("Expression")) {
      String elementType = checkExpressionType.checkExpressionType(
          mainNode.getChildrenList().pop(), functionTable);
      if (!type.getReturnType().contains(elementType)) {
        throw new GenericError(
            "Semantic Exception: Array error: the element does not have the same type as the array type"
                + genericNode.getLine());
      }
    }

  } else if (*//*type.getSymbolTypeName().equals("Record") && *//*genericNode.getValue()
        .equals(TokenType.DOT.getValue())){
    Type type1 = null;
    Type returnType = null;
    boolean isArray = false;
    String assignmentType = "";
    LinkedList<ASTNode> childrenList = mainNode.getChildrenList();
    for (int i = 0; i < childrenList.size(); i++) {
      ASTNode astNode = childrenList.get(i);
      if (astNode.getName().equals("Assignment")) {
        assignmentType = checkExpressionType.checkExpressionType(childrenList.get(i + 1),
            functionTable);
        break;
      }
      if (astNode.getName().equals("Identifier")) {
        GenericNode<?> astNode1 = (GenericNode<?>) astNode;
        LinkedList<String> recordNameList = lookInSymbolsTable(functionTable, astNode1.getValue(),
            astNode1.getLine());
        for (String s : recordNameList) {
          type1 = new Utils().lookInSymbolsTable(functionTable, s, astNode1.getLine());
          for (Type type2 : type1.getParameters()) {
            if (type2.getName().equals(astNode1.getValue())) {
              returnType = type2;
            }
          }

        }
        if (returnType == null) {
          throw new IllegalStateException();
        }
        isArray = returnType.getReturnType().contains("[]");
      }
      if (isArray && astNode.getName().equals("Expression")) {
        String elementType = checkExpressionType.checkExpressionType(astNode, functionTable);
        if (!elementType.equals("int")) {
          throw new IllegalStateException();
        }
      }
    }
    if (returnType == null && assignmentType.isEmpty()) {
      throw new TypeError(mainNode.getLine());
    }
    assert returnType != null;
    if (!returnType.getReturnType().contains(assignmentType)) {
      throw new TypeError(mainNode.getLine());

    }

  } else{
    throw new IllegalStateException("ersxedr");
  }
}
*/

