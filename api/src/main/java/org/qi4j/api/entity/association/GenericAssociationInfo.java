/*
 * Copyright 2008 Niclas Hedhman. All rights Reserved.
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
package org.qi4j.api.entity.association;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.qi4j.api.common.MetaInfo;
import org.qi4j.api.common.QualifiedName;
import org.qi4j.api.entity.Aggregated;
import org.qi4j.api.property.Immutable;

public class GenericAssociationInfo
    implements AssociationInfo
{
    public static Type getAssociationType( Method accessor )
    {
        return getAssociationType( accessor.getGenericReturnType() );
    }

    public static Type getAssociationType( Type methodReturnType )
    {
        if( methodReturnType instanceof ParameterizedType )
        {
            ParameterizedType parameterizedType = (ParameterizedType) methodReturnType;
            if( AbstractAssociation.class.isAssignableFrom( (Class<?>) parameterizedType.getRawType() ) )
            {
                return parameterizedType.getActualTypeArguments()[ 0 ];
            }
        }

        Type[] interfaces = ( (Class<?>) methodReturnType ).getGenericInterfaces();
        for( Type anInterface : interfaces )
        {
            Type associationType = getAssociationType( anInterface );
            if( associationType != null )
            {
                return associationType;
            }
        }
        return null;
    }

    /**
     * Get qualified association name from a URI
     *
     * @param uri of the association
     * @return qualified association name
     */
    public static String toQualifiedName( final String uri )
    {
        return uri.substring( "urn:qi4j:entitytype:".length() ).replace( '#', ':' );
    }

    private QualifiedName qualifiedName;
    private Type type;
    private MetaInfo metainfo;
    private boolean immutable;
    private boolean aggregated;

    public GenericAssociationInfo( Method accessor, MetaInfo metainfo )
    {
        this.qualifiedName = QualifiedName.fromMethod(accessor);
        this.metainfo = metainfo;
        immutable = metainfo.get( Immutable.class ) != null;
        aggregated = metainfo.get( Aggregated.class ) != null;
        Type methodReturnType = accessor.getGenericReturnType();
        type = getAssociationType( methodReturnType );
    }

    public <T> T metaInfo( Class<T> infoType )
    {
        return metainfo.get( infoType );
    }

    public String name()
    {
        return qualifiedName.name();
    }

    public QualifiedName qualifiedName()
    {
        return qualifiedName;
    }

    public Type type()
    {
        return type;
    }

    public boolean isImmutable()
    {
        return immutable;
    }

    public boolean isAggregated()
    {
        return aggregated;
    }
}