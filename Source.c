#include <stdio.h>
#include "Header.h"
//enum tag { IF, NUM, APP, BOOL, PRIM, KRET, KIF, KAPP, CHECKED, UNCHECKED };

expr* make_if(expr* c, expr* t, expr* f) {
	JIf* o = malloc(sizeof(JIf));
	o->h.t = IF;
	o->c = c;
	o->t = t;
	o->f = f;
	return o;
}

expr* make_num(int n) {
	JNum* o = malloc(sizeof(JNum));
	o->h.t = NUM;
	o->n = n;
	return o;
}

expr* make_app(expr* fun, expr* arg1, expr* arg2) {
	JApp* o = malloc(sizeof(JApp));
	o->h.t = APP;
	o->fun = fun;
	o->arg1 = arg1;
	o->arg2 = arg2;
	return o;
}

expr* make_bool(int v) {
	JBool* o = malloc(sizeof(JBool));
	o->h.t = BOOL;
	o->val = v;
	return o;
}

expr* make_prim(char* p) {
	prim* o = malloc(sizeof(prim));
	o->h.t = PRIM;
	o->p = p;
	return o;
}

expr* make_kret() {
	KRet* o = malloc(sizeof(KRet));
	o->h.t = KRET;
	return o;
}

expr* make_kif(expr* t, expr* f, expr* k) {
	KIf* o = malloc(sizeof(KIf));
	o->h.t = KIF;
	o->t = t;
	o->f = f;
	o->k = k;
	return o;
}

expr* make_kif(expr* fun, expr* c, expr* u, expr* k) {
	KApp* o = malloc(sizeof(KApp));
	o->h.t = KAPP;
	o->fun = fun;
	o->checked = c;
	o->unchecked = u;
	o->k = k;
	return o;
}

expr* make_checked(expr* list) {
	KChecked* o = malloc(sizeof(KChecked));
	o->h.t = CHECKED;
	o->list = list;
	return o;
}

expr* make_unchecked(expr* list) {
	KUnchecked* o = malloc(sizeof(KUnchecked));
	o->h.t = UNCHECKED;
	o->list = list;
	return o;
}

// Main
int main(int argc, char* argv[]) {
	printf("Hello!\n");
}