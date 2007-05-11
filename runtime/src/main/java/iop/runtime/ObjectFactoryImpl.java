/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2007, Niclas Hedhman. All Rights Reserved.
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
package iop.runtime;

import iop.api.MixinFactory;
import iop.api.ObjectFactory;
import iop.api.ObjectInstantiationException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IdentityHashMap;

/**
 * TODO
 *
 */
public final class ObjectFactoryImpl
    implements ObjectFactory
{
    ClassLoader loader;
    MixinFactory mixinFactory;
    InvocationInstancePool invocationInstancePool;

    public ObjectFactoryImpl( MixinFactory aMixinFactory )
    {
        mixinFactory = aMixinFactory;
        invocationInstancePool = new InvocationInstancePool( new ModifierInstanceFactory( this, mixinFactory) );
    }

    public <T> T newInstance( Class<T> anObjectType )
    {
        try
        {
            ObjectContext context= new ObjectContext( anObjectType, this, mixinFactory, invocationInstancePool);

            ObjectInvocationHandler handler = new ObjectInvocationHandler( context);
            ClassLoader proxyClassloader = anObjectType.getClassLoader();
            Class[] interfaces = new Class[]{ anObjectType };
            T proxy = (T) Proxy.newProxyInstance( proxyClassloader, interfaces, handler );
            return proxy;
        }
        catch( Exception e )
        {
            throw new ObjectInstantiationException( e );
        }
    }

    public <T> T cast( Class<T> anObjectType, Object anObject )
    {
        try
        {
            if( anObject instanceof Proxy )
            {
                InvocationHandler wrappedHandler = Proxy.getInvocationHandler( anObject );
                if( wrappedHandler instanceof WrappedObjectInvocationHandler )
                {
                    Object wrappedObject = ( (WrappedObjectInvocationHandler) wrappedHandler ).getWrappedInstance();
                    if( anObjectType.isInstance( wrappedObject ) )
                    {
                        anObject = wrappedObject;
                    }
                }
            }

            ObjectContext context = new ObjectContext( anObjectType, this, mixinFactory, invocationInstancePool );
            ObjectInvocationHandler handler = new WrappedObjectInvocationHandler( anObject, context );
            ClassLoader proxyClassLoader = anObjectType.getClassLoader();
            Class[] interfaces = new Class[]{ anObjectType };
            T proxy = (T) Proxy.newProxyInstance( proxyClassLoader, interfaces, handler );
            return proxy;
        }
        catch( Exception e )
        {
            throw new ObjectInstantiationException( e );
        }
    }

    public boolean isInstance( Class anObjectType, Object anObject )
    {
        if( anObjectType.isInstance( anObject ) )
        {
            return true;
        }
        if( anObject instanceof Proxy )
        {
            InvocationHandler handler = Proxy.getInvocationHandler( anObject );
            if( handler instanceof WrappedObjectInvocationHandler )
            {
                WrappedObjectInvocationHandler wrappedHandler = (WrappedObjectInvocationHandler) handler;
                return isInstance( anObjectType, wrappedHandler.getWrappedInstance());
            }
        }
        return false;
    }
    
    public <T> T getThat( T proxy )
    {
        InvocationHandler handler = Proxy.getInvocationHandler( proxy );
        if( handler instanceof ProxyReferenceInvocationHandler )
        {
            return (T) ((ProxyReferenceInvocationHandler) handler).getProxy();
        }
        if( handler instanceof ObjectInvocationHandler )
        {
            return proxy;
        }
        return null;
    }
}