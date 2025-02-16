package compiler.Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final FindSymbol findSymbol;
    private final List<Symbol> symbols;
    private int index;

    public Lexer(Reader input) {
        this.findSymbol = new FindSymbol();
        this.symbols = new ArrayList<>();
        this.index = 0;
        try {
            parseSymbols(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Symbol getNextSymbol() {
        if (index >= symbols.size()) return null;
        return symbols.get(index++);
    }

    private void parseSymbols(Reader reader) throws IOException {
        StringBuilder word = new StringBuilder();
        int nextChar;

        while ((nextChar = reader.read()) != -1) {
            char ch = (char) nextChar;

            if (!findSymbol.isAscii(ch)) {
                System.err.println("Not ASCII: " + ch);
                continue;
            }

            word.append(ch);
            List<String> symbolSet = findSymbol.symbols(word.toString());

            if (symbolSet.isEmpty()) {
                if (!word.isEmpty()) {
                    String longestMatch = findSymbol.longestMatchSymbols(word.substring(0, word.length() - 1));
                    if (longestMatch != null) {
                        symbols.add(new Symbol(longestMatch, word.substring(0, word.length() - 1)));
                    }
                    word = new StringBuilder().append(word.charAt(word.length() - 1));
                } else {
                    word.setLength(0);
                }
            }
        }

        if (!word.isEmpty()) {
            String longestMatch = findSymbol.longestMatchSymbols(word.toString());
            if (longestMatch != null) {
                symbols.add(new Symbol(longestMatch, word.toString()));
            }
        }
    }
}
