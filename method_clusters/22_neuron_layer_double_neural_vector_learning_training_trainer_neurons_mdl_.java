public boolean hasInputConnectionFrom(Neuron neuron) {
            for(Connection connection : inputConnections) {
                if (connection.getFromNeuron() == neuron) {
                    return true;
                }
            }            
            return false;            
        }
--------------------

private void initializeNeuralNetworkComponents() {
        comboTransferFunction.addItem("Tanh");
        comboTransferFunction.addItem("Sigmoid");
        neuronsCount = "2 1";
        transferFunctionType = TransferFunctionType.SIGMOID;
        neuralNetwork = NeuralNetworkFactory.createMLPerceptron(neuronsCount, transferFunctionType);
    }
--------------------

@Override
	public void reset() {
		super.reset();
		this.isCompeting = false;
	}
--------------------

