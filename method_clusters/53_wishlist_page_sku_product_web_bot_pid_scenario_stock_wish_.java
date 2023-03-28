public void goToProduct(String productId) {
        String originalPath = getPath();
        setPath(getPath() + "/" + productId);
        go();
        setPath(originalPath);
    }
--------------------

@Override
    public List<Product> getUnderPrice(Float max) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findProductsWithPriceRaiseOf");
        query.setParameter("max", max);
        return query.getResultList();
    }
--------------------

@Then("^the products are displayed in correct order$")
    public void the_products_are_displayed_in_correct_price_order() throws Throwable {

        // Note: without delay I got
        // org.openqa.selenium.StaleElementReferenceException: By is no longer attached to the DOM (WARNING: The server did not provide any stacktrace information)
        // TODO: better way to fix this -- close pop up
        Thread.sleep(3000);
        SortOrder sortOrder = (SortOrder) scenarioSession.getData(SORT_ORDER);

        List<Integer> integers = null;

        switch (sortOrder) {
            case PRICE_HIGH:
            case PRICE_LOW:
                integers = getNapProductListPage().getAllProductPrices();
                break;
            case DISCOUNT_HIGH_TO_LOW:
            case DISCOUNT_LOW:
                integers = getNapProductListPage().getAllProductPercentages();
                break;
            default:
                fail("Don't support this order yet in assertion: " + sortOrder);
        }
        assertTrue("Products are not sorted in correct order: " + sortOrder, SortOrderAssertion.assertIntegerValuesInCorrectOrders(sortOrder, integers));
    }
--------------------

