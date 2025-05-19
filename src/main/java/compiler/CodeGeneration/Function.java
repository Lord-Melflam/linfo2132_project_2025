package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class Function {

  public static Table table;
  LocalIndexAllocator allocator;

  public Function(Table table, LocalIndexAllocator allocator) {
    Function.table = table;
    this.allocator = allocator;
  }

  public void CheckFunction(ClassWriter cw, MainNode child) throws OperatorError {
    MethodVisitor methodVisitor;
    String functionName = "";
    String functionType = "";
    MainNode functionBlock = null;
    ArrayList<String> functionArgs = new ArrayList<>();
    ArrayList<String> functionArgsName = new ArrayList<>();
    Label start = new Label();
    Label end = new Label();

    for (ASTNode astNode : child.getChildrenList()) {
      if (astNode.getName().equals("Identifier")) {
        functionName = ((GenericNode<?>) astNode).getValue();
      }
      if (astNode.getName().equals("TypeSpecifier")) {
        functionType = ((GenericNode<?>) astNode).getValue();
      }
      if (astNode.getName().equals("FunctionBlock")) {
        functionBlock = (MainNode) astNode;
      }
      if (astNode.getName().equals("Parameters")) {
        getParams((MainNode) astNode, functionArgs, functionArgsName);
      }
    }
    /*if (functionType.equals("int")) {
      functionType = "float";
    }*/

    if (functionName.equals("main")) {
      functionArgs = new ArrayList<>(List.of("java.lang.String[]"));
      functionArgsName = new ArrayList<>(List.of("args"));

    }
    String descriptor = DescriptorUtils.getMethodDescriptor(functionType, functionArgs);

    table.getSymbols().put(functionName,
        new Table(functionName, DescriptorUtils.getTypeFromString(functionType).getDescriptor(),
            false, false, true, descriptor, -1));
    Table functionTable = new Table(table, table.getClassName());
    functionTable.getSymbols().put(functionName,
        new Table(functionName, DescriptorUtils.getTypeFromString(functionType).getDescriptor(),
            false, false, true, descriptor, -1));
    int access = ACC_PUBLIC | ACC_STATIC;
    if (!functionName.equals("main")) {
      access = ACC_PUBLIC;
    }

    methodVisitor = cw.visitMethod(access, functionName, descriptor, null, null);

    methodVisitor.visitCode();
    methodVisitor.visitLabel(start);

    allocator = new LocalIndexAllocator();
    if (functionName.equals("main")) {
      allocator = new LocalIndexAllocator(functionArgs.size());
      methodVisitor.visitTypeInsn(NEW, table.getClassName());
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitMethodInsn(INVOKESPECIAL, table.getClassName(), "<init>", "()V", false);
      methodVisitor.visitVarInsn(ASTORE, 1);
    }
    for (int i = 0; i < functionArgs.size(); i++) {
      String des = DescriptorUtils.getTypeFromString(functionArgs.get(i)).getDescriptor();
      int allocatedIndex = allocator.allocate();
      methodVisitor.visitLocalVariable(functionArgsName.get(i), des, null, start, end,
          allocatedIndex);
      Table table1 = new Table(functionArgsName.get(i), des, false, false, false, null,
          allocatedIndex);
      table1.setType(functionArgs.get(i));
      functionTable.getSymbols().put(functionArgsName.get(i), table1);
    }

    FunctionBody functionBody = new FunctionBody(allocator, start, end);
    assert functionBlock != null;
    if (!functionBlock.getChildrenList().isEmpty()) {
      functionBody.checkFunctionBody(methodVisitor, functionBlock.getChildrenList().getLast(),
          functionArgsName, functionType, functionTable,
          new AbstractMap.SimpleEntry<String, String>(functionName, functionType));
    }
    if (functionName.equals("main") || functionType.equals("void")) {
      methodVisitor.visitInsn(RETURN);
    }
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitLabel(end);
    methodVisitor.visitEnd();

  }

  private void getParams(MainNode astNode, ArrayList<String> functionArgs,
      ArrayList<String> functionArgsName) {
    for (ASTNode node : astNode.getChildrenList()) {
      MainNode mainNode = (MainNode) node;
      String type = ((GenericNode<?>) mainNode.getChildrenList().getLast()).getValue();
      functionArgsName.add(((GenericNode<?>) mainNode.getChildrenList().getFirst()).getValue());
      functionArgs.add(type);
    }
  }
  /*
   */

}
