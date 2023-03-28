public void actionPerformed(ActionEvent arg0) {
				
				weightedGraph.addEdge(strToInt(tVertice1.getText()), strToInt(tVertice2.getText()), strToInt(tWeight.getText()));
				
				tVertice1.setText(null);
				tVertice2.setText(null);
				tWeight.setText(null);	
			}
--------------------

public void addEdge(int from, int to) {
            adjacencyList[from].add(to);
            adjacencyList[to].add(from);
            checkEdges(from);
            checkEdges(to);
        }
--------------------

public String toString() {
        String g = (this.directed ? "di" : "") + "graph " + this.id + " {\n";
        g += graphOptions();
        if (this.nodes != null)
            for (int i = 0; i < this.nodes.length; i++) {
                g += this.nodes[i];
            }
        if (this.edges != null) {
            for (int i = 0; i < this.edges.length; i++) {
                g += this.edges[i];
            }
        }
        return g + "}";
    }
--------------------

