package compiler.CodeGeneration;

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

public class Assignment {

  private final Table functionTable;
  private final LocalIndexAllocator allocator;
  private final Label start;
  private final Label end;

  public Assignment(Table functionTable, LocalIndexAllocator allocator, Label start, Label end) {

    this.functionTable = functionTable;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public void checkAssignment(MethodVisitor cw, MainNode astNode) throws OperatorError {
    GenericNode<?> tableName = null;
    ASTNode expression = null;
    ASTNode assign = null;

    for (ASTNode node : astNode.getChildrenList()) {
      if (node.getName().equals("RecordName")) {
        tableName = (GenericNode<?>) ((MainNode) node).getChildrenList().getFirst();
      }
      if (node.getName().equals("RecordArrayAttribute")) {
        expression = ((MainNode) node).getChildrenList().get(1);
      }
      if (node.getName().equals("RecordAssignment")) {
        assign = ((MainNode) node).getChildrenList().get(1);
      }
    }

    VariableRightPart rightPart = new VariableRightPart(functionTable, null, allocator, start, end);
    LinkedList<String> arrayIndex = rightPart.checkVariableRightPart(cw, expression, false);
    System.out.println();
    LinkedList<String> arrayAssignment = rightPart.checkVariableRightPart(cw, assign, false);
    System.out.println();
    Table array = Utils.searchIdentifier(cw, functionTable, tableName.getValue(), true, false);
    Utils.execute("load", cw, new SimpleEntry<>("", "int"), Integer.parseInt(arrayIndex.get(1)));
    Utils.execute("load", cw, new SimpleEntry<>("", array.descriptor.substring(1)),
        Integer.parseInt(arrayAssignment.get(1)));

  }
}
