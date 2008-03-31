/*
 * Copyright 2008 Alin Dreghiciu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.qi4j.query;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.qi4j.property.Property;
import org.qi4j.query.grammar.BooleanExpression;
import org.qi4j.query.grammar.Conjunction;
import org.qi4j.query.grammar.Disjunction;
import org.qi4j.query.grammar.EqualsPredicate;
import org.qi4j.query.grammar.Negation;
import org.qi4j.query.grammar.PropertyReference;
import org.qi4j.query.grammar.SingleValueExpression;
import org.qi4j.query.grammar.impl.VariableValueExpression;

/**
 * Unit tests for {@link QueryExpressions}.
 */
public class QueryExpressionsTest
{

    /**
     * Tests a valid "equals".
     */
    @Test
    public void validEqual()
    {
        StringPropertyReference property = createMock( StringPropertyReference.class );
        EqualsPredicate<String> operator = QueryExpressions.eq( property, "Foo" );
        assertThat( "Property", operator.getPropertyReference(), is( equalTo( (PropertyReference) property ) ) );
        assertThat( "Value", ( (SingleValueExpression<String>) operator.getValueExpression() ).getValue(), is( equalTo( "Foo" ) ) );
    }

    /**
     * Tests a valid "equals" with a variable value.
     */
    @Test
    public void validEqualWithVariableValue()
    {
        StringPropertyReference property = createMock( StringPropertyReference.class );
        VariableValueExpression<String> variableExpression = QueryExpressions.variable( "var" );
        EqualsPredicate<String> operator = QueryExpressions.eq( property, variableExpression );
        variableExpression.setValue( "Foo" );
        assertThat( "Property", operator.getPropertyReference(), is( equalTo( (PropertyReference) property ) ) );
        assertThat( "Value", ( (VariableValueExpression<String>) operator.getValueExpression() ).getValue(), is( equalTo( "Foo" ) ) );
    }

    /**
     * Tests a valid "and".
     */
    @Test
    public void validAnd()
    {
        BooleanExpression left = createMock( BooleanExpression.class );
        BooleanExpression right = createMock( BooleanExpression.class );
        Conjunction conjunction = QueryExpressions.and( left, right );
        assertThat( "Left side expression", conjunction.getLeftSideExpression(), is( equalTo( left ) ) );
        assertThat( "Right side expression", conjunction.getRightSideExpression(), is( equalTo( right ) ) );
    }

    /**
     * Tests a valid "or".
     */
    @Test
    public void validOr()
    {
        BooleanExpression left = createMock( BooleanExpression.class );
        BooleanExpression right = createMock( BooleanExpression.class );
        Disjunction disjunction = QueryExpressions.or( left, right );
        assertThat( "Left side expression", disjunction.getLeftSideExpression(), is( equalTo( left ) ) );
        assertThat( "Right side expression", disjunction.getRightSideExpression(), is( equalTo( right ) ) );
    }

    /**
     * Tests a valid "not".
     */
    @Test
    public void validNot()
    {
        BooleanExpression expression = createMock( BooleanExpression.class );
        Negation negation = QueryExpressions.not( expression );
        assertThat( "Expression", negation.getExpression(), is( equalTo( expression ) ) );
    }

    static interface StringPropertyReference
        extends Property<String>, PropertyReference
    {

    }

}