#include "Source.c";
#include <stdio.h>;
int main(int argc, char* argv[]) {
expr* o = make_app(make_prim(+), make_app(make_prim(*), make_num(2), make_num(4)), make_num(8));
return 0;
}