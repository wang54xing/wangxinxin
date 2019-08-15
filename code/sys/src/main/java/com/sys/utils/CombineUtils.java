package com.sys.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * 组合算法
 * @author fourer
 *
 */
public class CombineUtils {  
    
    public static void main(String[] args) {  
        List<ObjectId> data = new ArrayList<ObjectId>();  
        data.add(new ObjectId());  
        data.add(new ObjectId());  
        data.add(new ObjectId());  
        data.add(new ObjectId());  
        
        List<CombineResult<ObjectId>>  ll=  new ArrayList<CombineResult<ObjectId>>();
        CombineUtils.combinerSelect(data, 2,ll); 
        System.out.println(ll);
    }  
      
    
    public static <E> void combinerSelect(List<E> data, int k,List<CombineResult<E>> reses) 
    {
    	combinerSelect(data, new ArrayList<E>(), data.size(), k,reses); 
    }
    /**
     * 
     * @param data
     * @param workSpace
     * @param n
     * @param k
     */
    private static <E> void combinerSelect(List<E> data, List<E> workSpace, int n, int k,List<CombineResult<E>> reses) {  
        List<E> copyData;  
        List<E> copyWorkSpace;  
          
        if(workSpace.size() == k) {  
        	CombineResult<E> res=new CombineResult<E>(workSpace);
        	reses.add(res);
        }  
          
        for(int i = 0; i < data.size(); i++) {  
            copyData = new ArrayList<E>(data);  
            copyWorkSpace = new ArrayList<E>(workSpace);  
              
            copyWorkSpace.add(copyData.get(i));  
            for(int j = i; j >=  0; j--)  
                copyData.remove(j);  
            combinerSelect(copyData, copyWorkSpace, n, k,reses);  
        }  
          
    }  
    
    
    
    public static class CombineResult<T>
    {
    	private List<T> list;

		public CombineResult(List<T> list) {
			super();
			this.list = list;
		}

		public List<T> getList() {
			return list;
		}

		public void setList(List<T> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "CombineResult [list=" + list + "]";
		}
    }
      
}  