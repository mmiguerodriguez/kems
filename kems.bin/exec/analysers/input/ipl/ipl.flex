package sats5;
import java_cup.runtime.*;

%%

/*

  Adolfo Gustavo Serra Seca Neto, December 2004
  Lexical analyser for classical propositional logic formulas
  SATLIB format with signs, implication, biimplication 
  and without headers

  This is sats for the new formula and signed formula classes.

  What to change (in case you want to change the file format):
	name of the parser and of the symbols class
	  - sats5Lexer, sats5sym (several references!)
      productions and states (obviously)

  How to produce the lexer:

  - Run the command:

  	java JFlex.Main satlib-sat-s5.flex

  	which will create the following file:

	  	sats5Lexer.java



*/

/* Name of the parser class to be generated */
%class sats5Lexer
%public
%line
%column

%cup

%{

    StringBuffer number = new StringBuffer();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}


%eofval{
  return symbol(sats5sym.EOF);
%eofval}

LineTerminator = \r|\n|\r\n
WhiteSpace = [ \t\f]
String = [a-z_A-Z_1-9][a-z_A-Z_0-9_,]*
Biimplies = "<=>"
Xor = "%"
Implies = "->"
Sign = "T"|"F"
Top = "TOP"
Bottom = "BOT"
Consistency = "@"
Inconsistency = "#"


%state FORMULA

%%

/* beginning of the lexical analyser */
<YYINITIAL>{

	{Sign}  			{
	  						yybegin(FORMULA);
						   	return symbol(sats5sym.SIGN, yytext());
						}

}
<FORMULA> {

    {LineTerminator}      {
    						yybegin(YYINITIAL);
    						return symbol(sats5sym.EOL);
	    				   }

    {Biimplies}        { return symbol(sats5sym.BIIMPLIES); }
    {Implies}          { return symbol(sats5sym.IMPLIES); }
    {Xor}              { return symbol(sats5sym.XOR); }
    "-"                { return symbol(sats5sym.NEG); }
    "*"                { return symbol(sats5sym.AND); }
    "+"                { return symbol(sats5sym.OR); }
    "("                { return symbol(sats5sym.LPAREN); }
    ")"                { return symbol(sats5sym.RPAREN); }
    {Top}              { return symbol(sats5sym.TOP); }
    {Bottom}           { return symbol(sats5sym.BOTTOM); }
    {Consistency}      { return symbol(sats5sym.CONSISTENCY); }
    {Inconsistency}      { return symbol(sats5sym.INCONSISTENCY); }
    

    {String}           { return symbol (sats5sym.STRING, yytext());}

    {WhiteSpace}       { /* just skip what was found, do nothing */ }

}

[^]                    { throw new Error("Illegal character <"+yytext()+ "> at line " + yyline + ", column " + yychar ); }


