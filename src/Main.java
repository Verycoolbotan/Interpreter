import Lexer.Lexer;
import Lexer.Token;
import Parser.Parser;
import RPN.RPN;
import StackMachine.Machine;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Iterator;

public class Main {
    static PrintStream out = System.out;
    static Parser parser;

    public static void main(String[] args) {
        /*parser = new Parser(Lexer.read("input.txt"), out);
        parser.lang();*/

        /*ArrayList<Token> list = Lexer.read("input.txt");
        Iterator<Token> it = list.iterator();
        while (it.hasNext()) {
            out.println(it.next().toString());
        }*/

        LinkedList<Token> tokens = RPN.getRPN(Lexer.read("input.txt"));
        Iterator<Token> it = tokens.iterator();
        while (it.hasNext()) {
            out.println(it.next().toString());
        }
        try {
            Machine.run(tokens);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
