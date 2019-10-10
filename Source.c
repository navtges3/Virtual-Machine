#include <stdio.h>
#include "Header.h"

Map* map = NULL;

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

expr* make_kif(expr* env, expr* t, expr* f, expr* k) {
	printf("__make kif\n");
	KIf* o = malloc(sizeof(KIf));
	o->h.t = KIF;
	o->env = env;
	o->t = t;
	o->f = f;
	o->k = k;
	return o;
}

expr* make_kapp(expr* fun, expr* c, expr* env, expr* u, expr* k) {
	printf("__make kapp\n");
	KApp* o = malloc(sizeof(KApp));
	o->h.t = KAPP;
	o->fun = fun;
	o->checked = c;
	o->env = env;
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

expr* make_fun(char* Name, expr* params) {
	printf("__make fun\n");
	Fun* o = malloc(sizeof(Fun));
	o->h.t = FUN;
	o->Name = Name;
	o->params = params;
	return o;
}

expr* make_var(char* n) {
	printf("__make var of %s\n", n);
	var* o = malloc(sizeof(var));
	o->h.t = VAR;
	o->name = n;
	return o;
}

void mapPush(expr* def) {
	Map* temp = malloc(sizeof(Map*));
	temp->def = def;
	temp->next = map;
	map = temp;
}

expr* inMap(expr* f) {
	Fun* fun = (Fun*)f;
	Map* temp = map;
	while (temp != NULL) {
		if (strcmp(((Fun*)temp->def->fun)->Name, fun->Name) == 0) {
			return temp->def;
		}
		else
			temp = temp->next;
	}
	return NULL;
}

expr* make_jdef(expr* fun, expr* e) {
	printf("__make jdef\n");
	if (inMap(fun)) {
		return NULL;
	}
	JDef* o = malloc(sizeof(JDef));
	o->h.t = JDEF;
	o->fun = fun;
	o->e = e;
	mapPush(o);
	return o;
}

expr* make_jenv(expr* v, expr* val, expr* next) {
	printf("__make jenv\n");
	printf("____var tag = %d\n", v->t);
	JEnv* o = malloc(sizeof(JEnv));
	o->h.t = ENV;
	o->v = v;
	o->val = val;
	o->next = next;
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

expr* subst(expr* e, expr* x, expr* v) {
	switch (e->t) {
	// stuff happens
	case IF: {
		JIf* temp = (JIf*)e;
		return make_if(subst(temp->c, x, v), subst(temp->t, x, v), subst(temp->f, x, v));
		break;
	}
	case APP: {
		JApp* temp = (JApp*)e;
		return make_app(subst(temp->fun, x, v), subst(temp->arg1, x, v), subst(temp->arg2, x, v));
		break;
	}
	case VAR: {
		if (e == x)
			return v;
		else
			return e;
		break;
	}

	// values
	case NUM:
	case BOOL:
	case PRIM:
	case FUN:
		return e;
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
	expr *env = NULL;

	while (1) {
		printf("TAG: %d\n", (*e)->t);
		switch ((*e)->t) {
		case IF: {
			printf("@: IF\n");
			JIf *temp = (JIf*)(*e);
			(*e) = temp->c;
			ok = make_kif(env, temp->t, temp->f, ok);
			break;
		}
		case APP: {
			printf("@: APP\n");
			JApp* temp = (JApp*)(*e);
			printf("TEMP: %d, %d, %d\n", temp->fun->t, temp->arg1->t, temp->arg2->t);
			(*e) = temp->fun;
			ok = make_kapp(NULL, NULL, env, make_unchecked(temp->arg1, make_unchecked(temp->arg2, NULL)), ok);
			break;
		}
		case FUN: {
			printf("@: FUN\n");
			Fun* temp = (Fun*)(*e);
			expr* def = inMap(temp);
			if (def != NULL) {
				expr* defexpr = ((JDef*)def)->e;
				expr* pnode = ((Fun*)((JDef*)def)->fun)->params;
				expr* cnode = temp->params;
				expr* envir = env;

				while (pnode != NULL && cnode != NULL) {
					envir = make_jenv(((KChecked*)pnode)->data, ((KChecked*)cnode)->data, envir);
					pnode = ((KChecked*)pnode)->next;
					cnode = ((KChecked*)cnode)->next;
				}
				*e = defexpr;
				env = envir;
			}
			break;
		}

		case VAR: {
			printf("@: VAR\n");
			var* temp = (var*)(*e);
			JEnv* nav = env;
			printf("^^^ nav->t = %d\n", nav->v->h.t);
			while (nav != NULL) {
				printf("def: %s\n", nav->v->name);
				printf("temp: %s\n", temp->name);
				if (strcmp(((var*)nav->v)->name, temp->name) == 0) {
					(*e) = nav->val;
					nav = NULL;
				}
				else
					nav = ((JEnv*)nav)->next;
			}

			break;
		}

		case NUM:
		case BOOL:
		case PRIM: {
			printf("@: VALUE of %d\n", (*e)->t);
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
					env = tempK->env;
					ok = tempK->k;
					break;
				}
				else {
					printf("\tRemoving one from unchecked\n");
					KUnchecked* uc = tempK->unchecked;
					(*e) = uc->data;
					uc = uc->next;
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
				env = tempK->env;
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
	make_jdef(make_fun("My fun", make_checked(make_var("x1"), make_checked(make_var("x2"), NULL))), make_app(make_prim("*"), make_var("x1"), make_var("x2")));
	make_jdef(make_fun("Double", make_checked(make_var("x"), NULL)), make_app(make_prim("+"), make_var("x"), make_var("x1")));

	expr* e = make_fun("My fun", make_checked(make_num(3), make_checked(make_fun("Double", make_checked(make_num(2), NULL)), NULL)));
	eval(&e);
	JNum* num = (JNum*)e;
	printf("Result is %d\n", num->n);
}