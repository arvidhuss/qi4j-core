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

package org.qi4j.composite;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation is used in composites to declare mixin implementation classes.
 * <p/>
 * Mixins tells the runtime which implementation class of a Mixin that should be
 * used. The ImplementedBY annotation can occur at any level in the composite hierarchy
 * and the runtime will match each found Mixin against a Mixins annotation.
 * All mixin interfaces must have an Mixins in the composite hierarchy or
 * an runtime exception will occur.
 * <p/>
 * Example;
 * <pre><code>
 *
 * @Mixins( MyBeerOrder.class )
 * public interface BeerOrderComposite extends BeerOrder, Composite
 * {
 * }
 * <p/>
 * public class MyBeerOrder
 * implements BeerOrder
 * {
 * :
 * }
 * </code></pre>
 * <p/>
 * Many implementations can be listed,
 * <pre><code>
 * @Mixins( { MyBeerOrder.class, DescriptionImpl.class } )
 * public interface BeerOrderComposite extends BeerOrder, Description, Composite
 * {
 * }
 * </code></pre>
 * <p/>
 * If the Mixins is a class that implements InvocationHandler, it will be
 * used for all mixins. To avoid that a invocation handler based implementations
 * are not servicing all mixin, use the AppliesTo annotation.
 * <p/>
 * <p/>
 * It is valid to have multiple Mixins for a mixin. The first one found
 * will be used. The search order is in the order they are written in the Mixins
 * annotation left-to-right, and depth-first recursive search of the super-interfaces again
 * left-to-right.
 * @see AppliesTo
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@Documented
public @interface Mixins
{
    Class[] value();
}