package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class IfControlStructure {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;

  public IfControlStructure(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public void checkControlStructure(MethodVisitor mv, MainNode astNode, String functionType,
      SimpleEntry<String, String> stringStringSimpleEntry) throws OperatorError {
    LinkedList<ASTNode> statement = new LinkedList<>();
    ASTNode expression = null;
    for (ASTNode node : astNode.getChildrenList()) {
      if (node.getName().contains("Expression")) {
        expression = node;
      }
      if (node.getName().equals("Statements")) {
        statement.add(node);
      }
    }

    Label elseLabel = new Label();
    Label endIfLabel = new Label();

    ExpressionCodeGenerator exprGen = new ExpressionCodeGenerator(table, allocator, start, end);
    Table condResult = exprGen.checkExpression(expression, mv, false);
    if (condResult.index != -1) {
      Utils.execute("load", mv, new SimpleEntry<>("", condResult.descriptor), condResult.index);
    }
    /*if (condResult.descriptor.equals("Z")) {
      mv.visitVarInsn(ILOAD, condResult.index);
    } else if (condResult.descriptor.equals("I")) {
      mv.visitVarInsn(ILOAD, condResult.index);
    } else if (condResult.descriptor.equals("F")) {
      mv.visitVarInsn(FLOAD, condResult.index);
    } else {
      mv.visitVarInsn(ALOAD, condResult.index);
    }*/
    mv.visitJumpInsn(IFEQ, elseLabel);    // si run == 0 (false), aller dans le else

// Bloc IF: value = value - 1
    FunctionBody functionBodyIf = new FunctionBody(allocator, start, end);
    functionBodyIf.checkFunctionBody(mv, statement.getFirst(), null, functionType, table,
        stringStringSimpleEntry);
    mv.visitJumpInsn(GOTO, endIfLabel);   // saut pour éviter le else
    if (statement.size() == 2) {
// Bloc ELSE
      mv.visitLabel(elseLabel);
      FunctionBody functionBodyElse = new FunctionBody(allocator, start, end);
      functionBodyElse.checkFunctionBody(mv, statement.getLast(), null, functionType, table,
          stringStringSimpleEntry);
    }
// Fin du if-else
    mv.visitLabel(endIfLabel);

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
