package compiler.Semantic.Utils;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.RecordError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Lexer.Symbols.Record;
import compiler.Lexer.Symbols.TypeSpecifier;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Constant.CheckConstantType;
import compiler.Semantic.Types.Statement.CheckBlock;
import compiler.Semantic.Types.Type;
import java.util.LinkedList;

public class CheckTypes {

  private String typeName;
  private LinkedList<ASTNode> children;

  public CheckTypes(String typeName, LinkedList<ASTNode> children) {
    this.typeName = typeName;
    this.children = children;
  }

  public void checkType(Type table)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
/*
    HashMap<String, String> table = new HashMap<>();
*/

    switch (typeName) {
      case "Constant", "Declaration" -> {
        CheckConstantType checkConstantType = new CheckConstantType();
        //checkConstantType.checkConstant(children, table);
      }
      case "Record" -> {
        GenericNode<?> record = ((GenericNode<?>) children.pop());
        if (table.getCurrent().containsKey(record.getValue())) {
          throw new GenericError("Deja declarer " + record.getValue());
        }
        String recordValue = record.getValue();
        MainNode recordFields = (MainNode) children.getLast();
        LinkedList<Type> recordFieldsTypes = new LinkedList<>();
        new Utils().getSymbols(recordFields, recordFieldsTypes, table);
        TypeSpecifier typeSpecifier = new TypeSpecifier();
        Record record1 = new Record();
        for (Type type : recordFieldsTypes) {
          if (!TypeSpecifier.TYPE_SPECIFIERS.contains(type.getReturnType())
              && !typeSpecifier.isArrayTypeSpecifier(type.getReturnType())) {
            if (!typeSpecifier.isArrayRecord(type.getReturnType()) && !record1.matches(
                type.getReturnType())) {
              throw new RecordError("Record error ");
            }
            String returnType = type.getReturnType();
            if (type.getReturnType().contains("[]")) {
              returnType = returnType.substring(0, returnType.length() - 2);
            }
            if (!table.getCurrent().containsKey(returnType) || returnType
                .equals(record.getValue())) {
              throw new RecordError("Record error ");
            }
          }
        }
        /*table.getCurrent().put(recordValue,
            new Type("Record", null, recordValue, recordFieldsTypes, table.getCurrent()));*/
      }
      case "Function" -> {
        CheckBlock checkBlock = new CheckBlock();
        checkBlock.checkBlock(table, children);
      }
      default -> {
        throw new IllegalStateException("");
      }
    }
  }

}
