/*
 * Copyright 2008 Niclas Hedhman.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.qi4j.api.unitofwork;

/**
 * If you try to create an EntityComposite whose identity already exists,
 * then this exception will be thrown.
 */
public class EntityCompositeAlreadyExistsException
    extends UnitOfWorkException
{
    private static final long serialVersionUID = -7297710939536508481L;

    private final String identity;
    private final String type;

    public EntityCompositeAlreadyExistsException( String identity, String type )
    {
        super( "EntityComposite (" + identity + " of type " + type + ") already exists." );
        this.identity = identity;
        this.type = type;
    }

    public String identity()
    {
        return identity;
    }

    public String type()
    {
        return type;
    }
}