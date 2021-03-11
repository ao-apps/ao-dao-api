/*
 * ao-dao-api - Simple data access objects framework API.
 * Copyright (C) 2013, 2015, 2016, 2021  AO Industries, Inc.
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

/**
 * A compound key with three columns.
 *
 * @author  AO Industries, Inc.
 */
public interface Tuple3<
	C1 extends Comparable<? super C1>,
	C2 extends Comparable<? super C2>,
	C3 extends Comparable<? super C3>,
	T extends Tuple3<C1, C2, C3, T> & Comparable<? super T>
>
	extends Tuple<T>
{

	C1 getColumn1();

	C2 getColumn2();

	C3 getColumn3();
}
