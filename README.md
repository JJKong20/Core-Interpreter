# Core-Interpreter

Built a scanner, parser, and interpreter for the Core language. The interpreter contains 3 regions of memory: static, stack, and heap, which handle variables and function calls. The interpreter also has a garbage collector implementation which identifies correct points to perform a garbage collection.

## Core Language Grammar
\<prog\> ::= program { \<decl-seq\> begin { \<stmt-seq\> } } | program { begin { \<stmt-seq\> } }

\<decl-seq\> ::= \<decl\> | \<decl\>\<decl-seq\> | \<func-decl\> | \<func-decl\>\<decl-seq\>

\<stmt-seq\> ::= \<stmt\> | \<stmt\>\<stmt-seq\> 

\<decl\> ::= \<decl-int\> | \<decl-ref\> 

\<decl-int\> ::= int \<id-list\> ; 

\<decl-ref\> ::= ref \<id-list\> ;

\<id-list\> ::= id | id , \<id-list\> 

\<func-decl\> ::= id ( ref \<formals\> ) { \<stmt-seq\> } 

\<formals\> ::= id | id , \<formals\>

\<stmt\> ::= \<assign\> | \<if\> | \<loop\> | \<out\> | \<decl\> | \<func-call\>

\<func-call\> ::= begin id ( \<formals\> ) ; 

\<assign\> ::= id = \<expr\> ; | id = new instance; | id = share id ;

\<out\> ::= output ( \<expr\> ) ;

\<if\> ::= if \<cond\> then { \<stmt-seq\> } 
 | if \<cond\> then { \<stmt-seq\> } else { \<stmt-seq\> }
 
\<loop\> ::= while \<cond\> { \<stmt-seq\> }

\<cond\> ::= \<cmpr\> | ! ( \<cond\> )
 | \<cmpr\> or \<cond\> 
 
\<cmpr\> ::= \<expr\> == \<expr\> | \<expr\> \< \<expr\> 
 | \<expr\> \<= \<expr\> 
 
\<expr\> ::= \<term\> | \<term\> + \<expr\> | \<term\> – \<expr\> 

\<term\> ::= \<factor\> | \<factor\> * \<term\> 

\<factor\> ::= id | const | ( \<expr\> ) | input ( )
