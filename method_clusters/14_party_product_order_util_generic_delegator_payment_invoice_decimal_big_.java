public String addShippingGroup(String carrierPartyId, String shipmentMethodTypeId, String contactMechId, String supplierPartyId) {
        // generate a sequence id for this shipping group
        String shipGroupSeqId = sequencify(orderItemShipGroupInfos.size() + 1);

        GenericValue oisg = delegator.makeValue("OrderItemShipGroup");
        oisg.set("carrierPartyId", carrierPartyId);
        oisg.set("shipGroupSeqId", shipGroupSeqId);
        oisg.set("shipmentMethodTypeId", shipmentMethodTypeId);
        oisg.set("maySplit", Boolean.FALSE); // needed for ShoppingcartItem.loadCartFromOrder
        oisg.set("isGift", Boolean.FALSE); // needed for ShoppingcartItem.loadCartFromOrder
        oisg.set("supplierPartyId", supplierPartyId);
        if (contactMechId != null) {
            oisg.set("contactMechId", contactMechId);
        }
        if (shipByDate != null) {
            oisg.set("shipByDate", shipByDate);
        }
        orderItemShipGroupInfos.add(oisg);
        orderItemShipGroups.add(oisg);

        taxInfoMap.put(shipGroupSeqId, new TaxProductInfo());

        return shipGroupSeqId;
    }
--------------------

@Override
    public Map<String, Object> toMap() {
        Map<String, Object> mapValue = new FastMap<String, Object>();
        mapValue.put("orderId", getOrderId());
        mapValue.put("partyId", getPartyId());
        mapValue.put("orderName", getOrderName());
        mapValue.put("orderDate", getOrderDate());
        mapValue.put("productStoreId", getProductStoreId());
        mapValue.put("statusId", getStatusId());
        mapValue.put("statusDescription", getStatusDescription());
        mapValue.put("correspondingPoId", getCorrespondingPoId());
        mapValue.put("currencyUom", getCurrencyUom());
        mapValue.put("grandTotal", getGrandTotal());
        mapValue.put("orderNameId", getOrderNameId());
        mapValue.put("partyName", getPartyName());
        mapValue.put("shipByDateString", getShipByDateString());
        mapValue.put("orderDateString", getOrderDateString());
        mapValue.put("trackingCodeId", getTrackingCodeId());
        return mapValue;
    }
--------------------

public static Map<String, Object> importOrders(DispatchContext dctx, Map<String, Object> context) {
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");
        TimeZone timeZone = (TimeZone) context.get("timeZone");

        try {

            // Make sure the productStore and organizationParty really exist
            GenericValue productStore = delegator.findByPrimaryKey("ProductStore", UtilMisc.toMap("productStoreId", AmazonConstants.productStoreId));
            if (UtilValidate.isEmpty(productStore)) {
                String errorLog = UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_NoProductStore", UtilMisc.toMap("productStoreId", AmazonConstants.productStoreId), locale);
                return ServiceUtil.returnError(errorLog);
            }
            GenericValue organizationParty = productStore.getRelatedOne("Party");
            if (UtilValidate.isEmpty(organizationParty)) {
                String errorLog = UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_NoOrgParty", UtilMisc.toMap("organizationPartyId", productStore.getString("payToPartyId")), locale);
                return ServiceUtil.returnError(errorLog);
            }

            EntityCondition cond = EntityCondition.makeCondition(EntityOperator.OR,
                                            EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, AmazonConstants.statusOrderCreated),
                                            EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, AmazonConstants.statusOrderImportedError));
            TransactionUtil.begin();
            EntityListIterator amazonOrderIt = delegator.findListIteratorByCondition("AmazonOrder", cond, null, null);
            TransactionUtil.commit();

            GenericValue amazonOrder = null;
            while ((amazonOrder = amazonOrderIt.next()) != null) {
                String errorMessage = null;
                boolean importSuccess = true;
                try {
                    TransactionUtil.begin();

                    // Check to see that the order *really* hasn't been imported
                    GenericValue amazonOrderImport = amazonOrder.getRelatedOne("AmazonOrderImport");
                    if (UtilValidate.isNotEmpty(amazonOrderImport)) {
                        errorMessage = UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_OrderAlreadyImported", UtilMisc.toMap("orderId", amazonOrderImport.getString("orderId")), locale);
                        throw new Exception(errorMessage);
                    }

                    // Check to see that importing of the order hasn't failed too many times
                    if (AmazonConstants.orderImportRetryThreshold <= amazonOrder.getLong("importFailures").intValue()) {
                        String errorLog = UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_ImportAttemptsOverThreshold", UtilMisc.<String, Object>toMap("amazonOrderId", amazonOrder.getString("amazonOrderId"), "threshold", AmazonConstants.orderImportRetryThreshold), locale);
                        Debug.logInfo(errorLog, MODULE);
                        continue;
                    }

                    String orderId = importOrder(dispatcher, delegator, amazonOrder, locale, userLogin, productStore, timeZone);

                    String successMessage = UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_OrderImportSuccess", UtilMisc.toMap("amazonOrderId", amazonOrder.getString("amazonOrderId"), "orderId", orderId), locale);
                    Debug.logInfo(successMessage, MODULE);

                    TransactionUtil.commit();

                } catch (Exception e) {
                    TransactionUtil.rollback();
                    Map<String, String> errorMap = UtilMisc.toMap("amazonOrderId", amazonOrder.getString("amazonOrderId"), "errorMessage", e.getMessage());
                    errorMessage = UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_OrderImportError", errorMap, locale);
                    Debug.logError(errorMessage, MODULE);
                    importSuccess = false;
                    if (AmazonConstants.sendErrorEmails) {
                        AmazonUtil.sendErrorEmail(dispatcher, userLogin, errorMap, UtilProperties.getMessage(AmazonConstants.errorResource, "AmazonError_ErrorEmailSubject_ImportOrder", errorMap, AmazonConstants.errorEmailLocale), AmazonConstants.errorEmailScreenUriOrders);
                    }
                }
                amazonOrder.set("statusId", importSuccess ? AmazonConstants.statusOrderImported : AmazonConstants.statusOrderImportedError);
                amazonOrder.set("importTimestamp", UtilDateTime.nowTimestamp());
                amazonOrder.set("importErrorMessage", errorMessage);
                amazonOrder.set("importFailures", importSuccess ? 0 : amazonOrder.getLong("importFailures") + 1);
                amazonOrder.store();
            }
            amazonOrderIt.close();

        } catch (GenericServiceException e) {
            return UtilMessage.createAndLogServiceError(e, MODULE);
        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogServiceError(e, MODULE);
        }

        return ServiceUtil.returnSuccess();
    }
--------------------

