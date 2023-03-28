package qlProject.ast.expression;

public abstract class UnaryExpression implements IExpression {
	
	private final IExpression subExpression;
	
	public UnaryExpression(IExpression subExpression){
		
		this.subExpression = subExpression;
	}


	public IExpression getSubExpression(){
		return subExpression;
	}
	
}
--------------------

package com.github.t3t5u.common.expression;

@SuppressWarnings("serial")
public class StringLiteral extends CharSequenceLiteral<String> {
	StringLiteral(final String value) {
		super(String.class, value);
	}
}

--------------------

package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;


public abstract class Declaration extends BlockElem {
	
	public boolean globalScope;
	public Type type;
	Declaration(Token firstToken) {
		super(firstToken);
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}

--------------------

