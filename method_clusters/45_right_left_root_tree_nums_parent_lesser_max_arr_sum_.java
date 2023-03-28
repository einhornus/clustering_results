public static Node findHighest(Node node, int currentRank, int k) {
        if (node == null) {
            return null;
        }

        Node result;
        // is the searhed node in the left subtree?
        if (currentRank + getRank(node.left) >= k) {
            result = findHighest(node.left, currentRank, k);
        }
        // current node?
        else if (currentRank + getRank(node.left) + 1 == k) {
            return node;
        }
        // right subtree?
        else {
            result = findHighest(node.right, currentRank + getRank(node.left) + 1, k);
        }

        if (result == null) {
            return node;
        }
        return result;
    }
--------------------

private void replaceWithChild(RedBlackNode<T> nodeToReplace, RedBlackNode<T> nodeToReplaceWith) {
		nodeToReplace.id = nodeToReplaceWith.id;
		nodeToReplace.color = nodeToReplaceWith.color;
		nodeToReplace.lesser = nodeToReplaceWith.lesser;
		if (nodeToReplace.lesser!=null) nodeToReplace.lesser.parent = nodeToReplace;
		nodeToReplace.greater = nodeToReplaceWith.greater;
		if (nodeToReplace.greater!=null) nodeToReplace.greater.parent = nodeToReplace;
	}
--------------------

private Node search(Session session, Row row) throws HsqlException {

        Object[] d = row.getData();
        Node     x = getRoot(session);

        while (x != null) {
            int c = compareRowForInsert(session, row, x.getRow());

            if (c == 0) {
                return x;
            } else if (c < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        return null;
    }
--------------------

