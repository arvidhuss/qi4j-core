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

package org.qi4j.spi.entitystore;

import org.qi4j.api.Qi4j;
import org.qi4j.api.concern.ConcernOf;
import org.qi4j.api.entity.EntityReference;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.usecase.Usecase;
import org.qi4j.spi.entity.EntityDescriptor;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.structure.ModuleSPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Concern that helps EntityStores do concurrent modification checks.
 * <p/>
 * It caches the versions of state that it loads, and forgets them when
 * the state is committed. For normal operation this means that it does
 * not have to go down to the underlying store to get the current version.
 * Whenever there is a concurrent modification the store will most likely
 * have to check with the underlying store what the current version is.
 */
public abstract class ConcurrentModificationCheckConcern
    extends ConcernOf<EntityStore>
    implements EntityStore
{
    @This
    private EntityStateVersions versions;
    @Structure
    private Qi4j api;

    @This
    private ReadWriteLock lock;


    public EntityStoreUnitOfWork newUnitOfWork( Usecase usecase, ModuleSPI module )
    {
        final EntityStoreUnitOfWork uow = next.newUnitOfWork( usecase, module );
        return new ConcurrentCheckingEntityStoreUnitOfWork( uow, api.dereference( versions ), api.dereference( lock ), module );
    }

    private class ConcurrentCheckingEntityStoreUnitOfWork
        implements EntityStoreUnitOfWork
    {
        private final EntityStoreUnitOfWork uow;
        private EntityStateVersions versions;
        private ReadWriteLock lock;
        private ModuleSPI module;

        private List<EntityState> loaded = new ArrayList<EntityState>();

        public ConcurrentCheckingEntityStoreUnitOfWork( EntityStoreUnitOfWork uow,
                                                        EntityStateVersions versions,
                                                        ReadWriteLock lock,
                                                        ModuleSPI module
        )
        {
            this.uow = uow;
            this.versions = versions;
            this.lock = lock;
            this.module = module;
        }

        public String identity()
        {
            return uow.identity();
        }

        public EntityState newEntityState( EntityReference anIdentity, EntityDescriptor entityDescriptor )
            throws EntityStoreException
        {
           return uow.newEntityState( anIdentity, entityDescriptor );
        }

        public StateCommitter applyChanges()
            throws EntityStoreException
        {
           lock.writeLock().lock();

           try
           {

               versions.checkForConcurrentModification( loaded, module );

               final StateCommitter committer = uow.applyChanges();


               return new StateCommitter()
               {
                   public void commit()
                   {
                       committer.commit();
                       versions.forgetVersions( loaded );
                       lock.writeLock().unlock();
                   }

                   public void cancel()
                   {
                       committer.cancel();
                       versions.forgetVersions( loaded );
                       lock.writeLock().unlock();
                   }
               };
           } catch( EntityStoreException e )
           {
              lock.writeLock().unlock();
              throw e;
           }
        }

        public void discard()
        {
            try
            {
                uow.discard();
            }
            finally
            {
               lock.writeLock().lock();

               try
               {
                  versions.forgetVersions( loaded );
               } finally
               {
                  lock.writeLock().unlock();
               }
            }
        }

        public EntityState getEntityState( EntityReference anIdentity )
            throws EntityStoreException, EntityNotFoundException
        {
            lock.readLock().lock();

           try
           {
              EntityState entityState = uow.getEntityState( anIdentity );
              versions.rememberVersion( entityState.identity(), entityState.version() );
              loaded.add( entityState );
              return entityState;
           } finally
           {
              lock.readLock().unlock();
           }
        }
    }
}
