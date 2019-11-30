#pragma once
#include <string.h>

enum tag { IF, NUM, APP, BOOL, PRIM, KRET, KIF, KAPP, CHECKED, UNCHECKED, LAMBDA, VAR, JDEF, ENV, CLOS};

typedef struct { enum tag t; }expr;

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
	int		val;
}JBool;

typedef struct {
	expr	h;
	char*	p;
}prim;

typedef struct {
	expr	h;
	char*	Name;
	expr*	params;
}lambda;

typedef struct {
	expr	h;
	char*	name;
}var;

typedef struct {
	expr	h;
	expr*	fun;
	expr*	e;
}JDef;

typedef struct{
	expr	h;
}KRet;

typedef struct {
	expr	h;
	expr*	env;
	expr*	t;
	expr*	f;
	expr*	k;
}KIf;

typedef struct {
	expr	h;
	expr*	fun;
	expr*	checked;
	expr*	env;
	expr*	unchecked;
	expr*	k;
}KApp;

typedef struct {
	expr	h;
	expr*	data;
	expr*	next;
}KChecked;

typedef struct {
	expr	h;
	expr*	data;
	expr*	next;
}KUnchecked;

typedef struct {
	expr	h;
	var*	v;
	expr*	val;
	expr*	next;
}JEnv;

typedef struct {
	expr	h;
	expr*	lambda;
	expr*	env;
}Closure;

typedef struct M{
	JDef* def;
	struct M* next;
}Map;