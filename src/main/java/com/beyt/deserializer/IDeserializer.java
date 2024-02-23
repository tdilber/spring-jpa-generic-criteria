package com.beyt.deserializer;

public interface IDeserializer {
    <T> T deserialize(Object value, Class<T> clazz) throws Exception;
}
