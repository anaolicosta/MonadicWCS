
:- 	retractall( clause(_) ),
	current_prolog_flag(argv, Argv), append(_, [Last], Argv),
	[syllogisms/Last],
	[grounding],
	atom_string(Last, String),
	string_concat('syllogisms/', String, X),
	string_concat(X, 'g.pl', GroundProgram),
	writegroundprogram(GroundProgram),
	[GroundProgram],
	[svl],
	string_concat('syllogisms/', String, Z),
	string_concat(Z, 'glm.pl', LeastModel),
	writeleastmodel(LeastModel),
	halt.

