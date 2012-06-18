package jdbc_pool.core ;


/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/



/**
basic sample DTO
*/

public class SimpleDTO {

private final String _name ;
private final int _number ;
private final String _stringRep ;

public SimpleDTO( final String name , final int number ){

	_name = name ;
	_number = number ;
	_stringRep = "[SimpleDTO: " + _name + "/" + _number + "]" ;

	return ;

} // ctor

public String getName(){
	return( _name ) ;
} // getName()

public int getNumber(){
	return( _number ) ;
} // getNumber()

public String toString(){
	return( _stringRep ) ;
} // toString()

} // public class SimpleDTO

