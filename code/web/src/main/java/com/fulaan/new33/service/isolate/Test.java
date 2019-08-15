package com.fulaan.new33.service.isolate;

import com.fulaan.new33.service.N33_AutoPaiKeByGroupService;
import com.fulaan.new33.service.N33_AutoPkService;
import org.bson.types.ObjectId;

public class Test {
	public static void main(String[] args){
		N33_AutoPaiKeByGroupService service = new N33_AutoPaiKeByGroupService();
		service.getAllNoCTJXB(new ObjectId("5ad40ca73328be801d6ed2f5"),new ObjectId("5b347c198fb25a98ed581e22"),new ObjectId("5ad42678384ba0aa56c6eb0c"),1);
		System.out.println("===================");
		//service.getAllNoCTJXB(new ObjectId("5ad40ca73328be801d6ed2f5"),new ObjectId("5b347c198fb25a98ed581e22"),new ObjectId("5ad42678384ba0aa56c6eb0c"),2);
		//System.out.println("===================");
		//service.getAllNoCTJXB(new ObjectId("5ad40ca73328be801d6ed2f5"),new ObjectId("5b347c198fb25a98ed581e22"),new ObjectId("5ad42678384ba0aa56c6eb0c"),3);
	}
}
