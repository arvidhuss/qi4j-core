/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2008, Edward Yakop. All Rights Reserved.
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
package org.qi4j.spi.property;

import org.qi4j.property.PropertyInfo;

/**
 * {@code PropertyInstance} represents a mutable property.
 *
 * @author Rickard Öberg
 * @since 0.1.0
 */
public class PropertyInstance<T> extends ComputedPropertyInstance<T>
{
    protected T value;

    /**
     * Construct an instance of {@code PropertyInstance} with the specified arguments.
     *
     * @param aPropertyInfo The property info. This argument must not be {@code null}.
     * @param aValue        The property value.
     * @throws IllegalArgumentException Thrown if the specified {@code aPropertyInfo} is {@code null}.
     * @since 0.1.0
     */
    public PropertyInstance( PropertyInfo aPropertyInfo, T aValue )
        throws IllegalArgumentException
    {
        super( aPropertyInfo );
        value = aValue;
    }

    /**
     * Returns this property value.
     *
     * @return This property value.
     * @since 0.1.0
     */
    public T get()
    {
        return value;
    }

    /**
     * Sets this property value.
     *
     * @param aNewValue The new value.
     */
    public T set( T aNewValue )
    {
        value = aNewValue;
        return value;
    }

    /**
     * Returns the value as string.
     *
     * @return The value as string.
     * @since 0.1.0
     */
    @Override
    public String toString()
    {
        return value == null ? "" : value.toString();
    }
}