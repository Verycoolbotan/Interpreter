package StackMachine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import Lexer.Token;
import Lexer.Lexeme;

public class Machine {
    //private static ArrayList<Token> lt;
    private static Stack<Token> stack = new Stack<>();

    private static Token mathCalc(Token operator) throws MachineException {

        System.out.println("Math calc...");

        Token op2 = stack.pop();
        Token op1 = stack.pop();

        int intOp1 = op1.getType().equals(Lexeme.VAR) ? Table.get(op1.getValue()) : Integer.parseInt(op1.getValue());
        int intOp2 = op2.getType().equals(Lexeme.VAR) ? Table.get(op2.getValue()) : Integer.parseInt(op2.getValue());

        Token result = new Token(null, null);

        switch (operator.getValue()) {
            case "+":
                result = new Token(String.valueOf(intOp1 + intOp2), Lexeme.NUM);
                break;
            case "-":
                result = new Token(String.valueOf(intOp1 - intOp2), Lexeme.NUM);
                break;
            case "*":
                result = new Token(String.valueOf(intOp1 * intOp2), Lexeme.NUM);
                break;
            case "/":
                result = new Token(String.valueOf((int) (intOp1 / intOp2)), Lexeme.NUM);
                break;
        }

        return result;
    }

    private static Token logicCalc(Token operator) throws MachineException {

        System.out.println("Logic calc...");

        Token op2 = stack.pop();
        Token op1 = stack.pop();

        int intOp1 = op1.getType().equals(Lexeme.VAR) ? Table.get(op1.getValue()) : Integer.parseInt(op1.getValue());
        int intOp2 = op2.getType().equals(Lexeme.VAR) ? Table.get(op2.getValue()) : Integer.parseInt(op2.getValue());

        Token result = new Token(null, null);

        switch (operator.getValue()) {
            case ">":
                result = new Token(String.valueOf(intOp1 > intOp2), Lexeme.LOGIC_VALUE);
                break;
            case "<":
                result = new Token(String.valueOf(intOp1 < intOp2), Lexeme.LOGIC_VALUE);
                break;
            case ">=":
                result = new Token(String.valueOf(intOp1 >= intOp2), Lexeme.LOGIC_VALUE);
                break;
            case "<=":
                result = new Token(String.valueOf(intOp1 <= intOp2), Lexeme.LOGIC_VALUE);
                break;
            case "==":
                result = new Token(String.valueOf(intOp1 == intOp2), Lexeme.LOGIC_VALUE);
                break;
            case "!=":
                result = new Token(String.valueOf(intOp1 != intOp2), Lexeme.LOGIC_VALUE);
                break;
        }

        return result;
    }

    private static void assign() {

        System.out.println("Assign...");

        //TODO: а если value - переменная?
        Token value = stack.pop();
        Token var = stack.pop();
        Table.assign(var.getValue(), Integer.parseInt(value.getValue()));
    }

    private static void print() throws MachineException {
        System.out.println("Print...");
        Token token = stack.pop();
        int arg = token.getType().equals(Lexeme.VAR) ? Table.get(token.getValue()) : Integer.parseInt(token.getValue());
        //TODO: передать в конструктор PrintStream?
        System.out.println(arg);
    }

    public static void run(LinkedList<Token> lt) throws MachineException {
        Token address = null;

        for (int i = 0; i < lt.size(); i++) {
            Token current = lt.get(i);
            switch (current.getType()) {
                case VAR:
                case NUM:
                case END: // Как ты мог забыть про него!
                    stack.push(current);
                    break;
                case ARITHMETICAL_OP:
                    stack.push(mathCalc(current));
                    break;
                case LOGICAL_OP:
                    stack.push(logicCalc(current));
                    break;
                case ASSIGN_OP:
                    assign();
                    break;
                case GOTO:
                    //address = stack.pop(); // Возникает исключение
                    i = Integer.parseInt(address.getValue()) - 1;
                    break;
                case GOTO_BY_FALSE:
                    address = stack.pop();
                    Token condition = stack.pop();

                    if (condition.getType().equals(Lexeme.LOGIC_VALUE) && condition.getValue().equals("false")) {
                        i = Integer.parseInt(address.getValue()) - 1;
                    }
                    break;
                case PRINT_KW:
                    print();
                    break;
            }
        }
    }
}
