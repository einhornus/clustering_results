public final JavaliWalker.methodHeading_return methodHeading() throws RecognitionException {
        JavaliWalker.methodHeading_return retval = new JavaliWalker.methodHeading_return();
        retval.start = input.LT(1);


        CommonTree n=null;
        String r =null;


        try {
            // D:\\Data\\Dokumente\\Doktorat\\Teaching\\14fs-advanced-compiler-design\\master-repo\\framework\\javali\\Javali\\src\\cd\\parser\\JavaliWalker.g:133:4: (r= type n= Identifier |r= type n= Identifier formalParamList[$formalParams] )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==Identifier) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==Identifier) ) {
                    int LA6_3 = input.LA(3);

                    if ( (LA6_3==MethodBody) ) {
                        alt6=1;
                    }
                    else if ( (LA6_3==VarDecl) ) {
                        alt6=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 3, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA6_0==ArrayType) ) {
                int LA6_2 = input.LA(2);

                if ( (LA6_2==DOWN) ) {
                    int LA6_4 = input.LA(3);

                    if ( (LA6_4==Identifier) ) {
                        int LA6_7 = input.LA(4);

                        if ( (LA6_7==UP) ) {
                            int LA6_8 = input.LA(5);

                            if ( (LA6_8==Identifier) ) {
                                int LA6_3 = input.LA(6);

                                if ( (LA6_3==MethodBody) ) {
                                    alt6=1;
                                }
                                else if ( (LA6_3==VarDecl) ) {
                                    alt6=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 6, 3, input);

                                    throw nvae;

                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 6, 8, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 7, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 4, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // D:\\Data\\Dokumente\\Doktorat\\Teaching\\14fs-advanced-compiler-design\\master-repo\\framework\\javali\\Javali\\src\\cd\\parser\\JavaliWalker.g:133:6: r= type n= Identifier
                    {
                    pushFollow(FOLLOW_type_in_methodHeading260);
                    r=type();

                    state._fsp--;


                    n=(CommonTree)match(input,Identifier,FOLLOW_Identifier_in_methodHeading264); 

                     retval.returnType = r; retval.mthName = (n!=null?n.getText():null); retval.formalParams = emptyList(); 

                    }
                    break;
                case 2 :
                    // D:\\Data\\Dokumente\\Doktorat\\Teaching\\14fs-advanced-compiler-design\\master-repo\\framework\\javali\\Javali\\src\\cd\\parser\\JavaliWalker.g:134:6: r= type n= Identifier formalParamList[$formalParams]
                    {
                    pushFollow(FOLLOW_type_in_methodHeading275);
                    r=type();

                    state._fsp--;


                    n=(CommonTree)match(input,Identifier,FOLLOW_Identifier_in_methodHeading279); 

                     retval.returnType = r; retval.mthName = (n!=null?n.getText():null); retval.formalParams = new ArrayList<Pair<String>>(); 

                    pushFollow(FOLLOW_formalParamList_in_methodHeading283);
                    formalParamList(retval.formalParams);

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
--------------------

public final JavaliParser.compOp_return compOp() throws RecognitionException {
		JavaliParser.compOp_return retval = new JavaliParser.compOp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token op=null;

		Object op_tree=null;
		RewriteRuleTokenStream stream_79=new RewriteRuleTokenStream(adaptor,"token 79");
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
		RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");

		try {
			// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:436:2: (op= '==' -> ^( B_EQUAL[$op, \"B_EQUAL\"] ) |op= '!=' -> ^( B_NOT_EQUAL[$op, \"B_NOT_EQUAL\"] ) |op= '<' -> ^( B_LESS_THAN[$op, \"B_LESS_THAN\"] ) |op= '<=' -> ^( B_LESS_OR_EQUAL[$op, \"B_LESS_OR_EQUAL\"] ) |op= '>' -> ^( B_GREATER_THAN[$op, \"B_GREATER_THAN\"] ) |op= '>=' -> ^( B_GREATER_OR_EQUAL[$op, \"B_GREATER_OR_EQUAL\"] ) )
			int alt26=6;
			switch ( input.LA(1) ) {
			case 81:
				{
				alt26=1;
				}
				break;
			case 66:
				{
				alt26=2;
				}
				break;
			case 78:
				{
				alt26=3;
				}
				break;
			case 79:
				{
				alt26=4;
				}
				break;
			case 82:
				{
				alt26=5;
				}
				break;
			case 83:
				{
				alt26=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 26, 0, input);
				throw nvae;
			}
			switch (alt26) {
				case 1 :
					// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:436:4: op= '=='
					{
					op=(Token)match(input,81,FOLLOW_81_in_compOp1593);  
					stream_81.add(op);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 437:3: -> ^( B_EQUAL[$op, \"B_EQUAL\"] )
					{
						// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:437:6: ^( B_EQUAL[$op, \"B_EQUAL\"] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(B_EQUAL, op, "B_EQUAL"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:438:4: op= '!='
					{
					op=(Token)match(input,66,FOLLOW_66_in_compOp1611);  
					stream_66.add(op);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 439:3: -> ^( B_NOT_EQUAL[$op, \"B_NOT_EQUAL\"] )
					{
						// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:439:6: ^( B_NOT_EQUAL[$op, \"B_NOT_EQUAL\"] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(B_NOT_EQUAL, op, "B_NOT_EQUAL"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:440:4: op= '<'
					{
					op=(Token)match(input,78,FOLLOW_78_in_compOp1629);  
					stream_78.add(op);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 441:3: -> ^( B_LESS_THAN[$op, \"B_LESS_THAN\"] )
					{
						// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:441:6: ^( B_LESS_THAN[$op, \"B_LESS_THAN\"] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(B_LESS_THAN, op, "B_LESS_THAN"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:442:4: op= '<='
					{
					op=(Token)match(input,79,FOLLOW_79_in_compOp1647);  
					stream_79.add(op);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 443:3: -> ^( B_LESS_OR_EQUAL[$op, \"B_LESS_OR_EQUAL\"] )
					{
						// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:443:6: ^( B_LESS_OR_EQUAL[$op, \"B_LESS_OR_EQUAL\"] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(B_LESS_OR_EQUAL, op, "B_LESS_OR_EQUAL"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:444:4: op= '>'
					{
					op=(Token)match(input,82,FOLLOW_82_in_compOp1665);  
					stream_82.add(op);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 445:3: -> ^( B_GREATER_THAN[$op, \"B_GREATER_THAN\"] )
					{
						// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:445:6: ^( B_GREATER_THAN[$op, \"B_GREATER_THAN\"] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(B_GREATER_THAN, op, "B_GREATER_THAN"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:446:4: op= '>='
					{
					op=(Token)match(input,83,FOLLOW_83_in_compOp1683);  
					stream_83.add(op);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 447:3: -> ^( B_GREATER_OR_EQUAL[$op, \"B_GREATER_OR_EQUAL\"] )
					{
						// /home/luca/svn-repos/cd_students/2015ss/master/CD2_A1/src/cd/parser/Javali.g:447:6: ^( B_GREATER_OR_EQUAL[$op, \"B_GREATER_OR_EQUAL\"] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(B_GREATER_OR_EQUAL, op, "B_GREATER_OR_EQUAL"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		catch (RecognitionException re) {
			reportError(re);
			throw re;
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
--------------------

static void f9100() {
        char[] cs = new char[] {
            '\u9100','\u9101','\u9102','\u9103','\u9104','\u9105','\u9106',
            '\u9107','\u9108','\u9109','\u910a','\u910b','\u910c','\u910d',
            '\u910e','\u910f','\u9110','\u9111','\u9112','\u9113','\u9114',
            '\u9115','\u9116','\u9117','\u9118','\u9119','\u911a','\u911b',
            '\u911c','\u911d','\u911e','\u911f','\u9120','\u9121','\u9122',
            '\u9123','\u9124','\u9125','\u9126','\u9127','\u9128','\u9129',
            '\u912a','\u912b','\u912c','\u912d','\u912e','\u912f','\u9130',
            '\u9131','\u9132','\u9133','\u9134','\u9135','\u9136','\u9137',
            '\u9138','\u9139','\u913a','\u913b','\u913c','\u913d','\u913e',
            '\u913f','\u9140','\u9141','\u9142','\u9143','\u9144','\u9145',
            '\u9146','\u9147','\u9148','\u9149','\u914a','\u914b','\u914c',
            '\u914d','\u914e','\u914f','\u9150','\u9151','\u9152','\u9153',
            '\u9154','\u9155','\u9156','\u9157','\u9158','\u9159','\u915a',
            '\u915b','\u915c','\u915d','\u915e','\u915f','\u9160','\u9161',
            '\u9162','\u9163','\u9164','\u9165','\u9166','\u9167','\u9168',
            '\u9169','\u916a','\u916b','\u916c','\u916d','\u916e','\u916f',
            '\u9170','\u9171','\u9172','\u9173','\u9174','\u9175','\u9176',
            '\u9177','\u9178','\u9179','\u917a','\u917b','\u917c','\u917d',
            '\u917e','\u917f','\u9180','\u9181','\u9182','\u9183','\u9184',
            '\u9185','\u9186','\u9187','\u9188','\u9189','\u918a','\u918b',
            '\u918c','\u918d','\u918e','\u918f','\u9190','\u9191','\u9192',
            '\u9193','\u9194','\u9195','\u9196','\u9197','\u9198','\u9199',
            '\u919a','\u919b','\u919c','\u919d','\u919e','\u919f','\u91a0',
            '\u91a1','\u91a2','\u91a3','\u91a4','\u91a5','\u91a6','\u91a7',
            '\u91a8','\u91a9','\u91aa','\u91ab','\u91ac','\u91ad','\u91ae',
            '\u91af','\u91b0','\u91b1','\u91b2','\u91b3','\u91b4','\u91b5',
            '\u91b6','\u91b7','\u91b8','\u91b9','\u91ba','\u91bb','\u91bc',
            '\u91bd','\u91be','\u91bf','\u91c0','\u91c1','\u91c2','\u91c3',
            '\u91c4','\u91c5','\u91c6','\u91c7','\u91c8','\u91c9','\u91ca',
            '\u91cb','\u91cc','\u91cd','\u91ce','\u91cf','\u91d0','\u91d1',
            '\u91d2','\u91d3','\u91d4','\u91d5','\u91d6','\u91d7','\u91d8',
            '\u91d9','\u91da','\u91db','\u91dc','\u91dd','\u91de','\u91df',
            '\u91e0','\u91e1','\u91e2','\u91e3','\u91e4','\u91e5','\u91e6',
            '\u91e7','\u91e8','\u91e9','\u91ea','\u91eb','\u91ec','\u91ed',
            '\u91ee','\u91ef','\u91f0','\u91f1','\u91f2','\u91f3','\u91f4',
            '\u91f5','\u91f6','\u91f7','\u91f8','\u91f9','\u91fa','\u91fb',
            };
        for (int i = 0; i < cs.length; i++) {
            int want = i+37120;
            int have = (int)cs[i];
            Tester.check(want==have, have + "!='\\u"+Integer.toHexString(want)+"'");
        }
    }
--------------------

