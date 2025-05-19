package compiler.Semantic.Types.Function;

import java.util.LinkedList;

public class BuiltIn {

  private String name;
  private String returnType;
  private LinkedList<String> args;

  public BuiltIn(String name, String returnType) {
    this.name = name;
    this.returnType = returnType;
  }

  public BuiltIn(String name, String returnType, LinkedList<String> args) {
    this.name = name;
    this.returnType = returnType;
    this.args = args;
  }

  public void add(String arg) {
    args.add(arg);
  }

  public String getName() {
    return name;
  }

  public String getReturnType() {
    return returnType;
  }

  public LinkedList<String> getArgs() {
    return args;
  }
}
