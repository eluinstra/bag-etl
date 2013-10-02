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

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import org.springframework.util.Log4jConfigurer;

public class Log4jUtils
{
	private static final class DefaultFilter extends Filter
	{
		private final Level level;

		private DefaultFilter(Level level)
		{
			this.level = level;
		}

		@Override
		public int decide(LoggingEvent event)
		{
		  if (event.getLevel().equals(level))
		    return ACCEPT;
		  else
		    return DENY;
		}
	}

	public static void loadConfig(String filename) throws FileNotFoundException
	{
		if (filename != null)
		{
			LogManager.resetConfiguration();
			Log4jConfigurer.initLogging(filename);
		}
	}
	
	public static void loadConfig(String filename, long refreshInterval) throws FileNotFoundException
	{
		if (filename != null)
		{
			LogManager.resetConfiguration();
			Log4jConfigurer.initLogging(filename,refreshInterval);
		}
	}
	
	public static String[] getLoggers()
	{
		ArrayList<String> result = new ArrayList<String>();
		for (@SuppressWarnings("unchecked")Enumeration<Logger> loggers = LogManager.getCurrentLoggers(); loggers.hasMoreElements();)
		{
			Logger logger = loggers.nextElement();
			result.add(logger.getName() + ": " + logger.getLevel());
		}
		Collections.sort(result);
		return result.toArray(new String[0]);
	}

	public static void setLogLevel(Level level)
	{
		LogManager.getRootLogger().setLevel(level);
	}
	
	public static void setLogLevel(String name, Level level)
	{
		Logger logger = LogManager.getLogger(name);
		if (logger != null)
			logger.setLevel(level);
	}

	public static void streamLogging(OutputStream stream)
	{
		streamLogging(stream,null);
	}
	
	public static void streamLogging(OutputStream stream, final Level level)
	{
		streamLogging(stream,level,null);
	}

	public static void streamLogging(OutputStream stream, final Level level, String pattern)
	{
		WriterAppender appender = new WriterAppender(pattern == null ? new PatternLayout() : new PatternLayout(pattern),stream);
		if (level != null)
			appender.addFilter(new DefaultFilter(level));
		Logger.getRootLogger().addAppender(appender);
	}
	
	public static void streamLogging(Writer writer)
	{
		streamLogging(writer,null);
	}

	public static void streamLogging(Writer writer, final Level level)
	{
		streamLogging(writer,level,null);
	}

	public static void streamLogging(Writer writer, final Level level, String pattern)
	{
		WriterAppender appender = new WriterAppender(pattern == null ? new PatternLayout() : new PatternLayout(pattern),writer);
		if (level != null)
			appender.addFilter(new DefaultFilter(level));
		Logger.getRootLogger().addAppender(appender);
	}
	
}
