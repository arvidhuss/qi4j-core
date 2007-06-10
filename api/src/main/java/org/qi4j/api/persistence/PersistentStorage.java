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
package org.qi4j.api.persistence;

import org.qi4j.api.persistence.composite.PersistenceComposite;
import java.io.Serializable;


/**
 * Persistent repositories must implement this.
 */
public interface PersistentStorage
{
    void create( PersistenceComposite aProxy )
        throws PersistenceException;

    void read( PersistenceComposite aProxy )
        throws PersistenceException;

    void update( PersistenceComposite aProxy, Serializable aMixin )
        throws PersistenceException;

    void delete( PersistenceComposite aProxy )
        throws PersistenceException;
}