package RPN;

import java.util.Stack;

import Lexer.Lexeme;
import Lexer.Token;

import java.util.LinkedList;

public class RPN {
    private static LinkedList<Token> out = new LinkedList<>();

    //Сравниваем приоритеты арифметических операторов без необходимости ковыряться в Enum
    private static boolean priority(Token op1, Token op2) {
        return ((op1.getValue().equals("*") || op1.getValue().equals("/")) && (op2.getValue().equals("+") || op2.getValue().equals("-")));
    }

    //TODO: возможно, придётся явно указывать тип скобок
    private static LinkedList<Token> brackets(LinkedList<Token> input, boolean curly) {
        LinkedList<Token> body = new LinkedList<>();
        int open = 1;
        int close = 0;

        while (open != close) {
            Token token = input.poll();
            if(curly){
                if (token.getType().equals(Lexeme.L_CB))
                    open++;
                else if (token.getType().equals(Lexeme.R_CB))
                    close++;
            } else {
                if (token.getType().equals(Lexeme.L_BR))
                    open++;
                else if (token.getType().equals(Lexeme.R_BR))
                    close++;
            }

            body.add(token);
        }
        body.removeLast();
        return body;
    }

    private static void if2RPN(LinkedList<Token> input) {
        /* Читаем условие в скобках
         * После него ставим GOTO по невыполнению
         * Читаем тело
         * Смотрим на адрес конца тела, после тела вставляем метку для GOTO с прочитанным адресом
         */
        boolean stop = false;
        int end = 0;
        Token endToken = new Token("", Lexeme.END);

        do {
            Token token = input.poll();
            switch (token.getType()) {
                case L_BR:
                    makeRPN(brackets(input, false));
                    input.addFirst(new Token(")", Lexeme.R_BR));
                    break;
                case R_BR:
                    //Вставляем адрес для перехода, значение будет определено в конце
                    out.add(endToken);
                    out.add(new Token("F", Lexeme.GOTO_BY_FALSE));
                    break;
                case L_CB:
                    makeRPN(brackets(input, true));
                    input.addFirst(new Token("}", Lexeme.R_CB));
                    break;
                case R_CB:
                    end = out.size();
                    stop = true;
                    break;
            }
        } while (!stop);
        endToken.setValue(String.valueOf(end));
    }

    private static void while2RPN(LinkedList<Token> input) {
        boolean stop = false;
        int start = out.size();
        int end = 0;
        Token startToken = new Token(String.valueOf(start), Lexeme.START);
        Token endToken = new Token("", Lexeme.END);;

        while (!stop) {
            Token token = input.poll();
            switch (token.getType()) {
                case L_BR:
                    LinkedList<Token> condition = brackets(input, false);
                    makeRPN(condition);
                    input.addFirst(new Token(")", Lexeme.R_BR));
                    break;
                case R_BR:
                    out.add(endToken);
                    out.add(new Token("", Lexeme.GOTO_BY_FALSE));
                    break;
                case L_CB:
                    makeRPN(brackets(input, true));
                    input.addFirst(new Token("}", Lexeme.R_CB));
                    break;
                case R_CB:
                    //Аналогично, но с адресом начала цикла
                    out.add(startToken);
                    out.add(new Token("", Lexeme.GOTO));
                    end = out.size();
                    stop = true;
                    break;
            }

            endToken.setValue(String.valueOf(end));
        }

    }

    private static LinkedList<Token> makeRPN(LinkedList<Token> input) {
        Stack<Token> stack = new Stack<>();
        Token top;

        while (!input.isEmpty()) {
            Token token = input.poll();
            //Алгоритм нагло взят из книги Антика
            switch (token.getType()) {
                //Операнды сразу отправляются в выходной список
                case VAR:
                case NUM:
                    out.add(token);
                    break;
                /* Если приоритет операции на вершине стека не меньше приоритета прочитанной, то перенести операцию из
                 * стека в выходной список и повторить этот шаг, иначе поместить прочитанную операцию в стек
                 */
                case ARITHMETICAL_OP:
                    while (!stack.isEmpty() && ((top = stack.peek()).getType().equals(Lexeme.ARITHMETICAL_OP)) && priority(top, token)) {
                        out.add(stack.pop());
                    }
                    stack.push(token);
                    break;
                //Открывающая скобка и прочие операции записываются в стек
                case L_BR:
                    //case L_CB:
                case PRINT_KW:
                case ASSIGN_OP:
                case LOGICAL_OP:
                    stack.push(token);
                    break;
                //Выталкиваем все операции из стека до открывающей скобки
                case R_BR:
                    while ((!stack.isEmpty()) && !(top = stack.pop()).getType().equals(Lexeme.L_BR)) {
                        out.add(top);
                    }
                    break;
                case WHILE_KW:
                    while2RPN(input);
                    break;
                case IF_KW:
                    if2RPN(input);
                    break;
            }
        }

        //Избавляемся от остатков стека
        while (!stack.isEmpty()) {
            out.add(stack.pop());
        }

        return out;
    }

    public static LinkedList<Token> getRPN(LinkedList<Token> input) {
        makeRPN(input);
        return out;
    }

}
