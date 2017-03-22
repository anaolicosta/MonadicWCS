:- dynamic clause_g/1.

writegroundprogram(String) :-
	all_cons(All),
	ground_clauses(All, String).
	

ground_clauses(Atoms, String) :-
	retractall( clause_g(_) ),
        open(String,write,Out),
	( clause(C),
	  % writeq(first(C)), nl,
	  term_variables(C, Vs),
	  instantiate_vs(Vs, Atoms),
	  % writeq(C), nl,
          assert( clause_g(C) ),
	  write(Out, clause_g(C)),
	  write(Out, '.\n'),
	  fail
	; true
	),
    close(Out).

instantiate_vs([], _).
instantiate_vs([X|Xs], Atoms) :-
	member(X, Atoms),
	instantiate_vs(Xs, Atoms).


all_cons(All) :- 
	current_predicate(clause/1), 
	setof(List, get_constants([List]), All) *-> true; 
	All = [aux_c].


get_constants(List) :-
	clause(H :- _),
	H =..[_|Atoms],
	member(A, Atoms),
	A \== 2,
	A \== 1,
	setof(A, atomic(A), List).	

get_constants(List) :-
	clause(H :- _),
	H =..[_|[Atoms]],
	Atoms \== 2,
	Atoms \== 1,
	setof(Atoms, atomic(Atoms), List).	
    

get_constants(List) :-
	clause(_ :- Body),
	member(B, Body),
	B =..[_|Atoms], 
	member(A, Atoms),
	A \== 2,
	A \== 1,
	setof(A, atomic(A), List).

