/*
 * Copyright 2007 Rickard Öberg
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.test.model2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.qi4j.api.annotation.AppliesTo;
import org.qi4j.api.annotation.Dependency;
import org.qi4j.api.annotation.Modifies;

/**
 * Override only the methods that have the FooAnnotation, and print out the value of
 * the annotation on the mixin method.
 *
 */
@AppliesTo( FooAnnotation.class )
public class FooModifier
    implements InvocationHandler
{
    // Attributes ----------------------------------------------------
    @Modifies InvocationHandler next;
    @Dependency Method foo;

    // InvocationHandler implementation -----------------------------
    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
    {
        args[ 0 ] = args[ 0 ] + foo.getAnnotation( FooAnnotation.class ).value();
        return next.invoke( proxy, method, args );
    }
}