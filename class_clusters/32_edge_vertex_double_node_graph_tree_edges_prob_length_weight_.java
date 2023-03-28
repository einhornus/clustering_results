package dr.evomodelxml.coalescent;

import dr.evolution.io.Importer;
import dr.evomodel.coalescent.GPSkytrackAnalysis;
import dr.inference.model.Parameter;
//import dr.evomodel.coalescent.GaussianProcessSkytrackLikelihood;
import dr.inference.trace.TraceException;
import dr.util.FileHelpers;
import dr.xml.*;

import java.io.File;
//import java.io.FileWriter;
//import java.io.PrintWriter;

/**
 */
public class GPSkytrackAnalysisParser extends AbstractXMLObjectParser {

    public static final String GP_ANALYSIS = "GPAnalysis";
    public static final String FILE_NAME = "fileName";
    public static final String BURN_IN = "burnIn";
//    public static final String HPD_LEVELS = "Confidencelevels";
//    public static final String QUANTILES = "useQuantiles";
//    public static final String LOG_SPACE = VariableDemographicModelParser.LOG_SPACE;
    public static final String N_GRID = "numGridPoints";
//    public static final String N_CHANGES = "nChanges";

//    public static final String TREE_LOG = "treeOfLoci";

    public static final String LOG_FILE_NAME = "logFileName";

    private String getElementText(XMLObject xo, String childName) throws XMLParseException {
        return xo.getChild(childName).getStringChild(0);
    }

    public String getParserName() {
        return GP_ANALYSIS;
    }

    public Object parseXMLObject(XMLObject xo) throws XMLParseException {
        System.err.println("The Summary Statistics are being created...");

        try {

            // 10% is brun-in default
            final double burnin = xo.getAttribute(BURN_IN, 0.1);
            if (burnin < 0) {
                throw new XMLParseException("burnIn should be either between 0 and 1 or a positive number");
            }



            Parameter numGridPoints = new Parameter.Default(0,1);
            if (xo.getChild(N_GRID) != null) {
                XMLObject cxo = xo.getChild(N_GRID);
                numGridPoints = (Parameter) cxo.getChild(Parameter.class);
            }


            final File log = FileHelpers.getFile(getElementText(xo, LOG_FILE_NAME));


            return new dr.evomodel.coalescent.GPSkytrackAnalysis(log,burnin,numGridPoints);

        } catch (java.io.IOException ioe) {
            throw new XMLParseException(ioe.getMessage());
        } catch (Importer.ImportException e) {
            throw new XMLParseException(e.toString());
        } catch (TraceException e) {
            throw new XMLParseException(e.toString());
        }
    }

    //************************************************************************
    // AbstractXMLObjectParser implementation
    //************************************************************************

    public String getParserDescription() {
        return "reconstruct population graph from GPSkytrack run.";
    }

    public Class getReturnType() {
        return GPSkytrackAnalysis.class;
    }

    public XMLSyntaxRule[] getSyntaxRules() {
        return rules;
    }

    private final XMLSyntaxRule[] rules = {
            AttributeRule.newDoubleRule(BURN_IN, true, "The number of states (not sampled states, but" +
                    " actual states) that are discarded from the beginning of the trace and are excluded from " +
                    "the analysis"),
            new ElementRule(LOG_FILE_NAME, String.class, "The name of a BEAST log file"),
            new ElementRule(N_GRID, new XMLSyntaxRule[]{
             new ElementRule(Parameter.class)
    })
    };

}

--------------------

package edu.stanford.rsl.conrad.dimreduction.objfunctions;


import edu.stanford.rsl.conrad.dimreduction.DimensionalityReduction;
import edu.stanford.rsl.conrad.dimreduction.utils.HelperClass;
import edu.stanford.rsl.conrad.dimreduction.utils.PointCloudViewableOptimizableFunction;
import edu.stanford.rsl.jpop.GradientOptimizableFunction;

/*
 * Copyright (C) 2013-14  Susanne Westphal, Andreas Maier
 * CONRAD is developed as an Open Source project under the GNU General Public License (GPL).
*/
public class WeightedInnerProductObjectiveFunction extends PointCloudViewableOptimizableFunction
		implements GradientOptimizableFunction {

	private double[][] distanceMap;
	private double k;
	private DimensionalityReduction dimRed;
	private int versionWIPOF;

	/**
	 * Constructor, sets the DimensionalityReduction and the default settings
	 * 
	 * @param optimization
	 */
	public WeightedInnerProductObjectiveFunction(int versionWIPOF, double k) {
		this.versionWIPOF = versionWIPOF;
		this.k = k;
	}

	

	/**
	 * sets the distance matrix
	 * 
	 * @param distances
	 *            distance matrix of the high-dimensional space
	 */
	public void setDistances(double[][] distances) {
		distanceMap = distances;
	}

	/**
	 * sets the actual k-factor
	 * 
	 * @param k
	 *            actual k-factor
	 */
	public void setK(double k) {
		this.k = k;
	}

	/**
	 * 
	 * @return the actual k-factor
	 */
	public double getK() {
		return this.k;
	}

	/**
	 * sets the version of WIPOF that is used
	 * 
	 * @param i
	 *            version of WIPOF
	 */
	public void setVersionWIPOF(int i) {
		this.versionWIPOF = i;
	}

	/**
	 * return the version of the WeightedInnerProductObjectiveFunction
	 * @return the version of the WeightedInnerProductObjectiveFunction
	 */
	public int getVersionWIPOF() {
		return this.versionWIPOF;
	}

	@Override
	public void setNumberOfProcessingBlocks(int number) {

	}

	@Override
	public int getNumberOfProcessingBlocks() {
		return 1;
	}

	/**
	 * Weighted Inner Product Objective Function version 2: \sum_p \sum_{q>p}
	 * \left(\frac{2 \cdot \left\langle \vec{x}_p, \vec{x}_q \right\rangle +
	 * d_{pq}^2}{d_{pq} + k}\right)^2 version 3: \sum_p \sum_{q>p} \frac{(2
	 * \cdot \left\langle \vec{x}_p, \vec{x}_q \right\rangle +
	 * d_{pq}^2)^2}{d_{pq} + k}
	 * @param x array of all point coordinates
	 * @param block not needed here
	 */
	public double evaluate(double[] x, int block) {

		if (!DimensionalityReduction.runConvexityTest) {
			updatePoints(x, dimRed);
		}

		double value = 0;
		for (int p = 0; p < distanceMap.length; p++) {
			for (int q = p + 1; q < distanceMap[0].length; q++) {
				double distance = HelperClass.innerProduct2D(x, p, q);

				// works only with random initialization
				if (versionWIPOF == 2) {

					value += Math.pow(
							(2 * (distance) + (distanceMap[p][q] * distanceMap[p][q])) / (k + distanceMap[p][q]), 2);

				} else if (versionWIPOF == 3) {

					value += Math.pow((2 * (distance) + (distanceMap[p][q] * distanceMap[p][q])), 2)
							/ (k + distanceMap[p][q]);

				}

			}
		}
		return Math.pow(value, 1);
	}

	@Override
	public double[] gradient(double[] x, int block) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * function to set the DimensionalityReduction
	 * 
	 * @param dimRed
	 */
	public void setDimensionalityReduction(DimensionalityReduction dimRed) {
		this.dimRed = dimRed;

	}
	
	/**
	 * sets the DimensionalityReduction
	 * @param dimRed DimensionalityReduction to set
	 */
	public void setOptimization(DimensionalityReduction dimRed) {
		this.dimRed = dimRed;
	}

}

--------------------

package cc.mallet.cluster.iterator;


import cc.mallet.cluster.Clustering;
import cc.mallet.cluster.neighbor_evaluator.AgglomerativeNeighbor;
import cc.mallet.cluster.util.ClusterUtils;
import cc.mallet.types.Instance;
import cc.mallet.util.Randoms;

/**
 * Samples merges of a singleton cluster with another (possibly
 * non-singleton) cluster.
 *
 * @author "Aron Culotta" <culotta@degas.cs.umass.edu>
 * @version 1.0
 * @since 1.0
 * @see PairSampleIterator, NeighborIterator
 */
public class NodeClusterSampleIterator extends ClusterSampleIterator {
	
	/**
	 *
	 * @param clustering True clustering.
	 * @param random Source of randomness.
	 * @param positiveProportion Proportion of Instances that should be positive examples.
	 * @param numberSamples Total number of samples to generate.
	 * @return
	 */
	public NodeClusterSampleIterator (Clustering clustering,
																		Randoms random,
																		double positiveProportion,
																		int numberSamples) {
		super(clustering, random, positiveProportion, numberSamples);
		this.random=random;
		this.positiveProportion=positiveProportion;
		this.numberSamples=numberSamples;
	}
	
	public Instance next () {
		AgglomerativeNeighbor neighbor = null;
		
		if (positiveCount < positiveTarget && nonsingletonClusters.length>0){ // Sample positive.
			positiveCount++;
			int label = nonsingletonClusters[random.nextInt(nonsingletonClusters.length)];
			
			int[] instances = clustering.getIndicesWithLabel(label);
			int[] subcluster = sampleFromArray(instances, random, 2);
			int[] cluster1 = new int[]{subcluster[random.nextInt(subcluster.length)]}; // Singleton.
			int[] cluster2 = new int[subcluster.length - 1]; 
			int nadded = 0;
			for (int i = 0; i < subcluster.length; i++)
				if (subcluster[i] != cluster1[0])
					cluster2[nadded++] = subcluster[i];
			
			neighbor = new AgglomerativeNeighbor(clustering,
																					 clustering,
																					 cluster1,
																					 cluster2);			
		} else { // Sample negative.
			int labeli = random.nextInt(clustering.getNumClusters());
			int labelj = random.nextInt(clustering.getNumClusters());
			while (labeli == labelj)
				labelj = random.nextInt(clustering.getNumClusters());

			int[] ii = sampleFromArray(clustering.getIndicesWithLabel(labeli), random, 1);
			int[] ij = sampleFromArray(clustering.getIndicesWithLabel(labelj), random, 1);

			neighbor =
				new AgglomerativeNeighbor(clustering,
																	ClusterUtils.copyAndMergeClusters(clustering,
																																		labeli,
																																		labelj),
																	ii,
																	new int[]{ij[random.nextInt(ij.length)]});						
		}
		totalCount++;
		return new Instance(neighbor, null, null, null);
	}
}

--------------------

