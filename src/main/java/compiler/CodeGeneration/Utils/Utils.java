package compiler.CodeGeneration.Utils;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.T_FLOAT;
import static org.objectweb.asm.Opcodes.T_INT;

import compiler.CodeGeneration.Table;
import compiler.Semantic.Types.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Map.Entry;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Utils {

  public static Table lookInSymbolsTable(Table symbolTable, String nodeValue) {
    Table temp = symbolTable;
    while (temp.getParent() != null) {
      if (temp.getSymbols().containsKey(nodeValue)) {
        return temp.getSymbols().get(nodeValue);
      }
      temp = temp.getParent();
    }
    if (temp.getSymbols().containsKey(nodeValue)) {
      return temp.getSymbols().get(nodeValue);
    }
    return null;
  }

  public static void InitAttribute(ClassWriter cw, String type, String variableIdentifier,
      int access, String descriptor, String rightPart) {
    switch (type) {
      case "int" -> {
        cw.visitField(access, variableIdentifier, descriptor, null, Integer.valueOf(rightPart));
      }
      case "float" -> {
        cw.visitField(access, variableIdentifier, descriptor, null, Float.valueOf(rightPart));
      }
      case "string" -> {
        cw.visitField(access, variableIdentifier, descriptor, null, rightPart);
      }
      case "bool", "boolean" -> {
        cw.visitField(access, variableIdentifier, descriptor, null, Boolean.valueOf(rightPart));
      }
      default -> {
        cw.visitField(access, variableIdentifier, descriptor, null, null);
      }
    }
  }

  public static String constructor(LinkedList<Type> args) {
    StringBuilder cons = new StringBuilder();
    cons.append("(");
    for (Type type : args) {
      String des = DescriptorUtils.getTypeFromString(type.getReturnType()).getDescriptor();
      cons.append(des);
    }
    cons.append(")V");
    return cons.toString();
  }

  public static void execute(String action, MethodVisitor mv, Entry<String, String> entry,
      int index) {
    switch (action) {
      case "load" -> loadField(mv, entry, index);
      case "store" -> store(mv, entry, index);
      case "return" -> returnFunction(mv, entry);
      case "array" -> array(mv, entry);
    }
  }

  private static void array(MethodVisitor mv, Entry<String, String> entry) {
    switch (entry.getValue()) {
      case "int", "I", "bool", "boolean", "Z", "[I" -> {
        mv.visitIntInsn(NEWARRAY, T_INT);          // new int[9]
      }
      case "float", "F", "[F" -> {
        mv.visitIntInsn(NEWARRAY, T_FLOAT);          // new int[9]
      }
      case "string", "Ljava/lang/String;", "[Ljava/lang/String;" -> {
        mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
      }
      default -> mv.visitTypeInsn(ANEWARRAY, entry.getValue());
    }
  }

  private static void store(MethodVisitor mv, Entry<String, String> entry, int index) {
    switch (entry.getValue()) {
      case "int", "I", "bool", "boolean", "Z" -> {
        mv.visitVarInsn(ISTORE, index);
      }
      case "float", "F" -> {
        mv.visitVarInsn(FSTORE, index);
      }
      case "string", "Ljava/lang/String;" -> {
        mv.visitVarInsn(ASTORE, index);
      }
      default -> mv.visitVarInsn(ASTORE, index);
    }
  }

  private static void returnFunction(MethodVisitor mv, Entry<String, String> entry) {
    switch (entry.getValue()) {
      case "int", "I", "bool", "boolean", "Z" -> {
        mv.visitInsn(IRETURN);
      }
      case "float", "F" -> {
        mv.visitInsn(FRETURN);
      }
      case "string", "Ljava/lang/String;" -> {
        mv.visitInsn(ARETURN);
      }
      default -> mv.visitInsn(ARETURN);
    }
  }

  public static void loadField(MethodVisitor mv, Entry<String, String> entry, int counter) {

    switch (entry.getValue()) {
      case "int", "I", "bool", "boolean", "Z" -> {
        mv.visitVarInsn(ILOAD, counter);
      }
      case "float", "F" -> {
        mv.visitVarInsn(FLOAD, counter);
      }
      case "string", "Ljava/lang/String;" -> {
        mv.visitVarInsn(ALOAD, counter);
      }
      default -> mv.visitVarInsn(ALOAD, counter);
    }
  }

  public static void loadLitField(MethodVisitor mv, Entry<String, String> entry, String v) {
    switch (entry.getValue()) {
      case "int", "I" -> {
        mv.visitLdcInsn(Integer.parseInt(v));
      }
      case "bool", "boolean", "Z" -> {
        mv.visitLdcInsn(Boolean.valueOf(v));
      }
      case "float", "F" -> {
        mv.visitLdcInsn(Float.valueOf(v));
      }
      case "string", "Ljava/lang/String;" -> {
        mv.visitLdcInsn(v);
      }
      default -> mv.visitLdcInsn(v);
    }
  }

  public static Table searchIdentifier(MethodVisitor mv, Table symbolTable, String nodeValue,
      boolean load, boolean constructor) {
    Table index = symbolTable.resolve(nodeValue);
    if (index == null) {
      throw new IllegalArgumentException("big problem");
    }
    if (index.isField) {
      if (load) {
        if (constructor) {
          mv.visitVarInsn(ALOAD, 0);
        } else {
          mv.visitVarInsn(ALOAD, 1);
        }
        mv.visitFieldInsn(GETFIELD, symbolTable.getClassName(), nodeValue, index.descriptor);
      }
      return index;
    }
    if (load) {
      loadField(mv, new SimpleEntry<String, String>("", index.descriptor), index.index);
    }
    return index;
  }
}
