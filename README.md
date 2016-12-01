

		 <<^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^>>
		<<<  Nintend oh Heros  (copyright infringe?)  >>>
		 <<vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv>>


Designer language made by Adam Pluth for cs 403 Dr. J. University of Alabama

Intro:
	The year was 1986 and it was a hot day of October in the desert
	when a small code monkey was born in the middle of a digital 
	revolution, he grew and wandered through existance trying to figure 
	out what to do with his life when he was faced with the challenge
	of creating the greatest language in the world. As this code monkey
	was well educated by the finest institute the state of Alabama has
	to offer, he accepted the challenge and created something that 
	reminded this narrartor of his youth when he was a still 
	stumbling through childhood trying and ultimately failing in 
	an attemp to resist the pixelized grip of control that was....


			VIDEO GAMES!!!!!!!!



Intro:		Designer Programming Language for Dr. j,
		CS403 at University of Alabama ROll Tide!!!
		Java like syntax with a couple exceptions.
		Dynamically typed, with all the basic stuff
		and a little extra. 

		NOT Object Oriented.

		EVERYTHING IS A STRING!!!
		when the lexer reads a token it saves as a string until 
		proper evaluation is needed (usually with operators)
		afterwhich the result is stored as a string.
	
		The execption to the is Arrays which is a lexeme 
		with an array of lexemes(whitch are strings).



Rules:

	File Ext:	.nes
	
	COMMENT: 	(single line only) symbol = @

	Programs: 	Consist of multiple definitions(see below...)

	Types: 	Strings, Integers, Reals and Booleans.
		Functions are kind of considered a type as you can 
		assign them like you would a variable!!!

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
			supports mixed types in same array!!!

		set(): set( array , index , value ) ; <-----------  will append if out of bounds!!!!!

		Insert(): insert( array ,index , value ) ;  <-----------  will append if out of bounds!!!!!

		append(); append( array , item ) ;

		remove(); remove( array , index ) ; <-----------  will remove last if out of bounds!!!!!

		length();  length( array ) ; <---  returns integer  duh!?*

	Built-ins:

		Print(): print with new line at end
		return <-- does whats expected
		break <--- also does whats expected
	
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


		base word	  alternates
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
		break		[ gameover ]
		nil		[ null , dk ]
		true		[ bubble ]
		false		[ bobble ]
		print		[ pit ]
		return		[ quit ]
		array		[ triforce ]
		append		[ link ]
		insert		[ zelda ]
		set		[ shiek ]
		remove		[ gannon ]
		length		[ size , quest ]
		lambda		[ samus ]

	

