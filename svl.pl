% :- dynamic clause_g/1.

:- multifile
      clause/1.

:- multifile
      iPosTemp/1.

:- multifile
      iNegTemp/1.

%:-[input].
%:-[gp].
%:-[i].%temporal: input interpretation for 1 iteration
%%%%% %%%%%%%% %%%%%%%%  %%%%%%%%
%       EXTERNAL CALL
%%%%% %%%%%%%% %%%%%%%%  %%%%%%%%
writeleastmodel(String):-
    open(String,write,Out),
    compute_fp(New_I_pos, New_I_neg),
   % write(Out,'positive I: '),
    write(Out, New_I_pos),
    write(Out, '-'),
    %write(Out,'negative I: '),
    write(Out, New_I_neg),
    close(Out).

writeleastmodel_split:-
    open('output2.txt',write,Out),
    compute_fp_split(New_I_pos, New_I_neg,Out),
   % write(Out,'positive I: '),
    write(Out, New_I_pos),
    write(Out, '-'),
    %write(Out,'negative I: '),
    write(Out, New_I_neg),
    close(Out),
    halt.

writeiterateonce:-
	open('iterate.txt',write,Out),
	iPosTemp(P),
	iNegTemp(N),
    iterateOnce(P,New_I_pos,N,New_I_neg),
   % write(Out,'positive I: '),
    write(Out, New_I_pos),
    write(Out, '-'),
    %write(Out,'negative I: '),
    write(Out, New_I_neg),
    close(Out),
    halt.

% TP
writeleastmodel_tp:-
    open('output.txt',write,Out),
    compute_fp_tp(New_I_pos, New_I_neg),
   % write(Out,'positive I: '),
    write(Out, New_I_pos),
    write(Out, '-'),
    %write(Out,'negative I: '),
    write(Out,'[]'), % New_I_neg),
    close(Out),
    halt.
writeleastmodel_split_tp:-
    open('output.txt',write,Out),
    compute_fp_split_tp(New_I_pos, New_I_neg,Out),
   %write(Out,'positive I: '),
    write(Out, New_I_pos),
    write(Out, '-'),
    % write(Out,'negative I: '),
    write(Out, '[]'), %New_I_neg),
    close(Out),
    halt.

writeiterateoncetp:-
	open('iterate.txt',write,Out),
	iPosTemp(P),
    iterateoncetp(P,I_pos,I_neg),
   % write(Out,'positive I: '),
    write(Out, I_pos),
    write(Out, '-'),
    %write(Out,'negative I: '),
    write(Out, '[]'), %I_neg),
    close(Out),
    halt.

writeabducibles:-
	open('candidates.txt',write,Out),
	get_cons_comb(P),
    write(Out, P),
    write(Out,'\n'),
    close(Out),
    halt.

%%%%% %%%%%%%% %%%%%%%%  %%%%%%%%
%       PROLOG QUERIES
%%%%% %%%%%%%% %%%%%%%%  %%%%%%%%
change_list([H|B],[H,B]).


body_is_true([t], _, _).
body_is_true(Body,I_pos, I_neg) :- members(Body,I_pos, I_neg).

members([n(X)],_,I_neg) :- member(X,I_neg).
members([X],I_pos, _) :- member(X,I_pos).
members([Head|Tail],I_pos, I_neg) :- member(Head,I_pos) , members(Tail,I_pos, I_neg).
members([n(Head)|Tail],I_pos, I_neg) :- member(Head,I_neg) , members(Tail,I_pos, I_neg).

% derivable positive facts

get_one_pos(Head, I_pos, I_neg) :- clause_g(Head:-Body), body_is_true(Body, I_pos, I_neg).

% setof(X,P,L) produce a list L of objects X that satisfy goal P.

get_all_pos(I_pos, List, I_neg) :-
	setof(Head, get_one_pos(Head, I_pos, I_neg), List) -> true;
	findall(Head, get_one_pos(Head, I_pos, I_neg), List).

% negative facts understood as disjunction, that is,
% if one element in the body is false, the body is false.

body_is_false([f], _, _).
body_is_false(B, I_neg,  _) :- member(Body, I_neg), member(Body, B).
body_is_false(B, _,  I_pos) :- member(Body, I_pos), member(n(Body), B).

% get all bodies for which H is the head of.
clauses(Head, List) :- setof(Body, clause_g(Head:- Body), List).

% derivable negative facts
% get only the negative facts that have for all clauses false

check_all_bodies([Body], I_neg,  I_pos) :- body_is_false(Body, I_neg,  I_pos).
check_all_bodies([Body|Tail], I_neg,  I_pos) :-  body_is_false(Body, I_neg,
 I_pos), check_all_bodies(Tail, I_neg,  I_pos).

get_one_neg(Head, I_neg,  I_pos) :- clauses(Head, List), check_all_bodies(List, I_neg,  I_pos).

get_all_neg(I_neg, List,  I_pos) :-
	setof(Head, get_one_neg(Head, I_neg,  I_pos), List) -> true;
	findall(Head, get_one_neg(Head, I_neg,  I_pos), List).


% the tp operator.

fixpoint_tp(I_pos, I_pos, I_neg, I_neg) :-
	get_all_pos(I_pos, I_pos, I_neg),
	get_all_neg(I_neg, I_neg, I_pos).

fixpoint_tp(I_pos, New_I_pos, I_neg, New_I_neg) :-
	get_all_pos(I_pos, P_pos, I_neg),
	get_all_neg(I_neg, P_neg, I_pos),
	fixpoint_tp(P_pos, New_I_pos, P_neg, New_I_neg).

compute_fp_tp(New_I_pos, New_I_neg) :-
	fixpoint_tp([], New_I_pos, [], New_I_neg).


% the Tp operator, splitted to write each step on the ouput file.
% ,Out provides the link to the output stream
fixpoint_split_tp(I_pos, I_pos, I_neg, I_neg,Out) :-
	get_all_pos(I_pos, I_pos, I_neg),
    get_all_neg(I_neg, I_neg, I_pos),
    write(Out,svl(I_pos,I_neg)),!.

fixpoint_split_tp(I_pos, New_I_pos, I_neg, New_I_neg,Out) :-
    get_all_pos(I_pos, P_pos, I_neg),
    get_all_neg(I_neg, P_neg, I_pos),
    write(Out, iteracion(P_pos, P_neg)),
    write(Out, '\n'),
    fixpoint_split_tp(P_pos, New_I_pos, P_neg, New_I_neg,Out).


compute_fp_split_tp(New_I_pos, New_I_neg,Out) :-
    fixpoint_split_tp([], New_I_pos, [], New_I_neg,Out).




% the svl operator.

fixpoint(I_pos, I_pos, I_neg, I_neg) :-
	get_all_pos(I_pos, I_pos, I_neg),
	get_all_neg(I_neg, I_neg, I_pos),
	assert(svl(I_pos,I_neg)),!.

fixpoint(I_pos, New_I_pos, I_neg, New_I_neg) :-
	get_all_pos(I_pos, P_pos, I_neg),
	get_all_neg(I_neg, P_neg, I_pos),
	assert(run(I_pos, P_pos, I_neg, P_neg)),
	fixpoint(P_pos, New_I_pos, P_neg, New_I_neg).


compute_fp(New_I_pos, New_I_neg) :-
	fixpoint([], New_I_pos, [], New_I_neg).

% the svl operator, splitted to write each step on the ouput file.
% ,Out provides the link to the output stream
fixpoint_split(I_pos, I_pos, I_neg, I_neg,Out) :-
	get_all_pos(I_pos, I_pos, I_neg),
	get_all_neg(I_neg, I_neg, I_pos),
	write(Out,svl(I_pos,I_neg)),!.

fixpoint_split(I_pos, New_I_pos, I_neg, New_I_neg,Out) :-
	get_all_pos(I_pos, P_pos, I_neg),
	get_all_neg(I_neg, P_neg, I_pos),
	write(Out, iteracion(P_pos, P_neg)),
	write(Out, '\n'),
	fixpoint_split(P_pos, New_I_pos, P_neg, New_I_neg,Out).


compute_fp_split(New_I_pos, New_I_neg,Out) :-
	fixpoint_split([], New_I_pos, [], New_I_neg,Out).

%NEW!!!
iterateOnce(I_pos, P_pos, I_neg, P_neg) :-
	get_all_pos(I_pos, P_pos, I_neg),
	get_all_neg(I_neg, P_neg, I_pos).
%	assert(run(I_pos, P_pos, I_neg, P_neg)).
%%% TP operator
iterateoncetp(I_pos, P_pos,I_neg) :-
	get_all_atoms(Atoms),
	subtract(Atoms,I_pos,I_neg),
	get_all_pos(I_pos, P_pos, I_neg).
	%,
	%!,true.


%


%% NEW
subtract([], _, []) :- !.
subtract([A|C], B, D) :-
        member(A, B), !,
        subtract(C, B, D).
subtract([A|B], C, [A|D]) :-
        subtract(B, C, D).
%% new

%generate set of possible explanations
%  feb -9

%generate set of possible explanations

get_all_poss_expl(AllPossExpl) :-
	get_cons_comb(ConsComb),
	setof(PossExpl, get_poss_expl(ConsComb, PossExpl), AllPossExpl).

get_poss_expl(ConsComb, PossExpl) :-
	member(Comb, ConsComb),
	setof(Fact, get_fact(Comb, Fact), PossExpl).

get_fact(PossComb, Fact) :-
	member(Literal, PossComb),
	not(Literal = []),
	(Literal = n(Atom) -> Fact = clause_g(Atom :- [f]);
	Fact = clause_g(Literal :- [t])).

%generate set of consistent sets of undefined literals

get_cons_comb(ConsComb) :-
	get_abd_comb(AbdComb),
	setof(List, is_consistent_member(List, AbdComb),ConsComb).

is_consistent_member(List, ListOfList) :-
	member(List, ListOfList),
	is_consistent(List).

%generate set of sets of undefined literals

get_abd_comb(AbdComb) :-
	get_undef_atoms(Undef),
	get_neg_undef_atoms(NegUndef),
	append(Undef, NegUndef, All),
	subsets(All, AbdComb).

is_consistent(List) :-
	(member(Atom, List), member(n(Atom), List) -> fail; true).

% code taken from somewhere, to generate all subsets of a set.

distrib(_,[],[]).
distrib(E,[Subset|RestSubsets],[[E|Subset]|RestSubsetsWithElement]) :-
     distrib(E, RestSubsets, RestSubsetsWithElement).

extend(E,SubsetsWithoutElement,Powerset) :-
     distrib(E,SubsetsWithoutElement,SubsetsWithElement),
     append(SubsetsWithoutElement,SubsetsWithElement,Powerset).

% -- base case
subsets([],[[]]).

% -- Set is ground, Powerset is free
subsets(Set,Powerset) :-
     nonvar(Set),
     var(Powerset),
     !,
     Set = [Element|Rest],
     subsets(Rest,RestPowerset),
     extend(Element,RestPowerset,Powerset),
     !.

% generate set of abducibles

get_all_abducibles(All) :-
	setof([PosList, NegList], get_abducible([PosList], [NegList]), All).

get_abducible(PosList, NegList) :-
	get_undef_def_atoms(Undef, _),
	member(Atom, Undef),
	setof(PosElement, get_positive_fact(Atom, PosElement), PosList),
	setof(NegElement, get_negative_fact(Atom, NegElement), NegList).

get_positive_fact(Atom, Pos) :- Pos = clause_g(Atom :- [t]).
get_negative_fact(Atom, Neg) :- Neg = clause_g(Atom :- [f]).

%  defined and undefined atoms

get_neg_undef_atoms(NegUndef) :-
	get_undef_atoms(Undef),
	setof(n(M), member(M, Undef), NegUndef).

get_undef_atoms(Undef) :-
	get_all_atoms(All),
	get_def_atoms(Def),
	subtract(All, Def, Undef).

get_def_atoms(Def) :- setof(Atom, def_atoms(Atom), Def).

def_atoms(Atom) :- clause_g(Atom :- _).

get_all_atoms(All) :-
	setof(List, get_atoms([List]), All).

get_atoms(List) :-
	clause_g(_ :- Body),
	member(B, Body),
	not(B=t), not(B=f),
	(B=n(A) -> Atom = A; Atom = B ),
	setof(Atom, nonvar(Atom), List).

get_atoms(List) :-
	setof(H, clause_g(H :- _), List).
