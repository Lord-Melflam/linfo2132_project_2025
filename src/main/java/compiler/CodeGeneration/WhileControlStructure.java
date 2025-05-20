package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.ILOAD;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class WhileControlStructure {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;

  public WhileControlStructure(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public void checkControlStructure(MethodVisitor mv, MainNode astNode, String functionType,
      SimpleEntry<String, String> stringStringSimpleEntry) throws OperatorError {
    ASTNode statement = null;
    ASTNode expression = null;
    for (ASTNode node : astNode.getChildrenList()) {
      if (node.getName().contains("Expression")) {
        expression = node;
      }
      if (node.getName().equals("Statements")) {
        statement = node;
      }
    }

    Label loopStart = new Label();
    Label loopEnd = new Label();

    mv.visitLabel(loopStart);

    ExpressionCodeGenerator exprGen = new ExpressionCodeGenerator(table, allocator, start, end);
    Table condResult = exprGen.checkExpression(expression, mv, false);
    if (condResult.descriptor.equals("Z")) {
      mv.visitVarInsn(ILOAD, condResult.index);
    } else if (condResult.descriptor.equals("I")) {
      mv.visitVarInsn(ILOAD, condResult.index);
    } else if (condResult.descriptor.equals("F")) {
      mv.visitVarInsn(FLOAD, condResult.index);
    } else {
      mv.visitVarInsn(ALOAD, condResult.index);
    }
    mv.visitJumpInsn(IFEQ, loopEnd);

    FunctionBody functionBody = new FunctionBody(allocator, loopStart, loopEnd, start, end);
    functionBody.checkFunctionBody(mv, statement, null, functionType, table,
        stringStringSimpleEntry);

    mv.visitJumpInsn(GOTO, loopStart);
    mv.visitLabel(loopEnd);
  }

  private void getValue(MainNode node, LinkedList<Integer> forSize) {
    for (ASTNode astNode : node.getChildrenList()) {
      if (astNode.getName().equals("Literal")) {
        GenericNode<?> value = (GenericNode<?>) astNode;
        forSize.add(Integer.parseInt(value.getValue()));
      }
    }
  }

}
