/*
 * ao-dao-api - Simple data access objects framework API.
 * Copyright (C) 2011, 2013, 2015, 2016, 2020, 2021, 2022  AO Industries, Inc.
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
 * along with ao-dao-api.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aoapps.dao;

import com.aoapps.lang.RunnableE;
import com.aoapps.lang.concurrent.CallableE;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
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
   * Gets the comparator used by this model.
   */
  Comparator<? super String> getComparator();

  /**
   * Gets the set of all tables in this model.  This is a map keyed on table
   * name to be useful in JSP EL without requiring a separate getter for each
   * table.
   */
  Map<String, ? extends Table<?, ?>> getTables();

  /**
   * Clears all caches for all tables for the current thread.
   */
  default void clearAllCaches() {
    for (Table<?, ?> table : getTables().values()) {
      table.clearCaches();
    }
  }

  /**
   * Executes an arbitrary transaction, providing automatic commit, rollback, and connection management.
   *
   * @see  #transactionRun(com.aoapps.lang.RunnableE)
   */
  default <V> V transactionCall(CallableE<? extends V, ? extends SQLException> callable) throws SQLException {
    return transactionCall(SQLException.class, callable);
  }

  /**
   * Executes an arbitrary transaction, providing automatic commit, rollback, and connection management.
   *
   * @param  <Ex>  An arbitrary exception type that may be thrown
   *
   * @see  #transactionRun(java.lang.Class, com.aoapps.lang.RunnableE)
   */
  <V, Ex extends Throwable> V transactionCall(Class<? extends Ex> exClass, CallableE<? extends V, ? extends Ex> callable) throws SQLException, Ex;

  /**
   * Executes an arbitrary transaction, providing automatic commit, rollback, and connection management.
   *
   * @see  #transactionCall(com.aoapps.lang.concurrent.CallableE)
   */
  default void transactionRun(RunnableE<? extends SQLException> runnable) throws SQLException {
    transactionCall(SQLException.class, () -> {
      runnable.run();
      return null;
    });
  }

  /**
   * Executes an arbitrary transaction, providing automatic commit, rollback, and connection management.
   *
   * @deprecated  Please use {@link #transactionRun(com.aoapps.lang.RunnableE)}
   */
  @Deprecated(forRemoval = true)
  default void executeTransaction(Runnable runnable) throws SQLException {
    transactionRun(runnable::run);
  }

  /**
   * Executes an arbitrary transaction, providing automatic commit, rollback, and connection management.
   *
   * @param  <Ex>  An arbitrary exception type that may be thrown
   *
   * @see  #transactionCall(java.lang.Class, com.aoapps.lang.concurrent.CallableE)
   */
  default <Ex extends Throwable> void transactionRun(Class<? extends Ex> exClass, RunnableE<? extends Ex> runnable) throws SQLException, Ex {
    transactionCall(exClass, () -> {
      runnable.run();
      return null;
    });
  }

  /**
   * Gets the set of all reports that are supported by this repository implementation, keyed on its unique name.
   */
  default Map<String, ? extends Report> getReports() throws SQLException {
    // By default, there are no reports.
    return Collections.emptyMap();
  }
}
