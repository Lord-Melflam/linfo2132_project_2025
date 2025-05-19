package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Type;
import java.io.FileOutputStream;
import java.io.IOException;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class CodeGeneration {

  private String outputName;
  private ClassWriter cw;
  private MainNode parser;
  private MainNode astRoot;
  private Table table;
  public static Type symbolTable;
  LocalIndexAllocator allocator = new LocalIndexAllocator();

  public CodeGeneration(String outputName, MainNode parser, Type symbols)
      throws IOException, OperatorError {
    this.outputName = outputName;
    symbolTable = symbols;
    this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    String[] temp = outputName.split("/");
    String className = temp[temp.length - 1].split("\\.")[0];
    this.table = new Table(null, className);
    cw.visit(V1_8, ACC_PUBLIC, className, null, "java/lang/Object", null);
    astRoot = parser;
    printTree();
    FileOutputStream fileOutputStream = new FileOutputStream(outputName);
    fileOutputStream.write(cw.toByteArray());
    fileOutputStream.close();
  }

  public static String capitalizeFirstLetter(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  public void printTree() throws IOException, OperatorError {
    Label start = new Label();
    Label end = new Label();
    MethodVisitor constructor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    constructor.visitCode();
    constructor.visitVarInsn(ALOAD, 0);                 // this
    constructor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    constructor.visitCode();
    constructor.visitLabel(start);
    for (ASTNode child : astRoot.getChildren()) {
      String name = child.getName();
      switch (name) {
        case "Constant", "Declaration", "Initialisation" -> {
          Variables variables = new Variables(table, allocator, null, null);
          variables.CheckVariables(cw, (MainNode) child, constructor);
        }
        case "Record" -> {
          Record record = new Record(table, allocator, outputName);
          record.checkRecord((MainNode) child);
        }
        case "Function" -> {
          Function function = new Function(table, new LocalIndexAllocator());
          function.CheckFunction(cw, (MainNode) child);
        }
        case "Deallocation" -> {

        }
        default -> {

        }

      }
    }
    constructor.visitInsn(RETURN);
    constructor.visitMaxs(0, 0);
    constructor.visitLabel(end);
    constructor.visitEnd();
    cw.visitEnd();
  }
}
