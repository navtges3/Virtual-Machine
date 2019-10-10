#include <stdio.h>
#include "Header.h"

expr* make_if(expr* c, expr* t, expr* f) {
	printf("__make JIF\n");
	JIf* o = malloc(sizeof(JIf));
	o->h.t = IF;
	o->c = c;
	o->t = t;
	o->f = f;
	return o;
}

expr* make_num(int n) {
	printf("__make JNum %d\n", n);
	JNum* o = malloc(sizeof(JNum));
	o->h.t = NUM;
	o->n = n;
	return o;
}

expr* make_app(expr* fun, expr* arg1, expr* arg2) {
	printf("__make JApp\n");
	JApp* o = malloc(sizeof(JApp));
	o->h.t = APP;
	o->fun = fun;
	o->arg1 = arg1;
	o->arg2 = arg2;
	return o;
}

expr* make_bool(int v) {
	printf("__make Bool\n");
	JBool* o = malloc(sizeof(JBool));
	o->h.t = BOOL;
	o->val = v;
	return o;
}

expr* make_prim(char* p) {
	printf("__make prim %s\n", p);
	prim* o = malloc(sizeof(prim));
	o->h.t = PRIM;
	o->p = p;
	return o;
}

expr* make_kret() {
	printf("__make kret\n");
	KRet* o = malloc(sizeof(KRet));
	o->h.t = KRET;
	return o;
}

expr* make_kif(expr* t, expr* f, expr* k) {
	printf("__make kif\n");
	KIf* o = malloc(sizeof(KIf));
	o->h.t = KIF;
	o->t = t;
	o->f = f;
	o->k = k;
	return o;
}

expr* make_kapp(expr* fun, expr* c, expr* u, expr* k) {
	printf("__make kapp\n");
	KApp* o = malloc(sizeof(KApp));
	o->h.t = KAPP;
	o->fun = fun;
	o->checked = c;
	o->unchecked = u;
	o->k = k;
	return o;
}

expr* make_checked(expr* data, expr* next) {
	printf("__make checked\n");
	KChecked* o = malloc(sizeof(KChecked));
	o->h.t = CHECKED;
	o->data = data;
	o->next = next;
	return o;
}

expr* make_unchecked(expr* data, expr* next) {
	printf("__make unchecked\n");
	KUnchecked* o = malloc(sizeof(KUnchecked));
	o->h.t = UNCHECKED;
	o->data = data;
	o->next = next;
	return o;
}

expr* make_fun(char* Name) {
	printf("__make fun\n");
	fun* o = malloc(sizeof(fun));
	o->h.t = FUN;
	o->Name = Name;
	return o;
}

expr* make_var(char* name) {
	printf("__make var\n");
	var* o = malloc(sizeof(var));
	o->h.t = FUN;
	o->name = name;
	return o;
}

expr* make_jdef(expr* fun, expr* params, expr* e) {
	printf("__make jdef\n");
	JDef* o = malloc(sizeof(JDef));
	o->h.t = JDEF;
	o->fun = fun;
	o->params = params;
	o->e = e;
	return o;
}

//enum tag { IF, NUM, APP, BOOL, PRIM, KRET, KIF, KAPP, CHECKED, UNCHECKED };
int boolVal(expr* e) {
	switch (e->t) {
	case NUM: {
		JNum* temp = (JNum*)e;
		return temp->n;
	}
	case BOOL: {
		JBool* temp = (JBool*)e;
		return temp->val;
	}
	case PRIM:
	default:
		return 0;
	}
}

expr* delta(expr* fun, expr* checked) {
	prim* funp = (prim*)fun;
	KChecked* cp = (KChecked*)checked;
	KChecked* cp2 = (KChecked*)(cp->next);

	JNum* arg1 = cp->data;
	JNum* arg2 = cp2->data;

	char* p = funp->p;
	int lhs = arg1->n;
	int rhs = arg2->n;

	if (!strcmp(p, "+")) { return make_num(lhs + rhs); }
	if (!strcmp(p, "*")) { return make_num(lhs * rhs); }
	if (!strcmp(p, "/")) { return make_num(lhs / rhs); }
	if (!strcmp(p, "-")) { return make_num(lhs - rhs); }
	if (!strcmp(p, "<")) { return make_bool(lhs < rhs); }
	if (!strcmp(p, "<=")) { return make_bool(lhs <= rhs); }
	if (!strcmp(p, "==")) { return make_bool(lhs == rhs); }
	if (!strcmp(p, ">")) { return make_bool(lhs > rhs); }
	if (!strcmp(p, ">=")) { return make_bool(lhs >= rhs); }
	if (!strcmp(p, "!=")) { return make_bool(lhs != rhs); }

	return make_num(6969);
}

void eval(expr** e) {
	expr *ok = make_kret();

	while (1) {
		switch ((*e)->t) {
		case IF: {
			printf("@: IF\n");
			JIf *temp = (JIf*)(*e);
			(*e) = temp->c;
			ok = make_kif(temp->t, temp->f, ok);
			break;
		}
		case APP: {
			printf("@: APP\n");
			JApp* temp = (JApp*)(*e);
			(*e) = temp->fun;
			ok = make_kapp(NULL, NULL, make_unchecked(temp->arg1, make_unchecked(temp->arg2, NULL)), ok);
			break;
		}
		case NUM:
		case BOOL:
		case FUN:
		case PRIM: {
			printf("@: VALUE\n");
			switch (ok->t) {
			case KRET: {
				printf("@: KRET\n");
				printf("e's value is %d\n", ((JNum*)(*e))->n);
				return;
			}
			case KAPP: {
				printf("@: KAPP\n");
				KApp* tempK = (KApp*)ok;
				expr* funp = tempK->fun;
				expr* checked = tempK->checked;
				if (!funp) {
					printf("\t!funp\n");
					funp = (*e);
					tempK->fun = funp;
				}
				else {
					printf("\tadd to checked\n");
					checked = make_checked((*e), checked);
					tempK->checked = checked;
				}
				if (tempK->unchecked == NULL) {
					printf("\tUnchecked is empty\n");
					printf("\ttempK->fun = %s\n", ((prim*)tempK->fun)->p);
					(*e) = delta(tempK->fun, tempK->checked);
					ok = tempK->k;
					break;
				}
				else {
					printf("\tRemoving one from unchecked\n");
					KUnchecked* uc = tempK->unchecked;
					(*e) = uc->data;
`					uc = uc->next;
					tempK->unchecked = uc;
					ok = tempK;
					break;
				}
				break;
			}
			case KIF: {
				printf("@: KIF\n");
				KIf* tempK = (KIf*)ok;
				(*e) = boolVal((*e)) ? tempK->t : tempK->f;
				ok = tempK->k;
				break;
			}
			}
		}
		}
	}
}

// Main
int main(int argc, char* argv[]) {
	expr* e = make_app(make_prim("+"), make_app(make_prim("*"), make_num(2), make_num(2)), make_num(5));
	eval(&e);
	JNum* num = (JNum*)e;
	printf("Result of + (* 2 2) 5 is %d\n", num->n);
}