/**
 */
package org.correttouml.grammars.property;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.correttouml.grammars.property.Declaration#getStateName <em>State Name</em>}</li>
 *   <li>{@link org.correttouml.grammars.property.Declaration#getObj <em>Obj</em>}</li>
 *   <li>{@link org.correttouml.grammars.property.Declaration#getStd <em>Std</em>}</li>
 *   <li>{@link org.correttouml.grammars.property.Declaration#getUMLStateName <em>UML State Name</em>}</li>
 *   <li>{@link org.correttouml.grammars.property.Declaration#getTrioVar <em>Trio Var</em>}</li>
 *   <li>{@link org.correttouml.grammars.property.Declaration#getTrio <em>Trio</em>}</li>
 * </ul>
 *
 * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration()
 * @model
 * @generated
 */
public interface Declaration extends EObject
{
  /**
   * Returns the value of the '<em><b>State Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>State Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>State Name</em>' attribute.
   * @see #setStateName(String)
   * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration_StateName()
   * @model
   * @generated
   */
  String getStateName();

  /**
   * Sets the value of the '{@link org.correttouml.grammars.property.Declaration#getStateName <em>State Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>State Name</em>' attribute.
   * @see #getStateName()
   * @generated
   */
  void setStateName(String value);

  /**
   * Returns the value of the '<em><b>Obj</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Obj</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Obj</em>' attribute.
   * @see #setObj(String)
   * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration_Obj()
   * @model
   * @generated
   */
  String getObj();

  /**
   * Sets the value of the '{@link org.correttouml.grammars.property.Declaration#getObj <em>Obj</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Obj</em>' attribute.
   * @see #getObj()
   * @generated
   */
  void setObj(String value);

  /**
   * Returns the value of the '<em><b>Std</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Std</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Std</em>' attribute.
   * @see #setStd(String)
   * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration_Std()
   * @model
   * @generated
   */
  String getStd();

  /**
   * Sets the value of the '{@link org.correttouml.grammars.property.Declaration#getStd <em>Std</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Std</em>' attribute.
   * @see #getStd()
   * @generated
   */
  void setStd(String value);

  /**
   * Returns the value of the '<em><b>UML State Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>UML State Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>UML State Name</em>' attribute.
   * @see #setUMLStateName(String)
   * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration_UMLStateName()
   * @model
   * @generated
   */
  String getUMLStateName();

  /**
   * Sets the value of the '{@link org.correttouml.grammars.property.Declaration#getUMLStateName <em>UML State Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>UML State Name</em>' attribute.
   * @see #getUMLStateName()
   * @generated
   */
  void setUMLStateName(String value);

  /**
   * Returns the value of the '<em><b>Trio Var</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trio Var</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trio Var</em>' attribute.
   * @see #setTrioVar(String)
   * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration_TrioVar()
   * @model
   * @generated
   */
  String getTrioVar();

  /**
   * Sets the value of the '{@link org.correttouml.grammars.property.Declaration#getTrioVar <em>Trio Var</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trio Var</em>' attribute.
   * @see #getTrioVar()
   * @generated
   */
  void setTrioVar(String value);

  /**
   * Returns the value of the '<em><b>Trio</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trio</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trio</em>' containment reference.
   * @see #setTrio(TRIO)
   * @see org.correttouml.grammars.property.PropertyPackage#getDeclaration_Trio()
   * @model containment="true"
   * @generated
   */
  TRIO getTrio();

  /**
   * Sets the value of the '{@link org.correttouml.grammars.property.Declaration#getTrio <em>Trio</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trio</em>' containment reference.
   * @see #getTrio()
   * @generated
   */
  void setTrio(TRIO value);

} // Declaration

--------------------

/**
 */
package org.emftrace.metamodel.OWLModel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.emftrace.metamodel.OWLModel.Datatype;
import org.emftrace.metamodel.OWLModel.OWLModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Datatype</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftrace.metamodel.OWLModel.impl.DatatypeImpl#getAbbreviatedIRI <em>Abbreviated IRI</em>}</li>
 *   <li>{@link org.emftrace.metamodel.OWLModel.impl.DatatypeImpl#getIRI <em>IRI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatatypeImpl extends DataRangeImpl implements Datatype {
	/**
	 * The default value of the '{@link #getAbbreviatedIRI() <em>Abbreviated IRI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbbreviatedIRI()
	 * @generated
	 * @ordered
	 */
	protected static final String ABBREVIATED_IRI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAbbreviatedIRI() <em>Abbreviated IRI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbbreviatedIRI()
	 * @generated
	 * @ordered
	 */
	protected String abbreviatedIRI = ABBREVIATED_IRI_EDEFAULT;

	/**
	 * The default value of the '{@link #getIRI() <em>IRI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIRI()
	 * @generated
	 * @ordered
	 */
	protected static final String IRI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIRI() <em>IRI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIRI()
	 * @generated
	 * @ordered
	 */
	protected String iri = IRI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DatatypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OWLModelPackage.eINSTANCE.getDatatype();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAbbreviatedIRI() {
		return abbreviatedIRI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbbreviatedIRI(String newAbbreviatedIRI) {
		String oldAbbreviatedIRI = abbreviatedIRI;
		abbreviatedIRI = newAbbreviatedIRI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWLModelPackage.DATATYPE__ABBREVIATED_IRI, oldAbbreviatedIRI, abbreviatedIRI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIRI() {
		return iri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIRI(String newIRI) {
		String oldIRI = iri;
		iri = newIRI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWLModelPackage.DATATYPE__IRI, oldIRI, iri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OWLModelPackage.DATATYPE__ABBREVIATED_IRI:
				return getAbbreviatedIRI();
			case OWLModelPackage.DATATYPE__IRI:
				return getIRI();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OWLModelPackage.DATATYPE__ABBREVIATED_IRI:
				setAbbreviatedIRI((String)newValue);
				return;
			case OWLModelPackage.DATATYPE__IRI:
				setIRI((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case OWLModelPackage.DATATYPE__ABBREVIATED_IRI:
				setAbbreviatedIRI(ABBREVIATED_IRI_EDEFAULT);
				return;
			case OWLModelPackage.DATATYPE__IRI:
				setIRI(IRI_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case OWLModelPackage.DATATYPE__ABBREVIATED_IRI:
				return ABBREVIATED_IRI_EDEFAULT == null ? abbreviatedIRI != null : !ABBREVIATED_IRI_EDEFAULT.equals(abbreviatedIRI);
			case OWLModelPackage.DATATYPE__IRI:
				return IRI_EDEFAULT == null ? iri != null : !IRI_EDEFAULT.equals(iri);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (abbreviatedIRI: ");
		result.append(abbreviatedIRI);
		result.append(", IRI: ");
		result.append(iri);
		result.append(')');
		return result.toString();
	}

} //DatatypeImpl

--------------------

/**
 */
package report.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import report.ReportPackage;
import report.Syntactical;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Syntactical</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link report.impl.SyntacticalImpl#getMissedRoles <em>Missed Roles</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SyntacticalImpl extends AnomalyImpl implements Syntactical {
	/**
	 * The cached value of the '{@link #getMissedRoles() <em>Missed Roles</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMissedRoles()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> missedRoles;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SyntacticalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ReportPackage.Literals.SYNTACTICAL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getMissedRoles() {
		if (missedRoles == null) {
			missedRoles = new EObjectResolvingEList<EObject>(EObject.class, this, ReportPackage.SYNTACTICAL__MISSED_ROLES);
		}
		return missedRoles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ReportPackage.SYNTACTICAL__MISSED_ROLES:
				return getMissedRoles();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ReportPackage.SYNTACTICAL__MISSED_ROLES:
				getMissedRoles().clear();
				getMissedRoles().addAll((Collection<? extends EObject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ReportPackage.SYNTACTICAL__MISSED_ROLES:
				getMissedRoles().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ReportPackage.SYNTACTICAL__MISSED_ROLES:
				return missedRoles != null && !missedRoles.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SyntacticalImpl

--------------------

