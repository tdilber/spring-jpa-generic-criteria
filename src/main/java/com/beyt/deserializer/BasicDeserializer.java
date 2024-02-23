package com.beyt.deserializer;

import com.beyt.util.ReflectionUtil;

public class BasicDeserializer implements IDeserializer {
    @Override
    public <T> T deserialize(Object value, Class<T> clazz) throws Exception {
        return ReflectionUtil.deserializeObject(value.toString(), clazz);
    }
}
