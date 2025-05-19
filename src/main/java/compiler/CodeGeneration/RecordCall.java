package compiler.CodeGeneration;

import static compiler.CodeGeneration.CodeGeneration.symbolTable;
import static compiler.CodeGeneration.Utils.Utils.constructor;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.I2F;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Type;
import compiler.Semantic.Utils.Utils;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RecordCall {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;

  public RecordCall(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public int checkConstructor(MethodVisitor cw, MainNode expression, String variableName)
      throws OperatorError {
    GenericNode<?> recordType = (GenericNode<?>) expression.getChildrenList().getFirst();
    MainNode recordParameters = (MainNode) expression.getChildrenList().get(1);
    ArrayList<Table> indexes = new ArrayList<>();
    GenericNode<?> genericNode = null;
    String typeVariable = "";
    for (ASTNode astNode : recordParameters.getChildrenList()) {
      if (astNode.getName().contains("Expression")) {
        ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
            allocator, start, end);
        Table index = expressionCodeGenerator.checkExpression(astNode, cw, false);
        if (index == null) {
          MainNode mainNode = (MainNode) astNode;
          genericNode = (GenericNode<?>) mainNode.getChildrenList().getFirst();
/*
          Table table1 = table.resolve(genericNode.getValue());
*/
          Table table1 = compiler.CodeGeneration.Utils.Utils.searchIdentifier(cw, table,
              genericNode.getValue(), true, false);
          indexes.add(table1);
        }
        indexes.add(index);
      } else if (astNode.getName().equals("ArrayInitialization")) {
        checkArray(indexes, cw, (MainNode) astNode, null);
      }
    }
    assert genericNode != null;

    Type type = new Utils().lookInSymbolsTable(symbolTable, recordType.getValue(), -1);
    String cons = constructor(type.getParameters());
    cw.visitTypeInsn(NEW, recordType.getValue());
    cw.visitInsn(DUP);
    if (indexes.size() == type.getParameters().size()) {
      for (int i = 0; i < indexes.size(); i++) {
        Table index = indexes.get(i);
        if (index.isField) {
          cw.visitVarInsn(ALOAD, 1);
          cw.visitFieldInsn(GETFIELD, table.getClassName(), index.name,
              DescriptorUtils.getTypeFromString(type.getParameters().get(i).getReturnType())
                  .getDescriptor());
        } else {
          compiler.CodeGeneration.Utils.Utils.execute("load", cw,
              new SimpleEntry<String, String>("", index.descriptor), index.index);
          if (!DescriptorUtils.getTypeFromString(type.getParameters().get(i).getReturnType())
              .getDescriptor().equals(index.descriptor)) {
            switch (DescriptorUtils.getTypeFromString(type.getParameters().get(i).getReturnType())
                .getDescriptor()) {
              case "I" -> cw.visitInsn(Opcodes.F2I);
              case "F" -> cw.visitInsn(I2F);

            }
          }
          //cw.visitVarInsn(ILOAD, index.index);
        }
      }
    }
    /*todo*/
    cw.visitMethodInsn(INVOKESPECIAL, recordType.getValue(), "<init>", cons, false);
    cw.visitVarInsn(ASTORE, allocator.allocate());
    table.getSymbols().put(variableName,
        new Table(variableName, "L" + recordType.getValue() + ";", false, false, false, null,
            allocator.previous()));
    cw.visitVarInsn(ALOAD, allocator.previous());
    cw.visitLocalVariable(variableName, "L" + recordType.getValue() + ";", null, start, end,
        allocator.previous());
    return allocator.previous();
  }

  public void checkArray(ArrayList<Table> indexes, MethodVisitor cw, MainNode child,
      MethodVisitor constructor)
      throws OperatorError {
    String variableIdentifier = "";
    String variableType = "";
    ASTNode expression = null;
    boolean isFinal = false;
    for (ASTNode astNode : child.getChildrenList()) {
      if (astNode.getName().equals("Keyword")) {
        GenericNode<?> keyword = ((GenericNode<?>) astNode);
        isFinal = keyword.getValue().equals("final");
      }
      if (astNode.getName().equals("Identifier")) {
        GenericNode<?> variableIdentifierNode = ((GenericNode<?>) astNode);
        variableIdentifier = variableIdentifierNode.getValue();
      }
      if (astNode.getName().equals("TypeSpecifier")) {
        GenericNode<?> variableTypeNode = ((GenericNode<?>) astNode);
        if (variableType.isEmpty()) {
          variableType = variableTypeNode.getValue();
        }
       /* if (variableType.contains("[]")) {
          checkArray(cw, child);
          break;
        }*/
      }
      if (astNode.getName().equals("Expression") || astNode.getName()
          .equals("BuiltInFunctionCall")) {
        expression = (MainNode) astNode;
      }
    }
    String descriptor = DescriptorUtils.getTypeFromString(variableType).getDescriptor();
    if (expression != null) {
      LinkedList<String> rightPart = new VariableRightPart(table, variableIdentifier, allocator,
          start, end).checkVariableRightPart(cw, expression, true);
      if (rightPart != null && !rightPart.isEmpty()) {
        if (rightPart.getFirst().equals("Identifier")) {
          compiler.CodeGeneration.Utils.Utils.execute("load", cw, new SimpleEntry<>("", "int"),
              Integer.parseInt(rightPart.getLast()));// this
          String elementType = variableType;
          if (elementType.contains("[]")) {
            elementType = variableType.substring(0, variableType.length() - 2);// 9}
          }
          compiler.CodeGeneration.Utils.Utils.execute("array", cw,
              new SimpleEntry<>("", elementType), -1);
          compiler.CodeGeneration.Utils.Utils.execute("store", cw,
              new SimpleEntry<>("", elementType + "[]"),
              allocator.allocate());
          indexes.add(new Table("array",
              DescriptorUtils.getTypeFromString(elementType + "[]").getDescriptor(), false, false,
              false, null, allocator.previous()));
        }
      }
    }
  }
}
