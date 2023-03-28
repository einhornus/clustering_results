@Test
  public void getRelatedNoResult() throws Exception {
    PushOneCommit push = pushFactory.create(db, admin.getIdent());
    PatchSet.Id ps = push.to(git, "refs/for/master").getPatchSetId();
    List<ChangeAndCommit> related = getRelated(ps);
    assertThat(related).isEmpty();
  }
--------------------

@Override
  public ChangeApi rebase(RebaseInput in) throws RestApiException {
    try {
      return changes.id(rebase.apply(revision, in)._number);
    } catch (OrmException | EmailException e) {
      throw new RestApiException("Cannot rebase ps", e);
    }
  }
--------------------

@Test
  public void retrieveEdit() throws Exception {
    RestResponse r = adminSession.get(urlEdit());
    assertThat(r.getStatusCode()).isEqualTo(SC_NO_CONTENT);
    assertThat(modifier.createEdit(change, ps)).isEqualTo(RefUpdate.Result.NEW);
    Optional<ChangeEdit> edit = editUtil.byChange(change);
    assertThat(modifier.modifyFile(edit.get(), FILE_NAME, RestSession.newRawInput(CONTENT_NEW)))
        .isEqualTo(RefUpdate.Result.FORCED);
    edit = editUtil.byChange(change);
    EditInfo info = toEditInfo(false);
    assertThat(info.commit.commit).isEqualTo(edit.get().getRevision().get());
    assertThat(info.commit.parents).hasSize(1);

    edit = editUtil.byChange(change);
    editUtil.delete(edit.get());

    r = adminSession.get(urlEdit());
    assertThat(r.getStatusCode()).isEqualTo(SC_NO_CONTENT);
  }
--------------------

