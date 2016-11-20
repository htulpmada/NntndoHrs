Nintend oh Heros  (copyright infringe?)

Designer language made by Adam Pluth for cs 403 Dr. J. University of Alabama

file Ext: .NtdHrs

Intro:



Types: 	string and ints for now
	must be declared,
	does not support empty assignment


Rules:
	COMMENT: 	(single line only) symbol = @

	Program: 	consists of multiple definitions

	Definitions are either:

		variable definition:	
			must declare variable type before variable name

				  ex...  [ int a = 5 ; ]
				  ex...  [ int a=5; ]

			white space optional in expression but must have 
			space after type and semi-colon after expression

		function definition:  
			must declare function keyword before function name

				  ex...  [ func main ( param ) { body } ]

			again whitespace only needed after keywords

		id definition: (other)
			really ends up being function calls and array accessors
			
			function call is [ name ( args ) ]<-- no semi colon

				  ex...  main()

			array accesor should be  var name with 
			expression in square brackets 

				  ex...  arr [ 2 ]
		

	Array functions:

		Initializer(): array var = [] or [1,] or [1,2,3,4]
			single item array MUST have comma!!!

		set(): set( array , index , value ) ; <-----------  will append if out of bounds!!!!!

		Insert(): insert( array ,index , value ) ;  <-----------  will append if out of bounds!!!!!

		append(); append( array , item ) ;

		remove(); remove( array , index ) ; <-----------  will remove last if out of bounds!!!!!

		length();  length( array ) ; <---  returns integer  duh!?*


	Conditionals:

		if: if ( boolean expr ) { block } 
		
		optional: else { block }
		
	
	Loops:

		while: while ( boolean expr ) { block }
	
	Operations:

		see AllCombosOfOps.NtdHrs
		
(attempting javaDocs for this project)
