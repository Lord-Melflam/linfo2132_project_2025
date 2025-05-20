package compiler.Semantic.Types.Constant;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.DuplicateDeclarationError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractGenericVisitor;
import compiler.Semantic.Types.Expression.CheckExpressionType;
import compiler.Semantic.Types.Function.CheckBuiltInFunction;
import compiler.Semantic.Types.Function.Function;
import compiler.Semantic.Types.Record.CheckRecordConstructor;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.LinkedList;

public class CheckConstantType {

  private String constantTypeSpecifier;
  private String constantIdentifier;

  public CheckConstantType() {
    constantTypeSpecifier = "";
    constantIdentifier = "";
  }

  public String getConstantTypeSpecifier() {
    return constantTypeSpecifier;
  }

  public void setConstantTypeSpecifier(String constantTypeSpecifier) {
    this.constantTypeSpecifier = constantTypeSpecifier;
  }

  public String getConstantIdentifier() {
    return constantIdentifier;
  }

  public void setConstantIdentifier(String constantIdentifier) {
    this.constantIdentifier = constantIdentifier;
  }

  public void getIdentifier(GenericNode<?> child) {
    if (child.getName().equals("Identifier")) {
      String identifier = child.getValue();
      setConstantIdentifier(identifier);
    }
  }

  public void getTypeSpecifier(GenericNode<?> child) {
    if (child.getName().equals("TypeSpecifier") || child.getName().equals("Record")) {
      String typeSpecifier = child.getValue();
      setConstantTypeSpecifier(typeSpecifier);
    }
  }


  public void checkConstant(LinkedList<ASTNode> children, Type table,
      ExtractGenericVisitor visitor) throws OperatorError {
    String identifier = "";
    String typeSpecifier = null;
    while (!children.isEmpty() && !children.peek().getName().equals("Assignment")) {
      ASTNode node = children.pop();
      GenericNode<?> child = node.accept(visitor);
      if (child == null) {
        continue;
      }
      getIdentifier(child);
      identifier = getConstantIdentifier();
      if (!identifier.isEmpty() && table.getCurrent().containsKey(identifier)) {
        throw new DuplicateDeclarationError(identifier, child.getLine());

      }
      getTypeSpecifier(child);
      typeSpecifier = getConstantTypeSpecifier();

    }
    table.getCurrent().put(identifier,
        new Type("Constant", identifier, null, typeSpecifier, null, table.getCurrent()));

  }

  public void checkDeclaration(LinkedList<ASTNode> children, Type table,
      ExtractGenericVisitor visitor) throws OperatorError {

    String identifier = "";
    String typeSpecifier = null;
    while (!children.isEmpty() && !children.peek().getName()
        .equals(TokenType.SEMICOLON.getValue())) {
      ASTNode node = children.pop();
      GenericNode<?> child = node.accept(visitor);
      if (child == null) {
        continue;
      }
      getIdentifier(child);
      identifier = getConstantIdentifier();
      if (table.getCurrent().containsKey(identifier)) {
        Type type = table.getCurrent().get(identifier);
        if (type.getSymbolTypeName().equals("Constant")) {
          throw new DuplicateDeclarationError(identifier, child.getLine());
        }
      }
      getTypeSpecifier(child);
      typeSpecifier = getConstantTypeSpecifier();
    }
    table.getCurrent().put(identifier,
        new Type("Declaration", identifier, null, typeSpecifier, null, table.getCurrent()));
  }

  public void checkInitialisation(LinkedList<ASTNode> children, Type table,
      ExtractGenericVisitor visitor) throws OperatorError {

    String identifier = "";
    String typeSpecifier = null;
    while (!children.isEmpty() && !children.peek().getName().equals("Assignment")) {
      ASTNode node = children.pop();
      GenericNode<?> child = node.accept(visitor);
      if (child == null) {
        continue;
      }

      getIdentifier(child);
      identifier = getConstantIdentifier();
      if (table.getCurrent().containsKey(identifier)) {
        Type type = table.getCurrent().get(identifier);
        typeSpecifier = type.getReturnType();
        setConstantTypeSpecifier(typeSpecifier);
        if (type.getSymbolTypeName().equals("Constant")) {
          throw new DuplicateDeclarationError(identifier, child.getLine());
        }
      }
      getTypeSpecifier(child);
      typeSpecifier = getConstantTypeSpecifier();
    }
    if (getConstantTypeSpecifier().isEmpty()) {
      children.pop();
      MainNode node = ((MainNode) children.peek());
      Type type = new Utils().lookInSymbolsTable(table, identifier, node.getLine());
      if (type.getSymbolTypeName().equals("Constant")) {
        /*todo*/
        System.out.println("cannot change constant value ????");
        throw new IllegalStateException();
      }
      setConstantTypeSpecifier(type.getReturnType());

    } else {
      ASTNode potentialArray = children.get(1);
      if (potentialArray.getName().equals("Keyword")) {
        if (((GenericNode<?>) potentialArray).getValue().equals("array")) {
          checkArray(children, table, identifier, typeSpecifier);
          return;
        }
      }
      table.getCurrent().put(identifier,
          new Type("Initialisation", identifier, null, typeSpecifier, null, table.getCurrent()));
    }
  }

  private void checkArray(LinkedList<ASTNode> children, Type table, String identifier,
      String typeSpecifier) throws OperatorError {
    while (!children.isEmpty() && !children.peek().getName().equals("Punctuation")) {
      ASTNode node = children.pop();
      if (node.getName().equals("Expression")) {
        MainNode mainNode = (MainNode) node;
        String v = new CheckExpressionType().checkExpressionType(node, table);
        if (!v.equals("int") && !v.equals("float")) {
          throw new GenericError(
              "Semantic Exception: Array error: the size provided is not an int type on line "
                  + mainNode.getLine());
        }
      }
      if (node.getName().equals("TypeSpecifier") || node.getName().equals("Record")) {
        GenericNode<?> genericNode = (GenericNode<?>) node;
        if (!typeSpecifier.contains(genericNode.getValue())) {
          throw new GenericError(
              "Semantic Exception: Array error: array does not contain the same type elements "
                  + genericNode.getLine());
        }
      }
    }
    table.getCurrent().put(identifier,
        new Type("Array", identifier, null, typeSpecifier, null, table.getCurrent()));
    children.pop();
  }


  public void check(LinkedList<ASTNode> children, Type table, String name)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    assert children.peek() != null;
    int line = children.peek().getLine();
    ExtractGenericVisitor visitor = new ExtractGenericVisitor();
    if (name.equals("Constant")) {
      checkConstant(children, table, visitor);
      children.pop();
    } else if (name.equals("Declaration")) {
      checkDeclaration(children, table, visitor);
      return;
    } else if (name.equals("Initialisation") || name.equals("Assignment")) {
      checkInitialisation(children, table, visitor);
      if (children.isEmpty()) {
        return;
      }
      if (children.getFirst().getName().equals("Assignment")) {
        children.pop();
      }
    }
    checkAssignment(children, table, line);

  }

  public void checkAssignment(LinkedList<ASTNode> children, Type table, int line)
      throws OperatorError {
    String v = "";
    assert children.peek() != null;
    if (children.peek().getName().contains("Expression")) {
      CheckExpressionType expressionType = new CheckExpressionType();
      MainNode mainNode = (MainNode) children.peek();
      assert mainNode != null;
      v = expressionType.checkExpressionType(((MainNode) mainNode).getChildren(), table);
    } else {
      assert children.peek() != null;
      if (children.peek().getName().contains("BuiltInFunction")) {
        CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();

        LinkedList<ASTNode> list = children;
        assert children.peek() != null;
        if (children.peek().getName().contains("BuiltInFunctionCall")) {
          MainNode node = (MainNode) children.peek();
          assert node != null;
          list = (LinkedList<ASTNode>) node.getChildren();
        }
        v = checkBuiltInFunction.checkBuiltInFunction(list, table);

      } else {
        assert children.peek() != null;
        if (children.peek().getName().equals("FunctionCall")) {
          Function function = new Function();
          MainNode mainNode = (MainNode) children.peek();
          assert mainNode != null;
          v = function.checkFunction(mainNode, table);
        }
        assert children.peek() != null;
        if (children.peek().getName().equals("RecordConstructorCall")) {
          CheckRecordConstructor checkRecordConstructor = new CheckRecordConstructor();
          MainNode mainNode = (MainNode) children.peek();
          assert mainNode != null;
          v = checkRecordConstructor.checkRecord(mainNode, table);
        }
        assert children.peek() != null;
        if (children.peek().getName().equals("ArrayAccess")) {
          MainNode mainNode = (MainNode) children.pop();
          CheckExpressionType checkExpressionType = new CheckExpressionType();
          LinkedList<String> stringLinkedList = new LinkedList<>();
          for (ASTNode astNode : mainNode.getChildrenList()) {
            String type = checkExpressionType.checkExpressionType(astNode, table);
            stringLinkedList.add(type);
          }
          if (stringLinkedList.size() < 2) {
            throw new TypeError(mainNode.getLine());
          }
          if (!stringLinkedList.get(1).equals("int") && !stringLinkedList.get(1)
              .contains("float")) {
            throw new TypeError(mainNode.getLine());
          }
          /*if (!constantTypeSpecifier.equals(stringLinkedList.getFirst())) {
            throw new TypeError(mainNode.getLine());
          }*/
          if (stringLinkedList.getFirst().contains("[]")) {
            v = stringLinkedList.getFirst().substring(0, stringLinkedList.getFirst().length() - 2);
          } else {
            v = stringLinkedList.getFirst();
          }
          System.out.println();
        }
        /*assert children.peek() != null;
        if (children.peek().getName().equals("RecordConstructorCall")) {
          CheckRecordConstructor checkRecordConstructor = new CheckRecordConstructor();
          MainNode mainNode = (MainNode) children.peek();
          assert mainNode != null;
          v = checkRecordConstructor.checkRecord(mainNode, table);
        }*/
      }
    }
    if (!v.equals(getConstantTypeSpecifier())) {
      throw new TypeError(line, getConstantTypeSpecifier(), v);
    }
  }

  public String checkAssignment(MainNode children, Type table)
      throws OperatorError {
    String v = "";
    if (children.getName().contains("Expression")) {
      CheckExpressionType expressionType = new CheckExpressionType();
      v = expressionType.checkExpressionType(((MainNode) children).getChildren(), table);
    } else {
      if (children.getName().contains("BuiltInFunction")) {
        CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();

        LinkedList<ASTNode> list = children.getChildrenList();
        if (list.peek().getName().contains("BuiltInFunctionCall")) {
          MainNode node = (MainNode) list.peek();
          assert node != null;
          list = (LinkedList<ASTNode>) node.getChildren();
        }
        v = checkBuiltInFunction.checkBuiltInFunction(list, table);
      } else {
        if (children.getName().equals("FunctionCall")) {
          Function function = new Function();
          v = function.checkFunction(children, table);
        }
        if (children.getName().equals("RecordConstructorCall")) {
          CheckRecordConstructor checkRecordConstructor = new CheckRecordConstructor();
          v = checkRecordConstructor.checkRecord(children, table);
        }

        /*assert children.peek() != null;
        if (children.peek().getName().equals("RecordConstructorCall")) {
          CheckRecordConstructor checkRecordConstructor = new CheckRecordConstructor();
          MainNode mainNode = (MainNode) children.peek();
          assert mainNode != null;
          v = checkRecordConstructor.checkRecord(mainNode, table);
        }*/
      }
    }
    return v;
  }
}
