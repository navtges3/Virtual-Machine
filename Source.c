#include <stdio.h>

typedef enum{TRUE, FALSE}bool;
typedef enum{plus, mult, div, sub,
lessEqu, less, great, greatEqu}prim;

typedef struct {
	int		type;
	int		n;
	bool	b;
	prim	p;
}val;

typedef struct {
	e* list;
}JApp;

typedef struct {
	e cond;
	e t;
	e f;
}JIf;

typedef struct {
	int type;
	val		v;
	JApp	a;
	JIf		i;
}e;

int main(int argc, char* argv[]) {
	printf("Hello!\n");
}