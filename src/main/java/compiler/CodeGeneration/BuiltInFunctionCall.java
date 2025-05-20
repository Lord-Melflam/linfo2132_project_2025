package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.D2I;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F2D;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.I2C;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.IXOR;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.SWAP;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Lexer.Symbols.Literal;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class BuiltInFunctionCall {

  private Table table;
  private String variableName;
  LocalIndexAllocator allocator;
  Label start, end;
  Table func;

  public BuiltInFunctionCall(Table table, String variableName, LocalIndexAllocator allocator,
      Label start, Label end) {
    this.table = table;
    this.variableName = variableName;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
    func = null;
  }

  public void setFunc(Table func) {
    this.func = func;
  }

  public Table CheckBuiltInFunctionCall(MethodVisitor methodVisitor, ASTNode astNode,
      Boolean isConstructor) throws OperatorError {
    MainNode mainNode = (MainNode) astNode;
    String builtInName = "";
    String args = "";
    LinkedList<String> strings = new LinkedList<>();
    HashMap<String, String> functionArgs = new HashMap<>();
    HashMap<String, String> test = new HashMap<>();

    MainNode mainNode1 = null;
    for (ASTNode node : mainNode.getChildrenList()) {
      //if (node.getName().equals("BuiltInFunctionCall")) {
      //MainNode builtInFunctionCallNode = (MainNode) node;
      //for (ASTNode builtInNode : builtInFunctionCallNode.getChildrenList()) {
      if (node.getName().equals("BuiltInFunction")) {
        GenericNode<?> builtInNameNode = ((GenericNode<?>) node);
        builtInName = builtInNameNode.getValue();
      }
      if (node.getName().equals("Parameters")) {
        getParams(methodVisitor, (MainNode) node, functionArgs, isConstructor, strings, test);
        mainNode1 = (MainNode) node;
      }
      //}
      //}
    }

    if (builtInName.equals("!")) {
      ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
          allocator, start, end);
      Table table1 = expressionCodeGenerator.checkExpression(mainNode1.getChildrenList().getFirst(),
          methodVisitor, isConstructor);
      if (functionArgs.containsKey("Literal")) {
        Utils.execute("load", methodVisitor, new SimpleEntry<>("", table1.descriptor),
            table1.index);
      }
      methodVisitor.visitInsn(ICONST_1);
      methodVisitor.visitInsn(IXOR);
      methodVisitor.visitVarInsn(ISTORE, allocator.allocate());
      return new Table(functionArgs.getOrDefault("Identifier", "temp"), "Z", true, false, false,
          null, allocator.previous());

    } else if (builtInName.equals("chr")) {
      ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
          allocator, start, end);
      Table table1 = expressionCodeGenerator.checkExpression(mainNode1.getChildrenList().getFirst(),
          methodVisitor, isConstructor);
      System.out.println();
      if (functionArgs.containsKey("Literal")) {
        Utils.execute("load", methodVisitor, new SimpleEntry<>("", table1.descriptor),
            table1.index);
      }
      methodVisitor.visitInsn(I2C); // Convertit int -> char (char est 16 bits unsigned)
      methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf",
          "(C)Ljava/lang/String;", false);
      methodVisitor.visitVarInsn(ASTORE, allocator.allocate());
      return new Table(functionArgs.getOrDefault("Identifier", "temp"), "Ljava/lang/String;", true,
          false, false, null, allocator.previous());
    } else if (builtInName.equals("floor")) {
      ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
          allocator, start, end);
      Table table1 = expressionCodeGenerator.checkExpression(mainNode1.getChildrenList().getFirst(),
          methodVisitor, isConstructor);
      /*if (functionArgs.containsKey("Literal")) {
        Utils.execute("load", methodVisitor, new SimpleEntry<>("", table1.descriptor),
            table1.index);
      }*/
      if (!table1.isField) {
        Utils.execute("load", methodVisitor, new SimpleEntry<>("", table1.descriptor),
            table1.index);
      }
      methodVisitor.visitInsn(F2D);
      methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
      methodVisitor.visitInsn(D2I);
      methodVisitor.visitVarInsn(ISTORE, allocator.allocate());

      return new Table(functionArgs.getOrDefault("Identifier", "temp"), "I", true, false, false,
          null, allocator.previous());
    } else if (builtInName.equals("min") || builtInName.equals("max")) {
      LinkedList<String> stringLinkedList = new LinkedList<>();
      LinkedList<String> load = new LinkedList<>();

      if (((MainNode) mainNode1.getChildrenList().get(0)).getName().equals("FunctionCall")) {
        int index = strings.indexOf("Function");
        load.add(strings.get(index));
        load.add(strings.get(index + 1));
      } else {
        load.add(strings.get(0));
        load.add(strings.get(1));
      }
      load.add(strings.get(strings.size() - 2));
      load.add(strings.getLast());

      LinkedList<String> list = new LinkedList<>();
      for (int i = 0; i < load.size(); i++) {
        String val = load.get(i);
        if (val.equals("Identifier")) {
          Table table1 = Utils.searchIdentifier(methodVisitor, table, load.get(i + 1), true,
              false);
          list.add(table1.descriptor);
        } else if (val.equals("Literal")) {
          String next = load.get(i + 1);
          String type = new Literal().typeOfLiteral(next);
          Utils.loadLitField(methodVisitor,
              new SimpleEntry<>("", type), next);
          list.add(DescriptorUtils.getTypeFromString(type).getDescriptor());
        } else if (val.equals("Function")) {
          String next = load.get(i + 1);
          Utils.execute("load", methodVisitor, new SimpleEntry<>("", func.descriptor),
              Integer.parseInt(next));
        } else if (val.equals("other")) {
          String next = load.get(i + 1);
          String[] des = next.split("_");
          list.add(des[0]);
          Utils.execute("load", methodVisitor, new SimpleEntry<>("", list.getLast()),
              Integer.parseInt(des[1]));
        }
      }
      if (list.stream().allMatch(a -> a.equals("I"))) {
        return minMaxFunction(methodVisitor, builtInName, "I");
      } else {
        return minMaxFunction(methodVisitor, builtInName, "F");
      }
    } else if (builtInName.equals("printType")) {
      Table table1 = Utils.searchIdentifier(methodVisitor, table, functionArgs.get("Identifier"),
          true, false);
      String descriptor = table1.descriptor;

      // Gérer les tableaux
      if (descriptor.startsWith("[")) {
        // obj.getClass().getSimpleName()
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass",
            "()Ljava/lang/Class;", false);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getSimpleName",
            "()Ljava/lang/String;", false);
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        methodVisitor.visitInsn(SWAP);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(Ljava/lang/String;)V", false);
        return null;
      }

      // Gérer les types primitifs
      switch (descriptor) {
        case "I": // int
          methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf",
              "(I)Ljava/lang/Integer;", false);
          break;
        case "Z": // boolean
          methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf",
              "(Z)Ljava/lang/Boolean;", false);
          break;
        case "F": // float
          methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf",
              "(F)Ljava/lang/Float;", false);
          break;
        default:
          // Référence directe, pas besoin de boxing
          break;
      }

      // obj.getClass().getSimpleName()
      methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass",
          "()Ljava/lang/Class;", false);
      methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getSimpleName",
          "()Ljava/lang/String;", false);

      // System.out.println(...)
      methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      methodVisitor.visitInsn(SWAP);
      methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
          "(Ljava/lang/String;)V", false);

      return null;
    }

    return extracted(methodVisitor, builtInName, functionArgs);
  }

  public Table minMaxFunction(MethodVisitor methodVisitor, String builtInName, String descriptor) {
    int index = allocator.allocate();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    stringBuilder.append(descriptor);
    stringBuilder.append(descriptor);
    stringBuilder.append(")");
    stringBuilder.append(descriptor);

    //if (builtInName.equals("min") || ) {
    methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Math", builtInName,
        stringBuilder.toString(), false);
    if (descriptor.equals("I")) {
      methodVisitor.visitVarInsn(ISTORE, index);
      Utils.execute("load", methodVisitor, new SimpleEntry<>("", "I"), index);
    } else {
      methodVisitor.visitVarInsn(FSTORE, index);
      Utils.execute("load", methodVisitor, new SimpleEntry<>("", "F"), index);
    }
    //}
    /* else {
      methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", stringBuilder.toString(),
          false);
      if (descriptor.equals("I")) {
        methodVisitor.visitVarInsn(ISTORE, index);
      } else {
        methodVisitor.visitVarInsn(FSTORE, index);
      }
    }*/
    return new Table(builtInName, descriptor, true, false, false,
        null, index);
  }

  private Table extracted(MethodVisitor mv, String builtInName,
      HashMap<String, String> functionArgs) {
    if (builtInName.contains("write")) {
      if (!functionArgs.isEmpty()) {
        write(functionArgs, mv, builtInName);
      } else {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "()V", false);
      }
      return null;
    } else if (builtInName.contains("read")) {
      return read(functionArgs, mv, builtInName);
      //"!", "floor", "chr"
    } else if (builtInName.equals("len")) {
      return len(functionArgs, mv, builtInName);
    }

    /*else if (builtInName.equals("readInt")) {
      mv.visitTypeInsn(NEW, "java/util/Scanner");
      mv.visitInsn(DUP);
      mv.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
      mv.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V",
          false);
      mv.visitVarInsn(ASTORE, allocator.allocate());
      mv.visitVarInsn(ALOAD, allocator.previous());
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
      mv.visitVarInsn(ISTORE, allocator.allocate());
      mv.visitLocalVariable(variableName, "I", null, Function.start, Function.end,
          allocator.previous());
      table.getSymbols()
          .put(variableName, new Table(variableName, "I", false, false, allocator.previous()));
    }*/
    return null;
  }

  private Table len(HashMap<String, String> functionArgs, MethodVisitor mv, String builtInName) {
    int index = allocator.allocate();
    if (functionArgs.containsKey("Literal") && !functionArgs.get("Literal").isEmpty()) {
      mv.visitLdcInsn(functionArgs.get("Literal"));
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
      mv.visitVarInsn(ISTORE, index);
    } else if (functionArgs.containsKey("Identifier") && !functionArgs.get("Identifier")
        .isEmpty()) {
      Utils.searchIdentifier(mv, table, functionArgs.get("Identifier"), true, false);
      mv.visitInsn(ARRAYLENGTH);
      mv.visitVarInsn(ISTORE, index);
    }
    return new Table("len", "I", false, false, false, null, index);
  }

  private Table read(HashMap<String, String> functionArgs, MethodVisitor mv, String builtInName) {
    String des = "";
    mv.visitTypeInsn(NEW, "java/util/Scanner");
    mv.visitInsn(DUP);
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
    mv.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V",
        false);
    mv.visitVarInsn(ASTORE, allocator.allocate());
    mv.visitVarInsn(ALOAD, allocator.previous());
    if (builtInName.substring(4).toLowerCase().equals("int")) {
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
      mv.visitVarInsn(ISTORE, allocator.allocate());
      mv.visitLocalVariable(variableName, "I", null, start, end, allocator.previous());
      table.getSymbols().put(variableName,
          new Table(variableName, "I", false, false, false, null, allocator.previous()));
      des = "I";
    } else if (builtInName.substring(4).equals("float")) {
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextFloat", "()F", false);

      mv.visitVarInsn(ISTORE, allocator.allocate());
      mv.visitLocalVariable(variableName, "F", null, start, end, allocator.previous());
      table.getSymbols().put(variableName,
          new Table(variableName, "F", false, false, false, null, allocator.previous()));
      des = "F";
    } else {
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;",
          false);
      mv.visitVarInsn(ISTORE, allocator.allocate());
      mv.visitLocalVariable(variableName, "Ljava/lang/String;", null, start, end,
          allocator.previous());
      table.getSymbols().put(variableName,
          new Table(variableName, "Ljava/lang/String;", false, false, false, null,
              allocator.previous()));
      des = "Ljava/lang/String;";
    }
    return new Table(builtInName, des, false, false, false, null, allocator.previous());

  }

  private void write(HashMap<String, String> functionArgs, MethodVisitor mv, String builtInName) {
    if (!functionArgs.getOrDefault("Function", "").isEmpty()) {
      mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      Utils.execute("load", mv, new SimpleEntry<String, String>("", func.descriptor),
          Integer.parseInt(functionArgs.get("Function")));
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
          "(" + func.descriptor + ")V", false);
    }
    if (!functionArgs.getOrDefault("Literal", "").isEmpty() && functionArgs.getOrDefault(
        "Identifier", "").isEmpty() && functionArgs.getOrDefault("Function", "").isEmpty()) {
      mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      String string = functionArgs.get("Literal").replace("\"", "");
      String typ = new Literal().typeOfLiteral(string);
      if (typ.equals("int")) {
        mv.visitLdcInsn(
            Integer.parseInt(string)
        );
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(" + DescriptorUtils.getTypeFromString("int") + ")V",
            false);
      } else if (typ.equals("float")) {
        mv.visitLdcInsn(
            Float.valueOf(string)
        );
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(" + DescriptorUtils.getTypeFromString("float") + ")V",
            false);
      } else {
        mv.visitLdcInsn(
            string
        );
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(Ljava/lang/String;)V",
            false);
      }

     /* mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
          "(" + DescriptorUtils.getTypeFromString(
              new Literal().typeOfLiteral(functionArgs.get("Literal").replace("\"", ""))) + ")V",
          false);*/
      //mv.visitMaxs(0, 0);
    } else if (!functionArgs.getOrDefault("Identifier", "").isEmpty() && functionArgs.getOrDefault(
        "Literal", "").isEmpty() && functionArgs.getOrDefault("Function", "").isEmpty()) {
      mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      Table ide = Utils.searchIdentifier(mv, table, functionArgs.get("Identifier"), true, false);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
          "(" + ide.descriptor + ")V", false);
      //mv.visitMaxs(0, 0);
      Utils.execute("Return", mv, new SimpleEntry<String, String>("", ide.descriptor), -1);
    } else if (functionArgs.containsKey("other")) {
      mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      //Table ide = Utils.searchIdentifier(mv, table, functionArgs.get("Identifier"), true, false);
      Utils.execute("load", mv, new SimpleEntry<>("", functionArgs.get("des")),
          Integer.parseInt(functionArgs.get("other")));
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
          "(" + functionArgs.get("des") + ")V", false);
    }
  }

  private void getParams(MethodVisitor methodVisitor, MainNode astNode,
      HashMap<String, String> functionArgs, Boolean isConstructor, LinkedList<String> strings,
      HashMap<String, String> test)
      throws OperatorError {
    for (ASTNode node : astNode.getChildrenList()) {
      MainNode mainNode = (MainNode) node;
      if (node.getName().equals("Expression")) {
        for (ASTNode expressionNode : mainNode.getChildrenList()) {
          if (expressionNode.getName().equals("Identifier")) {
            String type = ((GenericNode<?>) expressionNode).getValue();
            if (!functionArgs.containsKey("Identifier")) {
              functionArgs.put("Identifier", type);
            } else {
              functionArgs.put("Identifier1", type);
            }
            strings.add("Identifier");
            strings.add(type);
          } else {
            String type = ((GenericNode<?>) expressionNode).getValue();
            if (!functionArgs.containsKey("Literal")) {
              functionArgs.put("Literal", type);
            } else {
              functionArgs.put("Literal1", type);
            }

            strings.add("Literal");
            strings.add(type);
          }
        }
      } else if (node.getName().equals("FunctionCall")) {
        GenericNode<?> functionName = (GenericNode<?>) ((MainNode) node).getChildrenList()
            .getFirst();
        functionArgs.clear();
        getParams(methodVisitor, ((MainNode) ((MainNode) node).getChildrenList().get(1)),
            functionArgs, isConstructor, strings, test);
        Table function = table.resolve(functionName.getValue());
        setFunc(function);
        methodVisitor.visitVarInsn(ALOAD, 1);
        if (!functionArgs.isEmpty()) {
          StringBuilder stringBuilder = new StringBuilder("_");
          for (Entry<String, String> entry : functionArgs.entrySet()) {
            stringBuilder.append(entry.getValue());
            stringBuilder.append("_");
            if (entry.getKey().contains("Literal")) {
              Utils.loadLitField(methodVisitor,
                  new SimpleEntry<>("", new Literal().typeOfLiteral(entry.getValue())),
                  entry.getValue());
            } else if (entry.getKey().contains("Function")) {
              Utils.execute("load", methodVisitor,
                  new SimpleEntry<>("", func.descriptor),
                  Integer.parseInt(functionArgs.get("Function")));
            } else if (entry.getKey().contains("other")) {
              Utils.execute("load", methodVisitor, new SimpleEntry<>("", functionArgs.get("des")),
                  Integer.parseInt(entry.getValue()));
            } else if (entry.getKey().contains("des")) {
              continue;
            } else {
              Table arg = Utils.searchIdentifier(methodVisitor, table, entry.getValue(), true,
                  false);
            }
          }
          methodVisitor.visitMethodInsn(INVOKESPECIAL, table.getClassName(),
              functionName.getValue(),
              function.descriptorFunction, false);
          int index = allocator.allocate();
          //methodVisitor.visitVarInsn(Opcodes.ISTORE, index);
          Utils.execute("store", methodVisitor,
              new SimpleEntry<String, String>("", function.descriptor), index);
          String res = function.name + stringBuilder.substring(0, stringBuilder.length() - 1);
          table.getSymbols()
              .put(res, new Table(res, function.descriptor, false, false, false, null, index));
          if (!functionArgs.containsKey("Function")) {
            functionArgs.put("Function", String.valueOf(index));

          } else {
            functionArgs.put("Function1", String.valueOf(index));

          }
          strings.add("Function");
          strings.add(String.valueOf(index));
        }
      } else if (node.getName().equals("BinaryExpression") || node.getName()
          .equals("UnaryExpression") || node.getName().equals("FieldAccess")) {
        ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
            allocator, start, end);
        Table table1 = expressionCodeGenerator.checkExpression(
            mainNode,
            methodVisitor, isConstructor);
        if (!functionArgs.containsKey("other")) {
          functionArgs.put("other", String.valueOf(table1.index));
          functionArgs.put("des", table1.descriptor);
        } else {
          functionArgs.put("other1", String.valueOf(table1.index));
          functionArgs.put("des1", table1.descriptor);
        }
        strings.add("other");
        strings.add(table1.descriptor + "_" + String.valueOf(table1.index));
      }
    }
  }
}
