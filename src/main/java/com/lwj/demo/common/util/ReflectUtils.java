package com.lwj.demo.common.util;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 反射工具类
 */
public class ReflectUtils {


    /**
     * 拷贝对象属性值
     *
     * @param source    源对象
     * @param target    目标对象
     * @param relaParam 关联属性，key：目标对象属性；value：源对象的属性
     */
    public static void copyValue(Object source, Object target, Map<String, String> relaParam) throws Exception {
        Set<String> params = relaParam.keySet();

        for (String targetParam : params) {
            String copyParam = relaParam.get(targetParam);

            String getMethod = "get" + copyParam.substring(0, 1).toUpperCase() + copyParam.substring(1);
            String setMethod = "set" + targetParam.substring(0, 1).toUpperCase() + targetParam.substring(1);

            Object value = doMethod(source, getMethod);
            doMethod(target, setMethod, value);
        }
    }

    /**
     * 动态执行对象方法
     *
     * @param <T>
     * @param obj        对象
     * @param methodName 方法名称
     * @param parems     参数
     * @return
     * @throws Exception
     */
    public static <T> T doMethod(Object obj, String methodName, Object... parems) throws Exception {
        Method method = getMethod(obj.getClass(), methodName);

        if (method != null) {
            return (T) method.invoke(obj, parems);
        }

        return null;
    }

    /**
     * 获取类中定义的方法，包括父类与接口
     *
     * @param cls        类型
     * @param methodName 所需要的方法名称
     * @return
     */
    public static Method getMethod(Class<?> cls, final String methodName) {
        Set<String> methodSet = new HashSet<String>();
        methodSet.add(methodName);

        Map<String, Method> methodMap = getMethodMap(cls, methodSet);

        return methodMap.get(methodName);
    }

    /**
     * 获取类中定义的方法，包括父类与接口
     *
     * @param cls       类型
     * @param methodSet 所需要的方法名称列表
     * @return
     */
    public static Map<String, Method> getMethodMap(Class<?> cls, final Set<String> methodSet) {
        final Map<String, Method> methodMap = new TreeMap<String, Method>();

        loopClassMethod(cls, new LoopClassMethodDeal() {
            @Override
            public boolean deal(Method method) {
                String methodName = method.getName();
                if (!methodMap.containsKey(methodName) && methodSet.contains(methodName)) {
                    methodMap.put(methodName, method);
                    methodSet.remove(methodName);

                    if (methodSet.isEmpty()) {
                        return false;
                    }
                }

                return true;
            }
        });

        return methodMap;
    }

    /**
     * 判断对象是否是基础类型包装类
     *
     * @param obj
     * @return
     */
    public static boolean isBaseType(Object obj) {
        try {
            return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            if (obj instanceof String) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断对象是否是基础类型包装类
     *
     * @param cls
     * @return
     */
    public static boolean isBaseType(Class<?> cls) {
        try {
            return ((Class<?>) cls.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            if (equals(cls, String.class)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断是否为父类
     *
     * @param child  子类
     * @param parent 父类
     * @return
     */
    public static boolean isParent(Class<?> child, Class<?> parent) {
        return parent.isAssignableFrom(child);
    }

    /**
     * 判断是否相等
     *
     * @param firstCls  类1
     * @param secondCls 类2
     * @return
     */
    public static boolean equals(Class<?> firstCls, Class<?> secondCls) {
        return firstCls.equals(secondCls);
    }

    /**
     * 获取Type的泛型类型
     *
     * @param type
     * @param num
     * @return
     */
    public static Class<?> getGenericType(Type type, int num) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return (Class<?>) pt.getActualTypeArguments()[num];
        }
        return null;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return map;
    }


    /**
     * 获取字段值信息
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> getFieldValue(final Object obj) {
        final Map<String, Object> map = new HashMap<String, Object>();

        loopClassField(obj.getClass(), new LoopClassFieldDeal() {
            @Override
            public boolean deal(Field field) {
                if (!"serialVersionUID".equals(field.getName())) {
                    field.setAccessible(true);
                    try {
                        map.put(field.getName(), field.get(obj));
                    } catch (Exception e) {
                    }
                }

                return true;
            }
        });

        return map;
    }

    /**
     * 获取静态字段值
     *
     * @param cls       对象
     * @param fieldName 字段名
     * @param needDeep  是否需要深度搜索父类字段
     * @return
     */
    public static <T> T getStaticFieldValue(final Class<?> cls, final String fieldName, boolean needDeep) {
        if (needDeep) {
            final Object[] arr = new Object[1];
            Object value = null;

            loopClassField(cls, new LoopClassFieldDeal() {
                @Override
                public boolean deal(Field field) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        try {
                            arr[0] = field.get(cls);
                        } catch (Exception e) {
                        }

                        return false;
                    }

                    return true;
                }
            });

            if (arr.length == 1) {
                value = arr[0];
            }

            return (T) value;
        } else {
            try {
                Field field = cls.getField(fieldName);
                return (T) field.get(cls);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param needDeep  是否需要深度搜索父类字段
     * @return
     */
    public static <T> T getFieldValue(final Object obj, final String fieldName, boolean needDeep) {
        if (needDeep) {
            Class<?> cls = obj.getClass();
            final Object[] arr = new Object[1];
            Object value = null;

            loopClassField(cls, new LoopClassFieldDeal() {
                @Override
                public boolean deal(Field field) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        try {
                            arr[0] = field.get(obj);
                        } catch (Exception e) {
                        }

                        return false;
                    }

                    return true;
                }
            });

            if (arr.length == 1) {
                value = arr[0];
            }

            return (T) value;
        } else {
            try {
                Field field = obj.getClass().getField(fieldName);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 遍历Class方法
     *
     * @param cls  类型
     * @param deal 处理类
     */
    public static void loopClassMethod(Class<?> cls, final LoopClassMethodDeal deal) {
        // 遍历Class
        loopClass(cls, new LoopClassDeal() {
            @Override
            public boolean deal(Class<?> cls) {
                boolean isLoop = true;

                // 找到当前Class定义的方法
                for (Method method : cls.getDeclaredMethods()) {
                    isLoop = deal.deal(method);

                    if (!isLoop) {
                        break;
                    }
                }

                return isLoop;
            }
        });
    }

    /**
     * 遍历Class字段
     *
     * @param cls  类型
     * @param deal 处理类
     */
    public static void loopClassField(Class<?> cls, final LoopClassFieldDeal deal) {
        // 遍历Class
        loopClass(cls, new LoopClassDeal() {
            @Override
            public boolean deal(Class<?> cls) {
                boolean isLoop = true;

                // 找到当前Class定义的属性
                Field[] fields = cls.getDeclaredFields();

                for (Field field : fields) {
                    isLoop = deal.deal(field);

                    if (!isLoop) {
                        break;
                    }
                }

                return isLoop;
            }
        });
    }

    /**
     * 遍历Class
     *
     * @param cls  类型
     * @param deal 处理类
     */
    public static void loopClass(Class<?> cls, LoopClassDeal deal) {
        // 开始遍历
        while (cls != null) {
            if (deal.deal(cls)) {
                // 遍历父类属性
                cls = cls.getSuperclass();
            } else {
                break;
            }
        }
    }

    /**
     * 遍历处理Class接口
     *
     * @author hedongzhou
     */
    public static interface LoopClassDeal {

        /**
         * 处理
         *
         * @param cls 类型
         * @return false 结束循环
         */
        public boolean deal(Class<?> cls);

    }

    /**
     * 遍历处理Class字段接口
     *
     * @author hedongzhou
     */
    public static interface LoopClassFieldDeal {

        /**
         * 处理
         *
         * @param field 类型
         * @return false 结束循环
         */
        public boolean deal(Field field);

    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        if (null == obj) {
            return;
        }
        Field targetField = getTargetField(obj.getClass(), fieldName);
        try {
            FieldUtils.writeField(targetField, obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        Field targetField = getTargetField(obj.getClass(), fieldName);

        try {
            return FieldUtils.readField(targetField, obj, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getTargetField(Class<?> targetClass, String fieldName) {
        Field field = null;
        try {
            if (targetClass == null) {
                return field;
            }

            if (Object.class.equals(targetClass)) {
                return field;
            }

            field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
            if (field == null) {
                field = getTargetField(targetClass.getSuperclass(), fieldName);
            }
        } catch (Exception e) {
        }

        return field;
    }

    /**
     * 遍历处理Class方法接口
     *
     * @author hedongzhou
     */
    public static interface LoopClassMethodDeal {

        /**
         * 处理
         *
         * @param method 方法
         * @return false 结束循环
         */
        public boolean deal(Method method);

    }

}
