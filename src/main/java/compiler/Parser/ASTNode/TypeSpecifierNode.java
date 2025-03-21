package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

@JsonTypeName("TypeSpecifierNode")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeSpecifierNode extends ASTNode {

  private String type;

  public TypeSpecifierNode(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public TypeSpecifierNode() {
    type = "void";
  }

  @Override
  public String toString() {
    return "<TypeSpecifier," + type + '>';
  }

  @Override
  public String getName() {
    return "TypeSpecifier";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
