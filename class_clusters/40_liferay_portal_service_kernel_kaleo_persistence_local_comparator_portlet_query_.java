/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.service.persistence;

import at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator;

import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the patientnamegenerator service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author reihsr
 * @see patientnamegeneratorPersistenceImpl
 * @see patientnamegeneratorUtil
 * @generated
 */
public interface patientnamegeneratorPersistence extends BasePersistence<patientnamegenerator> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link patientnamegeneratorUtil} to access the patientnamegenerator persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the patientnamegenerators where name = &#63; and type = &#63;.
	*
	* @param name the name
	* @param type the type
	* @return the matching patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> findByNames(
		java.lang.String name, java.lang.String type)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the patientnamegenerators where name = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.impl.patientnamegeneratorModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param name the name
	* @param type the type
	* @param start the lower bound of the range of patientnamegenerators
	* @param end the upper bound of the range of patientnamegenerators (not inclusive)
	* @return the range of matching patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> findByNames(
		java.lang.String name, java.lang.String type, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the patientnamegenerators where name = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.impl.patientnamegeneratorModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param name the name
	* @param type the type
	* @param start the lower bound of the range of patientnamegenerators
	* @param end the upper bound of the range of patientnamegenerators (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> findByNames(
		java.lang.String name, java.lang.String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first patientnamegenerator in the ordered set where name = &#63; and type = &#63;.
	*
	* @param name the name
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching patientnamegenerator
	* @throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException if a matching patientnamegenerator could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator findByNames_First(
		java.lang.String name, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first patientnamegenerator in the ordered set where name = &#63; and type = &#63;.
	*
	* @param name the name
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching patientnamegenerator, or <code>null</code> if a matching patientnamegenerator could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator fetchByNames_First(
		java.lang.String name, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last patientnamegenerator in the ordered set where name = &#63; and type = &#63;.
	*
	* @param name the name
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching patientnamegenerator
	* @throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException if a matching patientnamegenerator could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator findByNames_Last(
		java.lang.String name, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last patientnamegenerator in the ordered set where name = &#63; and type = &#63;.
	*
	* @param name the name
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching patientnamegenerator, or <code>null</code> if a matching patientnamegenerator could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator fetchByNames_Last(
		java.lang.String name, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the patientnamegenerators before and after the current patientnamegenerator in the ordered set where name = &#63; and type = &#63;.
	*
	* @param patientnamegeneratorId the primary key of the current patientnamegenerator
	* @param name the name
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next patientnamegenerator
	* @throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException if a patientnamegenerator with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator[] findByNames_PrevAndNext(
		long patientnamegeneratorId, java.lang.String name,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the patientnamegenerators where name = &#63; and type = &#63; from the database.
	*
	* @param name the name
	* @param type the type
	* @throws SystemException if a system exception occurred
	*/
	public void removeByNames(java.lang.String name, java.lang.String type)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of patientnamegenerators where name = &#63; and type = &#63;.
	*
	* @param name the name
	* @param type the type
	* @return the number of matching patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public int countByNames(java.lang.String name, java.lang.String type)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Caches the patientnamegenerator in the entity cache if it is enabled.
	*
	* @param patientnamegenerator the patientnamegenerator
	*/
	public void cacheResult(
		at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator patientnamegenerator);

	/**
	* Caches the patientnamegenerators in the entity cache if it is enabled.
	*
	* @param patientnamegenerators the patientnamegenerators
	*/
	public void cacheResult(
		java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> patientnamegenerators);

	/**
	* Creates a new patientnamegenerator with the primary key. Does not add the patientnamegenerator to the database.
	*
	* @param patientnamegeneratorId the primary key for the new patientnamegenerator
	* @return the new patientnamegenerator
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator create(
		long patientnamegeneratorId);

	/**
	* Removes the patientnamegenerator with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param patientnamegeneratorId the primary key of the patientnamegenerator
	* @return the patientnamegenerator that was removed
	* @throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException if a patientnamegenerator with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator remove(
		long patientnamegeneratorId)
		throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException,
			com.liferay.portal.kernel.exception.SystemException;

	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator updateImpl(
		at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator patientnamegenerator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the patientnamegenerator with the primary key or throws a {@link at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException} if it could not be found.
	*
	* @param patientnamegeneratorId the primary key of the patientnamegenerator
	* @return the patientnamegenerator
	* @throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException if a patientnamegenerator with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator findByPrimaryKey(
		long patientnamegeneratorId)
		throws at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.NoSuchpatientnamegeneratorException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the patientnamegenerator with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param patientnamegeneratorId the primary key of the patientnamegenerator
	* @return the patientnamegenerator, or <code>null</code> if a patientnamegenerator with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator fetchByPrimaryKey(
		long patientnamegeneratorId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the patientnamegenerators.
	*
	* @return the patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the patientnamegenerators.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.impl.patientnamegeneratorModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of patientnamegenerators
	* @param end the upper bound of the range of patientnamegenerators (not inclusive)
	* @return the range of patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the patientnamegenerators.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.impl.patientnamegeneratorModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of patientnamegenerators
	* @param end the upper bound of the range of patientnamegenerators (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<at.graz.meduni.liferay.portlet.bibbox.kdssmp.service.model.patientnamegenerator> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the patientnamegenerators from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of patientnamegenerators.
	*
	* @return the number of patientnamegenerators
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}
--------------------

/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package at.graz.meduni.liferay.portlet.saat.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.security.ac.AccessControlled;
import com.liferay.portal.service.BaseService;
import com.liferay.portal.service.InvokableService;

/**
 * Provides the remote service interface for Dictionary. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author reihsr
 * @see DictionaryServiceUtil
 * @see at.graz.meduni.liferay.portlet.saat.service.base.DictionaryServiceBaseImpl
 * @see at.graz.meduni.liferay.portlet.saat.service.impl.DictionaryServiceImpl
 * @generated
 */
@AccessControlled
@JSONWebService
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface DictionaryService extends BaseService, InvokableService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link DictionaryServiceUtil} to access the dictionary remote service. Add custom service methods to {@link at.graz.meduni.liferay.portlet.saat.service.impl.DictionaryServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier();

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier);

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable;
}
--------------------

/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.bcnet.portlet.biobank.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableService;

/**
 * Provides the remote service utility for BiobankAttributeLists. This utility wraps
 * {@link com.bcnet.portlet.biobank.service.impl.BiobankAttributeListsServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author suyama
 * @see BiobankAttributeListsService
 * @see com.bcnet.portlet.biobank.service.base.BiobankAttributeListsServiceBaseImpl
 * @see com.bcnet.portlet.biobank.service.impl.BiobankAttributeListsServiceImpl
 * @generated
 */
public class BiobankAttributeListsServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.bcnet.portlet.biobank.service.impl.BiobankAttributeListsServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return getService().invokeMethod(name, parameterTypes, arguments);
	}

	public static void clearService() {
		_service = null;
	}

	public static BiobankAttributeListsService getService() {
		if (_service == null) {
			InvokableService invokableService = (InvokableService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					BiobankAttributeListsService.class.getName());

			if (invokableService instanceof BiobankAttributeListsService) {
				_service = (BiobankAttributeListsService)invokableService;
			}
			else {
				_service = new BiobankAttributeListsServiceClp(invokableService);
			}

			ReferenceRegistry.registerReference(BiobankAttributeListsServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setService(BiobankAttributeListsService service) {
	}

	private static BiobankAttributeListsService _service;
}
--------------------

