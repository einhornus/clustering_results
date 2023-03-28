public StereoMolecule[] getFragments(int[] fragmentNo, int fragmentCount) {
        StereoMolecule[] fragment = new StereoMolecule[fragmentCount];
        int[] atoms = new int[fragmentCount];
        int[] bonds = new int[fragmentCount];
        int[] atomMap = new int[mAllAtoms];
        for (int atom=0; atom<mAllAtoms; atom++)
        	if (fragmentNo[atom] != -1)
        		atomMap[atom] = atoms[fragmentNo[atom]]++;
        for (int bond=0; bond<mAllBonds; bond++) {
        	int f1 = fragmentNo[mBondAtom[0][bond]];
        	int f2 = fragmentNo[mBondAtom[1][bond]];
        	if (f1 == f2 && f1 != -1)
        		bonds[f1]++;
        	}
        for (int i=0; i<fragmentCount; i++) {
            fragment[i] = createMolecule(atoms[i], bonds[i]);
            copyMoleculeProperties(fragment[i]);
            }
        for (int atom=0; atom<mAllAtoms; atom++)
        	if (fragmentNo[atom] != -1)
        		copyAtom(fragment[fragmentNo[atom]], atom, 0, 0);
        for (int bond=0; bond<mAllBonds; bond++) {
        	int f1 = fragmentNo[mBondAtom[0][bond]];
        	int f2 = fragmentNo[mBondAtom[1][bond]];
        	if (f1 == f2 && f1 != -1)
	            copyBond(fragment[f1], bond, 0, 0, atomMap, false);
            }
        for (StereoMolecule f:fragment) {
        	f.renumberESRGroups(cESRTypeAnd);
        	f.renumberESRGroups(cESRTypeOr);
        	}

        return fragment;
        }
--------------------

private void handleHydrogens() {
		// find all hydrogens that are connected to a non-H atom and therefore can be implicit		
		boolean[] isHydrogen = findSimpleHydrogens();

					// move all hydrogen atoms to end of atom table
		int lastNonHAtom = mAllAtoms;
		do lastNonHAtom--;
			while ((lastNonHAtom >= 0) && isHydrogen[lastNonHAtom]);

		for (int atom=0; atom<lastNonHAtom; atom++) {
			if (isHydrogen[atom]) {
				swapAtoms(atom, lastNonHAtom);

				// swap simple H flags also
				boolean temp = isHydrogen[atom];
				isHydrogen[atom] = isHydrogen[lastNonHAtom];
				isHydrogen[lastNonHAtom] = temp;

				do lastNonHAtom--;
					while (isHydrogen[lastNonHAtom]);
				}
			}
		mAtoms = lastNonHAtom + 1;

					// move all bonds to hydrogen to end of bond table
		if (mAllAtoms == mAtoms) {
			mBonds = mAllBonds;
			return;
			}

		boolean isHydrogenBond[] = new boolean[mAllBonds];
		for (int bond=0; bond<mAllBonds; bond++) {	// mark all bonds to hydrogen
			int atom1 = mBondAtom[0][bond];
			int atom2 = mBondAtom[1][bond];
			if (isHydrogen[atom1]
			 || isHydrogen[atom2])
				isHydrogenBond[bond] = true;
			}

		int lastNonHBond = mAllBonds;
		do lastNonHBond--; while ((lastNonHBond >= 0) && isHydrogenBond[lastNonHBond]);

		for (int bond=0; bond<lastNonHBond; bond++) {
			if (isHydrogenBond[bond]) {
				int tempInt = mBondAtom[0][bond];
				mBondAtom[0][bond] = mBondAtom[0][lastNonHBond];
				mBondAtom[0][lastNonHBond] = tempInt;
				tempInt = mBondAtom[1][bond];
				mBondAtom[1][bond] = mBondAtom[1][lastNonHBond];
				mBondAtom[1][lastNonHBond] = tempInt;
				tempInt = mBondType[bond];
				mBondType[bond] = mBondType[lastNonHBond];
				mBondType[lastNonHBond] = tempInt;
				isHydrogenBond[bond] = false;
				do lastNonHBond--;
					while (isHydrogenBond[lastNonHBond]);
				}
			}
		mBonds = lastNonHBond + 1;
		}
--------------------

private int removeAcidicHydrogens(StereoMolecule mol, int maxHydrogens) {
		if (maxHydrogens > 0)	// HF
			maxHydrogens = removeHydrogensFromHalogene(mol, maxHydrogens, 9);
		if (maxHydrogens > 0)	// HCl
			maxHydrogens = removeHydrogensFromHalogene(mol, maxHydrogens, 17);
		if (maxHydrogens > 0)	// HBr
			maxHydrogens = removeHydrogensFromHalogene(mol, maxHydrogens, 35);
		if (maxHydrogens > 0)	// HI
			maxHydrogens = removeHydrogensFromHalogene(mol, maxHydrogens, 53);

		if (maxHydrogens > 0) {	// first handle all acidic oxygens vicinal to positively charged atoms
			for (int atom = 0; atom < mol.getAtoms(); atom++) {
				if (mol.getAtomCharge(atom) > 0) {
					boolean found = false;
					for (int i=0; i<mol.getConnAtoms(atom); i++) {
						int connAtom = mol.getConnAtom(atom, i);
						if (mol.getAtomCharge(connAtom) == 0
						 && mol.isElectronegative(connAtom)
						 && mol.getImplicitHydrogens(connAtom) > 0) {
							mol.setAtomCharge(connAtom, -1);
							maxHydrogens--;
							if (maxHydrogens == 0)
								return 0;
							found = true;
							break;
						}
					}
					if (found)
						continue;
				}
			}
		}

		if (maxHydrogens > 0)	// sulfonic & sulfinic acid
			maxHydrogens = removeAcidicHydrogensFromAcid(mol, maxHydrogens, 8, 16);
		if (maxHydrogens > 0)	// sulfonic & sulfinic acid
			maxHydrogens = removeAcidicHydrogensFromAcid(mol, maxHydrogens, 8, 15);
		if (maxHydrogens > 0)	// carboxylic acid
			maxHydrogens = removeAcidicHydrogensFromAcid(mol, maxHydrogens, 8, 6);
		if (maxHydrogens > 0)	// sulfonamids
			maxHydrogens = removeAcidicHydrogensFromAcid(mol, maxHydrogens, 7, 16);

		return maxHydrogens;
	}
--------------------

