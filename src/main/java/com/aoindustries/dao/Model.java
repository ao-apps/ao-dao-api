/*
 * ao-dao-api - Simple data access objects framework API.
 * Copyright (C) 2011, 2013, 2015, 2016, 2020  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-dao-api.
 *
 * ao-dao-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-dao-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-dao-api.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.dao;

import com.aoindustries.lang.RunnableE;
import com.aoindustries.util.concurrent.CallableE;
import java.sql.SQLException;
import java.text.Collator;
import java.util.Collections;
import java.util.Map;

/**
 * A model is a collection of tables, and a collection of reports.
 */
public interface Model {

	/**
	 * Gets the name of this model.
	 */
	String getName();

	/**
	 * Gets the collator used by this model.
	 */
	Collator getCollator();

	/**
	 * Gets the set of all tables in this model.  This is a map keyed on table
	 * name to be useful in JSP EL without requiring a separate getter for each
	 * table.
	 */
	Map<String,? extends Table<?,?>> getTables();

	/**
	 * Clears all caches for all tables for the current thread.
	 */
	default void clearAllCaches() {
		for(Table<?,?> table : getTables().values()) {
			table.clearCaches();
		}
	}

	/**
	 * Executes a transaction between any number of calls to this model and its tables.
	 *
	 * @see  #run(com.aoindustries.lang.RunnableE)
	 */
	default <V> V call(CallableE<? extends V,? extends SQLException> callable) throws SQLException {
		return call(SQLException.class, callable);
	}

	/**
	 * Executes a transaction between any number of calls to this model and its tables.
	 *
	 * @see  #run(java.lang.Class, com.aoindustries.lang.RunnableE)
	 */
	<V,E extends Throwable> V call(Class<? extends E> eClass, CallableE<? extends V,? extends E> callable) throws SQLException, E;

	/**
	 * Executes a transaction between any number of calls to this model and its tables.
	 *
	 * @see  #call(com.aoindustries.util.concurrent.CallableE)
	 */
	default void run(RunnableE<? extends SQLException> runnable) throws SQLException {
		call(SQLException.class, () -> {
			runnable.run();
			return null;
		});
	}

	/**
	 * @deprecated  Please use {@link #run(com.aoindustries.lang.RunnableE)}
	 */
	@Deprecated
	default void executeTransaction(Runnable runnable) throws SQLException {
		run(() -> runnable.run());
	}

	/**
	 * Executes a transaction between any number of calls to this model and its tables.
	 *
	 * @see  #call(java.lang.Class, com.aoindustries.util.concurrent.CallableE)
	 */
	default <E extends Throwable> void run(Class<? extends E> eClass, RunnableE<? extends E> runnable) throws SQLException, E {
		call(eClass, () -> {
			runnable.run();
			return null;
		});
	}

	/**
	 * Gets the set of all reports that are supported by this repository implementation, keyed on its unique name.
	 */
	default Map<String,? extends Report> getReports() throws SQLException {
		// By default, there are no reports.
		return Collections.emptyMap();
	}
}
