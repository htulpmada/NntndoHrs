Nintend oh Heros  (copyright infringe?)

Designer language made by Adam Pluth for cs 403 Dr. J. University of Alabama



Rules:
	COMMENT: 	(single line only) symbol = @

	Program: 	consists of multiple definitions

	File Ext:	.nes

	Intro:		Designer Programming Language for Dr. j,
			CS403 at University of Alabama ROll Tide!!!
			Java like syntax with a couple exceptions.
			Dynamically typed, with all the basic stuff
			and a little extra. NOT Object Oriented.



	Types: 	Strings, Integers, Reals and Booleans.
		Functions are kind of considered a type as you can 
		assign them like you would a variable!!!


	Programs: Consist of definitions(see below...)


	Definitions are either:

		Variable Definition:	

			Must declare variable type before variable name

				  ex...  [ int a = 5 ; ]
				  ex...  [ int a=5; ]

			white space optional in expression but must have 
			space after type and semi-colon after expression.
			Does NOT support empty declaration, must initialize!!!



		Function Definition:  

			Must declare function keyword before function name

				  ex...  [ func main ( param ) { body } ]

			Again, whitespace only needed after keywords.



		Id definition: (other)
			really ends up being function calls and array accessors
			
			function call is [ name ( args ) ]<-- 	no semi colon for
								top level but need 
								for non-global scope

				  ex...  main()

			array accesor should be  var name with 
			expression in square brackets 

				  ex...  arr [ 2 ]
				  ex...  arr [ 5 + 3 ]
		

	Array functions:
		
		Uses Java's Arraylists<> so O(1) time!!!

		Initializer(): array var = [] or [1,] or [1,2,3,4]
			single item array MUST have comma!!!

		set(): set( array , index , value ) ; <-----------  will append if out of bounds!!!!!

		Insert(): insert( array ,index , value ) ;  <-----------  will append if out of bounds!!!!!

		append(); append( array , item ) ;

		remove(); remove( array , index ) ; <-----------  will remove last if out of bounds!!!!!

		length();  length( array ) ; <---  returns integer  duh!?*

	Built-ins:

		Print(): print with new line at end
	
	Conditionals:

		if: if ( boolean expr ) { block } 
		
		optional: 	if else { block }
				else { block }
		
	
	Loops:

		while: while ( boolean expr ) { block }
	

	Operators: 
		
		+ 	plus			can add all types together except arrays and functions (takes on type of first operand)
						can only add string to int if string is number)

		- 	minus			only works on numbers!!!
							|
		* 	times				|
							|
		/ 	divide				|
							|
		//	integer division		|
							|
		^ 	exponent			V
----------------------------------------------------------------------------------------
		< 	less			can compare 	int <---> int	
							|	str <---> str
		<=	less or equal			|      bool <---> bool
							|	int <---> str
		> 	greater				|	str <---> int
							|	int <---> real
		>=	greater or equal		|      real <---> int
							|      real <---> real
		==	equlaity check			|
							|			
		&	logic AND			|
							|
		|	logic OR			|
							|
		!=	NOT equality check		V
----------------------------------------------------------------------------------------		
		!	NOT		<-------------	Only Booloean
		
		= 	assignment			
							
							

		see AllCombosOfOps.nes for all errors
			ints and real are interchangable in this file


	Functions:

		As First-Class objects you can save and pass function 
		closures around as much as you want. Another cool thing is 
		this language has Lambda functions, these can be returned 
		from a function or saved as a FUNCTION typed varialbe.
		function also exibit behavior know as Explicit return.
		this means that the last thing to be evaluated is what
		will be returned by function call.

	Lambdas:	
		
		lambda( params ) { body } returns a closure

	Alternates:
		
		Below are some base keywords followed by alternate 
		words with the exact same usage and effect as 
		the 'base' keyword.

		---------------------------------------------------
		integer		[ int , mario ]
		string		[ str , luigi ]
		real		[ kirby ]
		boolean		[ bool , player ]
		func		[ yoshi ]
		if 		[ doubleDragon ]
		else 		[ battleToads ]
		else if		[ battleToads doubleDragon ]
		while		[ game ]
		nil		[ null , dk ]
		true		[ bubble ]
		false		[ bobble ]
		print		[ pit ]
		array		[ triforce ]
		append		[ link ]
		insert		[ zelda ]
		set		[ shiek ]
		remove		[ gannon ]
		length		[ size , quest ]
		lambda		[ samus ]

	

