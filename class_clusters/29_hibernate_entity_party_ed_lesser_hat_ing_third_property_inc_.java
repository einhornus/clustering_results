/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.cache.ehcache.internal.regions;

import java.util.Properties;

import net.sf.ehcache.Ehcache;

import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory;
import org.hibernate.cache.spi.TimestampsRegion;

/**
 * A timestamps region specific wrapper around an Ehcache instance.
 *
 * @author Chris Dennis
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhcacheTimestampsRegion extends EhcacheGeneralDataRegion implements TimestampsRegion {
	/**
	 * Constructs an EhcacheTimestampsRegion around the given underlying cache.
	 *
	 * @param accessStrategyFactory The factory for building needed CollectionRegionAccessStrategy instance
	 * @param underlyingCache The ehcache cache instance
	 * @param properties Any additional[ properties
	 */
	public EhcacheTimestampsRegion(
			EhcacheAccessStrategyFactory accessStrategyFactory,
			Ehcache underlyingCache,
			Properties properties) {
		super( accessStrategyFactory, underlyingCache, properties );
	}
}

--------------------

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.persister.collection;

/**
 * The names of all the collection properties.
 *
 * @author josh
 */
public final class CollectionPropertyNames {
	private CollectionPropertyNames() {
	}

	public static final String COLLECTION_SIZE = "size";
	public static final String COLLECTION_ELEMENTS = "elements";
	public static final String COLLECTION_INDICES = "indices";
	public static final String COLLECTION_MAX_INDEX = "maxIndex";
	public static final String COLLECTION_MIN_INDEX = "minIndex";
	public static final String COLLECTION_MAX_ELEMENT = "maxElement";
	public static final String COLLECTION_MIN_ELEMENT = "minElement";
	public static final String COLLECTION_INDEX = "index";
}

--------------------

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008-2013, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.bytecode.internal.javassist;

import java.io.Serializable;

import org.hibernate.PropertyAccessException;
import org.hibernate.bytecode.spi.ReflectionOptimizer;
import org.hibernate.cfg.AvailableSettings;

/**
 * The {@link org.hibernate.bytecode.spi.ReflectionOptimizer.AccessOptimizer} implementation for Javassist
 * which simply acts as an adapter to the {@link BulkAccessor} class.
 *
 * @author Steve Ebersole
 */
public class AccessOptimizerAdapter implements ReflectionOptimizer.AccessOptimizer, Serializable {

	private static final String PROPERTY_GET_EXCEPTION = String.format(
			"exception getting property value with Javassist (set %s to false for more info)",
			AvailableSettings.USE_REFLECTION_OPTIMIZER
	);

	private static final String PROPERTY_SET_EXCEPTION =  String.format(
			"exception setting property value with Javassist (set %s to false for more info)",
			AvailableSettings.USE_REFLECTION_OPTIMIZER
	);

	private final BulkAccessor bulkAccessor;
	private final Class mappedClass;

	/**
	 * Constructs an AccessOptimizerAdapter
	 *
	 * @param bulkAccessor The bulk accessor to use
	 * @param mappedClass The mapped class
	 */
	public AccessOptimizerAdapter(BulkAccessor bulkAccessor, Class mappedClass) {
		this.bulkAccessor = bulkAccessor;
		this.mappedClass = mappedClass;
	}

	@Override
	public String[] getPropertyNames() {
		return bulkAccessor.getGetters();
	}

	@Override
	public Object[] getPropertyValues(Object object) {
		try {
			return bulkAccessor.getPropertyValues( object );
		}
		catch ( Throwable t ) {
			throw new PropertyAccessException(
					t,
					PROPERTY_GET_EXCEPTION,
					false,
					mappedClass,
					getterName( t, bulkAccessor )
			);
		}
	}

	@Override
	public void setPropertyValues(Object object, Object[] values) {
		try {
			bulkAccessor.setPropertyValues( object, values );
		}
		catch ( Throwable t ) {
			throw new PropertyAccessException(
					t,
					PROPERTY_SET_EXCEPTION,
					true,
					mappedClass,
					setterName( t, bulkAccessor )
			);
		}
	}

	private static String setterName(Throwable t, BulkAccessor accessor) {
		if (t instanceof BulkAccessorException ) {
			return accessor.getSetters()[ ( (BulkAccessorException) t ).getIndex() ];
		}
		else {
			return "?";
		}
	}

	private static String getterName(Throwable t, BulkAccessor accessor) {
		if (t instanceof BulkAccessorException ) {
			return accessor.getGetters()[ ( (BulkAccessorException) t ).getIndex() ];
		}
		else {
			return "?";
		}
	}
}

--------------------

