/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

/**
 * Mock data for {@link EncodeIfEmptyTest}.
 * 
 * @author Victor Tey (CSIRO Earth Science and Resource Engineering)
 */
public class EncodeIfEmptyMockData extends AbstractAppSchemaMockData {

    /**
     * Prefix for om namespace.
     */
    protected static final String OM_PREFIX = "om";

    /**
     * Prefix for om namespace.
     */
    protected static final String SWE_PREFIX = "swe";

    /**
     * URI for om namespace.
     */
    protected static final String OM_URI = "http://www.opengis.net/om/2.0";

    /**
     * URI for om namespace.
     */
    protected static final String SWE_URI = "http://www.opengis.net/swe/2.0";

    public EncodeIfEmptyMockData() {
        super(GML32_NAMESPACES);
        // add SchemaCatalog so validateGet() would work with unpublished schemas
        setSchemaCatalog("schemas/wml2dr_catalog.xml");
    }

    /**
     * @see org.geoserver.test.AbstractAppSchemaMockData#addContent()
     */
    @Override
    public void addContent() {
        putNamespace(OM_PREFIX, OM_URI);
        putNamespace(SWE_PREFIX, SWE_URI);
        putNamespace(WaterMLTimeSeriesMockData.WML2DR_PREFIX, WaterMLTimeSeriesMockData.WML2DR_URI);
        putNamespace(WaterMLTimeSeriesMockData.GMLCOV_PREFIX, WaterMLTimeSeriesMockData.GMLCOV_URI);
        addFeatureType(OM_PREFIX, "OM_Observation", "Observation_2_0_EncodeIfEmpty_Test.xml",
                "timeseries.properties", "schemas/wml2dr_catalog.xml");
    }

}

--------------------

/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.web.passwd;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.security.config.PasswordPolicyConfig;
import org.geoserver.security.web.SecurityNamedServicePanel;

/**
 * Configuration panel for {@link PasswordPolicy}.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class PasswordPolicyPanel extends SecurityNamedServicePanel<PasswordPolicyConfig> {

    MaxLengthPanel maxLengthPanel;

    public PasswordPolicyPanel(String id, IModel<PasswordPolicyConfig> model) {
        super(id, model);

        PasswordPolicyConfig pwPolicy = model.getObject();

        //add(new TextField("name").setRequired(true));
        add(new CheckBox("digitRequired"));
        add(new CheckBox("uppercaseRequired"));
        add(new CheckBox("lowercaseRequired"));
        add(new TextField<Integer>("minLength"));

        boolean unlimited = pwPolicy.getMaxLength() == -1;
        add(new AjaxCheckBox("unlimitedMaxLength", new Model(unlimited)) {
            
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Boolean value = getModelObject();
                maxLengthPanel.setVisible(!value);
                if (value) {
                    maxLengthPanel.setUnlimited();
                }
                target.addComponent(maxLengthPanel.getParent());
            }
        });
        add(maxLengthPanel = 
            (MaxLengthPanel) new MaxLengthPanel("maxLength").setVisible(!unlimited));
    }

    
    public void doSave(PasswordPolicyConfig config) throws Exception {
        getSecurityManager().savePasswordPolicy(config);
    }

    @Override
    public void doLoad(PasswordPolicyConfig config) throws Exception {
        getSecurityManager().loadPasswordPolicyConfig(config.getName());
    }

    class MaxLengthPanel extends FormComponentPanel {

        public MaxLengthPanel(String id) {
            super(id, new Model());
            add(new TextField<Integer>("maxLength"));
            setOutputMarkupId(true);
        }

        public void setUnlimited() {
            get("maxLength").setDefaultModelObject(-1);
        }
    }
}

--------------------

/* This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Jorge Gustavo Rocha / Universidade do Minho
 * @author Nuno Carvalho Oliveira / Universidade do Minho 
 */

package org.geoserver.w3ds.service;

import org.geoserver.config.impl.ServiceInfoImpl;

public class W3DSInfoImpl extends ServiceInfoImpl implements W3DSInfo {

    public W3DSInfoImpl() {
        setId( "w3ds" );
    }
    
}

--------------------

