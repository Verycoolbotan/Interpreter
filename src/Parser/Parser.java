package Parser;

import Lexer.Lexeme;
import Lexer.Token;

import java.util.LinkedList;

public class Parser {
    private LinkedList<Token> lt;
    private int currPos;
    private boolean endOfList;

    public Parser(LinkedList<Token> lt) {
        this.lt = lt;
        this.currPos = 0;
        this.endOfList = false;
    }

    private void terminal(Lexeme l) throws ParseException {
        try {
            Token token = lt.get(currPos);
            if (token.getType() == l) {
                currPos++;
            } else {
                throw new ParseException(l, token);
            }
        } catch (IndexOutOfBoundsException e) {
            endOfList = true;
            throw new ParseException("Parser message: Success");
        }
    }

    public void lang() throws ParseException{
        try {
            while (!endOfList) {
                expr();
            }
        } catch (Exception e) {
            if (!endOfList) throw e;
            //e.printStackTrace();
        }
    }

    private void expr() throws ParseException {
        try {
            ifExpr();
            return;
        } catch (ParseException e) {
            if (!(e.getToken() == null)) {
                switch (e.getType()) {
                    case L_BR:
                    case R_BR:
                    case L_CB:
                    case R_CB:
                    case VAR:
                    case NUM:
                    case LOGICAL_OP:
                        throw e;
                }
            } else if (!(e.getMessage() == null)) {
                throw e;
            }
        }

        try {
            whileExpr();
            return;
        } catch (ParseException e) {
            //TODO: нужно что-то делать с дублированием кода
            if (!(e.getToken() == null)) {
                switch (e.getType()) {
                    case L_BR:
                    case R_BR:
                    case L_CB:
                    case R_CB:
                    case VAR:
                    case NUM:
                    case LOGICAL_OP:
                        throw e;
                }
            } else if (!(e.getMessage() == null)) {
                throw e;
            }
        }

        try {
            assignExpr();
            return;
        } catch (ParseException e) {
            if (!(e.getToken() == null)) {
                //TODO: переписать условие
                if ((e.getToken().getType() == Lexeme.PRINT_KW) && (e.getToken().getPos() == 1)) {
                    //out.println("Debug: print caught");
                } else {
                    switch (e.getType()) {
                        case ASSIGN_OP:
                        case L_BR:
                        case R_BR:
                        case VAR:
                        case NUM:
                        case ARITHMETICAL_OP:
                            throw e;
                    }
                }

            } else if (!(e.getMessage() == null)) {
                throw e;
            }
        }

        try {
            printExpr();
            return;
        } catch (ParseException e) {
            if (!(e.getToken() == null)) {
                switch (e.getType()) {
                    case L_BR:
                    case R_BR:
                    case VAR:
                    case NUM:
                        throw e;
                }
            } else if (!(e.getMessage() == null)) {
                throw e;
            }
        }

        throw new ParseException("No expressions found");
    }

    private void ifExpr() throws ParseException {
        ifKW();
        exprCondition();
        body();
    }

    private void whileExpr() throws ParseException {
        whileKW();
        exprCondition();
        body();
    }

    private void assignExpr() throws ParseException {
        var();
        assign();
        value();
        while (true) {
            try {
                arOP();
                value();
            } catch (Exception e) {
                break;
            }
        }
        endOfLine();
    }

    private void printExpr() throws ParseException {
        printKW();
        lBR();
        value();
        rBR();
        endOfLine();
    }

    private void exprCondition() throws ParseException {
        lBR();
        condition();
        rBR();
    }

    private void condition() throws ParseException {
        value();
        while (true) {
            try {
                logOP();
                value();
            } catch (Exception e) {
                break;
            }
        }
    }

    private void body() throws ParseException {
        lCB();
        try {
            while (!endOfList) {
                expr();
            }
        } catch (Exception e) {
            //out.println(e.getMessage());
        }
        rCB();
    }

    private void value() throws ParseException {
        try {
            var();
            return;
        } catch (ParseException e) {
            //out.println(e.getMessage());
        }

        try {
            NUM();
            return;
        } catch (ParseException e) {
            //out.println(e.getMessage());
        }

        try {
            br_expr();
            return;
        } catch (ParseException e) {
            //out.println(e.getMessage());
        }

        throw new ParseException("Variable, NUM or expression expected");
    }

    private void br_expr() throws ParseException {
        lBR();
        value();
        while (true) {
            try {
                arOP();
                value();
            } catch (Exception e) {
                break;
            }
        }
        rBR();
    }

    //Обёртки для обработки терминалов
    private void ifKW() throws ParseException {
        terminal(Lexeme.IF_KW);
    }

    private void whileKW() throws ParseException {
        terminal(Lexeme.WHILE_KW);
    }

    private void printKW() throws ParseException {
        terminal(Lexeme.PRINT_KW);
    }

    private void var() throws ParseException {
        terminal(Lexeme.VAR);
    }

    private void NUM() throws ParseException {
        terminal(Lexeme.NUM);
    }

    private void assign() throws ParseException {
        terminal(Lexeme.ASSIGN_OP);
    }

    private void arOP() throws ParseException {
        terminal(Lexeme.ARITHMETICAL_OP);
    }

    private void logOP() throws ParseException {
        terminal(Lexeme.LOGICAL_OP);
    }

    private void lBR() throws ParseException {
        terminal(Lexeme.L_BR);
    }

    private void rBR() throws ParseException {
        terminal(Lexeme.R_BR);
    }

    private void lCB() throws ParseException {
        terminal(Lexeme.L_CB);
    }

    private void rCB() throws ParseException {
        terminal(Lexeme.R_CB);
    }

    private void endOfLine() throws ParseException {
        terminal(Lexeme.END_OF_LINE);
    }
}
