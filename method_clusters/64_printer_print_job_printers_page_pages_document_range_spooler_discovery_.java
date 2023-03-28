private void addFirstFakePrinter() {
        PrinterId printerId = generatePrinterId("1");
        PrinterInfo printer = new PrinterInfo.Builder(printerId, "Printer 1")
                .setStatus(PrinterInfo.STATUS_READY)
                .setMinMargins(new Margins(0, 0, 0, 0), new Margins(0, 0, 0, 0))
                .addMediaSize(PrintAttributes.MediaSize.ISO_A2, false)
                .addMediaSize(PrintAttributes.MediaSize.ISO_A3, false)
                .addMediaSize(PrintAttributes.MediaSize.ISO_A4, false)
                .addMediaSize(PrintAttributes.MediaSize.ISO_A5, false)
                .addMediaSize(PrintAttributes.MediaSize.ISO_A6, false)
                .addMediaSize(PrintAttributes.MediaSize.NA_LETTER, true)
                .addResolution(new Resolution("R1", getPackageName(),
                        R.string.resolution_600x600, 600, 600), true)
                .addInputTray(new Tray("FirstInputTray", getPackageName(),
                        R.string.input_tray_first), false)
                .addOutputTray(new Tray("FirstOutputTray", getPackageName(),
                        R.string.output_tray_first), false)
                .setDuplexModes(PrintAttributes.DUPLEX_MODE_NONE
                        | PrintAttributes.DUPLEX_MODE_LONG_EDGE
                        | PrintAttributes.DUPLEX_MODE_SHORT_EDGE,
                        PrintAttributes.DUPLEX_MODE_NONE)
                .setColorModes(PrintAttributes.COLOR_MODE_COLOR
                        | PrintAttributes.COLOR_MODE_MONOCHROME,
                        PrintAttributes.COLOR_MODE_COLOR)
                .setFittingModes(PrintAttributes.FITTING_MODE_NONE
                        | PrintAttributes.FITTING_MODE_FIT_TO_PAGE,
                        PrintAttributes.FITTING_MODE_NONE)
                .setOrientations(PrintAttributes.ORIENTATION_PORTRAIT
                        | PrintAttributes.ORIENTATION_LANDSCAPE,
                        PrintAttributes.ORIENTATION_PORTRAIT)
                .create();
        List<PrinterInfo> printers = new ArrayList<PrinterInfo>();
        printers.add(printer);
        addPrinters(printers);
    }
--------------------

@Override
        public void run() {
            mPosted = false;
            mPrinterUnavailable = true;
            onPrinterUnavailable(mPrinter);
        }
--------------------

public static PrinterJob getPrinterJob (String printerName)
	{
		PrinterJob pj = null;
		PrintService ps = null;
		try
		{
			pj = PrinterJob.getPrinterJob();

			//  find printer service
			if (printerName == null || printerName.length() == 0)
				printerName = Ini.getProperty(Ini.P_PRINTER);
			if (printerName != null && printerName.length() != 0)
			{
			//	System.out.println("CPrinter.getPrinterJob - searching " + printerName);
				for (int i = 0; i < s_services.length; i++)
				{
					String serviceName = s_services[i].getName();
					if (printerName.equals(serviceName))
					{
						ps = s_services[i];
					//	System.out.println("CPrinter.getPrinterJob - found " + printerName);
						break;
					}
				//	System.out.println("CPrinter.getPrinterJob - not: " + serviceName);
				}
			}   //  find printer service

			try
			{
				if (ps != null)
					pj.setPrintService(ps);
			}
			catch (Exception e)
			{
				log.warning("Could not set Print Service: " + e.toString());
			}
			//
			PrintService psUsed = pj.getPrintService();
			if (psUsed == null)
				log.warning("Print Service not Found");
			else
			{
				String serviceName = psUsed.getName();
				if (printerName != null && !printerName.equals(serviceName))
					log.warning("Not found: " + printerName + " - Used: " + serviceName);
			}
		}
		catch (Exception e)
		{
			log.warning("Could not create for " + printerName + ": " + e.toString());
		}
		return pj;
	}
--------------------

