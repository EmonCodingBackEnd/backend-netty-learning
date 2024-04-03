package com.coding.netty.example01.netty.codec.protostuff.codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtils {
    // 避免每次序列化都重新申请Buffer空间
    private static final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    // 缓存Schema
    private static final Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<Class<?>, Schema<?>>();

    static final DefaultIdStrategy STRATEGY = new DefaultIdStrategy(
        IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS | IdStrategy.MORPH_COLLECTION_INTERFACES
            | IdStrategy.MORPH_MAP_INTERFACES | IdStrategy.MORPH_NON_FINAL_POJOS);

    // 序列化方法，把指定对象序列化成字节数组
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>)obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    // 反序列化方法，将字节数组反序列化成指定Class类型
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>)schemaCache.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz, STRATEGY);
            if (schema != null) {
                schemaCache.put(clazz, schema);
            }
        }
        return schema;
    }
}
