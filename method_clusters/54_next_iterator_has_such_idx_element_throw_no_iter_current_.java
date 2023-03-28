public String nextElement() throws NoSuchElementException
            {
                if (field != null || hasMoreElements())
                {
                    String n = BufferUtil.to8859_1_String(field._name);
                    field = null;
                    return n;
                }
                throw new NoSuchElementException();
            }
--------------------

public E next() {
            E item = nextItem;
            if (item == null) throw new NoSuchElementException();
            advance();
            return item;
        }
--------------------

@Override
    public int doStartTag() throws JspTagException {
        IteratorTag iteratorTag =
            (IteratorTag) findAncestorWithClass(this, IteratorTag.class);

        if (iteratorTag == null)
            throw new JspTagException("IterateNextTag not inside IteratorTag.");

        Iterator<? extends Object> iterator = iteratorTag.getIterator();

        if (iterator == null || !iterator.hasNext())
            return SKIP_BODY;

        if (name == null)
            name = "next";

        // get the next element from the iterator
        Object element = iterator.next();

        pageContext.setAttribute(name, element);

        // expand a map element here if requested
        if (expandMap) {
            Map<String, ?> tempMap = UtilGenerics.cast(element);
            Iterator<Map.Entry<String, ?>> mapEntries = UtilGenerics.cast(tempMap.entrySet().iterator());

            while (mapEntries.hasNext()) {
                Map.Entry<String, ?> entry = mapEntries.next();
                Object value = entry.getValue();

                if (value == null) value = "";
                pageContext.setAttribute(entry.getKey(), value);
            }
        }

        // give the updated iterator back.
        iteratorTag.setIterator(iterator);

        return EVAL_BODY_AGAIN;
    }
--------------------

