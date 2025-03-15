package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

@JsonTypeName("TypeSpecifierNode")
public class TypeSpecifierNode extends ASTNode {

  private String type;

  @JsonCreator
  public TypeSpecifierNode(@JsonProperty("type") String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "<TypeSpecifier," + type + '>';
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
