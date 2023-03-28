public X_AD_Queue (Properties ctx, int AD_Queue_ID, String trxName)
    {
      super (ctx, AD_Queue_ID, trxName);
      /** if (AD_Queue_ID == 0)
        {
			setAD_Queue_ID (0);
			setAD_QueueType_ID (0);
        } */
    }
--------------------

public void setAD_Table_ScriptValidator_ID (int AD_Table_ScriptValidator_ID)
	{
		if (AD_Table_ScriptValidator_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Table_ScriptValidator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Table_ScriptValidator_ID, Integer.valueOf(AD_Table_ScriptValidator_ID));
	}
--------------------

public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Defaults Payment Term
		int ii = 0;
		if (isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		if (ii != 0)
			setC_PaymentTerm_ID(ii);
		//	Default Price List
		if (isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		if (ii != 0)
			setM_PriceList_ID(ii);
		//	Default Delivery/Via Rule
		String ss = bp.getDeliveryRule();
		if (ss != null && ss.trim().length() != 0)
			setDeliveryRule(ss);
		ss = bp.getDeliveryViaRule();
		if (ss != null && ss.trim().length() != 0)
			setDeliveryViaRule(ss);
		//	Default Invoice/Payment Rule
		ss = bp.getInvoiceRule();
		if (ss != null && ss.trim().length() != 0)
			setInvoiceRule(ss);
		ss = bp.getPaymentRule();
		if (ss != null && ss.trim().length() != 0)
			setPaymentRule(ss);
		//	Sales Rep
		ii = bp.getSalesRep_ID();
		if (ii != 0)
			setSalesRep_ID(ii);
		
		//	Discount
		setIsDiscountPrinted(bp.isDiscountPrinted());
		//	Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (int i = 0; i < locs.length; i++)
			{
				if (locs[i].isShipTo())
					super.setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
				if (locs[i].isBillTo())
					setBill_Location_ID(locs[i].getC_BPartner_Location_ID());
			}
			//	set to first
			if (getC_BPartner_Location_ID() == 0 && locs.length > 0)
				super.setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
			if (getBill_Location_ID() == 0 && locs.length > 0)
				setBill_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
		{	
			throw new BPartnerNoShipToAddressException(bp);
		}	
			
		if (getBill_Location_ID() == 0)
		{
			throw new BPartnerNoBillToAddressException(bp);
		}	

		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if (contacts != null && contacts.length == 1)
			setAD_User_ID(contacts[0].getAD_User_ID());
	}
--------------------

