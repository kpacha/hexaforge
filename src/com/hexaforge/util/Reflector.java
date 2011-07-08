package com.hexaforge.util;

import static org.fest.reflect.core.Reflection.method;

import com.google.appengine.api.users.User;

public class Reflector {
	private Object object;

	public Reflector(Object object){
		super();
		this.object = object;
	}
	
	public Object executeMethod(String methodName, Object...args) throws IllegalArgumentException{
		Class<?>[] t= new Class[args.length];
		Object[] arguments = new Object[args.length];
		for(int i=0; i<args.length; i++){
			//System.out.print("\nReflector: argumento ["+i+"]");
			try{
				t[i] = args[i].getClass();
			}catch(NullPointerException e){
				//System.out.print(" es nulo! Reduciendo el array de argumentos");
				Class<?>[] tmp = new Class[i];
				arguments = new Object[i];
				for(int j=0; j<i; j++){
					arguments[j] = args[j];
					tmp[j] = t[j];
				}
				t = new Class[i];
				t = tmp;
				args = new Object[i];
				args = arguments;
				break;
			}
			//System.out.print(" del tipo " + t[i]);
		}
		return method(methodName)
				.withParameterTypes(t)
				.in(this.object)
				.invoke(args);
	}
	
}
