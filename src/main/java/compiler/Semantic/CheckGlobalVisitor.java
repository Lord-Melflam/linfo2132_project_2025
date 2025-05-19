package compiler.Semantic;

import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.RecordError;
import compiler.Lexer.Symbols.Keyword;
import compiler.Lexer.Symbols.Literal;
import compiler.Lexer.Symbols.Record;
import compiler.Lexer.Symbols.TypeSpecifier;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;
import compiler.Semantic.Types.Constant.CheckConstantType;
import compiler.Semantic.Types.Function.CheckBuiltInFunction;
import compiler.Semantic.Types.Statement.CheckBlock;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.LinkedList;
import java.util.List;

public class CheckGlobalVisitor implements ASTVisitor<String> {

  private final Type symbolTable;

  public CheckGlobalVisitor(Type symbolTable) {
    this.symbolTable = symbolTable;
  }


  @Override
  public void visit(MainNode constantNode) {
  }

  @Override
  public String visitMainNode(MainNode node) throws OperatorError {
    LinkedList<ASTNode> children = node.getChildrenList();
    String name = node.getName();

    if (children == null || children.isEmpty()) {
      return "Unknown";
    }
    if (children.size() == 1) {
      return children.getFirst().accept(this);
    }
    switch (name) {
      case "Constant", "Declaration", "Initialisation" -> {
        CheckConstantType checkConstantType = new CheckConstantType();
        checkConstantType.check(node.getChildrenList(), symbolTable, name);
      }
      case "Record" -> {
        GenericNode<?> record = ((GenericNode<?>) children.pop());
        if (TypeSpecifier.TYPE_SPECIFIERS.contains(record.getValue()) || Keyword.KEYWORDS.contains(
            record.getValue())) {
          throw new RecordError("Record error ");
        }
        if (symbolTable.getCurrent().containsKey(record.getValue())) {
          throw new RecordError("Record error ");
        }
        String recordValue = record.getValue();
        MainNode recordFields = (MainNode) children.getLast();
        LinkedList<Type> recordFieldsTypes = new LinkedList<>();
        new Utils().getSymbols(recordFields, recordFieldsTypes, symbolTable);
        TypeSpecifier typeSpecifier = new TypeSpecifier();
        Record record1 = new Record();
        for (Type type : recordFieldsTypes) {
          if (!TypeSpecifier.TYPE_SPECIFIERS.contains(type.getReturnType())
              && !typeSpecifier.isArrayTypeSpecifierBis(type.getReturnType())) {
            if (!typeSpecifier.isArrayRecord(type.getReturnType()) && !record1.matches(
                type.getReturnType())) {
              throw new RecordError("Record error " + recordFields.getLine());
            }
            String returnType = type.getReturnType();
            if (type.getReturnType().contains("[]")) {
              returnType = returnType.substring(0, returnType.length() - 2);
            }
            if (!symbolTable.getCurrent().containsKey(returnType) || returnType.equals(
                record.getValue())) {
              throw new RecordError("Record error " + recordFields.getLine());

            }
          }
          symbolTable.addRecordAtt(type.getName(), record.getValue());
        }
        symbolTable.getCurrent().put(recordValue,
            new Type("Record", recordValue, null, recordValue, recordFieldsTypes,
                symbolTable.getCurrent()));

      }

      case "Function" -> {
        CheckBlock checkBlock = new CheckBlock();
        checkBlock.checkBlock(symbolTable, children);
      }

      case "Statements" -> {
        MainNode mainNode = (MainNode) node.getChildrenList().getFirst();
        if (mainNode.getName().equals("BuiltInFunctionCall")) {
          CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();
          checkBuiltInFunction.checkWriteBuiltIn(mainNode.getChildrenList(), symbolTable);
        }
/*
BuiltInFunction,FREE
*/
      }
      case "Deallocation" -> {
        for (ASTNode ast : children) {
          GenericNode<?> genericNode = ast.accept(new ExtractGenericVisitor());
          if (genericNode != null) {
            if (genericNode.getName().equals("Identifier")) {
              Type type = new Utils().lookInSymbolsTable(symbolTable, genericNode.getValue(),
                  genericNode.getLine());
              if (!type.getSymbolTypeName().equals("Record") && type.getSymbolTypeName()
                  .equals("Array")) {
                throw new IllegalStateException();
              }
            }
          }
        }
      }
      default -> {

      }
    }
    return "Unknown";
  }

  @Override
  public String visitGenericNode(GenericNode<?> node) throws OperatorError {
    String name = node.getName();
    String value = node.getValue();

    return switch (name) {
      case "Identifier" ->
          new Utils().lookInSymbolsTable(symbolTable, value, node.getLine()).getReturnType();
      case "Literal" -> {
        Literal literal = new Literal();
        if (literal.isFloat(value)) {
          yield "float";
        }
        if (literal.isInt(value)) {
          yield "int";
        }
        if (literal.isString(value)) {
          yield "string";
        }
        if (literal.isArray(value)) {
          yield "array";
        }
        yield "bool";
      }
      case "BuiltInFunction" -> {
        CheckBuiltInFunction check = new CheckBuiltInFunction();
        yield check.checkBuiltInFunction(new LinkedList<>(List.of(node)), symbolTable);
      }
      default -> "Unknown";
    };
  }
}

