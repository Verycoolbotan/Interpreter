package Parser;

import Lexer.Lexeme;
import Lexer.Token;

public class ParseException extends Exception {
    private Token token;
    private Lexeme type;

    public ParseException() {
        this.token = null;
        this.type = null;
    }

    public ParseException(String msg) {
        super(msg);
        this.token = null;
        this.type = null;
    }

    public ParseException(Lexeme type, Token token) {
        super(type + " expected at " + token.getLine() + ":" + token.getPos() + ", got '" + token.getValue() + "'");
        this.token = token;
        this.type = type;
    }

    public Token getToken() {
        return token;
    }

    public Lexeme getType() {
        return type;
    }
}
