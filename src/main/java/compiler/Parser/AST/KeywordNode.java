package compiler.Parser.AST;

public class KeywordNode {

  String name;

  public KeywordNode(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "KeywordNode(" + name + ")";
  }
}
