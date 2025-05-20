package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.ISTORE;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Lexer.Symbols.Literal;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class ControlStructure {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;

  public ControlStructure(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public void checkControlStructure(MethodVisitor mv, MainNode astNode, String functionType,
      SimpleEntry<String, String> stringStringSimpleEntry) throws OperatorError {
    GenericNode<?> genericNodeForIdentifier = null;
    GenericNode<?> genericNodeForTypeSpecifier = null;
    ASTNode statement = null;
    LinkedList<For> forSize = new LinkedList<>();
    for (ASTNode node : astNode.getChildrenList()) {
      if (node.getName().equals("Identifier")) {
        genericNodeForIdentifier = (GenericNode<?>) node;
      }
      if (node.getName().equals("TypeSpecifier")) {
        genericNodeForTypeSpecifier = (GenericNode<?>) node;
      }
      if (node.getName().contains("Expression") || node.getName().contains("BuiltInFunctionCall")) {
        getValue(mv, (MainNode) node, forSize);
      }
      if (node.getName().equals("Statements")) {
        System.out.println();
        statement = node;
      }
    }
    Label loopStart = new Label();
    Label loopEnd = new Label();
    int indexStartFor = allocator.allocate();

    if (genericNodeForTypeSpecifier == null) {
      Table ind = Utils.searchIdentifier(mv, table, genericNodeForIdentifier.getValue(), false,
          false);
      indexStartFor = ind.index;
    }

    if (genericNodeForTypeSpecifier != null) {
      table.getSymbols().put(genericNodeForIdentifier.getValue(),
          new Table(genericNodeForIdentifier.getValue(),
              DescriptorUtils.getTypeFromString(genericNodeForTypeSpecifier.getValue())
                  .getDescriptor(), false, false, false, null, indexStartFor));
    }
    if (forSize.getFirst().type.equals("Literal")) {
      mv.visitLdcInsn(forSize.getFirst().ivalue); // équivalent à: int o = 10;
    } else if (forSize.getFirst().type.equals("Identifier")) {
      //mv.visitVarInsn(Opcodes.ILOAD, forSize.getFirst().index);
      Utils.searchIdentifier(mv, table, forSize.get(0).svalue, true, false);
    }
    mv.visitVarInsn(ISTORE, indexStartFor);

    if (genericNodeForTypeSpecifier != null) {
      mv.visitLocalVariable(genericNodeForIdentifier.getValue(),
          DescriptorUtils.getTypeFromString(genericNodeForTypeSpecifier.getValue()).getDescriptor(),
          null, loopStart, loopEnd, indexStartFor);
    }
    mv.visitLabel(loopStart);
    mv.visitVarInsn(ILOAD, indexStartFor);

    if (forSize.get(1).type.equals("Literal")) {
      //mv.visitIntInsn(ILOAD, forSize.get(1).ivalue);
      Utils.loadLitField(mv,
          new SimpleEntry<>("", new Literal().typeOfLiteral(String.valueOf(forSize.get(1).ivalue))),
          String.valueOf(forSize.get(1).ivalue));
    } else if (forSize.get(1).type.equals("Identifier")) {
      //mv.visitVarInsn(ILOAD, forSize.get(1).index);
      try {
        Utils.searchIdentifier(mv, table, forSize.get(1).svalue, true, false);
      } catch (Exception e) {
        Utils.execute("load", mv, new SimpleEntry<>("", forSize.get(1).descriptor),
            forSize.get(1).index);
      }
    }
    mv.visitJumpInsn(IF_ICMPGT, loopEnd);

    // for body
    FunctionBody functionBody = new FunctionBody(allocator, loopStart, loopEnd, start, end);
    functionBody.checkFunctionBody(mv, statement, null, functionType, table,
        stringStringSimpleEntry);
//
    if (forSize.get(2).type.equals("Literal")) {
      mv.visitIincInsn(indexStartFor, forSize.get(2).ivalue);
    } else if (forSize.get(2).type.equals("Identifier")) {
      mv.visitVarInsn(ILOAD, indexStartFor);
      mv.visitVarInsn(ILOAD, forSize.get(2).index);
      mv.visitInsn(IADD);
      mv.visitVarInsn(ISTORE, indexStartFor);
    }
    mv.visitJumpInsn(GOTO, loopStart);
    mv.visitLabel(loopEnd);

  }

  private void getValue(MethodVisitor mv, MainNode node, LinkedList<For> forSize)
      throws OperatorError {
    if (node.getName().equals("BuiltInFunctionCall")) {
      BuiltInFunctionCall builtInFunctionCall = new BuiltInFunctionCall(table, "", allocator, start,
          end);
      Table table1 = builtInFunctionCall.CheckBuiltInFunctionCall(mv, node, false);
      forSize.add(
          new For("Identifier", table1.name, -1, table1.index, table1.isField, table1.descriptor));

      return;
    }
    if (node.getName().equals("BinaryExpression")) {
      ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
          allocator, start, end);
      Table table1 = expressionCodeGenerator.checkExpression(node, mv, false);
      forSize.add(
          new For("Identifier", table1.name, -1, table1.index, table1.isField, table1.descriptor));

      return;
    }
    for (ASTNode astNode : node.getChildrenList()) {
      if (astNode.getName().equals("Literal")) {
        GenericNode<?> value = (GenericNode<?>) astNode;
        forSize.add(new For(value.getName(), null, Integer.parseInt(value.getValue())));
      } else if (astNode.getName().equals("Identifier")) {
        GenericNode<?> value = (GenericNode<?>) astNode;
        Table table1 = Utils.searchIdentifier(mv, table, value.getValue(), false, false);
        forSize.add(new For(value.getName(), value.getValue(), -1, table1.index, table1.isField,
            table1.descriptor));
      }
    }
  }

  public class For {

    private String type;
    private String svalue;
    private int ivalue;
    private int index;
    private boolean isField;
    private String descriptor;

    public For(String type, String svalue, int ivalue) {
      this.type = type;
      this.svalue = svalue;
      this.ivalue = ivalue;
    }

    public For(String type, String svalue, int ivalue, int index, boolean isField) {
      this.type = type;
      this.svalue = svalue;
      this.ivalue = ivalue;
      this.index = index;
      this.isField = isField;
    }

    public For(String type, String svalue, int ivalue, int index, boolean isField,
        String descriptor) {
      this.type = type;
      this.svalue = svalue;
      this.ivalue = ivalue;
      this.index = index;
      this.isField = isField;
      this.descriptor = descriptor;
    }

    public String getDescriptor() {
      return descriptor;
    }

    public boolean isField() {
      return isField;
    }

    public String getType() {
      return type;
    }

    public String getSvalue() {
      return svalue;
    }

    public int getIvalue() {
      return ivalue;
    }

    public int getIndex() {
      return index;
    }
  }
}
