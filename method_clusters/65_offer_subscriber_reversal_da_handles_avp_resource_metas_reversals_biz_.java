private SubscriberInfo readSubscriber(String requestId, String customerId, List<Meta> metas) {
		logger.info("Invoking bucket retrieve read rop subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, customerId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch(InterruptedException e) {
			logger.error("Error while acquire() call",this.getClass().getName(),e);
		}
		semaphore.destroy();
		logger.info("Check if response received for Read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}
--------------------

public long getRenewalCount() throws CatalogException {
		long renewalCount = 0;
		Iterator<HistoryEvent> scanHistory = this.subscriptionHistory.iterator();
		while (scanHistory.hasNext()) {
			HistoryEvent event = scanHistory.next();
			if (event.state == SubscriptionLifeCycleState.RENEWAL_SUCCESS)
				renewalCount++;
		}
		
		return renewalCount;
	}
--------------------

@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("Customer Info Charge: process()");
		ReadCustomerInfoChargeRequest request = (ReadCustomerInfoChargeRequest) exchange.getIn().getBody();
		IConfig config = SefCoreServiceResolver.getConfigService();
		String channel = String.valueOf(request.getChannel());
		if(channel == null||channel.isEmpty()){
			logger.debug("Channel is null/empty");
			throw ExceptionUtil.toSmException(ErrorCode.invalidOperation);
		}
		
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.info("Collecting SOAP parameters");
		List<Meta> workflowMetas= new ArrayList<Meta>();
		workflowMetas.add(new Meta("msisdn", String.valueOf(request.getCustomerId())));
		workflowMetas.add(new Meta("AccessKey", String.valueOf(request.getAccessKey())));
		workflowMetas.add(new Meta("channelName", String.valueOf(request.getChannel())));
		workflowMetas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));

		workflowMetas.add(new Meta("READ_SUBSCRIBER","CUSTOMER_INFO_CHARGE")); 
		workflowMetas.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));


		logger.info("Collected SOAP parameters");
		logger.info("Going for Customer Info Charge Call");
		logger.info("Before read subscriber call");
		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), workflowMetas);
		String value = String.valueOf(subscriberObj);
		logger.info("subscriber call "+value);
		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() > 0) {
			logger.error("DB response: " + subscriberObj.getStatus());
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}
		
		if (subscriberObj.getLocalState() == null || subscriberObj.getSubscriber() == null) {
			logger.error("seems like subscriber was not found!!!");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}

		if(subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0 && subscriberObj.getLocalState().name().equalsIgnoreCase(ContractState.PREACTIVE.name())){
			logger.info("PRE_ACTIVE state");
			throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
		}
		if(subscriberObj!=null && subscriberObj.getLocalState().name().equalsIgnoreCase(ContractState.RECYCLED.name())){
			logger.info("DE_ACTIVE state");
			throw ExceptionUtil.toSmException(ErrorCode.invalidLifecycleError1);
		}
		logger.info("Recieved a SubscriberInfo Object and it is not null");
		logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
		logger.info("Billing Metas: " + subscriberObj.getMetas());
		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 

		logger.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();

		exchange.getOut().setBody(readAccountInfo(request.getCustomerId(),request.isTransactional(), subscriberObj.getSubscriber().getMetas()));
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
	}
--------------------

