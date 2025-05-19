package compiler.CodeGeneration.Utils;

public class LocalIndexAllocator {

  private int nextIndex;

  public LocalIndexAllocator(int nextIndex) {
    this.nextIndex = nextIndex;
  }

  public LocalIndexAllocator() {
    nextIndex = 1;
  }

  public int allocate() {
    int index = nextIndex;
    if (index == 3) {
      System.out.println();
    }
    nextIndex++;
    return index;
  }

  public int getIndex() {
    return nextIndex;
  }

  public int previous() {
    return Math.max(nextIndex - 1, 0);
  }

  public void reset() {
    nextIndex = 0;
  }
}
