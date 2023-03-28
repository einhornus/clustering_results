@Override
	public void deleteNote(
		final SessionIdentifier sessionIdentifier,
		final NoteIdentifier noteIdentifier
	) throws PermissionDeniedException, LoginRequiredException,
		NoteServiceException {
		try {
			authorizationService.existsPermission(authorizationService.createPermissionIdentifier(PERMISSION));
			logger.debug("deleteNote");
			final NoteBean note = noteDao.load(noteIdentifier);
			authorizationService.expectUser(sessionIdentifier, note.getOwner());
			noteDao.delete(note);
		} catch (final AuthorizationServiceException e) {
			throw new NoteServiceException(e);
		} catch (StorageException e) {
			throw new NoteServiceException(e);
		}
	}
--------------------

@Override
	public boolean hasOneOfPermissions(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier... permissionIdentifiers
	) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return true;
			}
			for (final PermissionIdentifier permissionIdentifier : permissionIdentifiers) {
				if (hasPermission(sessionIdentifier, permissionIdentifier)) {
					return true;
				}
			}
			return false;
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
--------------------

@Override
	public void updateNote(
		final SessionIdentifier sessionIdentifier,
		final NoteDto noteDto
	) throws PermissionDeniedException, LoginRequiredException, NoteServiceException,
		ValidationException {
		try {
			authorizationService.existsPermission(authorizationService.createPermissionIdentifier(PERMISSION));
			logger.debug("updateNote");

			final NoteBean bean = noteDao.load(noteDto.getId());
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());
			bean.setTitle(noteDto.getTitle());
			bean.setContent(noteDto.getContent());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			noteDao.save(bean);
		} catch (final AuthorizationServiceException e) {
			throw new NoteServiceException(e);
		} catch (StorageException e) {
			throw new NoteServiceException(e);
		}
	}
--------------------

