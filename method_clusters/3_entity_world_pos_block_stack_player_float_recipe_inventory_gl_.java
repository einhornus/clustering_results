protected boolean handleGrindingAttribute(GrindingAttributeDefenition grindingAttribute, List<ItemStack> droppableItems, BlockState state, Block block, BlockPos pos, ServerPlayer player,
			BlockEntity tileEntity, ItemStack heldItem, int experience, boolean isCreative) {

		// Allocate a flag to check if anything was ground.
		boolean wasAnythingGround = false;

		// Capture the ground items.
		List<ItemStack> groundItems = new ArrayList<ItemStack>();

		// Get the grinding attribute and check if its enabled.
		if (grindingAttribute.getValue()) {
			// Iterate through all the items that were going to be dropped.
			for (int i = droppableItems.size() - 1; i >= 0; i--) {
				// Get the droppable stack and get the grinding recipe for it if it exists.
				ItemStack droppableStack = droppableItems.get(i);
				RecipeMatchParameters matchParameters = new RecipeMatchParameters(droppableStack);
				Optional<GrinderRecipe> recipe = StaticPowerRecipeRegistry.getRecipe(GrinderRecipe.RECIPE_TYPE, matchParameters);

				// If the recipe is present, create the ground droppables.
				if (recipe.isPresent()) {
					boolean didGrindingSucceed = false;
					for (ProbabilityItemStackOutput output : recipe.get().getOutputItems()) {
						if (SDMath.diceRoll(output.getOutputChance())) {
							groundItems.add(output.getItem());
							didGrindingSucceed = true;
						}
					}

					// If the grinding of this particular item succeeded, remove it from the
					// droppable list.
					if (didGrindingSucceed) {
						droppableItems.remove(i);
					}
					wasAnythingGround = true;
				}
			}
		}

		// Add the ground items to the output.
		droppableItems.addAll(groundItems);

		return wasAnythingGround;
	}
--------------------

protected void onImpact(MovingObjectPosition hitPos)
    {
        if (isBurning() && hitPos.entityHit != null)
        {
        	hitPos.entityHit.setFire(1);
        }
    }
--------------------

@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase user) {
		if(user instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) user;

			List<EntityLivingBase> living = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.posX, player.posY, player.posZ, player.posX, player.posY, player.posZ).grow(5, 5, 5));
			living.remove(player);

			boolean attacked = false;
			for (EntityLivingBase entity : living) {
				if (entity.isEntityAlive() && !(entity instanceof IBLBoss) && entity instanceof EntityPlayer == false) {
					DamageSource source = new EntityDamageSource("magic", user).setDamageBypassesArmor().setMagicDamage();
					if (!world.isRemote) {
						attacked |= entity.attackEntityFrom(source, 20);
					} else if (!entity.isEntityInvulnerable(source)) {
						attacked = true;
						for (int i = 0; i < 20; i++) {
							BLParticles.SWAMP_SMOKE.spawn(world, entity.posX, entity.posY + entity.height / 2.0D, entity.posZ, ParticleFactory.ParticleArgs.get().withMotion((world.rand.nextFloat() - 0.5F) * 0.5F, (world.rand.nextFloat() - 0.5F) * 0.5F, (world.rand.nextFloat() - 0.5F) * 0.5F).withColor(1, 1, 1, 1));
						}
					}
				}
			}

			if (!world.isRemote && attacked) {
				stack.damageItem(1, player);
				world.playSound(null, player.posX, player.posY, player.posZ, SoundRegistry.VOODOO_DOLL, SoundCategory.PLAYERS, 0.5F, 1.0F - world.rand.nextFloat() * 0.3F);
			}
		}

		return stack;
	}
--------------------

