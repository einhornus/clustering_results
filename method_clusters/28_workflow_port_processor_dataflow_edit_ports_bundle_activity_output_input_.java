@Override
	protected void cloneInto(WorkflowBean clone, Cloning cloning) {
		BlockingControlLink cloneLink = (BlockingControlLink) clone;
		cloneLink.setBlock(cloning.cloneOrOriginal(getBlock()));
		cloneLink.setUntilFinished(cloning.cloneOrOriginal(getUntilFinished()));
	}
--------------------

protected OntModel save(final WorkflowBundle bundle) {
		final OntModel model = ModelFactory.createOntologyModel();
		bundle.accept(new VisitorWithPath() {
			Scufl2Tools scufl2Tools = new Scufl2Tools();

			public boolean visit() {
				WorkflowBean node = getCurrentNode();
				// System.out.println(node);
				if (node instanceof WorkflowBundle) {
					return true;
				}
				// @SuppressWarnings("rawtypes")

				if (node instanceof org.apache.taverna.scufl2.api.core.Workflow) {
					entityForBean(node, wfdesc.Workflow);
				} else if (node instanceof Processor) {
					Processor processor = (Processor) node;
					Individual process = entityForBean(processor, wfdesc.Process);
					Individual wf = entityForBean(processor.getParent(), wfdesc.Workflow);
					wf.addProperty(wfdesc.hasSubProcess, process);
				} else if (node instanceof InputPort) {
					WorkflowBean parent = ((Child) node).getParent();
					Individual input = entityForBean(node, wfdesc.Input);
					Individual process = entityForBean(parent, wfdesc.Process);
					process.addProperty(wfdesc.hasInput, input);

				} else if (node instanceof OutputPort) {
					WorkflowBean parent = ((Child) node).getParent();
					Individual output = entityForBean(node, wfdesc.Output);
					Individual process = entityForBean(parent, wfdesc.Process);
					process.addProperty(wfdesc.hasOutput, output);
				} else if (node instanceof DataLink) {
					WorkflowBean parent = ((Child) node).getParent();
					DataLink link = (DataLink) node;
					Individual dl = entityForBean(link, wfdesc.DataLink);

					Individual source = entityForBean(link.getReceivesFrom(), wfdesc.Output);
					dl.addProperty(wfdesc.hasSource, source);

					Individual sink = entityForBean(link.getSendsTo(), wfdesc.Input);
					dl.addProperty(wfdesc.hasSink, sink);
					Individual wf = entityForBean(parent, wfdesc.Workflow);
					wf.addProperty(wfdesc.hasDataLink, dl);
				} else if (node instanceof Profile) {
					// So that we can get at the ProcessorBinding - buy only if
					// it is the main Profile
					return node == bundle.getMainProfile();
				} else if (node instanceof ProcessorBinding) {
					ProcessorBinding b = (ProcessorBinding) node;
					Activity a = b.getBoundActivity();
					Processor boundProcessor = b.getBoundProcessor();
					Individual process = entityForBean(boundProcessor, wfdesc.Process);

					// Note: We don't describe the activity and processor
					// binding in wfdesc. Instead we
					// assign additional types and attributes to the parent
					// processor

					try {
						URI type = a.getType();
						Configuration c = scufl2Tools.configurationFor(a, b.getParent());
						JsonNode json = c.getJson();
						if (type.equals(BEANSHELL)) {
							process.addRDFType(wf4ever.BeanshellScript);
							String s = json.get("script").asText();
							process.addProperty(wf4ever.script, s);
							JsonNode localDep = json.get("localDependency");
							if (localDep != null && localDep.isArray()) {
								for (int i = 0; i < localDep.size(); i++) {
									String depStr = localDep.get(i).asText();
									// FIXME: Better class for dependency?
									Individual dep = model.createIndividual(OWL.Thing);
									dep.addLabel(depStr, null);
									dep.addComment("JAR dependency", "en");
									process.addProperty(roterms.requiresSoftware, dep);
									// Somehow this gets the whole thing to fall
									// out of the graph!
									// QName depQ = new
									// QName("http://google.com/", ""+
									// UUID.randomUUID());
									// sesameManager.rename(dep, depQ);

								}
							}
						}
						if (type.equals(RSHELL)) {
							process.addRDFType(wf4ever.RScript);
							String s = json.get("script").asText();
							process.addProperty(wf4ever.script, s);
						}
						if (type.equals(WSDL)) {
							process.addRDFType(wf4ever.SOAPService);
							JsonNode operation = json.get("operation");
							URI wsdl = URI.create(operation.get("wsdl").asText());
							process.addProperty(wf4ever.wsdlURI, wsdl.toASCIIString());
							process.addProperty(wf4ever.wsdlOperationName, operation.get("name").asText());
							process.addProperty(wf4ever.rootURI, wsdl.resolve("/").toASCIIString());
						}
						if (type.equals(REST)) {
							process.addRDFType(wf4ever.RESTService);
							// System.out.println(json);
							JsonNode request = json.get("request");
							String absoluteURITemplate = request.get("absoluteURITemplate").asText();
							String uriTemplate = absoluteURITemplate.replace("{", "");
							uriTemplate = uriTemplate.replace("}", "");
							// TODO: Detect {}
							try {
								URI root = new URI(uriTemplate).resolve("/");
								process.addProperty(wf4ever.rootURI, root.toASCIIString());
							} catch (URISyntaxException e) {
								logger.warning("Potentially invalid URI template: " + absoluteURITemplate);
								// Uncomment to temporarily break
								// TestInvalidURITemplate:
								// rest.getWfRootURI().add(URI.create("http://example.com/FRED"));
							}
						}
						if (type.equals(TOOL)) {
							process.addRDFType(wf4ever.CommandLineTool);
							JsonNode desc = json.get("toolDescription");
							// System.out.println(json);
							JsonNode command = desc.get("command");
							if (command != null) {
								process.addProperty(wf4ever.command, command.asText());
							}
						}
						if (type.equals(NESTED_WORKFLOW)) {
							Workflow nestedWf = scufl2Tools.nestedWorkflowForProcessor(boundProcessor, b.getParent());
							// The parent process is a specialization of the
							// nested workflow
							// (because the nested workflow could exist as
							// several processors)
							specializationOf(boundProcessor, nestedWf);
							process.addRDFType(wfdesc.Workflow);

							// Just like the Processor specializes the nested
							// workflow, the
							// ProcessorPorts specialize the WorkflowPort
							for (ProcessorPortBinding portBinding : b.getInputPortBindings()) {
								// Map from activity port (not in wfdesc) to
								// WorkflowPort
								WorkflowPort wfPort = nestedWf.getInputPorts()
										.getByName(portBinding.getBoundActivityPort().getName());
								if (wfPort == null) {
									continue;
								}
								specializationOf(portBinding.getBoundProcessorPort(), wfPort);
							}
							for (ProcessorPortBinding portBinding : b.getOutputPortBindings()) {
								WorkflowPort wfPort = nestedWf.getOutputPorts()
										.getByName(portBinding.getBoundActivityPort().getName());
								if (wfPort == null) {
									continue;
								}
								specializationOf(portBinding.getBoundProcessorPort(), wfPort);
							}
						}
					} catch (IndexOutOfBoundsException ex) {
					}
					return false;
				} else {
					// System.out.println("--NO!");
					return false;
				}
				for (Annotation ann : scufl2Tools.annotationsFor(node, bundle)) {
					String annotationBody = ann.getBody().toASCIIString();
					String baseURI = bundle.getGlobalBaseURI().resolve(ann.getBody()).toASCIIString();
					InputStream annotationStream;
					try {
						annotationStream = bundle.getResources().getResourceAsInputStream(annotationBody);

						try {
							// FIXME: Don't just assume Lang.TURTLE
							RDFDataMgr.read(model, annotationStream, baseURI, Lang.TURTLE);
						} catch (RiotException e) {
							logger.log(Level.WARNING, "Can't parse RDF Turtle from " + annotationBody, e);
						} finally {
							annotationStream.close();
						}
					} catch (IOException e) {
						logger.log(Level.WARNING, "Can't read " + annotationBody, e);
					}
				}
				if (node instanceof Named) {
					Named named = (Named) node;
					entityForBean(node, OWL.Thing).addLabel(named.getName(), null);
				}
				return true;
			}

			private void specializationOf(WorkflowBean special, WorkflowBean general) {
				Individual specialEnt = entityForBean(special, prov.Entity);
				Individual generalEnt = entityForBean(general, prov.Entity);
				specialEnt.addProperty(prov.specializationOf, generalEnt);
			}

			private Individual entityForBean(WorkflowBean bean, Resource thing) {
				return model.createIndividual(uriForBean(bean), thing);
			}

			// @Override
			// public boolean visitEnter(WorkflowBean node) {
			// if (node instanceof Processor
			// || node instanceof org.apache.taverna.scufl2.api.core.Workflow
			// || node instanceof Port || node instanceof DataLink) {
			// visit(node);
			// return true;
			// }
			// // The other node types (e.g. dispatch stack, configuration) are
			// // not (directly) represented in wfdesc
			//// System.out.println("Skipping " + node);
			// return false;
			// };
		});
		return model;
	}
--------------------

@Override
	public Set<ActivityInputPort> getInputPorts(JsonNode configuration)
			throws ActivityConfigurationException {
		Set<ActivityInputPort> inputPorts = new HashSet<>();
		if (configuration.has("inputPorts")) {
//			for (JsonNode inputPort : configuration.get("inputPorts")) {
                        for (Iterator<JsonNode> iter = configuration.get("inputPorts").iterator();iter.hasNext();) {
                                JsonNode inputPort = iter.next();
				inputPorts.add(edits.createActivityInputPort(inputPort.get("name").textValue(),
						inputPort.get("depth").intValue(), false, null, String.class));
			}
		}
		return inputPorts;
	}
--------------------

