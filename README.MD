# Interpreter
Интерпретатор C-подобного языка, определённого нижеописанной грамматикой:

```
lang → expr*
expr → if|while|assign|print
if → IF_KW if_cond if_body
IF_KW → if
if_cond → L_B cond R_B
cond → value LOGICAL_OP value
value → VAR|NUM|br_expression
br_expression → L_B value (ARITMETICAL_OP value)* R_B
VAR → [A-Za-z_]+
NUM → 0|([1-9][0-9]*)
L_B → (
R_B → )
LOGICAL_OP → >|<|>=|<=|==|!=
ARITHMETICAL_OP → +|-|*|/
if_body → L_CB expr* R_CB
L_CB → {
R_CB → }
while → WHILE_KW while_cond while_body
WHILE_KW → while
while_cond → if_cond
while_body → if_body
assign → VAR ASSIGN_OP value (ARITMETICAL_OP value)*
ASSIGN_OP → =
print → PRINT_KW print_expr
PRINT_KW → print
print_expr → L_B value R_B
```
Осталось реализовать:
* ~~Лексический анализатор~~
* ~~Парсер~~
* ~~Обратная польская запись и вычисления на стеке~~
* LinkedList
* HashSet