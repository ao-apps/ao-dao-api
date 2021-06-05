/*
 * ao-dao-api - Simple data access objects framework API.
 * Copyright (C) 2011, 2013, 2015, 2016, 2020, 2021  AO Industries, Inc.
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
package com.aoapps.dao;

import com.aoapps.lang.Throwables;
import java.sql.SQLException;
import java.util.List;

public class ReasonsSQLException
	extends SQLException
{

	private static final long serialVersionUID = 2L;

	private final List<? extends Reason> reasons;

	/**
	 * @param  reasons  No defensive copy is made
	 *
	 * @deprecated  Please provide SQLSTATE to {@link #ReasonsSQLException(java.lang.String, java.lang.String, java.util.List)}
	 */
	@Deprecated
	public ReasonsSQLException(String message, List<? extends Reason> reasons) {
		super(message);
		this.reasons = reasons;
	}

	/**
	 * @param  reasons  No defensive copy is made
	 */
	public ReasonsSQLException(String message, String sqlState, List<? extends Reason> reasons) {
		super(message, sqlState);
		this.reasons = reasons;
	}

	/**
	 * @return  No defensive copy is made
	 */
	@SuppressWarnings("ReturnOfCollectionOrArrayField")
	public List<? extends Reason> getReasons() {
		return reasons;
	}

	static {
		Throwables.registerSurrogateFactory(ReasonsSQLException.class, (template, cause) -> {
			ReasonsSQLException newEx = new ReasonsSQLException(template.getMessage(), template.getSQLState(), template.reasons);
			newEx.initCause(cause);
			return newEx;
		});
	}
}
