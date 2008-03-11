/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.runtime.entity;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.composite.CompositeBuilder;
import org.qi4j.entity.EntitySession;
import org.qi4j.entity.EntitySessionFactory;
import org.qi4j.entity.SessionCompletionException;
import org.qi4j.entity.memory.MemoryEntityStoreComposite;
import org.qi4j.spi.entity.UuidIdentityGeneratorComposite;
import org.qi4j.spi.service.provider.DefaultServiceInstanceProvider;
import org.qi4j.test.AbstractQi4jTest;
import org.qi4j.test.entity.AccountComposite;
import org.qi4j.test.entity.CustomerComposite;
import org.qi4j.test.entity.OrderComposite;
import org.qi4j.test.entity.Product;
import org.qi4j.test.entity.ProductComposite;

/**
 * TODO
 */
public class EntitySessionFactoryTest
    extends AbstractQi4jTest
{

    public void configure( ModuleAssembly module ) throws AssemblyException
    {
        module.addComposites( MemoryEntityStoreComposite.class );
        module.addComposites( UuidIdentityGeneratorComposite.class );
        module.addComposites( AccountComposite.class, OrderComposite.class, ProductComposite.class, CustomerComposite.class );
        module.addServices( DefaultServiceInstanceProvider.class, MemoryEntityStoreComposite.class );
        module.addServices( DefaultServiceInstanceProvider.class, UuidIdentityGeneratorComposite.class );
    }

    public void testEntitySession()
    {
        EntitySessionFactory entitySessionFactory = new EntitySessionFactoryImpl( moduleInstance );
        EntitySession session = entitySessionFactory.newEntitySession();

        // Create product
        CompositeBuilder<ProductComposite> cb = session.newEntityBuilder( null, ProductComposite.class );
        cb.propertiesOfComposite().name().set( "Chair" );
        cb.propertiesOfComposite().price().set( 57 );
        Product chair = cb.newInstance();

        System.out.println( "Product '" + chair.name().get() + "' costs " + chair.price() );

        try
        {
            session.complete();
        }
        catch( SessionCompletionException e )
        {
            e.printStackTrace();
        }
    }
}