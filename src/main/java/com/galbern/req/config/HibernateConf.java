package com.galbern.req.config;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class HibernateConf extends EmptyInterceptor implements Serializable { // custom entity interceptor

    public boolean onSave(
            final Object entity, final Serializable id, final Object[] state, final String[] propertyNames,
            final Type[] types) {
        if (types != null) {
            for (int i = 0; i < types.length; i++) {
                if (types[i].isCollectionType()) {
                    String propertyName = propertyNames[i];
                    propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    try {
                        Method method = entity.getClass().getMethod("get" + propertyName);
                        List<Object> objectList = (List<Object>) method.invoke(entity);
                        System.out.println("--HIBERNATE CUSTOM INTERCEPTOR ---");
                        if (objectList != null) {
                            for (Object object : objectList) {
                                String entityName = entity.getClass().getSimpleName();
                                Method eachMethod = object.getClass().getMethod("set" + entityName, entity.getClass());
                                eachMethod.invoke(object, entity);
                            }
                        }

                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return true;
    }

}