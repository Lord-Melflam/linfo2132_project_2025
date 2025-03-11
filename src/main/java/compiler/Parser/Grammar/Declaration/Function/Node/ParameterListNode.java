package compiler.Parser.Grammar.Declaration.Function.Node;

public class ParameterListNode {

  private ParameterNode parameterNode;
  private ParameterListTailNode parameterListTailNode;

  public ParameterListNode(ParameterNode parameterNode,
      ParameterListTailNode parameterListTailNode) {
    this.parameterNode = parameterNode;
    this.parameterListTailNode = parameterListTailNode;
  }

  @Override
  public String toString() {
    return "ParameterListNode{" +
        "parameterNode=" + parameterNode +
        ", parameterListTailNode=" + parameterListTailNode +
        '}';
  }
}
