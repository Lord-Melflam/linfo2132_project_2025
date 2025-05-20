package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.GOTO;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class FunctionBody {

  private final Label cStart;
  private final Label cEnd;
  LocalIndexAllocator allocator;
  Label start, end;
  boolean breakBool;
  boolean continueBool;

  public FunctionBody(LocalIndexAllocator allocator, Label start, Label end, Label cStart,
      Label cEnd) {
    this.allocator = allocator;
    this.start = start;
    this.end = end;
    this.cStart = start;
    this.cEnd = end;
  }

  public boolean isBreakBool() {
    return breakBool;
  }

  public void setBreakBool(boolean breakBool) {
    this.breakBool = breakBool;
  }

  public boolean isContinueBool() {
    return continueBool;
  }

  public void setContinueBool(boolean continueBool) {
    this.continueBool = continueBool;
  }

  public void checkFunctionBody(MethodVisitor cw, ASTNode functionBlock,
      ArrayList<String> functionArgs, String functionType, Table functionTable,
      SimpleEntry<String, String> stringStringSimpleEntry)
      throws OperatorError {
    MainNode mainNode = (MainNode) functionBlock;
    if (mainNode != null && !mainNode.getChildrenList().isEmpty()) {
      for (ASTNode astNode : mainNode.getChildrenList()) {
        switch (astNode.getName()) {
          case "Assignment" -> {
            Assignment assignment = new Assignment(functionTable, allocator, start, end);
            assignment.checkAssignment(cw, (MainNode) astNode);
          }
          case "RecordAssignment" -> {

          }
          case "Return" -> {
            Return returnCall = new Return(functionTable, allocator, start, end);
            returnCall.checkReturn(cw, astNode, functionType, stringStringSimpleEntry);
          }
          case "Declaration", "Initialisation"/*, "Assignment", "Expression"*/ -> {
            Variables variables = new Variables(functionTable, allocator, start, end);
            variables.CheckVariables2(cw, (MainNode) astNode);
          }
          case "FunctionCall", "Expression" -> {
            FunctionCall functionCall = new FunctionCall(functionTable, allocator, start, end);
            functionCall.checkFunction(cw, (MainNode) astNode);
          }
          case "BuiltInFunctionCall" -> {
            BuiltInFunctionCall builtInFunctionCall = new BuiltInFunctionCall(functionTable,
                null, allocator, start, end);
            builtInFunctionCall.CheckBuiltInFunctionCall(cw, astNode, false);
          }
          case "IfControlStructure" -> {
           /* Label ifStart = new Label();
            Label ifEnd = new Label();
            cw.visitLabel(ifStart);*/
            Table ifTable = new Table(functionTable, functionTable.getClassName());
            IfControlStructure ifControlStructure = new IfControlStructure(ifTable, allocator,
                start, end);
            ifControlStructure.checkControlStructure(cw, (MainNode) astNode, functionType,
                stringStringSimpleEntry);
/*
            cw.visitLabel(ifEnd);
*/

          }
          case "ForControlStructure" -> {
           /* Label forStart = new Label();
            Label forEnd = new Label();
            cw.visitLabel(forStart);*/
            Table forTable = new Table(functionTable, functionTable.getClassName());
            ControlStructure controlStructure = new ControlStructure(forTable, allocator,
                start, end);
            controlStructure.checkControlStructure(cw, (MainNode) astNode, functionType,
                stringStringSimpleEntry);
/*
            cw.visitLabel(forEnd);
*/

          }
          case "WhileControlStructure" -> {
          /*  Label whileStart = new Label();
            Label wileEnd = new Label();
            cw.visitLabel(whileStart);*/
            Table whileTable = new Table(functionTable, functionTable.getClassName());

            WhileControlStructure whileControlStructure = new WhileControlStructure(whileTable,
                allocator, start, end);
            whileControlStructure.checkControlStructure(cw, (MainNode) astNode, functionType,
                stringStringSimpleEntry);
/*
            cw.visitLabel(wileEnd);
*/
          }
          case "Break" -> {
            if (cEnd != null) {
              cw.visitJumpInsn(GOTO, end);
              return;
            }
          }
          case "Continue" -> {
            if (cStart != null) {
              cw.visitJumpInsn(GOTO, start);
              return;
            }
          }
        }
      }
    }
  }
}
