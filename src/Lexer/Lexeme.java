package Lexer;

import java.util.regex.Pattern;

public enum Lexeme {
    //Значения расположены по невозрастанию приоритета
    IF_KW(Pattern.compile("if")),
    WHILE_KW(Pattern.compile("while")),
    PRINT_KW(Pattern.compile("print")),
    L_BR(Pattern.compile("\\(")),
    VAR(Pattern.compile("[A-Za-z_]+")),
    R_BR(Pattern.compile("\\)")),
    LOGICAL_OP(Pattern.compile(">|<|>=|<=|==|!=")),
    ARITHMETICAL_OP(Pattern.compile("\\+|-|\\*|/|;")), // добавил ";"
    ASSIGN_OP(Pattern.compile("=")),
    NUM(Pattern.compile("-?(0|([1-9][0-9]*))")),
    L_CB(Pattern.compile("\\{")),
    R_CB(Pattern.compile("\\}")),

    //Служебные типы для стек-машины
    GOTO_BY_FALSE(null), //Переход по невыполнению условия в if и while
    GOTO(null),
    START(null), //Метка начала
    END(null), //Метка конца
    LOGIC_VALUE(null);

    private final Pattern pattern;

    Lexeme(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
