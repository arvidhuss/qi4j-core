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
package org.qi4j.api.model;

import java.util.ArrayList;
import java.util.List;
import org.qi4j.api.annotation.ModifiedBy;

/**
 * A mixin is an implementation of a particular interface,
 * and is used as a fragment in a composite.
 *
 * @author rickard
 * @version $Revision: 1.0 $
 */
public final class Mixin
    extends Fragment
{
    // Attribute -----------------------------------------------------
    List<Modifier> modifiers;

    // Constructors --------------------------------------------------
    public Mixin( Class mixinClass )
    {
        super( mixinClass );

        modifiers = new ArrayList<Modifier>();
        findModifiers( mixinClass );
    }

    public List<Modifier> getModifiers()
    {
        return modifiers;
    }

    // Private ------------------------------------------------------
    private void findModifiers( Class aClass )
    {
        ModifiedBy modifiedBy = (ModifiedBy) aClass.getAnnotation( ModifiedBy.class );
        if( modifiedBy != null )
        {
            for( Class modifier : modifiedBy.value() )
            {
                modifiers.add( new Modifier( modifier ) );
            }
        }

        // Check superclass
        if( !aClass.isInterface() && aClass != Object.class )
        {
            findModifiers( aClass.getSuperclass() );
        }
    }
}