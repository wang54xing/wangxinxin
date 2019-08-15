package com.sys.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 对一个对象进行序列化和反序列化
 * @author fourer
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public  class  ObjectSerializable< T extends Serializable> 
 {
	
	public  byte[] serialize(T t) throws IOException {
		if (t == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] result = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(t);
			
			result = bos.toByteArray();
		}  finally {
			os.close();
			bos.close();
		}
		return result;
	}


	
	public T deserialize(byte[] in) throws IOException, ClassNotFoundException {
		T result = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				result = (T) is.readObject();
				
			}
		}  finally {
			is.close();
			bis.close();
		}
		return result;
	}

}
