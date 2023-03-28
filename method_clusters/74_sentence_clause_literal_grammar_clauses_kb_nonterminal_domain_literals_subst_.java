public List<List<ActionSchema>> refinements(ActionSchema hla, State outcome) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (List<ActionSchema> refinement : ((HighLevelAction) hla).getRefinements()) {
            if (refinement.size() > 0) {
                if (outcome.isApplicable(refinement.get(0)))
                    result.add(refinement);
            } else
                result.add(refinement);
        }
        return result;
    }
--------------------

@Test
	public void testIsDefiniteClause() {
		Clause c1 = new Clause();
		Assert.assertFalse(c1.isDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertFalse(c1.isDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertTrue(c1.isDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		Assert.assertTrue(c1.isDefiniteClause());
		c1.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		Assert.assertTrue(c1.isDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred5", new ArrayList<Term>()));
		Assert.assertFalse(c1.isDefiniteClause());
	}
--------------------

private void constructFOLDomain() {
		// Ensure the target predicate is included
		addPredicate(getFOLName(dataSetSpecification.getTarget()));
		// Create the descriptive predicates
		for (String saName : dataSetSpecification.getNamesOfStringAttributes()) {
			if (dataSetSpecification.getTarget().equals(saName)) {
				// Don't add the target to the descriptive predicates
				continue;
			}
			String folSAName = getFOLName(saName);
			// Add a predicate for the attribute
			addPredicate(folSAName);

			descriptionPredicateNames.add(folSAName);
			descriptionDataSetNames.add(saName);

			List<String> attributeValues = dataSetSpecification
					.getPossibleAttributeValues(saName);
			// If a multivalued attribute need to setup
			// Constants for the different possible values
			if (isMultivalued(saName)) {
				for (String av : attributeValues) {
					addConstant(getFOLName(av));
				}
			}
		}
	}
--------------------

