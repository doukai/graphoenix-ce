// Generated from /Users/doukai/Work/graphoenix-ce/graphoenix-spi/src/main/antlr/GraphqlOperation.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class GraphqlOperationParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, BooleanValue=13, NullValue=14, FRAGMENT=15, 
		QUERY=16, MUTATION=17, SUBSCRIPTION=18, SCHEMA=19, SCALAR=20, TYPE=21, 
		INTERFACE=22, IMPLEMENTS=23, ENUM=24, UNION=25, INPUT=26, EXTEND=27, DIRECTIVE=28, 
		ON_KEYWORD=29, REPEATABLE=30, NAME=31, IntValue=32, FloatValue=33, StringValue=34, 
		Comment=35, LF=36, CR=37, LineTerminator=38, Space=39, Tab=40, Comma=41, 
		UnicodeBOM=42;
	public static final int
		RULE_operationDefinition = 0, RULE_variableDefinitions = 1, RULE_variableDefinition = 2, 
		RULE_selectionSet = 3, RULE_selection = 4, RULE_field = 5, RULE_alias = 6, 
		RULE_fragmentSpread = 7, RULE_inlineFragment = 8, RULE_fragmentDefinition = 9, 
		RULE_typeCondition = 10, RULE_operationType = 11, RULE_description = 12, 
		RULE_enumValue = 13, RULE_arrayValue = 14, RULE_arrayValueWithVariable = 15, 
		RULE_objectValue = 16, RULE_objectValueWithVariable = 17, RULE_objectField = 18, 
		RULE_objectFieldWithVariable = 19, RULE_directives = 20, RULE_directive = 21, 
		RULE_arguments = 22, RULE_argument = 23, RULE_baseName = 24, RULE_fragmentName = 25, 
		RULE_enumValueName = 26, RULE_name = 27, RULE_value = 28, RULE_valueWithVariable = 29, 
		RULE_variable = 30, RULE_defaultValue = 31, RULE_type = 32, RULE_typeName = 33, 
		RULE_listType = 34, RULE_nonNullType = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"operationDefinition", "variableDefinitions", "variableDefinition", "selectionSet", 
			"selection", "field", "alias", "fragmentSpread", "inlineFragment", "fragmentDefinition", 
			"typeCondition", "operationType", "description", "enumValue", "arrayValue", 
			"arrayValueWithVariable", "objectValue", "objectValueWithVariable", "objectField", 
			"objectFieldWithVariable", "directives", "directive", "arguments", "argument", 
			"baseName", "fragmentName", "enumValueName", "name", "value", "valueWithVariable", 
			"variable", "defaultValue", "type", "typeName", "listType", "nonNullType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "':'", "'{'", "'}'", "'...'", "'['", "']'", "'@'", 
			"'$'", "'='", "'!'", null, "'null'", "'fragment'", "'query'", "'mutation'", 
			"'subscription'", "'schema'", "'scalar'", "'type'", "'interface'", "'implements'", 
			"'enum'", "'union'", "'input'", "'extend'", "'directive'", "'on'", "'repeatable'", 
			null, null, null, null, null, null, null, null, null, null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "BooleanValue", "NullValue", "FRAGMENT", "QUERY", "MUTATION", "SUBSCRIPTION", 
			"SCHEMA", "SCALAR", "TYPE", "INTERFACE", "IMPLEMENTS", "ENUM", "UNION", 
			"INPUT", "EXTEND", "DIRECTIVE", "ON_KEYWORD", "REPEATABLE", "NAME", "IntValue", 
			"FloatValue", "StringValue", "Comment", "LF", "CR", "LineTerminator", 
			"Space", "Tab", "Comma", "UnicodeBOM"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "GraphqlOperation.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GraphqlOperationParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperationDefinitionContext extends ParserRuleContext {
		public SelectionSetContext selectionSet() {
			return getRuleContext(SelectionSetContext.class,0);
		}
		public OperationTypeContext operationType() {
			return getRuleContext(OperationTypeContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public VariableDefinitionsContext variableDefinitions() {
			return getRuleContext(VariableDefinitionsContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public OperationDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterOperationDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitOperationDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitOperationDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationDefinitionContext operationDefinition() throws RecognitionException {
		OperationDefinitionContext _localctx = new OperationDefinitionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_operationDefinition);
		int _la;
		try {
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(72);
				selectionSet();
				}
				break;
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
				enterOuterAlt(_localctx, 2);
				{
				setState(73);
				operationType();
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4294959104L) != 0)) {
					{
					setState(74);
					name();
					}
				}

				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(77);
					variableDefinitions();
					}
				}

				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(80);
					directives();
					}
				}

				setState(83);
				selectionSet();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDefinitionsContext extends ParserRuleContext {
		public List<VariableDefinitionContext> variableDefinition() {
			return getRuleContexts(VariableDefinitionContext.class);
		}
		public VariableDefinitionContext variableDefinition(int i) {
			return getRuleContext(VariableDefinitionContext.class,i);
		}
		public VariableDefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDefinitions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterVariableDefinitions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitVariableDefinitions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitVariableDefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDefinitionsContext variableDefinitions() throws RecognitionException {
		VariableDefinitionsContext _localctx = new VariableDefinitionsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_variableDefinitions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(T__0);
			setState(89); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(88);
				variableDefinition();
				}
				}
				setState(91); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__9 );
			setState(93);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDefinitionContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public DefaultValueContext defaultValue() {
			return getRuleContext(DefaultValueContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public VariableDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterVariableDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitVariableDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitVariableDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDefinitionContext variableDefinition() throws RecognitionException {
		VariableDefinitionContext _localctx = new VariableDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variableDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			variable();
			setState(96);
			match(T__2);
			setState(97);
			type();
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(98);
				defaultValue();
				}
			}

			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(101);
				directives();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectionSetContext extends ParserRuleContext {
		public List<SelectionContext> selection() {
			return getRuleContexts(SelectionContext.class);
		}
		public SelectionContext selection(int i) {
			return getRuleContext(SelectionContext.class,i);
		}
		public SelectionSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterSelectionSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitSelectionSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitSelectionSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionSetContext selectionSet() throws RecognitionException {
		SelectionSetContext _localctx = new SelectionSetContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_selectionSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(T__3);
			setState(106); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(105);
				selection();
				}
				}
				setState(108); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294959168L) != 0) );
			setState(110);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectionContext extends ParserRuleContext {
		public FieldContext field() {
			return getRuleContext(FieldContext.class,0);
		}
		public FragmentSpreadContext fragmentSpread() {
			return getRuleContext(FragmentSpreadContext.class,0);
		}
		public InlineFragmentContext inlineFragment() {
			return getRuleContext(InlineFragmentContext.class,0);
		}
		public SelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterSelection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitSelection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitSelection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionContext selection() throws RecognitionException {
		SelectionContext _localctx = new SelectionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_selection);
		try {
			setState(115);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				field();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				fragmentSpread();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				inlineFragment();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public SelectionSetContext selectionSet() {
			return getRuleContext(SelectionSetContext.class,0);
		}
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(117);
				alias();
				}
				break;
			}
			setState(120);
			name();
			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(121);
				arguments();
				}
			}

			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(124);
				directives();
				}
			}

			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(127);
				selectionSet();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AliasContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			name();
			setState(131);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FragmentSpreadContext extends ParserRuleContext {
		public FragmentNameContext fragmentName() {
			return getRuleContext(FragmentNameContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public FragmentSpreadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fragmentSpread; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterFragmentSpread(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitFragmentSpread(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitFragmentSpread(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FragmentSpreadContext fragmentSpread() throws RecognitionException {
		FragmentSpreadContext _localctx = new FragmentSpreadContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_fragmentSpread);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__5);
			setState(134);
			fragmentName();
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(135);
				directives();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InlineFragmentContext extends ParserRuleContext {
		public SelectionSetContext selectionSet() {
			return getRuleContext(SelectionSetContext.class,0);
		}
		public TypeConditionContext typeCondition() {
			return getRuleContext(TypeConditionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public InlineFragmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineFragment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterInlineFragment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitInlineFragment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitInlineFragment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineFragmentContext inlineFragment() throws RecognitionException {
		InlineFragmentContext _localctx = new InlineFragmentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_inlineFragment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(T__5);
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON_KEYWORD) {
				{
				setState(139);
				typeCondition();
				}
			}

			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(142);
				directives();
				}
			}

			setState(145);
			selectionSet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FragmentDefinitionContext extends ParserRuleContext {
		public TerminalNode FRAGMENT() { return getToken(GraphqlOperationParser.FRAGMENT, 0); }
		public FragmentNameContext fragmentName() {
			return getRuleContext(FragmentNameContext.class,0);
		}
		public TypeConditionContext typeCondition() {
			return getRuleContext(TypeConditionContext.class,0);
		}
		public SelectionSetContext selectionSet() {
			return getRuleContext(SelectionSetContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public FragmentDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fragmentDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterFragmentDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitFragmentDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitFragmentDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FragmentDefinitionContext fragmentDefinition() throws RecognitionException {
		FragmentDefinitionContext _localctx = new FragmentDefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_fragmentDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(FRAGMENT);
			setState(148);
			fragmentName();
			setState(149);
			typeCondition();
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(150);
				directives();
				}
			}

			setState(153);
			selectionSet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeConditionContext extends ParserRuleContext {
		public TerminalNode ON_KEYWORD() { return getToken(GraphqlOperationParser.ON_KEYWORD, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TypeConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterTypeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitTypeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitTypeCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeConditionContext typeCondition() throws RecognitionException {
		TypeConditionContext _localctx = new TypeConditionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_typeCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(ON_KEYWORD);
			setState(156);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperationTypeContext extends ParserRuleContext {
		public TerminalNode SUBSCRIPTION() { return getToken(GraphqlOperationParser.SUBSCRIPTION, 0); }
		public TerminalNode MUTATION() { return getToken(GraphqlOperationParser.MUTATION, 0); }
		public TerminalNode QUERY() { return getToken(GraphqlOperationParser.QUERY, 0); }
		public OperationTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterOperationType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitOperationType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitOperationType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationTypeContext operationType() throws RecognitionException {
		OperationTypeContext _localctx = new OperationTypeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_operationType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DescriptionContext extends ParserRuleContext {
		public TerminalNode StringValue() { return getToken(GraphqlOperationParser.StringValue, 0); }
		public DescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_description; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitDescription(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitDescription(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionContext description() throws RecognitionException {
		DescriptionContext _localctx = new DescriptionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			match(StringValue);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumValueContext extends ParserRuleContext {
		public EnumValueNameContext enumValueName() {
			return getRuleContext(EnumValueNameContext.class,0);
		}
		public EnumValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterEnumValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitEnumValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitEnumValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueContext enumValue() throws RecognitionException {
		EnumValueContext _localctx = new EnumValueContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_enumValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			enumValueName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayValueContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ArrayValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterArrayValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitArrayValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitArrayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueContext arrayValue() throws RecognitionException {
		ArrayValueContext _localctx = new ArrayValueContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_arrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(T__6);
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 34359730320L) != 0)) {
				{
				{
				setState(165);
				value();
				}
				}
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(171);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayValueWithVariableContext extends ParserRuleContext {
		public List<ValueWithVariableContext> valueWithVariable() {
			return getRuleContexts(ValueWithVariableContext.class);
		}
		public ValueWithVariableContext valueWithVariable(int i) {
			return getRuleContext(ValueWithVariableContext.class,i);
		}
		public ArrayValueWithVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayValueWithVariable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterArrayValueWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitArrayValueWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitArrayValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueWithVariableContext arrayValueWithVariable() throws RecognitionException {
		ArrayValueWithVariableContext _localctx = new ArrayValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_arrayValueWithVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(T__6);
			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 34359731344L) != 0)) {
				{
				{
				setState(174);
				valueWithVariable();
				}
				}
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(180);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectValueContext extends ParserRuleContext {
		public List<ObjectFieldContext> objectField() {
			return getRuleContexts(ObjectFieldContext.class);
		}
		public ObjectFieldContext objectField(int i) {
			return getRuleContext(ObjectFieldContext.class,i);
		}
		public ObjectValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterObjectValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitObjectValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitObjectValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectValueContext objectValue() throws RecognitionException {
		ObjectValueContext _localctx = new ObjectValueContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_objectValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			match(T__3);
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4294959104L) != 0)) {
				{
				{
				setState(183);
				objectField();
				}
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(189);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectValueWithVariableContext extends ParserRuleContext {
		public List<ObjectFieldWithVariableContext> objectFieldWithVariable() {
			return getRuleContexts(ObjectFieldWithVariableContext.class);
		}
		public ObjectFieldWithVariableContext objectFieldWithVariable(int i) {
			return getRuleContext(ObjectFieldWithVariableContext.class,i);
		}
		public ObjectValueWithVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectValueWithVariable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterObjectValueWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitObjectValueWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitObjectValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectValueWithVariableContext objectValueWithVariable() throws RecognitionException {
		ObjectValueWithVariableContext _localctx = new ObjectValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_objectValueWithVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(T__3);
			setState(195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4294959104L) != 0)) {
				{
				{
				setState(192);
				objectFieldWithVariable();
				}
				}
				setState(197);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(198);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectFieldContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ObjectFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterObjectField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitObjectField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitObjectField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectFieldContext objectField() throws RecognitionException {
		ObjectFieldContext _localctx = new ObjectFieldContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_objectField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			name();
			setState(201);
			match(T__2);
			setState(202);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectFieldWithVariableContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ValueWithVariableContext valueWithVariable() {
			return getRuleContext(ValueWithVariableContext.class,0);
		}
		public ObjectFieldWithVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectFieldWithVariable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterObjectFieldWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitObjectFieldWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitObjectFieldWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectFieldWithVariableContext objectFieldWithVariable() throws RecognitionException {
		ObjectFieldWithVariableContext _localctx = new ObjectFieldWithVariableContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_objectFieldWithVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			name();
			setState(205);
			match(T__2);
			setState(206);
			valueWithVariable();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirectivesContext extends ParserRuleContext {
		public List<DirectiveContext> directive() {
			return getRuleContexts(DirectiveContext.class);
		}
		public DirectiveContext directive(int i) {
			return getRuleContext(DirectiveContext.class,i);
		}
		public DirectivesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directives; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterDirectives(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitDirectives(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitDirectives(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectivesContext directives() throws RecognitionException {
		DirectivesContext _localctx = new DirectivesContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_directives);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(208);
				directive();
				}
				}
				setState(211); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__8 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirectiveContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public DirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveContext directive() throws RecognitionException {
		DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_directive);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(T__8);
			setState(214);
			name();
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(215);
				arguments();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(T__0);
			setState(220); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(219);
				argument();
				}
				}
				setState(222); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294959104L) != 0) );
			setState(224);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ValueWithVariableContext valueWithVariable() {
			return getRuleContext(ValueWithVariableContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			name();
			setState(227);
			match(T__2);
			setState(228);
			valueWithVariable();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BaseNameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(GraphqlOperationParser.NAME, 0); }
		public TerminalNode FRAGMENT() { return getToken(GraphqlOperationParser.FRAGMENT, 0); }
		public TerminalNode QUERY() { return getToken(GraphqlOperationParser.QUERY, 0); }
		public TerminalNode MUTATION() { return getToken(GraphqlOperationParser.MUTATION, 0); }
		public TerminalNode SUBSCRIPTION() { return getToken(GraphqlOperationParser.SUBSCRIPTION, 0); }
		public TerminalNode SCHEMA() { return getToken(GraphqlOperationParser.SCHEMA, 0); }
		public TerminalNode SCALAR() { return getToken(GraphqlOperationParser.SCALAR, 0); }
		public TerminalNode TYPE() { return getToken(GraphqlOperationParser.TYPE, 0); }
		public TerminalNode INTERFACE() { return getToken(GraphqlOperationParser.INTERFACE, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(GraphqlOperationParser.IMPLEMENTS, 0); }
		public TerminalNode ENUM() { return getToken(GraphqlOperationParser.ENUM, 0); }
		public TerminalNode UNION() { return getToken(GraphqlOperationParser.UNION, 0); }
		public TerminalNode INPUT() { return getToken(GraphqlOperationParser.INPUT, 0); }
		public TerminalNode EXTEND() { return getToken(GraphqlOperationParser.EXTEND, 0); }
		public TerminalNode DIRECTIVE() { return getToken(GraphqlOperationParser.DIRECTIVE, 0); }
		public TerminalNode REPEATABLE() { return getToken(GraphqlOperationParser.REPEATABLE, 0); }
		public BaseNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterBaseName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitBaseName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitBaseName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseNameContext baseName() throws RecognitionException {
		BaseNameContext _localctx = new BaseNameContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_baseName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3758063616L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FragmentNameContext extends ParserRuleContext {
		public BaseNameContext baseName() {
			return getRuleContext(BaseNameContext.class,0);
		}
		public TerminalNode BooleanValue() { return getToken(GraphqlOperationParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlOperationParser.NullValue, 0); }
		public FragmentNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fragmentName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterFragmentName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitFragmentName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitFragmentName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FragmentNameContext fragmentName() throws RecognitionException {
		FragmentNameContext _localctx = new FragmentNameContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_fragmentName);
		try {
			setState(235);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(232);
				baseName();
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(233);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(234);
				match(NullValue);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumValueNameContext extends ParserRuleContext {
		public BaseNameContext baseName() {
			return getRuleContext(BaseNameContext.class,0);
		}
		public TerminalNode ON_KEYWORD() { return getToken(GraphqlOperationParser.ON_KEYWORD, 0); }
		public EnumValueNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValueName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterEnumValueName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitEnumValueName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitEnumValueName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueNameContext enumValueName() throws RecognitionException {
		EnumValueNameContext _localctx = new EnumValueNameContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_enumValueName);
		try {
			setState(239);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(237);
				baseName();
				}
				break;
			case ON_KEYWORD:
				enterOuterAlt(_localctx, 2);
				{
				setState(238);
				match(ON_KEYWORD);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NameContext extends ParserRuleContext {
		public BaseNameContext baseName() {
			return getRuleContext(BaseNameContext.class,0);
		}
		public TerminalNode BooleanValue() { return getToken(GraphqlOperationParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlOperationParser.NullValue, 0); }
		public TerminalNode ON_KEYWORD() { return getToken(GraphqlOperationParser.ON_KEYWORD, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_name);
		try {
			setState(245);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(241);
				baseName();
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(242);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(243);
				match(NullValue);
				}
				break;
			case ON_KEYWORD:
				enterOuterAlt(_localctx, 4);
				{
				setState(244);
				match(ON_KEYWORD);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode StringValue() { return getToken(GraphqlOperationParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(GraphqlOperationParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(GraphqlOperationParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(GraphqlOperationParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlOperationParser.NullValue, 0); }
		public EnumValueContext enumValue() {
			return getRuleContext(EnumValueContext.class,0);
		}
		public ArrayValueContext arrayValue() {
			return getRuleContext(ArrayValueContext.class,0);
		}
		public ObjectValueContext objectValue() {
			return getRuleContext(ObjectValueContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_value);
		try {
			setState(255);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case StringValue:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(250);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(251);
				match(NullValue);
				}
				break;
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case ON_KEYWORD:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 6);
				{
				setState(252);
				enumValue();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 7);
				{
				setState(253);
				arrayValue();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 8);
				{
				setState(254);
				objectValue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueWithVariableContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode StringValue() { return getToken(GraphqlOperationParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(GraphqlOperationParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(GraphqlOperationParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(GraphqlOperationParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlOperationParser.NullValue, 0); }
		public EnumValueContext enumValue() {
			return getRuleContext(EnumValueContext.class,0);
		}
		public ArrayValueWithVariableContext arrayValueWithVariable() {
			return getRuleContext(ArrayValueWithVariableContext.class,0);
		}
		public ObjectValueWithVariableContext objectValueWithVariable() {
			return getRuleContext(ObjectValueWithVariableContext.class,0);
		}
		public ValueWithVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueWithVariable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterValueWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitValueWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueWithVariableContext valueWithVariable() throws RecognitionException {
		ValueWithVariableContext _localctx = new ValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_valueWithVariable);
		try {
			setState(266);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				variable();
				}
				break;
			case StringValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(258);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(259);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(260);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(261);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 6);
				{
				setState(262);
				match(NullValue);
				}
				break;
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case ON_KEYWORD:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 7);
				{
				setState(263);
				enumValue();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 8);
				{
				setState(264);
				arrayValueWithVariable();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 9);
				{
				setState(265);
				objectValueWithVariable();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			match(T__9);
			setState(269);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefaultValueContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public DefaultValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterDefaultValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitDefaultValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitDefaultValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultValueContext defaultValue() throws RecognitionException {
		DefaultValueContext _localctx = new DefaultValueContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_defaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(T__10);
			setState(272);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ListTypeContext listType() {
			return getRuleContext(ListTypeContext.class,0);
		}
		public NonNullTypeContext nonNullType() {
			return getRuleContext(NonNullTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_type);
		try {
			setState(277);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(274);
				typeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(275);
				listType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(276);
				nonNullType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeNameContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListTypeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ListTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterListType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitListType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitListType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListTypeContext listType() throws RecognitionException {
		ListTypeContext _localctx = new ListTypeContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_listType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			match(T__6);
			setState(282);
			type();
			setState(283);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonNullTypeContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ListTypeContext listType() {
			return getRuleContext(ListTypeContext.class,0);
		}
		public NonNullTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonNullType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).enterNonNullType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlOperationListener ) ((GraphqlOperationListener)listener).exitNonNullType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlOperationVisitor ) return ((GraphqlOperationVisitor<? extends T>)visitor).visitNonNullType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonNullTypeContext nonNullType() throws RecognitionException {
		NonNullTypeContext _localctx = new NonNullTypeContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_nonNullType);
		try {
			setState(291);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BooleanValue:
			case NullValue:
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case ON_KEYWORD:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(285);
				typeName();
				setState(286);
				match(T__11);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(288);
				listType();
				setState(289);
				match(T__11);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001*\u0126\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000L\b\u0000\u0001"+
		"\u0000\u0003\u0000O\b\u0000\u0001\u0000\u0003\u0000R\b\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000V\b\u0000\u0001\u0001\u0001\u0001\u0004\u0001"+
		"Z\b\u0001\u000b\u0001\f\u0001[\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0003\u0002d\b\u0002\u0001\u0002\u0003"+
		"\u0002g\b\u0002\u0001\u0003\u0001\u0003\u0004\u0003k\b\u0003\u000b\u0003"+
		"\f\u0003l\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0003\u0004t\b\u0004\u0001\u0005\u0003\u0005w\b\u0005\u0001\u0005\u0001"+
		"\u0005\u0003\u0005{\b\u0005\u0001\u0005\u0003\u0005~\b\u0005\u0001\u0005"+
		"\u0003\u0005\u0081\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0003\u0007\u0089\b\u0007\u0001\b\u0001\b\u0003"+
		"\b\u008d\b\b\u0001\b\u0003\b\u0090\b\b\u0001\b\u0001\b\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0003\t\u0098\b\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0005\u000e\u00a7\b\u000e\n\u000e\f\u000e\u00aa\t\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0005\u000f\u00b0\b\u000f\n"+
		"\u000f\f\u000f\u00b3\t\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001"+
		"\u0010\u0005\u0010\u00b9\b\u0010\n\u0010\f\u0010\u00bc\t\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0005\u0011\u00c2\b\u0011\n\u0011"+
		"\f\u0011\u00c5\t\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0004\u0014\u00d2\b\u0014\u000b\u0014\f\u0014\u00d3\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u00d9\b\u0015\u0001\u0016\u0001"+
		"\u0016\u0004\u0016\u00dd\b\u0016\u000b\u0016\f\u0016\u00de\u0001\u0016"+
		"\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u00ec\b\u0019"+
		"\u0001\u001a\u0001\u001a\u0003\u001a\u00f0\b\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u00f6\b\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0003\u001c\u0100\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d"+
		"\u010b\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001 \u0001 \u0001 \u0003 \u0116\b \u0001!\u0001!\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003"+
		"#\u0124\b#\u0001#\u0000\u0000$\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF\u0000"+
		"\u0002\u0001\u0000\u0010\u0012\u0002\u0000\u000f\u001c\u001e\u001f\u0132"+
		"\u0000U\u0001\u0000\u0000\u0000\u0002W\u0001\u0000\u0000\u0000\u0004_"+
		"\u0001\u0000\u0000\u0000\u0006h\u0001\u0000\u0000\u0000\bs\u0001\u0000"+
		"\u0000\u0000\nv\u0001\u0000\u0000\u0000\f\u0082\u0001\u0000\u0000\u0000"+
		"\u000e\u0085\u0001\u0000\u0000\u0000\u0010\u008a\u0001\u0000\u0000\u0000"+
		"\u0012\u0093\u0001\u0000\u0000\u0000\u0014\u009b\u0001\u0000\u0000\u0000"+
		"\u0016\u009e\u0001\u0000\u0000\u0000\u0018\u00a0\u0001\u0000\u0000\u0000"+
		"\u001a\u00a2\u0001\u0000\u0000\u0000\u001c\u00a4\u0001\u0000\u0000\u0000"+
		"\u001e\u00ad\u0001\u0000\u0000\u0000 \u00b6\u0001\u0000\u0000\u0000\""+
		"\u00bf\u0001\u0000\u0000\u0000$\u00c8\u0001\u0000\u0000\u0000&\u00cc\u0001"+
		"\u0000\u0000\u0000(\u00d1\u0001\u0000\u0000\u0000*\u00d5\u0001\u0000\u0000"+
		"\u0000,\u00da\u0001\u0000\u0000\u0000.\u00e2\u0001\u0000\u0000\u00000"+
		"\u00e6\u0001\u0000\u0000\u00002\u00eb\u0001\u0000\u0000\u00004\u00ef\u0001"+
		"\u0000\u0000\u00006\u00f5\u0001\u0000\u0000\u00008\u00ff\u0001\u0000\u0000"+
		"\u0000:\u010a\u0001\u0000\u0000\u0000<\u010c\u0001\u0000\u0000\u0000>"+
		"\u010f\u0001\u0000\u0000\u0000@\u0115\u0001\u0000\u0000\u0000B\u0117\u0001"+
		"\u0000\u0000\u0000D\u0119\u0001\u0000\u0000\u0000F\u0123\u0001\u0000\u0000"+
		"\u0000HV\u0003\u0006\u0003\u0000IK\u0003\u0016\u000b\u0000JL\u00036\u001b"+
		"\u0000KJ\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LN\u0001\u0000"+
		"\u0000\u0000MO\u0003\u0002\u0001\u0000NM\u0001\u0000\u0000\u0000NO\u0001"+
		"\u0000\u0000\u0000OQ\u0001\u0000\u0000\u0000PR\u0003(\u0014\u0000QP\u0001"+
		"\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000"+
		"ST\u0003\u0006\u0003\u0000TV\u0001\u0000\u0000\u0000UH\u0001\u0000\u0000"+
		"\u0000UI\u0001\u0000\u0000\u0000V\u0001\u0001\u0000\u0000\u0000WY\u0005"+
		"\u0001\u0000\u0000XZ\u0003\u0004\u0002\u0000YX\u0001\u0000\u0000\u0000"+
		"Z[\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000"+
		"\u0000\\]\u0001\u0000\u0000\u0000]^\u0005\u0002\u0000\u0000^\u0003\u0001"+
		"\u0000\u0000\u0000_`\u0003<\u001e\u0000`a\u0005\u0003\u0000\u0000ac\u0003"+
		"@ \u0000bd\u0003>\u001f\u0000cb\u0001\u0000\u0000\u0000cd\u0001\u0000"+
		"\u0000\u0000df\u0001\u0000\u0000\u0000eg\u0003(\u0014\u0000fe\u0001\u0000"+
		"\u0000\u0000fg\u0001\u0000\u0000\u0000g\u0005\u0001\u0000\u0000\u0000"+
		"hj\u0005\u0004\u0000\u0000ik\u0003\b\u0004\u0000ji\u0001\u0000\u0000\u0000"+
		"kl\u0001\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000no\u0005\u0005\u0000\u0000o\u0007\u0001"+
		"\u0000\u0000\u0000pt\u0003\n\u0005\u0000qt\u0003\u000e\u0007\u0000rt\u0003"+
		"\u0010\b\u0000sp\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000sr\u0001"+
		"\u0000\u0000\u0000t\t\u0001\u0000\u0000\u0000uw\u0003\f\u0006\u0000vu"+
		"\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000"+
		"\u0000xz\u00036\u001b\u0000y{\u0003,\u0016\u0000zy\u0001\u0000\u0000\u0000"+
		"z{\u0001\u0000\u0000\u0000{}\u0001\u0000\u0000\u0000|~\u0003(\u0014\u0000"+
		"}|\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u0080\u0001\u0000"+
		"\u0000\u0000\u007f\u0081\u0003\u0006\u0003\u0000\u0080\u007f\u0001\u0000"+
		"\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000\u0081\u000b\u0001\u0000"+
		"\u0000\u0000\u0082\u0083\u00036\u001b\u0000\u0083\u0084\u0005\u0003\u0000"+
		"\u0000\u0084\r\u0001\u0000\u0000\u0000\u0085\u0086\u0005\u0006\u0000\u0000"+
		"\u0086\u0088\u00032\u0019\u0000\u0087\u0089\u0003(\u0014\u0000\u0088\u0087"+
		"\u0001\u0000\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u000f"+
		"\u0001\u0000\u0000\u0000\u008a\u008c\u0005\u0006\u0000\u0000\u008b\u008d"+
		"\u0003\u0014\n\u0000\u008c\u008b\u0001\u0000\u0000\u0000\u008c\u008d\u0001"+
		"\u0000\u0000\u0000\u008d\u008f\u0001\u0000\u0000\u0000\u008e\u0090\u0003"+
		"(\u0014\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000"+
		"\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u0092\u0003\u0006"+
		"\u0003\u0000\u0092\u0011\u0001\u0000\u0000\u0000\u0093\u0094\u0005\u000f"+
		"\u0000\u0000\u0094\u0095\u00032\u0019\u0000\u0095\u0097\u0003\u0014\n"+
		"\u0000\u0096\u0098\u0003(\u0014\u0000\u0097\u0096\u0001\u0000\u0000\u0000"+
		"\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000"+
		"\u0099\u009a\u0003\u0006\u0003\u0000\u009a\u0013\u0001\u0000\u0000\u0000"+
		"\u009b\u009c\u0005\u001d\u0000\u0000\u009c\u009d\u0003B!\u0000\u009d\u0015"+
		"\u0001\u0000\u0000\u0000\u009e\u009f\u0007\u0000\u0000\u0000\u009f\u0017"+
		"\u0001\u0000\u0000\u0000\u00a0\u00a1\u0005\"\u0000\u0000\u00a1\u0019\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a3\u00034\u001a\u0000\u00a3\u001b\u0001\u0000"+
		"\u0000\u0000\u00a4\u00a8\u0005\u0007\u0000\u0000\u00a5\u00a7\u00038\u001c"+
		"\u0000\u00a6\u00a5\u0001\u0000\u0000\u0000\u00a7\u00aa\u0001\u0000\u0000"+
		"\u0000\u00a8\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000"+
		"\u0000\u00a9\u00ab\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000"+
		"\u0000\u00ab\u00ac\u0005\b\u0000\u0000\u00ac\u001d\u0001\u0000\u0000\u0000"+
		"\u00ad\u00b1\u0005\u0007\u0000\u0000\u00ae\u00b0\u0003:\u001d\u0000\u00af"+
		"\u00ae\u0001\u0000\u0000\u0000\u00b0\u00b3\u0001\u0000\u0000\u0000\u00b1"+
		"\u00af\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2"+
		"\u00b4\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b4"+
		"\u00b5\u0005\b\u0000\u0000\u00b5\u001f\u0001\u0000\u0000\u0000\u00b6\u00ba"+
		"\u0005\u0004\u0000\u0000\u00b7\u00b9\u0003$\u0012\u0000\u00b8\u00b7\u0001"+
		"\u0000\u0000\u0000\u00b9\u00bc\u0001\u0000\u0000\u0000\u00ba\u00b8\u0001"+
		"\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bd\u00be\u0005"+
		"\u0005\u0000\u0000\u00be!\u0001\u0000\u0000\u0000\u00bf\u00c3\u0005\u0004"+
		"\u0000\u0000\u00c0\u00c2\u0003&\u0013\u0000\u00c1\u00c0\u0001\u0000\u0000"+
		"\u0000\u00c2\u00c5\u0001\u0000\u0000\u0000\u00c3\u00c1\u0001\u0000\u0000"+
		"\u0000\u00c3\u00c4\u0001\u0000\u0000\u0000\u00c4\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c6\u00c7\u0005\u0005\u0000"+
		"\u0000\u00c7#\u0001\u0000\u0000\u0000\u00c8\u00c9\u00036\u001b\u0000\u00c9"+
		"\u00ca\u0005\u0003\u0000\u0000\u00ca\u00cb\u00038\u001c\u0000\u00cb%\u0001"+
		"\u0000\u0000\u0000\u00cc\u00cd\u00036\u001b\u0000\u00cd\u00ce\u0005\u0003"+
		"\u0000\u0000\u00ce\u00cf\u0003:\u001d\u0000\u00cf\'\u0001\u0000\u0000"+
		"\u0000\u00d0\u00d2\u0003*\u0015\u0000\u00d1\u00d0\u0001\u0000\u0000\u0000"+
		"\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000"+
		"\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4)\u0001\u0000\u0000\u0000\u00d5"+
		"\u00d6\u0005\t\u0000\u0000\u00d6\u00d8\u00036\u001b\u0000\u00d7\u00d9"+
		"\u0003,\u0016\u0000\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001"+
		"\u0000\u0000\u0000\u00d9+\u0001\u0000\u0000\u0000\u00da\u00dc\u0005\u0001"+
		"\u0000\u0000\u00db\u00dd\u0003.\u0017\u0000\u00dc\u00db\u0001\u0000\u0000"+
		"\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u00dc\u0001\u0000\u0000"+
		"\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000"+
		"\u0000\u00e0\u00e1\u0005\u0002\u0000\u0000\u00e1-\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e3\u00036\u001b\u0000\u00e3\u00e4\u0005\u0003\u0000\u0000\u00e4"+
		"\u00e5\u0003:\u001d\u0000\u00e5/\u0001\u0000\u0000\u0000\u00e6\u00e7\u0007"+
		"\u0001\u0000\u0000\u00e71\u0001\u0000\u0000\u0000\u00e8\u00ec\u00030\u0018"+
		"\u0000\u00e9\u00ec\u0005\r\u0000\u0000\u00ea\u00ec\u0005\u000e\u0000\u0000"+
		"\u00eb\u00e8\u0001\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000\u0000\u0000"+
		"\u00eb\u00ea\u0001\u0000\u0000\u0000\u00ec3\u0001\u0000\u0000\u0000\u00ed"+
		"\u00f0\u00030\u0018\u0000\u00ee\u00f0\u0005\u001d\u0000\u0000\u00ef\u00ed"+
		"\u0001\u0000\u0000\u0000\u00ef\u00ee\u0001\u0000\u0000\u0000\u00f05\u0001"+
		"\u0000\u0000\u0000\u00f1\u00f6\u00030\u0018\u0000\u00f2\u00f6\u0005\r"+
		"\u0000\u0000\u00f3\u00f6\u0005\u000e\u0000\u0000\u00f4\u00f6\u0005\u001d"+
		"\u0000\u0000\u00f5\u00f1\u0001\u0000\u0000\u0000\u00f5\u00f2\u0001\u0000"+
		"\u0000\u0000\u00f5\u00f3\u0001\u0000\u0000\u0000\u00f5\u00f4\u0001\u0000"+
		"\u0000\u0000\u00f67\u0001\u0000\u0000\u0000\u00f7\u0100\u0005\"\u0000"+
		"\u0000\u00f8\u0100\u0005 \u0000\u0000\u00f9\u0100\u0005!\u0000\u0000\u00fa"+
		"\u0100\u0005\r\u0000\u0000\u00fb\u0100\u0005\u000e\u0000\u0000\u00fc\u0100"+
		"\u0003\u001a\r\u0000\u00fd\u0100\u0003\u001c\u000e\u0000\u00fe\u0100\u0003"+
		" \u0010\u0000\u00ff\u00f7\u0001\u0000\u0000\u0000\u00ff\u00f8\u0001\u0000"+
		"\u0000\u0000\u00ff\u00f9\u0001\u0000\u0000\u0000\u00ff\u00fa\u0001\u0000"+
		"\u0000\u0000\u00ff\u00fb\u0001\u0000\u0000\u0000\u00ff\u00fc\u0001\u0000"+
		"\u0000\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u00ff\u00fe\u0001\u0000"+
		"\u0000\u0000\u01009\u0001\u0000\u0000\u0000\u0101\u010b\u0003<\u001e\u0000"+
		"\u0102\u010b\u0005\"\u0000\u0000\u0103\u010b\u0005 \u0000\u0000\u0104"+
		"\u010b\u0005!\u0000\u0000\u0105\u010b\u0005\r\u0000\u0000\u0106\u010b"+
		"\u0005\u000e\u0000\u0000\u0107\u010b\u0003\u001a\r\u0000\u0108\u010b\u0003"+
		"\u001e\u000f\u0000\u0109\u010b\u0003\"\u0011\u0000\u010a\u0101\u0001\u0000"+
		"\u0000\u0000\u010a\u0102\u0001\u0000\u0000\u0000\u010a\u0103\u0001\u0000"+
		"\u0000\u0000\u010a\u0104\u0001\u0000\u0000\u0000\u010a\u0105\u0001\u0000"+
		"\u0000\u0000\u010a\u0106\u0001\u0000\u0000\u0000\u010a\u0107\u0001\u0000"+
		"\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010a\u0109\u0001\u0000"+
		"\u0000\u0000\u010b;\u0001\u0000\u0000\u0000\u010c\u010d\u0005\n\u0000"+
		"\u0000\u010d\u010e\u00036\u001b\u0000\u010e=\u0001\u0000\u0000\u0000\u010f"+
		"\u0110\u0005\u000b\u0000\u0000\u0110\u0111\u00038\u001c\u0000\u0111?\u0001"+
		"\u0000\u0000\u0000\u0112\u0116\u0003B!\u0000\u0113\u0116\u0003D\"\u0000"+
		"\u0114\u0116\u0003F#\u0000\u0115\u0112\u0001\u0000\u0000\u0000\u0115\u0113"+
		"\u0001\u0000\u0000\u0000\u0115\u0114\u0001\u0000\u0000\u0000\u0116A\u0001"+
		"\u0000\u0000\u0000\u0117\u0118\u00036\u001b\u0000\u0118C\u0001\u0000\u0000"+
		"\u0000\u0119\u011a\u0005\u0007\u0000\u0000\u011a\u011b\u0003@ \u0000\u011b"+
		"\u011c\u0005\b\u0000\u0000\u011cE\u0001\u0000\u0000\u0000\u011d\u011e"+
		"\u0003B!\u0000\u011e\u011f\u0005\f\u0000\u0000\u011f\u0124\u0001\u0000"+
		"\u0000\u0000\u0120\u0121\u0003D\"\u0000\u0121\u0122\u0005\f\u0000\u0000"+
		"\u0122\u0124\u0001\u0000\u0000\u0000\u0123\u011d\u0001\u0000\u0000\u0000"+
		"\u0123\u0120\u0001\u0000\u0000\u0000\u0124G\u0001\u0000\u0000\u0000\u001f"+
		"KNQU[cflsvz}\u0080\u0088\u008c\u008f\u0097\u00a8\u00b1\u00ba\u00c3\u00d3"+
		"\u00d8\u00de\u00eb\u00ef\u00f5\u00ff\u010a\u0115\u0123";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}