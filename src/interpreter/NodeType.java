package interpreter;

public enum NodeType {
	PROGRAM,
	STMT_LIST,
	STMT,
	SUBST,
	FOR_STMT,
	ASSIGN_STMT,
	BLOCK,
	IF_BLOCK,
	LOOP_BLOCK,
	COND,
    EXPR_LIST,
	EXPR,
	FUNCTION_CALL,
	VAR,
	FACT,
    STRING_CONSTANT,
    INT_CONSTANT,
    DOUBLE_CONSTANT,
    BOOL_CONSTANT,
    END,
}
