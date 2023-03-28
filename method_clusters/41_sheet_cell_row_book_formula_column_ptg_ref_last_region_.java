private ForkedEvaluationSheet getSharedSheet(String sheetName) {
		ForkedEvaluationSheet result = _sharedSheetsByName.get(sheetName);
		if (result == null) {
			result = new ForkedEvaluationSheet(_masterBook.getSheet(_masterBook
					.getSheetIndex(sheetName)));
			_sharedSheetsByName.put(sheetName, result);
		}
		return result;
	}
--------------------

@Override
	AbstractRowAdv getRow(int rowIdx, boolean proxy) {
		AbstractRowAdv rowObj = _rows.get(rowIdx);
		if(rowObj != null){
			return rowObj;
		}
		return proxy?new RowProxy(this,rowIdx):null;
	}
--------------------

public static void setWholeRowCellStyle(SRange range, StyleApplyer applyer){
		SSheet sheet = range.getSheet();
		for(int r = range.getRow(); r <= range.getLastRow(); r++){
			SRow row = sheet.getRow(r);
			applyer.applyStyle(row);
			
			HashSet<Integer> cellProcessed = new HashSet<Integer>();
			
			Iterator<SCell> cells = sheet.getCellIterator(r);
			while(cells.hasNext()){
				SCell cell = cells.next();
				//the case the cell or column has local style
				if(cell.getCellStyle(true)!=null ||
						sheet.getColumn(cell.getColumnIndex()).getCellStyle(true)!=null){
					applyer.applyStyle(cell);
				}
				cellProcessed.add(cell.getColumnIndex());
			}
			
			//has to force set the style on the row/column across cell to avoid row/column style conflict on null cell
			Iterator<SColumn> columns = sheet.getColumnIterator();
			while(columns.hasNext()){
				SColumn column = columns.next();
				if(cellProcessed.contains(column.getIndex())){
					continue;
				}
				if(column.getCellStyle(true)!=null){
					applyer.applyStyle(sheet.getCell(r, column.getIndex()));
				}
			}
		}
	}
--------------------

