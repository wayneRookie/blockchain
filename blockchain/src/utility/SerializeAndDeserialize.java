package utility;
/*
 * 对象的序列化和反序列化的工具类
 * 将对象序列化存入数据库
 * 在数据库中读出反序列化为对象
 * */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeAndDeserialize {
	public static String serializeToString(Object obj) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
		objOut.writeObject(obj);
		String str = byteOut.toString("ISO-8859-1");
		return str;
	}
	
	public static Object deserializeToObject(String str) throws ClassNotFoundException, IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
		ObjectInputStream objIn = new ObjectInputStream(byteIn);
		Object obj = objIn.readObject();
		return obj;
	}
}
