public Connection getConnection() {
		
		try{
			DriverManager.registerDriver(new org.postgresql.Driver());
			return DriverManager.getConnection("jdbc:postgresql://localhost:5432/spring", "root", "502010");
		}catch(SQLException e){
			throw new RuntimeException();
		}
		
	}
--------------------

public CassandraCacheStoreFactory<K, V> setDataSource(DataSource dataSrc) {
        this.dataSrc = dataSrc;

        return this;
    }
--------------------

public static void setDBTarget (CConnection cc)
	{
		if (cc == null)
			throw new IllegalArgumentException("Connection is NULL");

		if (s_cc != null && s_cc.equals(cc))
			return;

		DB.closeTarget();
		//
		synchronized(s_ccLock)
		{
			s_cc = cc;
		}

		s_cc.setDataSource();

		log.config(s_cc + " - DS=" + s_cc.isDataSource());
	//	Trace.printStack();
	}
--------------------

