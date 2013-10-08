/**
 * Copyright 2013 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.ordina.bag.etl.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanLocator
{
	private static BeanLocator beanLocator;
	private ApplicationContext context;

	private BeanLocator(String...files)
	{
		context = new ClassPathXmlApplicationContext(files);
	}

	public static BeanLocator getInstance(String...files)
	{
		if (beanLocator == null)
			beanLocator = new BeanLocator(files);
		return beanLocator;
	}

	public Object get(String id)
	{
		return context.getBean(id);
	}

}
