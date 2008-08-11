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
package org.qi4j.runtime.query;

import java.lang.reflect.Proxy;
import org.qi4j.query.QueryExpressions;
import org.qi4j.query.QueryExpressionsProvider;
import org.qi4j.query.grammar.AssociationIsNotNullPredicate;
import org.qi4j.query.grammar.AssociationIsNullPredicate;
import org.qi4j.query.grammar.AssociationReference;
import org.qi4j.query.grammar.BooleanExpression;
import org.qi4j.query.grammar.Conjunction;
import org.qi4j.query.grammar.Disjunction;
import org.qi4j.query.grammar.EqualsPredicate;
import org.qi4j.query.grammar.GreaterOrEqualPredicate;
import org.qi4j.query.grammar.GreaterThanPredicate;
import org.qi4j.query.grammar.LessOrEqualPredicate;
import org.qi4j.query.grammar.LessThanPredicate;
import org.qi4j.query.grammar.MatchesPredicate;
import org.qi4j.query.grammar.Negation;
import org.qi4j.query.grammar.NotEqualsPredicate;
import org.qi4j.query.grammar.OrderBy;
import org.qi4j.query.grammar.PropertyIsNotNullPredicate;
import org.qi4j.query.grammar.PropertyIsNullPredicate;
import org.qi4j.query.grammar.PropertyReference;
import org.qi4j.query.grammar.SingleValueExpression;
import org.qi4j.query.grammar.VariableValueExpression;
import org.qi4j.runtime.query.grammar.impl.AssociationIsNotNullPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.AssociationIsNullPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.ConjunctionImpl;
import org.qi4j.runtime.query.grammar.impl.DisjunctionImpl;
import org.qi4j.runtime.query.grammar.impl.EqualsPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.GreaterOrEqualPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.GreaterThanPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.LessOrEqualPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.LessThanPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.MatchesPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.NegationImpl;
import org.qi4j.runtime.query.grammar.impl.NotEqualsPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.OrderByImpl;
import org.qi4j.runtime.query.grammar.impl.PropertyIsNotNullPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.PropertyIsNullPredicateImpl;
import org.qi4j.runtime.query.grammar.impl.SingleValueExpressionImpl;
import org.qi4j.runtime.query.grammar.impl.VariableValueExpressionImpl;
import org.qi4j.runtime.query.proxy.MixinTypeProxy;

public class QueryExpressionsProviderImpl
    implements QueryExpressionsProvider
{

    /**
     * Creates a template for the a mixin type to be used to access properties in type safe fashion.
     *
     * @param mixinType mixin type
     * @return template instance
     */
    @SuppressWarnings( "unchecked" )
    public <T> T templateFor( final Class<T> mixinType )
    {
        return (T) Proxy.newProxyInstance(
            QueryExpressions.class.getClassLoader(),
            new Class[]{ mixinType },
            new MixinTypeProxy( mixinType )
        );
    }

    public <T> VariableValueExpression<T> newVariableValueExpression( String name )
    {
        return new VariableValueExpressionImpl<T>( name );
    }

    public <T> PropertyIsNullPredicate<T> newPropertyIsNullPredicate( PropertyReference<T> tPropertyReference )
    {
        return new PropertyIsNullPredicateImpl<T>( tPropertyReference );
    }

    public AssociationIsNullPredicate newAssociationIsNullPredicate( AssociationReference associationReference )
    {
        return new AssociationIsNullPredicateImpl( associationReference );
    }

    public <T> PropertyIsNotNullPredicate<T> newPropertyIsNotNullPredicate( PropertyReference<T> tPropertyReference )
    {
        return new PropertyIsNotNullPredicateImpl<T>( tPropertyReference );
    }

    public AssociationIsNotNullPredicate newAssociationIsNotNullPredicate( AssociationReference associationReference )
    {
        return new AssociationIsNotNullPredicateImpl( associationReference );
    }

    public <T> EqualsPredicate<T> newEqualsPredicate( PropertyReference<T> tPropertyReference, SingleValueExpression<T> tStaticValueExpression )
    {
        return new EqualsPredicateImpl<T>( tPropertyReference, tStaticValueExpression );
    }

    public <T> EqualsPredicate<T> newEqualsPredicate( PropertyReference<T> tPropertyReference, VariableValueExpression<T> valueExpression )
    {
        return new EqualsPredicateImpl<T>( tPropertyReference, valueExpression );
    }

    public <T> NotEqualsPredicate<T> newNotEqualsPredicate( PropertyReference<T> tPropertyReference, SingleValueExpression<T> tStaticValueExpression )
    {
        return new NotEqualsPredicateImpl<T>( tPropertyReference, tStaticValueExpression );
    }

    public <T> NotEqualsPredicate<T> newNotEqualsPredicate( PropertyReference<T> tPropertyReference, VariableValueExpression<T> valueExpression )
    {
        return new NotEqualsPredicateImpl<T>( tPropertyReference, valueExpression );
    }

    public <T> LessThanPredicate<T> newLessThanPredicate( PropertyReference<T> tPropertyReference, SingleValueExpression<T> tStaticValueExpression )
    {
        return new LessThanPredicateImpl<T>( tPropertyReference, tStaticValueExpression );
    }

    public <T> LessThanPredicate<T> newLessThanPredicate( PropertyReference<T> tPropertyReference, VariableValueExpression<T> valueExpression )
    {
        return new LessThanPredicateImpl<T>( tPropertyReference, valueExpression );
    }

    public <T> LessOrEqualPredicate<T> newLessOrEqualPredicate( PropertyReference<T> tPropertyReference, SingleValueExpression<T> tStaticValueExpression )
    {
        return new LessOrEqualPredicateImpl<T>( tPropertyReference, tStaticValueExpression );
    }

    public <T> LessOrEqualPredicate<T> newLessOrEqualPredicate( PropertyReference<T> tPropertyReference, VariableValueExpression<T> valueExpression )
    {
        return new LessOrEqualPredicateImpl<T>( tPropertyReference, valueExpression );
    }

    public <T> GreaterThanPredicate<T> newGreaterThanPredicate( PropertyReference<T> tPropertyReference, SingleValueExpression<T> tStaticValueExpression )
    {
        return new GreaterThanPredicateImpl<T>( tPropertyReference, tStaticValueExpression );
    }

    public <T> GreaterThanPredicate<T> newGreaterThanPredicate( PropertyReference<T> tPropertyReference, VariableValueExpression<T> valueExpression )
    {
        return new GreaterThanPredicateImpl<T>( tPropertyReference, valueExpression );
    }

    public <T> GreaterOrEqualPredicate<T> newGreaterOrEqualPredicate( PropertyReference<T> tPropertyReference, SingleValueExpression<T> tStaticValueExpression )
    {
        return new GreaterOrEqualPredicateImpl<T>( tPropertyReference, tStaticValueExpression );
    }

    public <T> GreaterOrEqualPredicate<T> newGreaterOrEqualPredicate( PropertyReference<T> tPropertyReference, VariableValueExpression<T> valueExpression )
    {
        return new GreaterOrEqualPredicateImpl<T>( tPropertyReference, valueExpression );
    }

    public MatchesPredicate newMatchesPredicate( PropertyReference<String> stringPropertyReference, SingleValueExpression<String> stringSingleValueExpression )
    {
        return new MatchesPredicateImpl( stringPropertyReference, stringSingleValueExpression );
    }

    public Conjunction newConjunction( BooleanExpression left, BooleanExpression right )
    {
        return new ConjunctionImpl( left, right );
    }

    public Disjunction newDisjunction( BooleanExpression left, BooleanExpression right )
    {
        return new DisjunctionImpl( left, right );
    }

    public Negation newNegation( BooleanExpression expression )
    {
        return new NegationImpl( expression );
    }

    public OrderBy newOrderBy( PropertyReference<?> tPropertyReference, OrderBy.Order order )
    {
        return new OrderByImpl( tPropertyReference, order );
    }

    public <T> SingleValueExpression<T> newSingleValueExpression( T value )
    {
        return new SingleValueExpressionImpl<T>( value );
    }
}