package StackMachine;

import java.util.LinkedList;
import java.util.Stack;

import Lexer.Token;
import Lexer.Lexeme;

public class Machine {
    //private static ArrayList<Token> lt;
    private static Stack<Token> stack = new Stack<>();

    private static Token mathCalc(Token operator) throws MachineException {
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
        //TODO: а если value - переменная?
        Token value = stack.pop();
        Token var = stack.pop();
        Table.assign(var.getValue(), Integer.parseInt(value.getValue()));
    }

    private static void print() throws MachineException {
        Token token = stack.pop();
        int arg = token.getType().equals(Lexeme.VAR) ? Table.get(token.getValue()) : Integer.parseInt(token.getValue());
        //TODO: передать в конструктор PrintStream?
        System.out.println(arg);
    }

    public static void run(LinkedList<Token> lt) throws MachineException {
        Token address;

        for (int i = 0; i < lt.size(); i++) {
            Token current = lt.get(i);
            switch (current.getType()) {
                case VAR:
                case NUM:
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
                case START:
                case END:
                    stack.push(current);
                    break;
                case GOTO:
                    address = stack.pop();
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
