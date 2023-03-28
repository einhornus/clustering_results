package edu.kit.imi.knoholem.cu.rules.dataquality;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Tonev</a>
 */
public class TemplatesTest {

    private STGroup templates;

    @Before
    public void setup() throws URISyntaxException {
        System.setProperty("line.separator", "\n");
        templates = new STGroupFile("templates/templates.stg");
    }

    @Test
    public void testInitialized() {
        Assert.assertNotNull(templates);
        Assert.assertNotNull(templates.getInstanceOf("prefixes"));
    }

    @Test
    public void testPrefixes() {
        String expected = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX ontology: <http://example.com/ontology.owl#>";

        ST template = templates.getInstanceOf("prefixes");
        template.add("prefix", "ontology");
        template.add("namespace", "http://example.com/ontology.owl#");

        Assert.assertEquals(expected, template.render());
    }

    @Test
    public void testIndividualInClass() {
        String expected = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX ontology: <http://example.com/ontology.owl#>\n" +
                "\n" +
                "ASK WHERE {\n" +
                "  { ontology:individualName a ontology:classOne }\n" +
                "}";

        ST template = templates.getInstanceOf("individualInClasses");
        template.add("prefix", "ontology");
        template.add("namespace", "http://example.com/ontology.owl#");
        template.add("individualName", "individualName");
        template.add("classes", Arrays.asList("classOne"));

        Assert.assertEquals(expected, template.render());
    }

    @Test
    public void testIndividualInClasses() {
        String expected = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX ontology: <http://example.com/ontology.owl#>\n" +
                "\n" +
                "ASK WHERE {\n" +
                "  { ontology:individualName a ontology:classOne } UNION\n" +
                "  { ontology:individualName a ontology:classTwo } UNION\n" +
                "  { ontology:individualName a ontology:classThree }\n" +
                "}";

        ST template = templates.getInstanceOf("individualInClasses");
        template.add("prefix", "ontology");
        template.add("namespace", "http://example.com/ontology.owl#");
        template.add("individualName", "individualName");
        template.add("classes", Arrays.asList("classOne", "classTwo", "classThree"));

        Assert.assertEquals(expected, template.render());
    }
}

--------------------

/*
 * Copyright (C) 2010-2011 The University of Manchester
 * 
 * See the file "LICENSE.txt" for license terms.
 */
package org.taverna.server.master;

import static eu.medsea.util.MimeUtil.getMimeType;
import static java.lang.Math.min;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.springframework.jmx.support.MetricType.COUNTER;
import static org.springframework.jmx.support.MetricType.GAUGE;
import static org.taverna.server.master.TavernaServerImpl.JMX_ROOT;
import static org.taverna.server.master.common.Roles.ADMIN;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedMetric;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.taverna.server.master.common.Permission;
import org.taverna.server.master.common.Workflow;
import org.taverna.server.master.exceptions.FilesystemAccessException;
import org.taverna.server.master.exceptions.NoCreateException;
import org.taverna.server.master.exceptions.NoDestroyException;
import org.taverna.server.master.exceptions.NoListenerException;
import org.taverna.server.master.exceptions.NoUpdateException;
import org.taverna.server.master.exceptions.UnknownRunException;
import org.taverna.server.master.factories.ListenerFactory;
import org.taverna.server.master.factories.RunFactory;
import org.taverna.server.master.interfaces.File;
import org.taverna.server.master.interfaces.Input;
import org.taverna.server.master.interfaces.Listener;
import org.taverna.server.master.interfaces.LocalIdentityMapper;
import org.taverna.server.master.interfaces.Policy;
import org.taverna.server.master.interfaces.RunStore;
import org.taverna.server.master.interfaces.TavernaRun;
import org.taverna.server.master.interfaces.TavernaSecurityContext;
import org.taverna.server.master.utils.InvocationCounter;
import org.taverna.server.master.utils.UsernamePrincipal;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Web application support utilities.
 * 
 * @author Donal Fellows
 */
@ManagedResource(objectName = JMX_ROOT + "Webapp", description = "The main web-application interface to Taverna Server.")
public class TavernaServerSupport {
	/** The main webapp log. */
	public static final Log log = getLog("Taverna.Server.Webapp");
	private Log accessLog = getLog("Taverna.Server.Webapp.Access");;
	/** Bean used to log counts of external calls. */
	private InvocationCounter counter;
	/** A storage facility for workflow runs. */
	private RunStore runStore;
	/** Encapsulates the policies applied by this server. */
	private Policy policy;
	/** Connection to the persistent state of this service. */
	private ManagementModel stateModel;
	/** A factory for event listeners to attach to workflow runs. */
	private ListenerFactory listenerFactory;
	/** A factory for workflow runs. */
	private RunFactory runFactory;
	/** How to map the user ID to who to run as. */
	private LocalIdentityMapper idMapper;
	/** The code that is coupled to CXF. */
	private TavernaServer webapp;
	/**
	 * Whether to log failures during principal retrieval. Should be normally on
	 * as it indicates a serious problem, but can be switched off for testing.
	 */
	private boolean logGetPrincipalFailures = true;
	private Map<String, String> contentTypeMap;
	/** Number of bytes to read when guessing the MIME type. */
	private static final int SAMPLE_SIZE = 1024;
	/** Number of bytes to ask for when copying a stream to a file. */
	private static final int TRANSFER_SIZE = 32768;

	/**
	 * @return Count of the number of external calls into this webapp.
	 */
	@ManagedMetric(description = "Count of the number of external calls into this webapp.", metricType = COUNTER, category = "throughput")
	public int getInvocationCount() {
		return counter.getCount();
	}

	/**
	 * @return Current number of runs.
	 */
	@ManagedMetric(description = "Current number of runs.", metricType = GAUGE, category = "utilization")
	public int getCurrentRunCount() {
		return runStore.listRuns(null, policy).size();
	}

	/**
	 * @return Whether to write submitted workflows to the log.
	 */
	@ManagedAttribute(description = "Whether to write submitted workflows to the log.")
	public boolean getLogIncomingWorkflows() {
		return stateModel.getLogIncomingWorkflows();
	}

	/**
	 * @param logIncomingWorkflows
	 *            Whether to write submitted workflows to the log.
	 */
	@ManagedAttribute(description = "Whether to write submitted workflows to the log.")
	public void setLogIncomingWorkflows(boolean logIncomingWorkflows) {
		stateModel.setLogIncomingWorkflows(logIncomingWorkflows);
	}

	/**
	 * @return Whether outgoing exceptions should be logged before being
	 *         converted to responses.
	 */
	@ManagedAttribute(description = "Whether outgoing exceptions should be logged before being converted to responses.")
	public boolean getLogOutgoingExceptions() {
		return stateModel.getLogOutgoingExceptions();
	}

	/**
	 * @param logOutgoing
	 *            Whether outgoing exceptions should be logged before being
	 *            converted to responses.
	 */
	@ManagedAttribute(description = "Whether outgoing exceptions should be logged before being converted to responses.")
	public void setLogOutgoingExceptions(boolean logOutgoing) {
		stateModel.setLogOutgoingExceptions(logOutgoing);
	}

	/**
	 * @return Whether to permit any new workflow runs to be created.
	 */
	@ManagedAttribute(description = "Whether to permit any new workflow runs to be created; has no effect on existing runs.")
	public boolean getAllowNewWorkflowRuns() {
		return stateModel.getAllowNewWorkflowRuns();
	}

	/**
	 * @param allowNewWorkflowRuns
	 *            Whether to permit any new workflow runs to be created.
	 */
	@ManagedAttribute(description = "Whether to permit any new workflow runs to be created; has no effect on existing runs.")
	public void setAllowNewWorkflowRuns(boolean allowNewWorkflowRuns) {
		stateModel.setAllowNewWorkflowRuns(allowNewWorkflowRuns);
	}

	public int getMaxSimultaneousRuns() {
		Integer limit = policy.getMaxRuns(getPrincipal());
		if (limit == null)
			return policy.getMaxRuns();
		return min(limit.intValue(), policy.getMaxRuns());
	}

	public List<Workflow> getPermittedWorkflows() {
		return policy.listPermittedWorkflows(getPrincipal());
	}

	public List<String> getListenerTypes() {
		return listenerFactory.getSupportedListenerTypes();
	}

	/**
	 * @param policy
	 *            The policy being installed by Spring.
	 */
	@Required
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	/**
	 * @param listenerFactory
	 *            The listener factory being installed by Spring.
	 */
	@Required
	public void setListenerFactory(ListenerFactory listenerFactory) {
		this.listenerFactory = listenerFactory;
	}

	/**
	 * @param runFactory
	 *            The run factory being installed by Spring.
	 */
	@Required
	public void setRunFactory(RunFactory runFactory) {
		this.runFactory = runFactory;
	}

	/**
	 * @param runStore
	 *            The run store being installed by Spring.
	 */
	@Required
	public void setRunStore(RunStore runStore) {
		this.runStore = runStore;
	}

	/**
	 * @param stateModel
	 *            The state model engine being installed by Spring.
	 */
	@Required
	public void setStateModel(ManagementModel stateModel) {
		this.stateModel = stateModel;
	}

	/**
	 * @param mapper
	 *            The identity mapper being installed by Spring.
	 */
	@Required
	public void setIdMapper(LocalIdentityMapper mapper) {
		this.idMapper = mapper;
	}

	/**
	 * @param counter
	 *            The object whose job it is to manage the counting of
	 *            invocations. Installed by Spring.
	 */
	@Required
	public void setInvocationCounter(InvocationCounter counter) {
		this.counter = counter;
	}

	/**
	 * @param webapp
	 *            The web-app being installed by Spring.
	 */
	@Required
	public void setWebapp(TavernaServer webapp) {
		this.webapp = webapp;
	}

	/**
	 * @param logthem
	 *            Whether to log failures relating to principals.
	 */
	public void setLogGetPrincipalFailures(boolean logthem) {
		logGetPrincipalFailures = logthem;
	}

	public Map<String, String> getContentTypeMap() {
		return contentTypeMap;
	}

	/**
	 * Mapping from filename suffixes (e.g., "baclava") to content types.
	 * 
	 * @param contentTypeMap
	 *            The mapping to install.
	 */
	@Required
	public void setContentTypeMap(Map<String, String> contentTypeMap) {
		this.contentTypeMap = contentTypeMap;
	}

	/**
	 * Test whether the current user can do updates to the given run.
	 * 
	 * @param run
	 *            The workflow run to do the test on.
	 * @throws NoUpdateException
	 *             If the current user is not permitted to update the run.
	 */
	public void permitUpdate(@NonNull TavernaRun run) throws NoUpdateException {
		if (isSuperUser()) {
			accessLog
					.warn("check for admin powers passed; elevated access rights granted for update");
			return; // Superusers are fully authorized to access others things
		}
		policy.permitUpdate(getPrincipal(), run);
	}

	/**
	 * Test whether the current user can destroy or control the lifespan of the
	 * given run.
	 * 
	 * @param run
	 *            The workflow run to do the test on.
	 * @throws NoDestroyException
	 *             If the current user is not permitted to destroy the run.
	 */
	public void permitDestroy(TavernaRun run) throws NoDestroyException {
		if (isSuperUser()) {
			accessLog
					.warn("check for admin powers passed; elevated access rights granted for destroy");
			return; // Superusers are fully authorized to access others things
		}
		policy.permitDestroy(getPrincipal(), run);
	}

	/**
	 * Gets the identity of the user currently accessing the webapp, which is
	 * stored in a thread-safe way in the webapp's container's context.
	 * 
	 * @return The identity of the user accessing the webapp.
	 */
	@NonNull
	public UsernamePrincipal getPrincipal() {
		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (auth == null || !auth.isAuthenticated()) {
				if (logGetPrincipalFailures)
					log.warn("failed to get auth; going with <NOBODY>");
				return new UsernamePrincipal("<NOBODY>");
			}
			return new UsernamePrincipal(auth);
		} catch (RuntimeException e) {
			if (logGetPrincipalFailures)
				log.info("failed to map principal", e);
			throw e;
		}
	}

	/**
	 * Obtain the workflow run with a particular name.
	 * 
	 * @param name
	 *            The name of the run to look up.
	 * @return A workflow run handle that the current user has at least
	 *         permission to read.
	 * @throws UnknownRunException
	 *             If the workflow run doesn't exist or the current user doesn't
	 *             have permission to see it.
	 */
	@NonNull
	public TavernaRun getRun(@NonNull String name) throws UnknownRunException {
		if (isSuperUser()) {
			accessLog
					.info("check for admin powers passed; elevated access rights granted for read");
			return runStore.getRun(name);
		}
		return runStore.getRun(getPrincipal(), policy, name);
	}

	/**
	 * Construct a listener attached to the given run.
	 * 
	 * @param run
	 *            The workflow run to attach the listener to.
	 * @param type
	 *            The name of the type of run to create.
	 * @param configuration
	 *            The configuration description to pass into the listener. The
	 *            format of this string is up to the listener to define.
	 * @return A handle to the listener which can be used to further configure
	 *         any properties.
	 * @throws NoListenerException
	 *             If the listener type is unrecognized or the configuration is
	 *             invalid.
	 * @throws NoUpdateException
	 *             If the run does not permit the current user to add listeners
	 *             (or perform other types of update).
	 */
	@NonNull
	public Listener makeListener(@NonNull TavernaRun run, @NonNull String type,
			@NonNull String configuration) throws NoListenerException,
			NoUpdateException {
		permitUpdate(run);
		return listenerFactory.makeListener(run, type, configuration);
	}

	/**
	 * Obtain a listener that is already attached to a workflow run.
	 * 
	 * @param run
	 *            The workflow run to search.
	 * @param listenerName
	 *            The name of the listener to look up.
	 * @return The listener instance interface.
	 * @throws NoListenerException
	 *             If no listener with that name exists.
	 */
	@NonNull
	public Listener getListener(TavernaRun run, String listenerName)
			throws NoListenerException {
		for (Listener l : run.getListeners())
			if (l.getName().equals(listenerName))
				return l;
		throw new NoListenerException();
	}

	/**
	 * Get the permission description for the given user.
	 * 
	 * @param context
	 *            A security context associated with a particular workflow run.
	 *            Note that only the owner of a workflow run may get the
	 *            security context in the first place.
	 * @param userName
	 *            The name of the user to look up the permission for.
	 * @return A permission description.
	 */
	@NonNull
	public Permission getPermission(@NonNull TavernaSecurityContext context,
			@NonNull String userName) {
		if (context.getPermittedDestroyers().contains(userName))
			return Permission.Destroy;
		if (context.getPermittedUpdaters().contains(userName))
			return Permission.Update;
		if (context.getPermittedReaders().contains(userName))
			return Permission.Read;
		return Permission.None;
	}

	/**
	 * Set the permissions for the given user.
	 * 
	 * @param context
	 *            A security context associated with a particular workflow run.
	 *            Note that only the owner of a workflow run may get the
	 *            security context in the first place.
	 * @param userName
	 *            The name of the user to set the permission for.
	 * @param permission
	 *            The description of the permission to grant. Note that the
	 *            owner of a workflow run always has the equivalent of
	 *            {@link Permission#Destroy}; this is always enforced before
	 *            checking for other permissions.
	 */
	@SuppressWarnings("SF_SWITCH_FALLTHROUGH")
	public void setPermission(TavernaSecurityContext context, String userName,
			Permission permission) {
		Set<String> permSet;
		boolean doRead = false, doWrite = false, doKill = false;

		switch (permission) {
		case Destroy:
			doKill = true;
		case Update:
			doWrite = true;
		case Read:
			doRead = true;
		}

		permSet = context.getPermittedReaders();
		if (doRead) {
			if (!permSet.contains(userName)) {
				permSet = new HashSet<String>(permSet);
				permSet.add(userName);
				context.setPermittedReaders(permSet);
			}
		} else if (permSet.contains(userName)) {
			permSet = new HashSet<String>(permSet);
			permSet.remove(userName);
			context.setPermittedReaders(permSet);
		}

		permSet = context.getPermittedUpdaters();
		if (doWrite) {
			if (!permSet.contains(userName)) {
				permSet = new HashSet<String>(permSet);
				permSet.add(userName);
				context.setPermittedUpdaters(permSet);
			}
		} else if (permSet.contains(userName)) {
			permSet = new HashSet<String>(permSet);
			permSet.remove(userName);
			context.setPermittedUpdaters(permSet);
		}

		permSet = context.getPermittedDestroyers();
		if (doKill) {
			if (!permSet.contains(userName)) {
				permSet = new HashSet<String>(permSet);
				permSet.add(userName);
				context.setPermittedDestroyers(permSet);
			}
		} else if (permSet.contains(userName)) {
			permSet = new HashSet<String>(permSet);
			permSet.remove(userName);
			context.setPermittedDestroyers(permSet);
		}
	}

	/**
	 * Stops a run from being possible to be looked up and destroys it.
	 * 
	 * @param runName
	 *            The name of the run.
	 * @param run
	 *            The workflow run. <i>Must</i> correspond to the name.
	 * @throws NoDestroyException
	 *             If the user is not permitted to destroy the workflow run.
	 * @throws UnknownRunException
	 *             If the run is unknown (e.g., because it is already
	 *             destroyed).
	 */
	public void unregisterRun(@NonNull String runName, @NonNull TavernaRun run)
			throws NoDestroyException, UnknownRunException {
		if (run == null)
			run = getRun(runName);
		policy.permitDestroy(getPrincipal(), run);
		runStore.unregisterRun(runName);
		run.destroy();
	}

	/**
	 * Changes the expiry date of a workflow run. The expiry date is when the
	 * workflow run becomes eligible for automated destruction.
	 * 
	 * @param run
	 *            The handle to the workflow run.
	 * @param date
	 *            When the workflow run should be expired.
	 * @return When the workflow run will actually be expired.
	 * @throws NoDestroyException
	 *             If the user is not permitted to destroy the workflow run.
	 *             (Note that lifespan management requires the ability to
	 *             destroy.)
	 */
	@NonNull
	public Date updateExpiry(@NonNull TavernaRun run, @NonNull Date date)
			throws NoDestroyException {
		policy.permitDestroy(getPrincipal(), run);
		run.setExpiry(date);
		return run.getExpiry();
	}

	/**
	 * Manufacture a workflow run instance.
	 * 
	 * @param workflow
	 *            The workflow document (t2flow, scufl2?) to instantiate.
	 * @return The ID of the created workflow run.
	 * @throws NoCreateException
	 *             If the user is not permitted to create workflows.
	 */
	public String buildWorkflow(Workflow workflow) throws NoCreateException {
		UsernamePrincipal p = getPrincipal();
		if (!stateModel.getAllowNewWorkflowRuns())
			throw new NoCreateException("run creation not currently enabled");
		try {
			if (stateModel.getLogIncomingWorkflows()) {
				log.info(workflow.marshal());
			}
		} catch (JAXBException e) {
			log.warn("problem when logging workflow", e);
		}

		// Security checks
		policy.permitCreate(p, workflow);
		if (idMapper != null && idMapper.getUsernameForPrincipal(p) == null) {
			log.error("cannot map principal to local user id");
			throw new NoCreateException(
					"failed to map security token to local user id");
		}

		TavernaRun run;
		try {
			run = runFactory.create(p, workflow);
			TavernaSecurityContext c = run.getSecurityContext();
			c.initializeSecurityFromContext(SecurityContextHolder.getContext());
			webapp.initObsoleteSecurity(c);
		} catch (Exception e) {
			log.error("failed to build workflow run worker", e);
			throw new NoCreateException("failed to build workflow run worker");
		}

		return runStore.registerRun(run);
	}

	private boolean isSuperUser() {
		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (auth == null || !auth.isAuthenticated())
				return false;
			UserDetails details = (UserDetails) auth.getPrincipal();
			if (log.isDebugEnabled())
				log.debug("checking for admin role for user <" + auth.getName()
						+ "> in collection " + details.getAuthorities());
			return details.getAuthorities().contains(ADMIN);
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Get a particular input to a workflow run.
	 * 
	 * @param run
	 *            The workflow run to search.
	 * @param portName
	 *            The name of the input.
	 * @return The handle of the input, or <tt>null</tt> if no such handle
	 *         exists.
	 */
	@Nullable
	public Input getInput(TavernaRun run, String portName) {
		for (Input i : run.getInputs())
			if (i.getName().equals(portName))
				return i;
		return null;
	}

	/**
	 * Get a listener attached to a run.
	 * 
	 * @param runName
	 *            The name of the run to look up
	 * @param listenerName
	 *            The name of the listener.
	 * @return The handle of the listener.
	 * @throws NoListenerException
	 *             If no such listener exists.
	 * @throws UnknownRunException
	 *             If no such workflow run exists, or if the user does not have
	 *             permission to access it.
	 */
	public Listener getListener(String runName, String listenerName)
			throws NoListenerException, UnknownRunException {
		return getListener(getRun(runName), listenerName);
	}

	/**
	 * Given a file, produce a guess at its content type. This uses the content
	 * type map property, and if that search fails it falls back on the Medsea
	 * mime type library.
	 * 
	 * @param f
	 *            The file handle.
	 * @return The content type. If all else fails, produces good old
	 *         "application/octet-stream".
	 */
	@NonNull
	public String getEstimatedContentType(@NonNull File f) {
		String name = f.getName();
		for (int idx = name.indexOf('.'); idx != -1; idx = name.indexOf('.',
				idx + 1)) {
			String mt = contentTypeMap.get(name.substring(idx + 1));
			if (mt != null)
				return mt;
		}
		try {
			return getMimeType(new ByteArrayInputStream(f.getContents(0,
					SAMPLE_SIZE)));
		} catch (FilesystemAccessException e) {
			// Ignore; fall back to just serving as bytes
			return APPLICATION_OCTET_STREAM;
		}
	}

	public void copyDataToFile(DataHandler handler, File file)
			throws FilesystemAccessException {
		try {
			copyStreamToFile(handler.getInputStream(), file);
		} catch (IOException e) {
			throw new FilesystemAccessException(
					"problem constructing stream from data source", e);
		}
	}

	public void copyDataToFile(URI uri, File file)
			throws MalformedURLException, FilesystemAccessException,
			IOException {
		copyStreamToFile(uri.toURL().openStream(), file);
	}

	public void copyStreamToFile(InputStream stream, File file)
			throws FilesystemAccessException {
		String name = file.getFullName();
		long total = 0;
		try {
			byte[] buffer = new byte[TRANSFER_SIZE];
			while (true) {
				int len = stream.read(buffer);
				if (len < 0)
					break;
				total += len;
				log.debug("read " + len + " bytes from source stream (total: "
						+ total + ") bound for " + name);
				if (len == buffer.length)
					file.appendContents(buffer);
				else {
					byte[] newBuf = new byte[len];
					System.arraycopy(buffer, 0, newBuf, 0, len);
					file.appendContents(newBuf);
				}
			}
		} catch (IOException exn) {
			throw new FilesystemAccessException("failed to transfer bytes", exn);
		}
	}

	public boolean getAllowStartWorkflowRuns() {
		return runFactory.isAllowingRunsToStart();
	}
}

--------------------

package com.alvazan.orm.layer0.base;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alvazan.orm.api.base.MetaLayer;
import com.alvazan.orm.api.base.NoSqlEntityManager;
import com.alvazan.orm.api.base.Query;
import com.alvazan.orm.api.z3api.NoSqlTypedSession;
import com.alvazan.orm.api.z5api.IndexColumnInfo;
import com.alvazan.orm.api.z5api.NoSqlSession;
import com.alvazan.orm.api.z5api.SpiMetaQuery;
import com.alvazan.orm.api.z5api.SpiQueryAdapter;
import com.alvazan.orm.api.z8spi.KeyValue;
import com.alvazan.orm.api.z8spi.MetaLoader;
import com.alvazan.orm.api.z8spi.MetaLookup;
import com.alvazan.orm.api.z8spi.Row;
import com.alvazan.orm.api.z8spi.action.Column;
import com.alvazan.orm.api.z8spi.conv.StorageTypeEnum;
import com.alvazan.orm.api.z8spi.iter.AbstractCursor;
import com.alvazan.orm.api.z8spi.iter.Cursor;
import com.alvazan.orm.api.z8spi.iter.DirectCursor;
import com.alvazan.orm.api.z8spi.iter.IndiceToVirtual;
import com.alvazan.orm.api.z8spi.iter.IterToVirtual;
import com.alvazan.orm.api.z8spi.iter.IterableWrappingCursor;
import com.alvazan.orm.api.z8spi.meta.DboColumnIdMeta;
import com.alvazan.orm.api.z8spi.meta.DboColumnMeta;
import com.alvazan.orm.api.z8spi.meta.DboDatabaseMeta;
import com.alvazan.orm.api.z8spi.meta.DboTableMeta;
import com.alvazan.orm.api.z8spi.meta.IndexData;
import com.alvazan.orm.api.z8spi.meta.RowToPersist;
import com.alvazan.orm.api.z8spi.meta.ViewInfo;
import com.alvazan.orm.impl.meta.data.MetaClass;
import com.alvazan.orm.impl.meta.data.MetaIdField;
import com.alvazan.orm.impl.meta.data.MetaInfo;
import com.alvazan.orm.impl.meta.data.NoSqlProxy;
import com.alvazan.orm.layer3.typed.IndiceCursorProxy;
import com.alvazan.orm.layer3.typed.NoSqlTypedSessionImpl;

public class BaseEntityManagerImpl implements NoSqlEntityManager, MetaLookup, MetaLoader {

	private static final Logger log = LoggerFactory.getLogger(BaseEntityManagerImpl.class);
	
	@Inject @Named("readcachelayer")
	private NoSqlSession session;
	@Inject
	private MetaInfo metaInfo;
	@SuppressWarnings("rawtypes")
	@Inject
	private Provider<QueryAdapter> adapterFactory;
	@Inject
	private NoSqlTypedSessionImpl typedSession;
	@Inject
	private DboDatabaseMeta databaseInfo;
	@Inject
	private MetaLayerImpl metaImpl;
	
	private boolean isTypedSessionInitialized = false;
	
	@SuppressWarnings("rawtypes")
	public void put(Object entity, boolean isInsert) {
		boolean needRead = false;
		if(!isInsert && !(entity instanceof NoSqlProxy)) {
			if(log.isDebugEnabled())
				log.debug("need read as isInsert="+isInsert);
			needRead = true;
		}
		
		Class cl = entity.getClass();
		MetaClass metaClass = metaInfo.getMetaClass(cl);
		if(metaClass == null)
			throw new IllegalArgumentException("Entity type="+entity.getClass().getName()+" was not scanned and added to meta information on startup.  It is either missing @NoSqlEntity annotation or it was not in list of scanned packages");
		
		putImpl(entity, needRead, metaClass);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void put(Object entity) {
		Class cl = entity.getClass();
		MetaClass metaClass = metaInfo.getMetaClass(cl);
		if(metaClass == null)
			throw new IllegalArgumentException("Entity type="+entity.getClass().getName()+" was not scanned and added to meta information on startup.  It is either missing @NoSqlEntity annotation or it was not in list of scanned packages");

		boolean needRead = false;
		MetaIdField idField = metaClass.getIdField();
		Object id = metaClass.fetchId(entity);
		if(idField.isAutoGen() && id != null && !(entity instanceof NoSqlProxy)) {
			//We do NOT have the data from the database IF this is NOT a NoSqlProxy and we need it since this is an 
			//update
			needRead = true;
		}
		putImpl(entity, needRead, metaClass);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putImpl(Object originalEntity, boolean needRead, MetaClass metaClass) {
		Object entity = originalEntity;
		if(needRead) {
			Object id = metaClass.fetchId(originalEntity);
			Object temp = find(metaClass.getMetaClass(), id);
			if(log.isDebugEnabled())
				log.debug("entity with id="+id+" is="+temp);
			if(temp != null) {
				entity = temp;
				BeanProps.copyProps(originalEntity, entity);
			}
		}
		
		RowToPersist row = metaClass.translateToRow(entity);
		
		DboTableMeta metaDbo = metaClass.getMetaDbo();
		if (metaDbo.isEmbeddable())
			throw new IllegalArgumentException("Entity type="+entity.getClass().getName()+" can not be saved as it is Embedded Entity. And Embeddable entitites are only saved along with their parent entity");
		//This is if we need to be removing columns from the row that represents the entity in a oneToMany or ManyToMany
		//as the entity.accounts may have removed one of the accounts!!!
		if(row.hasRemoves())
			session.remove(metaDbo, row.getKey(), row.getColumnNamesToRemove());

		//NOW for index removals if any indexed values change of the entity, we remove from the index
		for(IndexData ind : row.getIndexToRemove()) {
			session.removeFromIndex(metaDbo, ind.getColumnFamilyName(), ind.getRowKeyBytes(), ind.getIndexColumn());
		}
		
		//NOW for index adds, if it is a new entity or if values change, we persist those values
		for(IndexData ind : row.getIndexToAdd()) {
			ind.getIndexColumn().setTtl(row.getTtl());
			session.persistIndex(metaDbo, ind.getColumnFamilyName(), ind.getRowKeyBytes(), ind.getIndexColumn());
		}

		byte[] virtKey = row.getVirtualKey();
		List<Column> cols = row.getColumns();
		int ttl = row.getTtl();
		if (ttl > 0) {
			for(Column c:cols) {
				c.setTtl(ttl);
			}
		}
		session.put(metaDbo, virtKey, cols);
	}

	@Override
	public <T> T find(Class<T> entityType, Object key) {
		if(key == null)
			throw new IllegalArgumentException("key must be supplied but was null");
		List<Object> keys = new ArrayList<Object>();
		keys.add(key);
		Cursor<KeyValue<T>> entities = findAll(entityType, keys);
		if (entities.next())
			return entities.getCurrent().getValue();
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Cursor<KeyValue<T>> findAll(Class<T> entityType, Iterable<? extends Object> keys) {
		if(keys == null)
			throw new IllegalArgumentException("keys list cannot be null");
		MetaClass<T> meta = metaInfo.getMetaClass(entityType);
		if(meta == null)
			throw new IllegalArgumentException("Class type="+entityType.getName()+" was not found, please check that you scanned the right package and look at the logs to see if this class was scanned");

		Iterable<byte[]> iter = new IterableKey<T>(meta, keys);
		Iterable<byte[]> virtKeys = new IterToVirtual(meta.getMetaDbo(), iter);
		
		//we pass in null for batch size such that we do infinite size or basically all keys passed into this method in one
		//shot
		return findAllImpl2(meta, new IterableWrappingCursor<byte[]>(virtKeys), null, true, null);
	}
	
	<T> AbstractCursor<KeyValue<T>> findAllImpl2(MetaClass<T> meta, ViewInfo mainView, DirectCursor<IndexColumnInfo> keys, String query, boolean cacheResults, Integer batchSize) {
		//OKAY, so this gets interesting.  The noSqlKeys could be a proxy iterable to 
		//millions of keys with some batch size.  We canNOT do a find inline here but must do the find in
		//batches as well
		IndiceCursorProxy indiceCursor = new IndiceCursorProxy(mainView, keys);
		DirectCursor<byte[]> virtKeys = new IndiceToVirtual(meta.getMetaDbo(), indiceCursor);
		return findAllImpl2(meta, virtKeys, query, cacheResults, batchSize);
	}
	
	
	<T> AbstractCursor<KeyValue<T>> findAllImpl2(MetaClass<T> meta, DirectCursor<byte[]> keys, String query, boolean cacheResults, Integer batchSize) {
		boolean skipCache = query != null;
		AbstractCursor<KeyValue<Row>> cursor = session.find(meta.getMetaDbo(), keys, skipCache, cacheResults, batchSize);
		return new CursorRow<T>(session, meta, cursor, query);
	}

	@SuppressWarnings("unchecked")
	public <T> List<KeyValue<T>> findAllList(Class<T> entityType, List<? extends Object> keys) {
		if(keys == null)
			throw new IllegalArgumentException("keys list cannot be null");
		MetaClass<T> meta = metaInfo.getMetaClass(entityType);
		if(meta == null)
			throw new IllegalArgumentException("Class type="+entityType.getName()+" was not found, please check that you scanned the right package and look at the logs to see if this class was scanned");
		
		List<KeyValue<T>> all = new ArrayList<KeyValue<T>>();
		Cursor<KeyValue<T>> results = findAll(entityType, keys);
		while(results.next()) {
			KeyValue<T> r = results.getCurrent();
			all.add(r);
		}
		
		return all;
	}
	
	@Override
	public void flush() {
		session.flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Query<T> createNamedQuery(Class<T> forEntity, String namedQuery) {
		MetaClass<T> metaClass = metaInfo.getMetaClass(forEntity);
		if(metaClass == null)
			throw new IllegalArgumentException("Class not scanned="+metaClass+" so you may need to add @NoSqlEntity");
		SpiMetaQuery metaQuery = metaClass.getNamedQuery(forEntity, namedQuery);
		
		SpiQueryAdapter spiAdapter = metaQuery.createQueryInstanceFromQuery(session);
		
		//We cannot return MetaQuery since it is used by all QueryAdapters and each QueryAdapter
		//runs in a different thread potentially while MetaQuery is one used by all threads
		QueryAdapter<T> adapter = adapterFactory.get();
		adapter.setup(metaClass, metaQuery, spiAdapter, this, forEntity);
		return adapter;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getReference(Class<T> entityType, Object key) {
		MetaClass<T> metaClass = metaInfo.getMetaClass(entityType);
		MetaIdField<T> field = metaClass.getIdField();
		return field.convertIdToProxy(session, key, null, entityType);
	}

	@Override
	public NoSqlSession getSession() {
		return session;
	}

	@Override
	public MetaLayer getMeta() {
		return metaImpl;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void fillInWithKey(Object entity) {
		MetaClass metaClass = metaInfo.getMetaClass(entity.getClass());
		MetaIdField idField = metaClass.getIdField();
		if (idField != null)
			idField.fillInAndFetchId(entity);
	}

	@Override
	public void clearDatabase(boolean recreateMeta) {
		session.clearDb();
		
		saveMetaData();
	}

	void saveMetaData() {
		BaseEntityManagerImpl tempMgr = this;
        //DboDatabaseMeta existing = tempMgr.find(DboDatabaseMeta.class, DboDatabaseMeta.META_DB_ROWKEY);
//        if(existing != null)
//        	throw new IllegalStateException("Your property NoSqlEntityManagerFactory.AUTO_CREATE_KEY is set to 'create' which only creates meta data if none exist already but meta already exists");
		
        for(DboTableMeta table : databaseInfo.getAllTables()) {
        	
        	for(DboColumnMeta col : table.getAllColumns()) {
        		tempMgr.put(col);
        	}
			if (!table.isEmbeddable() && table.getIdColumnMeta() != null)
				tempMgr.put(table.getIdColumnMeta());
        	
        	tempMgr.put(table);
        }
        
        databaseInfo.setId(DboDatabaseMeta.META_DB_ROWKEY);
        
        //NOW, on top of the ORM entites, we have 3 special index column families of String, BigInteger and BigDecimal
        //which are one of the types in the composite column name.(the row keys are all strings).  The column names
        //are <value being indexed of String or BigInteger or BigDecimal><primarykey><length of first value> so we can
        //sort it BUT we can determine the length of first value so we can get to primary key.
        
        for(StorageTypeEnum type : StorageTypeEnum.values()) {
        	//Do we want a byte[] index that could be used with == but not < and not >, etc. etc.
        	//Do we want a separate Boolean index????  I don't think so
        	if(type != StorageTypeEnum.DECIMAL
        			&& type != StorageTypeEnum.INTEGER
        			&& type != StorageTypeEnum.STRING)
        		continue;
        	
        	DboTableMeta cf = new DboTableMeta();

        	//TODO: PUT this in virtual partition????
        	cf.setup(null, type.getIndexTableName(), false, false);
        	cf.setColNamePrefixType(type);
        	
        	DboColumnIdMeta idMeta = new DboColumnIdMeta();
        	idMeta.setup(cf, "id", String.class, false);
        	
        	tempMgr.put(idMeta);
        	tempMgr.put(cf);
        	
        	databaseInfo.addMetaClassDbo(cf);
        }
        
        tempMgr.put(databaseInfo);
        tempMgr.flush();
	}
	
	public void setup() {
		session.setOrmSessionForMeta(this);		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void remove(Object entity) {
        if (entity == null)
            return;
	    MetaClass metaClass = metaInfo.getMetaClass(entity.getClass());
		if(metaClass == null)
			throw new IllegalArgumentException("Entity type="+entity.getClass().getName()+" was not scanned and added to meta information on startup.  It is either missing @NoSqlEntity annotation or it was not in list of scanned packages");

		Object proxy = entity;
		Object pk = metaClass.fetchId(entity);
		MetaIdField idField = metaClass.getIdField();
		byte[] rowKey = idField.convertIdToNonVirtKey(pk);
		byte[] virtKey = idField.formVirtRowKey(rowKey);
		DboTableMeta metaDbo = metaClass.getMetaDbo();
		
		if(!metaClass.hasIndexedField(entity)) {
			session.remove(metaDbo, virtKey);
			return;
		} else if(!(entity instanceof NoSqlProxy)) {
			//then we don't have the database information for indexes so we need to read from the database
			proxy = find(metaClass.getMetaClass(), pk);
		}
		
		List<IndexData> indexToRemove = metaClass.findIndexRemoves((NoSqlProxy)proxy, rowKey);
		
		//REMOVE EVERYTHING HERE, we are probably removing extra and could optimize this later
		for(IndexData ind : indexToRemove) {
			session.removeFromIndex(metaDbo, ind.getColumnFamilyName(), ind.getRowKeyBytes(), ind.getIndexColumn());
		}
		
		session.remove(metaDbo, virtKey);
	}

	@Override
	public NoSqlTypedSession getTypedSession() {
		if(!isTypedSessionInitialized) {
			typedSession.setInformation(session, this);
		}
		return typedSession;
	}

	@Override
	public void clear() {
		session.clear();
	}

	@Override
	public <T> Cursor<T> allRows(Class<T> baseEntity, String cf, int batchSize) {
		MetaClass<T> meta;
		if(cf == null) {
			meta = metaInfo.getMetaClass(baseEntity);
			if(meta == null)
				throw new IllegalArgumentException("Class type="+baseEntity.getName()+" was not found, please check that you scanned the right package and look at the logs to see if this class was scanned");
		} else {
			meta = metaInfo.lookupCf(cf);
			if(meta == null)
				throw new IllegalArgumentException("A real CF="+cf+" was not found in the meta data");
			else if(!baseEntity.isAssignableFrom(meta.getMetaClass()))
				throw new IllegalArgumentException("baseEntity="+baseEntity+" was not a superclass of type="+meta.getMetaClass());
		}

		AbstractCursor<Row> allRows = session.allRows(meta.getMetaDbo(), batchSize);
		boolean isVirtual = baseEntity.equals(Object.class);
		if(meta != null) {
			isVirtual = meta.getMetaDbo().isVirtualCf();
		}
		return new CursorRowPlain<T>(session, meta, allRows, metaInfo, isVirtual);
	}

}

--------------------

