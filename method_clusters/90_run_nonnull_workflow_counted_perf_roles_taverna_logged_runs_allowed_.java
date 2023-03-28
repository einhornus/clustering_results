@Override
	@CallCounted
	@PerfLogged
	public String getOwner() {
		return context.getOwner().getName();
	}
--------------------

@Override
	@CallCounted
	@PerfLogged
	@RolesAllowed(USER)
	public String getStderr() throws NoListenerException {
		return support.getProperty(run, "io", "stderr");
	}
--------------------

@RolesAllowed(ADMIN)
	@Override
	public boolean setLogWorkflows(boolean newValue) {
		state.setLogIncomingWorkflows(newValue);
		return state.getLogIncomingWorkflows();
	}
--------------------

