#pragma once

enum tag { IF, NUM, APP, BOOL, PRIM, KRET, KIF, KAPP, CHECKED, UNCHECKED };

typedef struct{ enum tag t}expr;

typedef struct {
	expr	h;
	expr*	c;
	expr*	t;
	expr*	f;
}JIf;

typedef struct {
	expr	h;
	int		n;
}JNum;

typedef struct {
	expr	h;
	expr*	fun;
	expr*	arg1;
	expr*	arg2;
}JApp;

typedef struct {
	expr	h;
	int		val
}JBool;

typedef struct {
	expr	h;
	char*	p;
}prim;

typedef struct{
	expr	h;
}KRet;

typedef struct {
	expr	h;
	expr*	t;
	expr*	f;
	expr*	k;
}KIf;

typedef struct {
	expr	h;
	expr*	fun;
	expr*	checked;
	expr*	unchecked;
	expr*	k;
}KApp;

typedef struct {
	expr	h;
	expr*	list;
}KChecked;

typedef struct {
	expr	h;
	expr*	list;
}KUnchecked;