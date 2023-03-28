public boolean isGroupBy () 
	{
		Object oo = get_Value(COLUMNNAME_IsGroupBy);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
--------------------

public boolean isFixedAsset () 
	{
		Object oo = get_Value(COLUMNNAME_IsFixedAsset);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
--------------------

public boolean isStartNewYear () 
	{
		Object oo = get_Value(COLUMNNAME_StartNewYear);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
--------------------

