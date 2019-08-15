package com.sys.serialize;

import java.io.Serializable;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * 对一个list序列化和反序列化
 * @author fourer
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class ListObjectSerializable<T extends Serializable> 
{
	private static final Logger logger =Logger.getLogger(ListObjectSerializable.class);
	
	
	public byte[] serialize(List<T> values) throws IOException {
		if (values == null)
			throw new NullPointerException("Can't serialize null");
		byte[] results = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			for (T m : values) {
				os.writeObject(m);
			}
			results = bos.toByteArray();
		} finally {
			os.close();
			bos.close();
		}
		return results;
	}


	public List<T> deserialize(byte[] in) throws IOException, ClassNotFoundException {
		List<T> list = new ArrayList<T>();
	    ByteArrayInputStream bis = null;
	    ObjectInputStream is = null;
	    try {
	      if (in != null) {
	        bis = new ByteArrayInputStream(in);
	        is = new ObjectInputStream(bis);
	        while (true) {
	          T m = (T)is.readObject();
	          if (m == null) {
	            break;
	          }
	          list.add(m);
	        }
	        is.close();
	        bis.close();
	      }
	    } catch (IOException e) {  

	  } catch (ClassNotFoundException e) {  
		  logger.error("", e);
	  }  finally {
		  is.close();
		  bis.close();
	    }
	    
	    return  list;
	}
	

}
