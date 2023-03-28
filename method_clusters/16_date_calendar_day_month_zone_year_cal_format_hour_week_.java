private SimpleDateFormat getDateFormat() {
        if (this.dateFormatter == null) {
            this.dateFormatter = new SimpleDateFormat(DATE_FORMAT, DATE_LOCALE);
            this.dateFormatter.setTimeZone(TIME_ZONE);
        }
        return this.dateFormatter;
    }
--------------------

public void setValue (Object value)
	{
		log.finest("Value=" + value);
		m_oldText = "";
		if (value == null)
			;
		else if (value instanceof java.util.Date)
			m_oldText = m_format.format(value);
		else
		{
			String strValue = value.toString();
			//	String values - most likely in YYYY-MM-DD	(JDBC format)
			try
			{
				java.util.Date date = DisplayType.getDateFormat_JDBC().parse (strValue);
				m_oldText = m_format.format(date);		//	convert to display value
			}
			catch (ParseException pe0)
			{
			//	Try local string format
				try
				{
					java.util.Date date = m_format.parse(strValue);
					m_oldText = m_format.format(date);
				}
				catch (ParseException pe1)
				{
					log.log(Level.SEVERE, "setValue - " + pe1.getMessage());
					m_oldText = "";
				}
			}
		}
		if (m_setting)
			return;
		m_text.setText(m_oldText);
		m_initialText = m_oldText;
	}
--------------------

private static Calendar getFirstDayOfNextMonth(Calendar date) {
        Calendar newDate = (Calendar) date.clone();
        if (date.get(Calendar.MONTH) < Calendar.DECEMBER) {
            newDate.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1);
        } else {
            newDate.set(Calendar.MONTH, 1);
            newDate.set(Calendar.YEAR, date.get(Calendar.YEAR) + 1);
        }
        newDate.set(Calendar.DATE, 1);
        return newDate;
    }
--------------------

