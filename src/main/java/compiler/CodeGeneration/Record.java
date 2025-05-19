package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Record {

  private Table table;
  LocalIndexAllocator allocator;
  String path;

  public Record(Table table, LocalIndexAllocator allocator, String outputName) {
    this.table = table;
    this.allocator = allocator;
    int lastSlash = outputName.lastIndexOf("/");
    path = outputName.substring(0, lastSlash + 1);
  }

  public void checkRecord(MainNode child) throws IOException {
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    String recordType = "";
    HashMap<String, String> recordValue = new HashMap<>();
    LinkedList<Attribute> attributes = new LinkedList<>();
    for (ASTNode astNode : child.getChildrenList()) {
      if (astNode.getName().equals("RecordFields")) {
        MainNode recordBody = (MainNode) astNode;
        for (ASTNode recordAttribute : recordBody.getChildrenList()) {
          if (recordAttribute.getName().equals("Field")) {
            MainNode field = (MainNode) recordAttribute;
            recordValue.put(((GenericNode<?>) field.getChildrenList().getFirst()).getValue(),
                ((GenericNode<?>) field.getChildrenList().getLast()).getValue());
            attributes.add(
                new Attribute(((GenericNode<?>) field.getChildrenList().getFirst()).getValue(),
                    ((GenericNode<?>) field.getChildrenList().getLast()).getValue()));
          }
        }
      } else {
        if (astNode.getName().equals("Record")) {
          GenericNode<?> genericNode = (GenericNode<?>) astNode;
          recordType = genericNode.getValue();
        }
      }
    }
    table.getSymbols().put(recordType,
        new Table(recordType, "L" + recordType + ";", false, false, false, null, -1, recordValue));
    cw.visit(V1_8, ACC_PUBLIC, recordType, null, "java/lang/Object", null);
    StringBuilder constructorDescriptor = new StringBuilder();
    constructorDescriptor.append("(");
    for (Attribute attribute : attributes) {
      String fieldDescriptor = DescriptorUtils.getTypeFromString(attribute.type).getDescriptor();
      cw.visitField(ACC_PUBLIC, attribute.name, fieldDescriptor, null, null);
      constructorDescriptor.append(fieldDescriptor);
    }
   /* for (Entry<String, String> entry : recordValue.entrySet()) {
      String fieldDescriptor = DescriptorUtils.getTypeFromString(entry.getValue()).getDescriptor();
      cw.visitField(ACC_PUBLIC, entry.getKey(), fieldDescriptor, null, null);
      constructorDescriptor.append(fieldDescriptor);
    }*/
    constructorDescriptor.append(")V");
    MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", constructorDescriptor.toString(), null,
        null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0); // this
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    int counter = 1;
    for (Attribute attribute : attributes) {
      mv.visitVarInsn(ALOAD, 0);
      Utils.execute("load", mv, new SimpleEntry<>(attribute.name, attribute.type), counter++);
      mv.visitFieldInsn(PUTFIELD, recordType, attribute.name,
          DescriptorUtils.getTypeFromString(attribute.type).getDescriptor());
    }

   /* for (Entry<String, String> entry : recordValue.entrySet()) {
      mv.visitVarInsn(ALOAD, 0);
      Utils.execute("load", mv, entry, counter++);
      mv.visitFieldInsn(PUTFIELD, recordType, entry.getKey(),
          DescriptorUtils.getTypeFromString(entry.getValue()).getDescriptor());
    }*/
    mv.visitInsn(RETURN);
    //mv.visitMaxs(recordValue.size(), recordValue.size());
    mv.visitMaxs(0, 0);
    mv.visitEnd();
    FileOutputStream fileOutputStream = new FileOutputStream(path + recordType + ".class");
    fileOutputStream.write(cw.toByteArray());
    fileOutputStream.close();
  }

  public class Attribute {

    public String name;
    public String type;

    public Attribute(String name, String type) {
      this.name = name;
      this.type = type;
    }
  }
}
