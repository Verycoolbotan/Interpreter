import Lexer.Lexeme;
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

        LinkedList<Token> lex = Lexer.read("new.txt");

        for (Token lexToken:lex)
            System.out.println(lexToken);
        System.out.println("\n\n");

        //////////
        ///* Либо всё это
        LinkedList<Token> tokens = new LinkedList<Token>();
        tokens.add(new Token("a", Lexeme.VAR,0,0));
        tokens.add(new Token("3", Lexeme.NUM,0,0));
        tokens.add(new Token("2", Lexeme.NUM,0,0));
        tokens.add(new Token("+", Lexeme.ARITHMETICAL_OP,0,0));
        tokens.add(new Token("=", Lexeme.ASSIGN_OP,0,0));

        tokens.add(new Token("a", Lexeme.VAR,0,0));
        tokens.add(new Token("print", Lexeme.PRINT_KW,0,0));
         //*/
        // Либо это
        //LinkedList<Token> tokens = RPN.getRPN(lex);
        //////////

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
