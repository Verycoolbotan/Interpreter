import Lexer.*;
import Parser.Parser;
import RPN.RPN;
import StackMachine.Machine;

import java.util.LinkedList;

public class Main {
    static Parser parser;
    static LinkedList<Token> input;

    public static void main(String[] args) {
        try {
            input = Lexer.read(args[0]);
            //input = Lexer.read("input.txt");
            parser = new Parser(input);
            parser.lang();
            System.out.println("Parser success");

            /*Добавляем неявные скобки в выражения для корректной работы присваивания.
             *Да, я ОЧЕНЬ торопился.
             */
            int i = 0;
            while (i < input.size()) {
                Token current = input.get(i);
                switch (current.getType()) {
                    case ASSIGN_OP:
                        input.add(i + 1, new Token("(", Lexeme.L_BR));
                        i += 2;
                        break;
                    case END_OF_LINE:
                        input.add(i, new Token(")", Lexeme.R_BR));
                        i += 2;
                        break;
                    default:
                        i++;
                        break;
                }
            }

            LinkedList<Token> rpn = RPN.getRPN(input);
            Machine.run(rpn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
