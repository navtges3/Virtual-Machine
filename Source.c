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

expr* make_lambda(char* Name, expr* params) {
	printf("__make fun\n");
	lambda* o = malloc(sizeof(lambda));
	o->h.t = LAMBDA;
	o->Name = Name;
	o->params = params;
	return o;
}

expr* make_closure(expr* lambda, expr* env) {
	printf("__make closure\n");
	Closure* o = malloc(sizeof(Closure));
	o->h.t = CLOS;
	o->lambda = lambda;
	o->env = env;
}

expr* make_var(char* n) {
	printf("__make var of %s\n", n);
	var* o = malloc(sizeof(var));
	o->h.t = VAR;
	o->name = n;
	return o;
}

expr* make_unit() {
	printf("__make unit\n");
	unit* o = malloc(sizeof(unit));
	o->h.t = UNIT;
	return o;
}

expr* make_pair(expr* l, expr* r) {
	printf("__make pair\n");
	pair* o = malloc(sizeof(pair));
	o->h.t = PAIR;
	o->l = l;
	o->r = r;
	return o;
}

expr* make_inl(expr* e) {
	printf("__make inl\n");
	inl* o = malloc(sizeof(inl));
	o->h.t = INL;
	o->v = e;
	return o;
}

expr* make_inr(expr* e) {
	printf("__make inl\n");
	inr* o = malloc(sizeof(inr));
	o->h.t = INR;
	o->v = e;
	return o;
}

expr* make_case(expr* e, expr* l, expr* le, expr* r, expr* re) {
	printf("__make case\n");
	JCase* o = malloc(sizeof(JCase));
	o->h.t = CASE;
	o->e = e;
	o->l = l;
	o->le = le;
	o->r = r;
	o->re = re;
	return o;
}

expr* make_kcase(expr* l, expr* le, expr* r, expr* re, expr* env, expr* k) {
	printf("__make case\n");
	KCase* o = malloc(sizeof(KCase));
	o->h.t = CASE;
	o->l = l;
	o->le = le;
	o->r = r;
	o->re = re;
	o->env = env;
	o->k = k;
	return o;
}

void mapPush(expr* def) {
	Map* temp = malloc(sizeof(Map));
	temp->def = def;
	temp->next = map;
	map = temp;
}

expr* inMap(expr* f) {
	lambda* fun = (lambda*)f;
	Map* temp = map;
	while (temp != NULL) {
		if (strcmp(((lambda*)temp->def->fun)->Name, fun->Name) == 0) {
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
	case LAMBDA:
		return e;
	}
}

expr* delta(expr* fun, expr* checked) {
	prim* funp = (prim*)fun;
	KChecked* cp = (KChecked*)checked;
	KChecked* cp2 = (KChecked*)(cp->next);

	char* p = funp->p;

	if (cp->data->t == NUM && cp2->data->t == NUM) {
		JNum* arg1 = cp->data;
		JNum* arg2 = cp2->data;
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
	}
	else {
		if (!strcmp(p, "pair")) { return make_pair(cp->data, cp2->data); }
		if (!strcmp(p, "inl")) { return make_inl(cp->data); }
		if (!strcmp(p, "inr")) { return make_inr(cp2->data); }
		if (!strcmp(p, "fst")) { return cp->data; }
		if (!strcmp(p, "snd")) { return cp2->data; }

	}

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
		case LAMBDA: {
			printf("@: LAMBDA\n");
			((JEnv*)env) = make_jenv(((lambda*)e)->Name, NULL, env);
			e = make_closure(e, env);
			((JEnv*)env)->val = e;
			env = NULL;
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
		case CASE: {
			printf("@: CASE\n");
			JCase* temp = (JCase*)(*e);
			ok = make_kcase(temp->l, temp->le, temp->r, temp->re, env, ok);
			*e = temp->e;
			break;
		}
		case CLOS:
		case NUM:
		case BOOL:
		case INL:
		case INR:
		case PRIM: {
			printf("@: VALUE of %d\n", (*e)->t);
			switch (ok->t) {
			case KRET: {
				printf("@: KRET\n");
				printf("e's value is %d\n", ((JNum*)(*e))->n);
				return;
			}
			case KCASE: {
				KCase* tempK = (KCase*)ok;
				if ((*e)->t == INL)
					*e = tempK->le;
				else
					*e = tempK->re;

				env = tempK->env;
				ok = tempK->k;
				break;
			}
			case KAPP: {
				printf("@: KAPP\n");
				KApp* tempK = (KApp*)ok;
				expr* funp = tempK->fun;
				expr* checked = tempK->checked;

				if (((KChecked*)checked)->data->t == CLOS) {
					expr* checkedParams = NULL;
					expr* uncheckedParams = ((lambda*)((Closure*)((KChecked*)checked)->data)->lambda)->params;

					while (uncheckedParams != NULL) {
						if (((KChecked*)uncheckedParams)->data->t == VAR) {
							JEnv* thisEnv = ((JEnv*)((Closure*)((KChecked*)checked)->data)->env);
							while (thisEnv != NULL) {
								if (((var*)((KChecked*)uncheckedParams)->data)->name == thisEnv->v->name) {
									((KChecked*)uncheckedParams)->data = thisEnv->val;
									thisEnv = NULL;
								}
								else
									thisEnv = thisEnv->next;
							}
						}
						if (checkedParams == NULL) {
							checkedParams = make_checked(((KChecked*)uncheckedParams)->data, NULL);
						}
						else {
							expr* temp = checkedParams;
							while (((KChecked*)temp)->next != NULL) {
								temp = ((KChecked*)temp)->next;
							}
							((KChecked*)temp)->next = make_checked(((KChecked*)uncheckedParams)->data, NULL);
						}
						uncheckedParams = ((KChecked*)uncheckedParams)->next;
					}
					((lambda*)((Closure*)((KChecked*)checked)->data)->lambda)->params = checkedParams;
				}

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
	
}