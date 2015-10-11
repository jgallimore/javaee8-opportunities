/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.supertribe.interceptors.interim.stack;

import org.apache.openejb.util.Join;
import org.junit.Assert;
import org.junit.Test;

import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

public class InterceptorStackTest {

    @Test
    public void test() throws Exception {
        final ArrayList<Interception> interceptors = new ArrayList<>();

        interceptors.add(new DefaultInterceptorOne()::businessMethodInterceptor);
        interceptors.add(new DefaultInterceptorTwo()::businessMethodInterceptor);
        interceptors.add(new ClassLevelInterceptorSuperClassOne()::businessMethodInterceptor);
        interceptors.add(new ClassLevelInterceptorSuperClassTwo()::businessMethodInterceptor);
        interceptors.add(new ClassLevelInterceptorOne()::businessMethodInterceptor);
        interceptors.add(new ClassLevelInterceptorTwo()::businessMethodInterceptor);
        interceptors.add(new MethodLevelInterceptorOne()::businessMethodInterceptor);
        interceptors.add(new MethodLevelInterceptorTwo()::businessMethodInterceptor);
        interceptors.add(new FullyInterceptedBean()::beanClassBusinessMethodInterceptor);

        final InterceptorStack stack = new InterceptorStack(new FullyInterceptedBean(), FullyInterceptedBean.class.getMethod("businessMethod", int.class, String.class), interceptors);

        final List<String> invoke = (List<String>) stack.invoke(42, "Answer");

        final List<String> expected = new ArrayList<String>();
        expected.add("Before:DefaultInterceptorOne");
        expected.add("Before:DefaultInterceptorTwo");
        expected.add("Before:ClassLevelInterceptorSuperClassOne");
        expected.add("Before:ClassLevelInterceptorSuperClassTwo");
        expected.add("Before:ClassLevelInterceptorOne");
        expected.add("Before:ClassLevelInterceptorTwo");
        expected.add("Before:MethodLevelInterceptorOne");
        expected.add("Before:MethodLevelInterceptorTwo");
        expected.add("Before:beanClassBusinessMethodInterceptor");
        expected.add("businessMethod");
        expected.add("42, Answer");
        expected.add("After:beanClassBusinessMethodInterceptor");
        expected.add("After:MethodLevelInterceptorTwo");
        expected.add("After:MethodLevelInterceptorOne");
        expected.add("After:ClassLevelInterceptorTwo");
        expected.add("After:ClassLevelInterceptorOne");
        expected.add("After:ClassLevelInterceptorSuperClassTwo");
        expected.add("After:ClassLevelInterceptorSuperClassOne");
        expected.add("After:DefaultInterceptorTwo");
        expected.add("After:DefaultInterceptorOne");

        Assert.assertEquals(Join.join("\n", invoke), Join.join("\n", expected));
    }


}
