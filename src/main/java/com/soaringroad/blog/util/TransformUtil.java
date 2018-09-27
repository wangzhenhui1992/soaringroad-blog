package com.soaringroad.blog.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransformUtil {

    private TransformUtil() {
    };

    private static final ThreadLocal<ObjectMapper> objectMapper = new ThreadLocal<ObjectMapper>() {

        @Override
        protected ObjectMapper initialValue() {
            return new ObjectMapper();
        }

    };

    public static <T> T parseMapToEntity(Object map, Class<T> clazz) {
        ObjectMapper om = objectMapper.get();
        T result = null;
        try {
            result = om.readValue(om.writeValueAsString(map), clazz);
        } catch (IOException e) {
            log.error("Fail to pasrse redis result[LinkedHashMap] to "+clazz.getName(),e);
            return result;
        }
        return result;
    }
}
