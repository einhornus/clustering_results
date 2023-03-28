public void restore(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CondicaoOperacaoDTO condicao = (CondicaoOperacaoDTO) document.getBean();
		CondicaoOperacaoService condicaoService = (CondicaoOperacaoService) ServiceLocator.getInstance().getService(CondicaoOperacaoService.class, null);

		condicao = (CondicaoOperacaoDTO) condicaoService.restore(condicao);

		HTMLForm form = document.getForm("form");
		form.clear();
		form.setValues(condicao);
	}
--------------------

public static boolean actualizar (Articulos art) throws SQLException, NamingException {
        try {
            conexion = conectarbd();
            PreparedStatement ps = conexion.prepareStatement("UPDATE Articulos SET Titulo = ?, Contenido = ? "+ " WHERE Id = ?");
            ps.setString(1, art.getTitulo());
            ps.setString(2, art.getContenido());
            ps.setInt(3, art.getId());
            ps.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
	}   
        return true;
    }
--------------------

private String getUnidade(Integer id) throws ServiceException, Exception {

		UnidadeService unidadeService = (UnidadeService) ServiceLocator.getInstance().getService(UnidadeService.class, null);
		
		UnidadeDTO unidadeDto = new UnidadeDTO();
		unidadeDto.setIdUnidade(id);
		unidadeDto = (UnidadeDTO) unidadeService.restore(unidadeDto);

		if (unidadeDto != null) {
			return unidadeDto.getNome();
		}

		return null;
	}
--------------------

