package com.example.demo.baseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 序列化工具
 *
 * @author chenxin
 * @date 2022/09/27 16:38
 */
public class SerializeUtils {
    private static Logger logger = LoggerFactory.getLogger(SerializeUtils.class);
    /**
     * 序列化
     */
    public static byte[] serialize(Object o){
        byte[] bytes=null;

        if(o == null){
            return new byte[0];
        }

        try{
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(128);
            try{
                if(!(o instanceof Serializable)){
                    throw new IllegalArgumentException("类未实现序列化");
                }
                ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(o);
                objectOutputStream.flush();
                bytes=byteArrayOutputStream.toByteArray();
            }catch (Throwable e){
                throw new Exception("Failed to serialize", e);
            }
        }catch (Exception e){
            logger.error("Failed to serialize", e);
        }
        return bytes;
    }

    /**
     * 反序列化
     */
    public static Object deserialize(byte[] bytes){
        Object object=null;
        if(isEmpty(bytes)){
            return null;
        }

        try{
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream=new ObjectInputStream(byteArrayInputStream);
            try{
                object=objectInputStream.readObject();
            }catch (ClassNotFoundException e){
                throw new IllegalArgumentException("Failed to deserialize");
            }
        }catch (Exception e){
            logger.error("Failed to deserialize", e);
        }
        return  object;
    }

    private static boolean isEmpty(byte[] bytes) {
        return bytes != null && bytes.length>0;
    }
}
