public COST_OO_FileLoader(SPECCHIOClient specchio_client, SpecchioCampaignDataLoader campaignDataLoader) {
		super("COST_OO_CSV", specchio_client, campaignDataLoader);


	}
--------------------

public InstrumentDescriptor[] getInstrumentDescriptors() throws SPECCHIOClientException {
		
		return realClient.getInstrumentDescriptors();
		
	}
--------------------

@POST
	@Path("loadSpace")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Space loadSpace(Space space) throws SPECCHIOFactoryException {
		
		SpaceFactory factory = new SpaceFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		factory.loadSpace(space);
		factory.dispose();
		
		return space;
		
	}
--------------------

