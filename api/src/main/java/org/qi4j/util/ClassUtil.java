/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.qi4j.property.ComputedPropertyInstance;

/**
 * Class-related utility methods
 */
public class ClassUtil
{
    public static <T> T propertyMethodOf( Class<T> type )
    {
        return (T) Proxy.newProxyInstance( type.getClassLoader(), new Class[]{ type }, new InvocationHandler()
        {
            public Object invoke( Object o, final Method method, Object[] objects ) throws Throwable
            {
                return new ComputedPropertyInstance( method )
                {
                    public Object get()
                    {
                        return method;
                    }
                };
            }
        } );
    }

    /**
     * Get all interfaces for the given type,
     * including the provided type. No type
     * is included twice in the list.
     *
     * @param type to extract interfaces from
     * @return set of interfaces of given type
     */
    public static Set<Type> interfacesOf( Type type )
    {
        Set<Type> interfaces = new LinkedHashSet<Type>();
        addInterfaces( type, interfaces );
        return interfaces;
    }

    public static Set<Type> interfacesWithMethods( Set<Type> interfaces )
    {
        Set<Type> newSet = new LinkedHashSet<Type>();
        for( Type type : interfaces )
        {
            if( type instanceof Class && ( (Class) type ).isInterface() && ( (Class) type ).getDeclaredMethods().length > 0 )
            {
                newSet.add( type );
            }
        }

        return newSet;
    }

    public static Set<Type> typesOf( Type type )
    {
        Set<Type> types = new LinkedHashSet<Type>();
        addInterfaces( type, types );

        if( type instanceof Class )
        {
            Class current = (Class) type;
            while( current != null )
            {
                types.add( current );
                current = current.getSuperclass();
            }
        }

        return types;
    }


    public static Class[] toClassArray( Set<Type> types )
    {
        Class[] array = new Class[types.size()];
        int idx = 0;
        for( Type type : types )
        {
            array[ idx++ ] = (Class) type;
        }

        return array;
    }

    public static Type actualTypeOf( Type type )
    {
        Set<Type> types = interfacesOf( type );
        for( Type type1 : types )
        {
            if( type1 instanceof ParameterizedType )
            {
                return ( (ParameterizedType) type1 ).getActualTypeArguments()[ 0 ];
            }
        }
        return null;
    }

    public static List<Constructor> constructorsOf( Class clazz )
    {
        List<Constructor> constructors = new ArrayList<Constructor>();
        addConstructors( clazz, constructors );
        return constructors;
    }

    private static void addConstructors( Class clazz, List<Constructor> constructors )
    {
        if( clazz != null && !clazz.equals( Object.class ) )
        {
            constructors.addAll( asList( clazz.getDeclaredConstructors() ) );
            addConstructors( clazz.getSuperclass(), constructors );
        }
    }

    public static List<Method> methodsOf( Class clazz )
    {
        List<Method> methods = new ArrayList<Method>();
        addMethods( clazz, methods );
        return methods;
    }

    private static void addMethods( Class clazz, List<Method> methods )
    {
        if( clazz != null && !clazz.equals( Object.class ) )
        {
            methods.addAll( asList( clazz.getDeclaredMethods() ) );
            addMethods( clazz.getSuperclass(), methods );
        }
    }

    public static List<Field> fieldsOf( Class clazz )
    {
        List<Field> fields = new ArrayList<Field>();
        addFields( clazz, fields );
        return fields;
    }

    private static void addFields( Class clazz, List<Field> fields )
    {
        if( clazz != null && !clazz.equals( Object.class ) )
        {
            fields.addAll( asList( clazz.getDeclaredFields() ) );
            addFields( clazz.getSuperclass(), fields );
        }
    }

    private static void addInterfaces( Type type, Set<Type> interfaces )
    {
        if( !interfaces.contains( type ) )
        {
            if( type instanceof Class )
            {
                Class clazz = (Class) type;

                if( clazz.isInterface() )
                {
                    interfaces.add( clazz );
                }

                Type[] subTypes = clazz.getGenericInterfaces();
                for( Type subType : subTypes )
                {
                    addInterfaces( subType, interfaces );
                }
            }
        }
    }
}