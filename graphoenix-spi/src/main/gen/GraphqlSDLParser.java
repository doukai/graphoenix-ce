// Generated from /Users/doukai/Work/graphoenix-ce/graphoenix-spi/src/main/antlr/GraphqlSDL.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class GraphqlSDLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, BooleanValue=14, NullValue=15, 
		FRAGMENT=16, QUERY=17, MUTATION=18, SUBSCRIPTION=19, SCHEMA=20, SCALAR=21, 
		TYPE=22, INTERFACE=23, IMPLEMENTS=24, ENUM=25, UNION=26, INPUT=27, EXTEND=28, 
		DIRECTIVE=29, ON_KEYWORD=30, REPEATABLE=31, NAME=32, IntValue=33, FloatValue=34, 
		StringValue=35, Comment=36, LF=37, CR=38, LineTerminator=39, Space=40, 
		Tab=41, Comma=42, UnicodeBOM=43;
	public static final int
		RULE_typeSystemDefinition = 0, RULE_typeSystemExtension = 1, RULE_schemaDefinition = 2, 
		RULE_schemaExtension = 3, RULE_operationTypeDefinition = 4, RULE_typeDefinition = 5, 
		RULE_typeExtension = 6, RULE_emptyParentheses = 7, RULE_scalarTypeDefinition = 8, 
		RULE_scalarTypeExtensionDefinition = 9, RULE_objectTypeDefinition = 10, 
		RULE_objectTypeExtensionDefinition = 11, RULE_implementsInterfaces = 12, 
		RULE_fieldsDefinition = 13, RULE_extensionFieldsDefinition = 14, RULE_fieldDefinition = 15, 
		RULE_argumentsDefinition = 16, RULE_inputValueDefinition = 17, RULE_interfaceTypeDefinition = 18, 
		RULE_interfaceTypeExtensionDefinition = 19, RULE_unionTypeDefinition = 20, 
		RULE_unionTypeExtensionDefinition = 21, RULE_unionMembership = 22, RULE_unionMembers = 23, 
		RULE_enumTypeDefinition = 24, RULE_enumTypeExtensionDefinition = 25, RULE_enumValueDefinitions = 26, 
		RULE_extensionEnumValueDefinitions = 27, RULE_enumValueDefinition = 28, 
		RULE_inputObjectTypeDefinition = 29, RULE_inputObjectTypeExtensionDefinition = 30, 
		RULE_inputObjectValueDefinitions = 31, RULE_extensionInputObjectValueDefinitions = 32, 
		RULE_directiveDefinition = 33, RULE_directiveLocation = 34, RULE_directiveLocations = 35, 
		RULE_operationType = 36, RULE_description = 37, RULE_enumValue = 38, RULE_arrayValue = 39, 
		RULE_arrayValueWithVariable = 40, RULE_objectValue = 41, RULE_objectValueWithVariable = 42, 
		RULE_objectField = 43, RULE_objectFieldWithVariable = 44, RULE_directives = 45, 
		RULE_directive = 46, RULE_arguments = 47, RULE_argument = 48, RULE_baseName = 49, 
		RULE_fragmentName = 50, RULE_enumValueName = 51, RULE_name = 52, RULE_value = 53, 
		RULE_valueWithVariable = 54, RULE_variable = 55, RULE_defaultValue = 56, 
		RULE_type = 57, RULE_typeName = 58, RULE_listType = 59, RULE_nonNullType = 60;
	private static String[] makeRuleNames() {
		return new String[] {
			"typeSystemDefinition", "typeSystemExtension", "schemaDefinition", "schemaExtension", 
			"operationTypeDefinition", "typeDefinition", "typeExtension", "emptyParentheses", 
			"scalarTypeDefinition", "scalarTypeExtensionDefinition", "objectTypeDefinition", 
			"objectTypeExtensionDefinition", "implementsInterfaces", "fieldsDefinition", 
			"extensionFieldsDefinition", "fieldDefinition", "argumentsDefinition", 
			"inputValueDefinition", "interfaceTypeDefinition", "interfaceTypeExtensionDefinition", 
			"unionTypeDefinition", "unionTypeExtensionDefinition", "unionMembership", 
			"unionMembers", "enumTypeDefinition", "enumTypeExtensionDefinition", 
			"enumValueDefinitions", "extensionEnumValueDefinitions", "enumValueDefinition", 
			"inputObjectTypeDefinition", "inputObjectTypeExtensionDefinition", "inputObjectValueDefinitions", 
			"extensionInputObjectValueDefinitions", "directiveDefinition", "directiveLocation", 
			"directiveLocations", "operationType", "description", "enumValue", "arrayValue", 
			"arrayValueWithVariable", "objectValue", "objectValueWithVariable", "objectField", 
			"objectFieldWithVariable", "directives", "directive", "arguments", "argument", 
			"baseName", "fragmentName", "enumValueName", "name", "value", "valueWithVariable", 
			"variable", "defaultValue", "type", "typeName", "listType", "nonNullType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "':'", "'&'", "'('", "')'", "'='", "'|'", "'@'", 
			"'['", "']'", "'$'", "'!'", null, "'null'", "'fragment'", "'query'", 
			"'mutation'", "'subscription'", "'schema'", "'scalar'", "'type'", "'interface'", 
			"'implements'", "'enum'", "'union'", "'input'", "'extend'", "'directive'", 
			"'on'", "'repeatable'", null, null, null, null, null, null, null, null, 
			null, null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "BooleanValue", "NullValue", "FRAGMENT", "QUERY", "MUTATION", 
			"SUBSCRIPTION", "SCHEMA", "SCALAR", "TYPE", "INTERFACE", "IMPLEMENTS", 
			"ENUM", "UNION", "INPUT", "EXTEND", "DIRECTIVE", "ON_KEYWORD", "REPEATABLE", 
			"NAME", "IntValue", "FloatValue", "StringValue", "Comment", "LF", "CR", 
			"LineTerminator", "Space", "Tab", "Comma", "UnicodeBOM"
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
	public String getGrammarFileName() { return "GraphqlSDL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GraphqlSDLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeSystemDefinitionContext extends ParserRuleContext {
		public SchemaDefinitionContext schemaDefinition() {
			return getRuleContext(SchemaDefinitionContext.class,0);
		}
		public TypeDefinitionContext typeDefinition() {
			return getRuleContext(TypeDefinitionContext.class,0);
		}
		public DirectiveDefinitionContext directiveDefinition() {
			return getRuleContext(DirectiveDefinitionContext.class,0);
		}
		public TypeSystemDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSystemDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterTypeSystemDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitTypeSystemDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitTypeSystemDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSystemDefinitionContext typeSystemDefinition() throws RecognitionException {
		TypeSystemDefinitionContext _localctx = new TypeSystemDefinitionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_typeSystemDefinition);
		try {
			setState(125);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(122);
				schemaDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(123);
				typeDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(124);
				directiveDefinition();
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
	public static class TypeSystemExtensionContext extends ParserRuleContext {
		public SchemaExtensionContext schemaExtension() {
			return getRuleContext(SchemaExtensionContext.class,0);
		}
		public TypeExtensionContext typeExtension() {
			return getRuleContext(TypeExtensionContext.class,0);
		}
		public TypeSystemExtensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSystemExtension; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterTypeSystemExtension(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitTypeSystemExtension(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitTypeSystemExtension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSystemExtensionContext typeSystemExtension() throws RecognitionException {
		TypeSystemExtensionContext _localctx = new TypeSystemExtensionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_typeSystemExtension);
		try {
			setState(129);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(127);
				schemaExtension();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(128);
				typeExtension();
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
	public static class SchemaDefinitionContext extends ParserRuleContext {
		public TerminalNode SCHEMA() { return getToken(GraphqlSDLParser.SCHEMA, 0); }
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public List<OperationTypeDefinitionContext> operationTypeDefinition() {
			return getRuleContexts(OperationTypeDefinitionContext.class);
		}
		public OperationTypeDefinitionContext operationTypeDefinition(int i) {
			return getRuleContext(OperationTypeDefinitionContext.class,i);
		}
		public SchemaDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterSchemaDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitSchemaDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitSchemaDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaDefinitionContext schemaDefinition() throws RecognitionException {
		SchemaDefinitionContext _localctx = new SchemaDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_schemaDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(131);
				description();
				}
			}

			setState(134);
			match(SCHEMA);
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(135);
				directives();
				}
			}

			setState(138);
			match(T__0);
			setState(140); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(139);
				operationTypeDefinition();
				}
				}
				setState(142); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 34360655872L) != 0) );
			setState(144);
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
	public static class SchemaExtensionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode SCHEMA() { return getToken(GraphqlSDLParser.SCHEMA, 0); }
		public List<DirectivesContext> directives() {
			return getRuleContexts(DirectivesContext.class);
		}
		public DirectivesContext directives(int i) {
			return getRuleContext(DirectivesContext.class,i);
		}
		public List<OperationTypeDefinitionContext> operationTypeDefinition() {
			return getRuleContexts(OperationTypeDefinitionContext.class);
		}
		public OperationTypeDefinitionContext operationTypeDefinition(int i) {
			return getRuleContext(OperationTypeDefinitionContext.class,i);
		}
		public SchemaExtensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaExtension; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterSchemaExtension(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitSchemaExtension(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitSchemaExtension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaExtensionContext schemaExtension() throws RecognitionException {
		SchemaExtensionContext _localctx = new SchemaExtensionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_schemaExtension);
		int _la;
		try {
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				match(EXTEND);
				setState(147);
				match(SCHEMA);
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(148);
					directives();
					}
				}

				setState(151);
				match(T__0);
				setState(153); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(152);
					operationTypeDefinition();
					}
					}
					setState(155); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 34360655872L) != 0) );
				setState(157);
				match(T__1);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(159);
				match(EXTEND);
				setState(160);
				match(SCHEMA);
				setState(162); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(161);
					directives();
					}
					}
					setState(164); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__8 );
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
	public static class OperationTypeDefinitionContext extends ParserRuleContext {
		public OperationTypeContext operationType() {
			return getRuleContext(OperationTypeContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public OperationTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterOperationTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitOperationTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitOperationTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationTypeDefinitionContext operationTypeDefinition() throws RecognitionException {
		OperationTypeDefinitionContext _localctx = new OperationTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_operationTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(168);
				description();
				}
			}

			setState(171);
			operationType();
			setState(172);
			match(T__2);
			setState(173);
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
	public static class TypeDefinitionContext extends ParserRuleContext {
		public ScalarTypeDefinitionContext scalarTypeDefinition() {
			return getRuleContext(ScalarTypeDefinitionContext.class,0);
		}
		public ObjectTypeDefinitionContext objectTypeDefinition() {
			return getRuleContext(ObjectTypeDefinitionContext.class,0);
		}
		public InterfaceTypeDefinitionContext interfaceTypeDefinition() {
			return getRuleContext(InterfaceTypeDefinitionContext.class,0);
		}
		public UnionTypeDefinitionContext unionTypeDefinition() {
			return getRuleContext(UnionTypeDefinitionContext.class,0);
		}
		public EnumTypeDefinitionContext enumTypeDefinition() {
			return getRuleContext(EnumTypeDefinitionContext.class,0);
		}
		public InputObjectTypeDefinitionContext inputObjectTypeDefinition() {
			return getRuleContext(InputObjectTypeDefinitionContext.class,0);
		}
		public TypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDefinitionContext typeDefinition() throws RecognitionException {
		TypeDefinitionContext _localctx = new TypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_typeDefinition);
		try {
			setState(181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				scalarTypeDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
				objectTypeDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(177);
				interfaceTypeDefinition();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(178);
				unionTypeDefinition();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(179);
				enumTypeDefinition();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(180);
				inputObjectTypeDefinition();
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
	public static class TypeExtensionContext extends ParserRuleContext {
		public ObjectTypeExtensionDefinitionContext objectTypeExtensionDefinition() {
			return getRuleContext(ObjectTypeExtensionDefinitionContext.class,0);
		}
		public InterfaceTypeExtensionDefinitionContext interfaceTypeExtensionDefinition() {
			return getRuleContext(InterfaceTypeExtensionDefinitionContext.class,0);
		}
		public UnionTypeExtensionDefinitionContext unionTypeExtensionDefinition() {
			return getRuleContext(UnionTypeExtensionDefinitionContext.class,0);
		}
		public ScalarTypeExtensionDefinitionContext scalarTypeExtensionDefinition() {
			return getRuleContext(ScalarTypeExtensionDefinitionContext.class,0);
		}
		public EnumTypeExtensionDefinitionContext enumTypeExtensionDefinition() {
			return getRuleContext(EnumTypeExtensionDefinitionContext.class,0);
		}
		public InputObjectTypeExtensionDefinitionContext inputObjectTypeExtensionDefinition() {
			return getRuleContext(InputObjectTypeExtensionDefinitionContext.class,0);
		}
		public TypeExtensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeExtension; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterTypeExtension(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitTypeExtension(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitTypeExtension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeExtensionContext typeExtension() throws RecognitionException {
		TypeExtensionContext _localctx = new TypeExtensionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_typeExtension);
		try {
			setState(189);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(183);
				objectTypeExtensionDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(184);
				interfaceTypeExtensionDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(185);
				unionTypeExtensionDefinition();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(186);
				scalarTypeExtensionDefinition();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(187);
				enumTypeExtensionDefinition();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(188);
				inputObjectTypeExtensionDefinition();
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
	public static class EmptyParenthesesContext extends ParserRuleContext {
		public EmptyParenthesesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyParentheses; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEmptyParentheses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEmptyParentheses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEmptyParentheses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyParenthesesContext emptyParentheses() throws RecognitionException {
		EmptyParenthesesContext _localctx = new EmptyParenthesesContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_emptyParentheses);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(T__0);
			setState(192);
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
	public static class ScalarTypeDefinitionContext extends ParserRuleContext {
		public TerminalNode SCALAR() { return getToken(GraphqlSDLParser.SCALAR, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public ScalarTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scalarTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterScalarTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitScalarTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitScalarTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScalarTypeDefinitionContext scalarTypeDefinition() throws RecognitionException {
		ScalarTypeDefinitionContext _localctx = new ScalarTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_scalarTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(194);
				description();
				}
			}

			setState(197);
			match(SCALAR);
			setState(198);
			name();
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(199);
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
	public static class ScalarTypeExtensionDefinitionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode SCALAR() { return getToken(GraphqlSDLParser.SCALAR, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public ScalarTypeExtensionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scalarTypeExtensionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterScalarTypeExtensionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitScalarTypeExtensionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitScalarTypeExtensionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScalarTypeExtensionDefinitionContext scalarTypeExtensionDefinition() throws RecognitionException {
		ScalarTypeExtensionDefinitionContext _localctx = new ScalarTypeExtensionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_scalarTypeExtensionDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			match(EXTEND);
			setState(203);
			match(SCALAR);
			setState(204);
			name();
			setState(205);
			directives();
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
	public static class ObjectTypeDefinitionContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(GraphqlSDLParser.TYPE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ImplementsInterfacesContext implementsInterfaces() {
			return getRuleContext(ImplementsInterfacesContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public FieldsDefinitionContext fieldsDefinition() {
			return getRuleContext(FieldsDefinitionContext.class,0);
		}
		public ObjectTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterObjectTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitObjectTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitObjectTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectTypeDefinitionContext objectTypeDefinition() throws RecognitionException {
		ObjectTypeDefinitionContext _localctx = new ObjectTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_objectTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(207);
				description();
				}
			}

			setState(210);
			match(TYPE);
			setState(211);
			name();
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(212);
				implementsInterfaces(0);
				}
			}

			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(215);
				directives();
				}
			}

			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(218);
				fieldsDefinition();
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
	public static class ObjectTypeExtensionDefinitionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode TYPE() { return getToken(GraphqlSDLParser.TYPE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ExtensionFieldsDefinitionContext extensionFieldsDefinition() {
			return getRuleContext(ExtensionFieldsDefinitionContext.class,0);
		}
		public ImplementsInterfacesContext implementsInterfaces() {
			return getRuleContext(ImplementsInterfacesContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public EmptyParenthesesContext emptyParentheses() {
			return getRuleContext(EmptyParenthesesContext.class,0);
		}
		public ObjectTypeExtensionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectTypeExtensionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterObjectTypeExtensionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitObjectTypeExtensionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitObjectTypeExtensionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectTypeExtensionDefinitionContext objectTypeExtensionDefinition() throws RecognitionException {
		ObjectTypeExtensionDefinitionContext _localctx = new ObjectTypeExtensionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_objectTypeExtensionDefinition);
		int _la;
		try {
			setState(247);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(221);
				match(EXTEND);
				setState(222);
				match(TYPE);
				setState(223);
				name();
				setState(225);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IMPLEMENTS) {
					{
					setState(224);
					implementsInterfaces(0);
					}
				}

				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(227);
					directives();
					}
				}

				setState(230);
				extensionFieldsDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(232);
				match(EXTEND);
				setState(233);
				match(TYPE);
				setState(234);
				name();
				setState(236);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IMPLEMENTS) {
					{
					setState(235);
					implementsInterfaces(0);
					}
				}

				setState(238);
				directives();
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(239);
					emptyParentheses();
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(242);
				match(EXTEND);
				setState(243);
				match(TYPE);
				setState(244);
				name();
				setState(245);
				implementsInterfaces(0);
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
	public static class ImplementsInterfacesContext extends ParserRuleContext {
		public TerminalNode IMPLEMENTS() { return getToken(GraphqlSDLParser.IMPLEMENTS, 0); }
		public List<TypeNameContext> typeName() {
			return getRuleContexts(TypeNameContext.class);
		}
		public TypeNameContext typeName(int i) {
			return getRuleContext(TypeNameContext.class,i);
		}
		public ImplementsInterfacesContext implementsInterfaces() {
			return getRuleContext(ImplementsInterfacesContext.class,0);
		}
		public ImplementsInterfacesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implementsInterfaces; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterImplementsInterfaces(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitImplementsInterfaces(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitImplementsInterfaces(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImplementsInterfacesContext implementsInterfaces() throws RecognitionException {
		return implementsInterfaces(0);
	}

	private ImplementsInterfacesContext implementsInterfaces(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ImplementsInterfacesContext _localctx = new ImplementsInterfacesContext(_ctx, _parentState);
		ImplementsInterfacesContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_implementsInterfaces, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(250);
			match(IMPLEMENTS);
			setState(252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(251);
				match(T__3);
				}
			}

			setState(255); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(254);
					typeName();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(257); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
			_ctx.stop = _input.LT(-1);
			setState(264);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ImplementsInterfacesContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_implementsInterfaces);
					setState(259);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(260);
					match(T__3);
					setState(261);
					typeName();
					}
					} 
				}
				setState(266);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldsDefinitionContext extends ParserRuleContext {
		public List<FieldDefinitionContext> fieldDefinition() {
			return getRuleContexts(FieldDefinitionContext.class);
		}
		public FieldDefinitionContext fieldDefinition(int i) {
			return getRuleContext(FieldDefinitionContext.class,i);
		}
		public FieldsDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldsDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterFieldsDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitFieldsDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitFieldsDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldsDefinitionContext fieldsDefinition() throws RecognitionException {
		FieldsDefinitionContext _localctx = new FieldsDefinitionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_fieldsDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(T__0);
			setState(271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 42949656576L) != 0)) {
				{
				{
				setState(268);
				fieldDefinition();
				}
				}
				setState(273);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(274);
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
	public static class ExtensionFieldsDefinitionContext extends ParserRuleContext {
		public List<FieldDefinitionContext> fieldDefinition() {
			return getRuleContexts(FieldDefinitionContext.class);
		}
		public FieldDefinitionContext fieldDefinition(int i) {
			return getRuleContext(FieldDefinitionContext.class,i);
		}
		public ExtensionFieldsDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extensionFieldsDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterExtensionFieldsDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitExtensionFieldsDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitExtensionFieldsDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtensionFieldsDefinitionContext extensionFieldsDefinition() throws RecognitionException {
		ExtensionFieldsDefinitionContext _localctx = new ExtensionFieldsDefinitionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_extensionFieldsDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			match(T__0);
			setState(278); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(277);
				fieldDefinition();
				}
				}
				setState(280); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 42949656576L) != 0) );
			setState(282);
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
	public static class FieldDefinitionContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ArgumentsDefinitionContext argumentsDefinition() {
			return getRuleContext(ArgumentsDefinitionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public FieldDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterFieldDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitFieldDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitFieldDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldDefinitionContext fieldDefinition() throws RecognitionException {
		FieldDefinitionContext _localctx = new FieldDefinitionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_fieldDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(284);
				description();
				}
			}

			setState(287);
			name();
			setState(289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(288);
				argumentsDefinition();
				}
			}

			setState(291);
			match(T__2);
			setState(292);
			type();
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(293);
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
	public static class ArgumentsDefinitionContext extends ParserRuleContext {
		public List<InputValueDefinitionContext> inputValueDefinition() {
			return getRuleContexts(InputValueDefinitionContext.class);
		}
		public InputValueDefinitionContext inputValueDefinition(int i) {
			return getRuleContext(InputValueDefinitionContext.class,i);
		}
		public ArgumentsDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentsDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterArgumentsDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitArgumentsDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitArgumentsDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsDefinitionContext argumentsDefinition() throws RecognitionException {
		ArgumentsDefinitionContext _localctx = new ArgumentsDefinitionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_argumentsDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(T__4);
			setState(298); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(297);
				inputValueDefinition();
				}
				}
				setState(300); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 42949656576L) != 0) );
			setState(302);
			match(T__5);
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
	public static class InputValueDefinitionContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DefaultValueContext defaultValue() {
			return getRuleContext(DefaultValueContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public InputValueDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputValueDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterInputValueDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitInputValueDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitInputValueDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputValueDefinitionContext inputValueDefinition() throws RecognitionException {
		InputValueDefinitionContext _localctx = new InputValueDefinitionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_inputValueDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(304);
				description();
				}
			}

			setState(307);
			name();
			setState(308);
			match(T__2);
			setState(309);
			type();
			setState(311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(310);
				defaultValue();
				}
			}

			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(313);
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
	public static class InterfaceTypeDefinitionContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(GraphqlSDLParser.INTERFACE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ImplementsInterfacesContext implementsInterfaces() {
			return getRuleContext(ImplementsInterfacesContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public FieldsDefinitionContext fieldsDefinition() {
			return getRuleContext(FieldsDefinitionContext.class,0);
		}
		public InterfaceTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterInterfaceTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitInterfaceTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitInterfaceTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceTypeDefinitionContext interfaceTypeDefinition() throws RecognitionException {
		InterfaceTypeDefinitionContext _localctx = new InterfaceTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_interfaceTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(316);
				description();
				}
			}

			setState(319);
			match(INTERFACE);
			setState(320);
			name();
			setState(322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(321);
				implementsInterfaces(0);
				}
			}

			setState(325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(324);
				directives();
				}
			}

			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(327);
				fieldsDefinition();
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
	public static class InterfaceTypeExtensionDefinitionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode INTERFACE() { return getToken(GraphqlSDLParser.INTERFACE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ExtensionFieldsDefinitionContext extensionFieldsDefinition() {
			return getRuleContext(ExtensionFieldsDefinitionContext.class,0);
		}
		public ImplementsInterfacesContext implementsInterfaces() {
			return getRuleContext(ImplementsInterfacesContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public EmptyParenthesesContext emptyParentheses() {
			return getRuleContext(EmptyParenthesesContext.class,0);
		}
		public InterfaceTypeExtensionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceTypeExtensionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterInterfaceTypeExtensionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitInterfaceTypeExtensionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitInterfaceTypeExtensionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceTypeExtensionDefinitionContext interfaceTypeExtensionDefinition() throws RecognitionException {
		InterfaceTypeExtensionDefinitionContext _localctx = new InterfaceTypeExtensionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_interfaceTypeExtensionDefinition);
		int _la;
		try {
			setState(356);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(330);
				match(EXTEND);
				setState(331);
				match(INTERFACE);
				setState(332);
				name();
				setState(334);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IMPLEMENTS) {
					{
					setState(333);
					implementsInterfaces(0);
					}
				}

				setState(337);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(336);
					directives();
					}
				}

				setState(339);
				extensionFieldsDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(341);
				match(EXTEND);
				setState(342);
				match(INTERFACE);
				setState(343);
				name();
				setState(345);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IMPLEMENTS) {
					{
					setState(344);
					implementsInterfaces(0);
					}
				}

				setState(347);
				directives();
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(348);
					emptyParentheses();
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(351);
				match(EXTEND);
				setState(352);
				match(INTERFACE);
				setState(353);
				name();
				setState(354);
				implementsInterfaces(0);
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
	public static class UnionTypeDefinitionContext extends ParserRuleContext {
		public TerminalNode UNION() { return getToken(GraphqlSDLParser.UNION, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public UnionMembershipContext unionMembership() {
			return getRuleContext(UnionMembershipContext.class,0);
		}
		public UnionTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterUnionTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitUnionTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitUnionTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionTypeDefinitionContext unionTypeDefinition() throws RecognitionException {
		UnionTypeDefinitionContext _localctx = new UnionTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_unionTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(358);
				description();
				}
			}

			setState(361);
			match(UNION);
			setState(362);
			name();
			setState(364);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(363);
				directives();
				}
			}

			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(366);
				unionMembership();
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
	public static class UnionTypeExtensionDefinitionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode UNION() { return getToken(GraphqlSDLParser.UNION, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public UnionMembershipContext unionMembership() {
			return getRuleContext(UnionMembershipContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public UnionTypeExtensionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionTypeExtensionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterUnionTypeExtensionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitUnionTypeExtensionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitUnionTypeExtensionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionTypeExtensionDefinitionContext unionTypeExtensionDefinition() throws RecognitionException {
		UnionTypeExtensionDefinitionContext _localctx = new UnionTypeExtensionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_unionTypeExtensionDefinition);
		int _la;
		try {
			setState(382);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(369);
				match(EXTEND);
				setState(370);
				match(UNION);
				setState(371);
				name();
				setState(373);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(372);
					directives();
					}
				}

				setState(375);
				unionMembership();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(377);
				match(EXTEND);
				setState(378);
				match(UNION);
				setState(379);
				name();
				setState(380);
				directives();
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
	public static class UnionMembershipContext extends ParserRuleContext {
		public UnionMembersContext unionMembers() {
			return getRuleContext(UnionMembersContext.class,0);
		}
		public UnionMembershipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionMembership; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterUnionMembership(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitUnionMembership(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitUnionMembership(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionMembershipContext unionMembership() throws RecognitionException {
		UnionMembershipContext _localctx = new UnionMembershipContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_unionMembership);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			match(T__6);
			setState(385);
			unionMembers(0);
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
	public static class UnionMembersContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public UnionMembersContext unionMembers() {
			return getRuleContext(UnionMembersContext.class,0);
		}
		public UnionMembersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionMembers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterUnionMembers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitUnionMembers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitUnionMembers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionMembersContext unionMembers() throws RecognitionException {
		return unionMembers(0);
	}

	private UnionMembersContext unionMembers(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		UnionMembersContext _localctx = new UnionMembersContext(_ctx, _parentState);
		UnionMembersContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_unionMembers, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(389);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(388);
				match(T__7);
				}
			}

			setState(391);
			typeName();
			}
			_ctx.stop = _input.LT(-1);
			setState(398);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new UnionMembersContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_unionMembers);
					setState(393);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(394);
					match(T__7);
					setState(395);
					typeName();
					}
					} 
				}
				setState(400);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumTypeDefinitionContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(GraphqlSDLParser.ENUM, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public EnumValueDefinitionsContext enumValueDefinitions() {
			return getRuleContext(EnumValueDefinitionsContext.class,0);
		}
		public EnumTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEnumTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEnumTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEnumTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumTypeDefinitionContext enumTypeDefinition() throws RecognitionException {
		EnumTypeDefinitionContext _localctx = new EnumTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_enumTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(401);
				description();
				}
			}

			setState(404);
			match(ENUM);
			setState(405);
			name();
			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(406);
				directives();
				}
			}

			setState(410);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(409);
				enumValueDefinitions();
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
	public static class EnumTypeExtensionDefinitionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode ENUM() { return getToken(GraphqlSDLParser.ENUM, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ExtensionEnumValueDefinitionsContext extensionEnumValueDefinitions() {
			return getRuleContext(ExtensionEnumValueDefinitionsContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public EmptyParenthesesContext emptyParentheses() {
			return getRuleContext(EmptyParenthesesContext.class,0);
		}
		public EnumTypeExtensionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumTypeExtensionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEnumTypeExtensionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEnumTypeExtensionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEnumTypeExtensionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumTypeExtensionDefinitionContext enumTypeExtensionDefinition() throws RecognitionException {
		EnumTypeExtensionDefinitionContext _localctx = new EnumTypeExtensionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_enumTypeExtensionDefinition);
		int _la;
		try {
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(412);
				match(EXTEND);
				setState(413);
				match(ENUM);
				setState(414);
				name();
				setState(416);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(415);
					directives();
					}
				}

				setState(418);
				extensionEnumValueDefinitions();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(420);
				match(EXTEND);
				setState(421);
				match(ENUM);
				setState(422);
				name();
				setState(423);
				directives();
				setState(425);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(424);
					emptyParentheses();
					}
				}

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
	public static class EnumValueDefinitionsContext extends ParserRuleContext {
		public List<EnumValueDefinitionContext> enumValueDefinition() {
			return getRuleContexts(EnumValueDefinitionContext.class);
		}
		public EnumValueDefinitionContext enumValueDefinition(int i) {
			return getRuleContext(EnumValueDefinitionContext.class,i);
		}
		public EnumValueDefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValueDefinitions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEnumValueDefinitions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEnumValueDefinitions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEnumValueDefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueDefinitionsContext enumValueDefinitions() throws RecognitionException {
		EnumValueDefinitionsContext _localctx = new EnumValueDefinitionsContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_enumValueDefinitions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			match(T__0);
			setState(433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 42949607424L) != 0)) {
				{
				{
				setState(430);
				enumValueDefinition();
				}
				}
				setState(435);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(436);
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
	public static class ExtensionEnumValueDefinitionsContext extends ParserRuleContext {
		public List<EnumValueDefinitionContext> enumValueDefinition() {
			return getRuleContexts(EnumValueDefinitionContext.class);
		}
		public EnumValueDefinitionContext enumValueDefinition(int i) {
			return getRuleContext(EnumValueDefinitionContext.class,i);
		}
		public ExtensionEnumValueDefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extensionEnumValueDefinitions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterExtensionEnumValueDefinitions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitExtensionEnumValueDefinitions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitExtensionEnumValueDefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtensionEnumValueDefinitionsContext extensionEnumValueDefinitions() throws RecognitionException {
		ExtensionEnumValueDefinitionsContext _localctx = new ExtensionEnumValueDefinitionsContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_extensionEnumValueDefinitions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			match(T__0);
			setState(440); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(439);
				enumValueDefinition();
				}
				}
				setState(442); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 42949607424L) != 0) );
			setState(444);
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
	public static class EnumValueDefinitionContext extends ParserRuleContext {
		public EnumValueContext enumValue() {
			return getRuleContext(EnumValueContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public EnumValueDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValueDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEnumValueDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEnumValueDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEnumValueDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueDefinitionContext enumValueDefinition() throws RecognitionException {
		EnumValueDefinitionContext _localctx = new EnumValueDefinitionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_enumValueDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(446);
				description();
				}
			}

			setState(449);
			enumValue();
			setState(451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(450);
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
	public static class InputObjectTypeDefinitionContext extends ParserRuleContext {
		public TerminalNode INPUT() { return getToken(GraphqlSDLParser.INPUT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public InputObjectValueDefinitionsContext inputObjectValueDefinitions() {
			return getRuleContext(InputObjectValueDefinitionsContext.class,0);
		}
		public InputObjectTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputObjectTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterInputObjectTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitInputObjectTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitInputObjectTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputObjectTypeDefinitionContext inputObjectTypeDefinition() throws RecognitionException {
		InputObjectTypeDefinitionContext _localctx = new InputObjectTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_inputObjectTypeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(453);
				description();
				}
			}

			setState(456);
			match(INPUT);
			setState(457);
			name();
			setState(459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(458);
				directives();
				}
			}

			setState(462);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(461);
				inputObjectValueDefinitions();
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
	public static class InputObjectTypeExtensionDefinitionContext extends ParserRuleContext {
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode INPUT() { return getToken(GraphqlSDLParser.INPUT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ExtensionInputObjectValueDefinitionsContext extensionInputObjectValueDefinitions() {
			return getRuleContext(ExtensionInputObjectValueDefinitionsContext.class,0);
		}
		public DirectivesContext directives() {
			return getRuleContext(DirectivesContext.class,0);
		}
		public EmptyParenthesesContext emptyParentheses() {
			return getRuleContext(EmptyParenthesesContext.class,0);
		}
		public InputObjectTypeExtensionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputObjectTypeExtensionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterInputObjectTypeExtensionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitInputObjectTypeExtensionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitInputObjectTypeExtensionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputObjectTypeExtensionDefinitionContext inputObjectTypeExtensionDefinition() throws RecognitionException {
		InputObjectTypeExtensionDefinitionContext _localctx = new InputObjectTypeExtensionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_inputObjectTypeExtensionDefinition);
		int _la;
		try {
			setState(479);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(464);
				match(EXTEND);
				setState(465);
				match(INPUT);
				setState(466);
				name();
				setState(468);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(467);
					directives();
					}
				}

				setState(470);
				extensionInputObjectValueDefinitions();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(472);
				match(EXTEND);
				setState(473);
				match(INPUT);
				setState(474);
				name();
				setState(475);
				directives();
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(476);
					emptyParentheses();
					}
				}

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
	public static class InputObjectValueDefinitionsContext extends ParserRuleContext {
		public List<InputValueDefinitionContext> inputValueDefinition() {
			return getRuleContexts(InputValueDefinitionContext.class);
		}
		public InputValueDefinitionContext inputValueDefinition(int i) {
			return getRuleContext(InputValueDefinitionContext.class,i);
		}
		public InputObjectValueDefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputObjectValueDefinitions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterInputObjectValueDefinitions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitInputObjectValueDefinitions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitInputObjectValueDefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputObjectValueDefinitionsContext inputObjectValueDefinitions() throws RecognitionException {
		InputObjectValueDefinitionsContext _localctx = new InputObjectValueDefinitionsContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_inputObjectValueDefinitions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			match(T__0);
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 42949656576L) != 0)) {
				{
				{
				setState(482);
				inputValueDefinition();
				}
				}
				setState(487);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(488);
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
	public static class ExtensionInputObjectValueDefinitionsContext extends ParserRuleContext {
		public List<InputValueDefinitionContext> inputValueDefinition() {
			return getRuleContexts(InputValueDefinitionContext.class);
		}
		public InputValueDefinitionContext inputValueDefinition(int i) {
			return getRuleContext(InputValueDefinitionContext.class,i);
		}
		public ExtensionInputObjectValueDefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extensionInputObjectValueDefinitions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterExtensionInputObjectValueDefinitions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitExtensionInputObjectValueDefinitions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitExtensionInputObjectValueDefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtensionInputObjectValueDefinitionsContext extensionInputObjectValueDefinitions() throws RecognitionException {
		ExtensionInputObjectValueDefinitionsContext _localctx = new ExtensionInputObjectValueDefinitionsContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_extensionInputObjectValueDefinitions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			match(T__0);
			setState(492); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(491);
				inputValueDefinition();
				}
				}
				setState(494); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 42949656576L) != 0) );
			setState(496);
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
	public static class DirectiveDefinitionContext extends ParserRuleContext {
		public TerminalNode DIRECTIVE() { return getToken(GraphqlSDLParser.DIRECTIVE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode ON_KEYWORD() { return getToken(GraphqlSDLParser.ON_KEYWORD, 0); }
		public DirectiveLocationsContext directiveLocations() {
			return getRuleContext(DirectiveLocationsContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ArgumentsDefinitionContext argumentsDefinition() {
			return getRuleContext(ArgumentsDefinitionContext.class,0);
		}
		public TerminalNode REPEATABLE() { return getToken(GraphqlSDLParser.REPEATABLE, 0); }
		public DirectiveDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directiveDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDirectiveDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDirectiveDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDirectiveDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveDefinitionContext directiveDefinition() throws RecognitionException {
		DirectiveDefinitionContext _localctx = new DirectiveDefinitionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_directiveDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(499);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringValue) {
				{
				setState(498);
				description();
				}
			}

			setState(501);
			match(DIRECTIVE);
			setState(502);
			match(T__8);
			setState(503);
			name();
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(504);
				argumentsDefinition();
				}
			}

			setState(508);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REPEATABLE) {
				{
				setState(507);
				match(REPEATABLE);
				}
			}

			setState(510);
			match(ON_KEYWORD);
			setState(511);
			directiveLocations(0);
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
	public static class DirectiveLocationContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DirectiveLocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directiveLocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDirectiveLocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDirectiveLocation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDirectiveLocation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveLocationContext directiveLocation() throws RecognitionException {
		DirectiveLocationContext _localctx = new DirectiveLocationContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_directiveLocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
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
	public static class DirectiveLocationsContext extends ParserRuleContext {
		public DirectiveLocationContext directiveLocation() {
			return getRuleContext(DirectiveLocationContext.class,0);
		}
		public DirectiveLocationsContext directiveLocations() {
			return getRuleContext(DirectiveLocationsContext.class,0);
		}
		public DirectiveLocationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directiveLocations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDirectiveLocations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDirectiveLocations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDirectiveLocations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveLocationsContext directiveLocations() throws RecognitionException {
		return directiveLocations(0);
	}

	private DirectiveLocationsContext directiveLocations(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		DirectiveLocationsContext _localctx = new DirectiveLocationsContext(_ctx, _parentState);
		DirectiveLocationsContext _prevctx = _localctx;
		int _startState = 70;
		enterRecursionRule(_localctx, 70, RULE_directiveLocations, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(516);
			directiveLocation();
			}
			_ctx.stop = _input.LT(-1);
			setState(523);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DirectiveLocationsContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_directiveLocations);
					setState(518);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(519);
					match(T__7);
					setState(520);
					directiveLocation();
					}
					} 
				}
				setState(525);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperationTypeContext extends ParserRuleContext {
		public TerminalNode SUBSCRIPTION() { return getToken(GraphqlSDLParser.SUBSCRIPTION, 0); }
		public TerminalNode MUTATION() { return getToken(GraphqlSDLParser.MUTATION, 0); }
		public TerminalNode QUERY() { return getToken(GraphqlSDLParser.QUERY, 0); }
		public OperationTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterOperationType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitOperationType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitOperationType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationTypeContext operationType() throws RecognitionException {
		OperationTypeContext _localctx = new OperationTypeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_operationType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) ) {
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
		public TerminalNode StringValue() { return getToken(GraphqlSDLParser.StringValue, 0); }
		public DescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_description; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDescription(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDescription(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionContext description() throws RecognitionException {
		DescriptionContext _localctx = new DescriptionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEnumValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEnumValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEnumValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueContext enumValue() throws RecognitionException {
		EnumValueContext _localctx = new EnumValueContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_enumValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(530);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterArrayValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitArrayValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitArrayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueContext arrayValue() throws RecognitionException {
		ArrayValueContext _localctx = new ArrayValueContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_arrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(532);
			match(T__9);
			setState(536);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 68719461378L) != 0)) {
				{
				{
				setState(533);
				value();
				}
				}
				setState(538);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(539);
			match(T__10);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterArrayValueWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitArrayValueWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitArrayValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueWithVariableContext arrayValueWithVariable() throws RecognitionException {
		ArrayValueWithVariableContext _localctx = new ArrayValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_arrayValueWithVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541);
			match(T__9);
			setState(545);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 68719465474L) != 0)) {
				{
				{
				setState(542);
				valueWithVariable();
				}
				}
				setState(547);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(548);
			match(T__10);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterObjectValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitObjectValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitObjectValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectValueContext objectValue() throws RecognitionException {
		ObjectValueContext _localctx = new ObjectValueContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_objectValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(550);
			match(T__0);
			setState(554);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8589918208L) != 0)) {
				{
				{
				setState(551);
				objectField();
				}
				}
				setState(556);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(557);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterObjectValueWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitObjectValueWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitObjectValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectValueWithVariableContext objectValueWithVariable() throws RecognitionException {
		ObjectValueWithVariableContext _localctx = new ObjectValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_objectValueWithVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			match(T__0);
			setState(563);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8589918208L) != 0)) {
				{
				{
				setState(560);
				objectFieldWithVariable();
				}
				}
				setState(565);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(566);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterObjectField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitObjectField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitObjectField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectFieldContext objectField() throws RecognitionException {
		ObjectFieldContext _localctx = new ObjectFieldContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_objectField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			name();
			setState(569);
			match(T__2);
			setState(570);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterObjectFieldWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitObjectFieldWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitObjectFieldWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectFieldWithVariableContext objectFieldWithVariable() throws RecognitionException {
		ObjectFieldWithVariableContext _localctx = new ObjectFieldWithVariableContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_objectFieldWithVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(572);
			name();
			setState(573);
			match(T__2);
			setState(574);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDirectives(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDirectives(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDirectives(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectivesContext directives() throws RecognitionException {
		DirectivesContext _localctx = new DirectivesContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_directives);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(577); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(576);
					directive();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(579); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,77,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveContext directive() throws RecognitionException {
		DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_directive);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(581);
			match(T__8);
			setState(582);
			name();
			setState(584);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(583);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586);
			match(T__4);
			setState(588); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(587);
				argument();
				}
				}
				setState(590); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 8589918208L) != 0) );
			setState(592);
			match(T__5);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
			name();
			setState(595);
			match(T__2);
			setState(596);
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
		public TerminalNode NAME() { return getToken(GraphqlSDLParser.NAME, 0); }
		public TerminalNode FRAGMENT() { return getToken(GraphqlSDLParser.FRAGMENT, 0); }
		public TerminalNode QUERY() { return getToken(GraphqlSDLParser.QUERY, 0); }
		public TerminalNode MUTATION() { return getToken(GraphqlSDLParser.MUTATION, 0); }
		public TerminalNode SUBSCRIPTION() { return getToken(GraphqlSDLParser.SUBSCRIPTION, 0); }
		public TerminalNode SCHEMA() { return getToken(GraphqlSDLParser.SCHEMA, 0); }
		public TerminalNode SCALAR() { return getToken(GraphqlSDLParser.SCALAR, 0); }
		public TerminalNode TYPE() { return getToken(GraphqlSDLParser.TYPE, 0); }
		public TerminalNode INTERFACE() { return getToken(GraphqlSDLParser.INTERFACE, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(GraphqlSDLParser.IMPLEMENTS, 0); }
		public TerminalNode ENUM() { return getToken(GraphqlSDLParser.ENUM, 0); }
		public TerminalNode UNION() { return getToken(GraphqlSDLParser.UNION, 0); }
		public TerminalNode INPUT() { return getToken(GraphqlSDLParser.INPUT, 0); }
		public TerminalNode EXTEND() { return getToken(GraphqlSDLParser.EXTEND, 0); }
		public TerminalNode DIRECTIVE() { return getToken(GraphqlSDLParser.DIRECTIVE, 0); }
		public TerminalNode REPEATABLE() { return getToken(GraphqlSDLParser.REPEATABLE, 0); }
		public BaseNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterBaseName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitBaseName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitBaseName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseNameContext baseName() throws RecognitionException {
		BaseNameContext _localctx = new BaseNameContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_baseName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7516127232L) != 0)) ) {
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
		public TerminalNode BooleanValue() { return getToken(GraphqlSDLParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlSDLParser.NullValue, 0); }
		public FragmentNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fragmentName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterFragmentName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitFragmentName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitFragmentName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FragmentNameContext fragmentName() throws RecognitionException {
		FragmentNameContext _localctx = new FragmentNameContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_fragmentName);
		try {
			setState(603);
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
				setState(600);
				baseName();
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(601);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(602);
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
		public TerminalNode ON_KEYWORD() { return getToken(GraphqlSDLParser.ON_KEYWORD, 0); }
		public EnumValueNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValueName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterEnumValueName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitEnumValueName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitEnumValueName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueNameContext enumValueName() throws RecognitionException {
		EnumValueNameContext _localctx = new EnumValueNameContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_enumValueName);
		try {
			setState(607);
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
				setState(605);
				baseName();
				}
				break;
			case ON_KEYWORD:
				enterOuterAlt(_localctx, 2);
				{
				setState(606);
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
		public TerminalNode BooleanValue() { return getToken(GraphqlSDLParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlSDLParser.NullValue, 0); }
		public TerminalNode ON_KEYWORD() { return getToken(GraphqlSDLParser.ON_KEYWORD, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_name);
		try {
			setState(613);
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
				setState(609);
				baseName();
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(610);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(611);
				match(NullValue);
				}
				break;
			case ON_KEYWORD:
				enterOuterAlt(_localctx, 4);
				{
				setState(612);
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
		public TerminalNode StringValue() { return getToken(GraphqlSDLParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(GraphqlSDLParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(GraphqlSDLParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(GraphqlSDLParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlSDLParser.NullValue, 0); }
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_value);
		try {
			setState(623);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case StringValue:
				enterOuterAlt(_localctx, 1);
				{
				setState(615);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(616);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(617);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(618);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(619);
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
				setState(620);
				enumValue();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 7);
				{
				setState(621);
				arrayValue();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 8);
				{
				setState(622);
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
		public TerminalNode StringValue() { return getToken(GraphqlSDLParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(GraphqlSDLParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(GraphqlSDLParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(GraphqlSDLParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(GraphqlSDLParser.NullValue, 0); }
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterValueWithVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitValueWithVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueWithVariableContext valueWithVariable() throws RecognitionException {
		ValueWithVariableContext _localctx = new ValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_valueWithVariable);
		try {
			setState(634);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__11:
				enterOuterAlt(_localctx, 1);
				{
				setState(625);
				variable();
				}
				break;
			case StringValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(626);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(627);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(628);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(629);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 6);
				{
				setState(630);
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
				setState(631);
				enumValue();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 8);
				{
				setState(632);
				arrayValueWithVariable();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 9);
				{
				setState(633);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(636);
			match(T__11);
			setState(637);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterDefaultValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitDefaultValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitDefaultValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultValueContext defaultValue() throws RecognitionException {
		DefaultValueContext _localctx = new DefaultValueContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_defaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			match(T__6);
			setState(640);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_type);
		try {
			setState(645);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(642);
				typeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(643);
				listType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(644);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterListType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitListType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitListType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListTypeContext listType() throws RecognitionException {
		ListTypeContext _localctx = new ListTypeContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_listType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(649);
			match(T__9);
			setState(650);
			type();
			setState(651);
			match(T__10);
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
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).enterNonNullType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GraphqlSDLListener ) ((GraphqlSDLListener)listener).exitNonNullType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GraphqlSDLVisitor ) return ((GraphqlSDLVisitor<? extends T>)visitor).visitNonNullType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonNullTypeContext nonNullType() throws RecognitionException {
		NonNullTypeContext _localctx = new NonNullTypeContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_nonNullType);
		try {
			setState(659);
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
				setState(653);
				typeName();
				setState(654);
				match(T__12);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(656);
				listType();
				setState(657);
				match(T__12);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return implementsInterfaces_sempred((ImplementsInterfacesContext)_localctx, predIndex);
		case 23:
			return unionMembers_sempred((UnionMembersContext)_localctx, predIndex);
		case 35:
			return directiveLocations_sempred((DirectiveLocationsContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean implementsInterfaces_sempred(ImplementsInterfacesContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean unionMembers_sempred(UnionMembersContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean directiveLocations_sempred(DirectiveLocationsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001+\u0296\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
		"2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"+
		"7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0002"+
		"<\u0007<\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000~\b\u0000\u0001"+
		"\u0001\u0001\u0001\u0003\u0001\u0082\b\u0001\u0001\u0002\u0003\u0002\u0085"+
		"\b\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u0089\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0004\u0002\u008d\b\u0002\u000b\u0002\f\u0002\u008e\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003\u0096"+
		"\b\u0003\u0001\u0003\u0001\u0003\u0004\u0003\u009a\b\u0003\u000b\u0003"+
		"\f\u0003\u009b\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0004\u0003\u00a3\b\u0003\u000b\u0003\f\u0003\u00a4\u0003\u0003\u00a7"+
		"\b\u0003\u0001\u0004\u0003\u0004\u00aa\b\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0003\u0005\u00b6\b\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u00be\b\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0003\b\u00c4\b\b\u0001\b"+
		"\u0001\b\u0001\b\u0003\b\u00c9\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\n\u0003\n\u00d1\b\n\u0001\n\u0001\n\u0001\n\u0003\n\u00d6\b\n"+
		"\u0001\n\u0003\n\u00d9\b\n\u0001\n\u0003\n\u00dc\b\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00e2\b\u000b\u0001\u000b\u0003"+
		"\u000b\u00e5\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u00ed\b\u000b\u0001\u000b\u0001\u000b\u0003"+
		"\u000b\u00f1\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u00f8\b\u000b\u0001\f\u0001\f\u0001\f\u0003\f\u00fd"+
		"\b\f\u0001\f\u0004\f\u0100\b\f\u000b\f\f\f\u0101\u0001\f\u0001\f\u0001"+
		"\f\u0005\f\u0107\b\f\n\f\f\f\u010a\t\f\u0001\r\u0001\r\u0005\r\u010e\b"+
		"\r\n\r\f\r\u0111\t\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0004\u000e"+
		"\u0117\b\u000e\u000b\u000e\f\u000e\u0118\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0003\u000f\u011e\b\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0122"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0127\b\u000f"+
		"\u0001\u0010\u0001\u0010\u0004\u0010\u012b\b\u0010\u000b\u0010\f\u0010"+
		"\u012c\u0001\u0010\u0001\u0010\u0001\u0011\u0003\u0011\u0132\b\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0138\b\u0011\u0001"+
		"\u0011\u0003\u0011\u013b\b\u0011\u0001\u0012\u0003\u0012\u013e\b\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0143\b\u0012\u0001\u0012"+
		"\u0003\u0012\u0146\b\u0012\u0001\u0012\u0003\u0012\u0149\b\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u014f\b\u0013\u0001"+
		"\u0013\u0003\u0013\u0152\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u015a\b\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u015e\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0003\u0013\u0165\b\u0013\u0001\u0014\u0003\u0014\u0168"+
		"\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u016d\b\u0014"+
		"\u0001\u0014\u0003\u0014\u0170\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0003\u0015\u0176\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u017f\b\u0015"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0003\u0017"+
		"\u0186\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0005\u0017\u018d\b\u0017\n\u0017\f\u0017\u0190\t\u0017\u0001\u0018\u0003"+
		"\u0018\u0193\b\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0198"+
		"\b\u0018\u0001\u0018\u0003\u0018\u019b\b\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0003\u0019\u01a1\b\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019"+
		"\u01aa\b\u0019\u0003\u0019\u01ac\b\u0019\u0001\u001a\u0001\u001a\u0005"+
		"\u001a\u01b0\b\u001a\n\u001a\f\u001a\u01b3\t\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001b\u0001\u001b\u0004\u001b\u01b9\b\u001b\u000b\u001b\f\u001b"+
		"\u01ba\u0001\u001b\u0001\u001b\u0001\u001c\u0003\u001c\u01c0\b\u001c\u0001"+
		"\u001c\u0001\u001c\u0003\u001c\u01c4\b\u001c\u0001\u001d\u0003\u001d\u01c7"+
		"\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u01cc\b\u001d"+
		"\u0001\u001d\u0003\u001d\u01cf\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0003\u001e\u01d5\b\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u01de\b\u001e"+
		"\u0003\u001e\u01e0\b\u001e\u0001\u001f\u0001\u001f\u0005\u001f\u01e4\b"+
		"\u001f\n\u001f\f\u001f\u01e7\t\u001f\u0001\u001f\u0001\u001f\u0001 \u0001"+
		" \u0004 \u01ed\b \u000b \f \u01ee\u0001 \u0001 \u0001!\u0003!\u01f4\b"+
		"!\u0001!\u0001!\u0001!\u0001!\u0003!\u01fa\b!\u0001!\u0003!\u01fd\b!\u0001"+
		"!\u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0005#\u020a\b#\n#\f#\u020d\t#\u0001$\u0001$\u0001%\u0001%\u0001&\u0001"+
		"&\u0001\'\u0001\'\u0005\'\u0217\b\'\n\'\f\'\u021a\t\'\u0001\'\u0001\'"+
		"\u0001(\u0001(\u0005(\u0220\b(\n(\f(\u0223\t(\u0001(\u0001(\u0001)\u0001"+
		")\u0005)\u0229\b)\n)\f)\u022c\t)\u0001)\u0001)\u0001*\u0001*\u0005*\u0232"+
		"\b*\n*\f*\u0235\t*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001,\u0001"+
		",\u0001,\u0001,\u0001-\u0004-\u0242\b-\u000b-\f-\u0243\u0001.\u0001.\u0001"+
		".\u0003.\u0249\b.\u0001/\u0001/\u0004/\u024d\b/\u000b/\f/\u024e\u0001"+
		"/\u0001/\u00010\u00010\u00010\u00010\u00011\u00011\u00012\u00012\u0001"+
		"2\u00032\u025c\b2\u00013\u00013\u00033\u0260\b3\u00014\u00014\u00014\u0001"+
		"4\u00034\u0266\b4\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00035\u0270\b5\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u0001"+
		"6\u00016\u00036\u027b\b6\u00017\u00017\u00017\u00018\u00018\u00018\u0001"+
		"9\u00019\u00019\u00039\u0286\b9\u0001:\u0001:\u0001;\u0001;\u0001;\u0001"+
		";\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0003<\u0294\b<\u0001<\u0000"+
		"\u0003\u0018.F=\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfh"+
		"jlnprtvx\u0000\u0002\u0001\u0000\u0011\u0013\u0002\u0000\u0010\u001d\u001f"+
		" \u02cb\u0000}\u0001\u0000\u0000\u0000\u0002\u0081\u0001\u0000\u0000\u0000"+
		"\u0004\u0084\u0001\u0000\u0000\u0000\u0006\u00a6\u0001\u0000\u0000\u0000"+
		"\b\u00a9\u0001\u0000\u0000\u0000\n\u00b5\u0001\u0000\u0000\u0000\f\u00bd"+
		"\u0001\u0000\u0000\u0000\u000e\u00bf\u0001\u0000\u0000\u0000\u0010\u00c3"+
		"\u0001\u0000\u0000\u0000\u0012\u00ca\u0001\u0000\u0000\u0000\u0014\u00d0"+
		"\u0001\u0000\u0000\u0000\u0016\u00f7\u0001\u0000\u0000\u0000\u0018\u00f9"+
		"\u0001\u0000\u0000\u0000\u001a\u010b\u0001\u0000\u0000\u0000\u001c\u0114"+
		"\u0001\u0000\u0000\u0000\u001e\u011d\u0001\u0000\u0000\u0000 \u0128\u0001"+
		"\u0000\u0000\u0000\"\u0131\u0001\u0000\u0000\u0000$\u013d\u0001\u0000"+
		"\u0000\u0000&\u0164\u0001\u0000\u0000\u0000(\u0167\u0001\u0000\u0000\u0000"+
		"*\u017e\u0001\u0000\u0000\u0000,\u0180\u0001\u0000\u0000\u0000.\u0183"+
		"\u0001\u0000\u0000\u00000\u0192\u0001\u0000\u0000\u00002\u01ab\u0001\u0000"+
		"\u0000\u00004\u01ad\u0001\u0000\u0000\u00006\u01b6\u0001\u0000\u0000\u0000"+
		"8\u01bf\u0001\u0000\u0000\u0000:\u01c6\u0001\u0000\u0000\u0000<\u01df"+
		"\u0001\u0000\u0000\u0000>\u01e1\u0001\u0000\u0000\u0000@\u01ea\u0001\u0000"+
		"\u0000\u0000B\u01f3\u0001\u0000\u0000\u0000D\u0201\u0001\u0000\u0000\u0000"+
		"F\u0203\u0001\u0000\u0000\u0000H\u020e\u0001\u0000\u0000\u0000J\u0210"+
		"\u0001\u0000\u0000\u0000L\u0212\u0001\u0000\u0000\u0000N\u0214\u0001\u0000"+
		"\u0000\u0000P\u021d\u0001\u0000\u0000\u0000R\u0226\u0001\u0000\u0000\u0000"+
		"T\u022f\u0001\u0000\u0000\u0000V\u0238\u0001\u0000\u0000\u0000X\u023c"+
		"\u0001\u0000\u0000\u0000Z\u0241\u0001\u0000\u0000\u0000\\\u0245\u0001"+
		"\u0000\u0000\u0000^\u024a\u0001\u0000\u0000\u0000`\u0252\u0001\u0000\u0000"+
		"\u0000b\u0256\u0001\u0000\u0000\u0000d\u025b\u0001\u0000\u0000\u0000f"+
		"\u025f\u0001\u0000\u0000\u0000h\u0265\u0001\u0000\u0000\u0000j\u026f\u0001"+
		"\u0000\u0000\u0000l\u027a\u0001\u0000\u0000\u0000n\u027c\u0001\u0000\u0000"+
		"\u0000p\u027f\u0001\u0000\u0000\u0000r\u0285\u0001\u0000\u0000\u0000t"+
		"\u0287\u0001\u0000\u0000\u0000v\u0289\u0001\u0000\u0000\u0000x\u0293\u0001"+
		"\u0000\u0000\u0000z~\u0003\u0004\u0002\u0000{~\u0003\n\u0005\u0000|~\u0003"+
		"B!\u0000}z\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000}|\u0001\u0000"+
		"\u0000\u0000~\u0001\u0001\u0000\u0000\u0000\u007f\u0082\u0003\u0006\u0003"+
		"\u0000\u0080\u0082\u0003\f\u0006\u0000\u0081\u007f\u0001\u0000\u0000\u0000"+
		"\u0081\u0080\u0001\u0000\u0000\u0000\u0082\u0003\u0001\u0000\u0000\u0000"+
		"\u0083\u0085\u0003J%\u0000\u0084\u0083\u0001\u0000\u0000\u0000\u0084\u0085"+
		"\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0088"+
		"\u0005\u0014\u0000\u0000\u0087\u0089\u0003Z-\u0000\u0088\u0087\u0001\u0000"+
		"\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000"+
		"\u0000\u0000\u008a\u008c\u0005\u0001\u0000\u0000\u008b\u008d\u0003\b\u0004"+
		"\u0000\u008c\u008b\u0001\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000"+
		"\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0091\u0005\u0002\u0000"+
		"\u0000\u0091\u0005\u0001\u0000\u0000\u0000\u0092\u0093\u0005\u001c\u0000"+
		"\u0000\u0093\u0095\u0005\u0014\u0000\u0000\u0094\u0096\u0003Z-\u0000\u0095"+
		"\u0094\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096"+
		"\u0097\u0001\u0000\u0000\u0000\u0097\u0099\u0005\u0001\u0000\u0000\u0098"+
		"\u009a\u0003\b\u0004\u0000\u0099\u0098\u0001\u0000\u0000\u0000\u009a\u009b"+
		"\u0001\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009b\u009c"+
		"\u0001\u0000\u0000\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u009e"+
		"\u0005\u0002\u0000\u0000\u009e\u00a7\u0001\u0000\u0000\u0000\u009f\u00a0"+
		"\u0005\u001c\u0000\u0000\u00a0\u00a2\u0005\u0014\u0000\u0000\u00a1\u00a3"+
		"\u0003Z-\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000"+
		"\u0000\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a4\u00a5\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a7\u0001\u0000\u0000\u0000\u00a6\u0092\u0001\u0000"+
		"\u0000\u0000\u00a6\u009f\u0001\u0000\u0000\u0000\u00a7\u0007\u0001\u0000"+
		"\u0000\u0000\u00a8\u00aa\u0003J%\u0000\u00a9\u00a8\u0001\u0000\u0000\u0000"+
		"\u00a9\u00aa\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000"+
		"\u00ab\u00ac\u0003H$\u0000\u00ac\u00ad\u0005\u0003\u0000\u0000\u00ad\u00ae"+
		"\u0003t:\u0000\u00ae\t\u0001\u0000\u0000\u0000\u00af\u00b6\u0003\u0010"+
		"\b\u0000\u00b0\u00b6\u0003\u0014\n\u0000\u00b1\u00b6\u0003$\u0012\u0000"+
		"\u00b2\u00b6\u0003(\u0014\u0000\u00b3\u00b6\u00030\u0018\u0000\u00b4\u00b6"+
		"\u0003:\u001d\u0000\u00b5\u00af\u0001\u0000\u0000\u0000\u00b5\u00b0\u0001"+
		"\u0000\u0000\u0000\u00b5\u00b1\u0001\u0000\u0000\u0000\u00b5\u00b2\u0001"+
		"\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b5\u00b4\u0001"+
		"\u0000\u0000\u0000\u00b6\u000b\u0001\u0000\u0000\u0000\u00b7\u00be\u0003"+
		"\u0016\u000b\u0000\u00b8\u00be\u0003&\u0013\u0000\u00b9\u00be\u0003*\u0015"+
		"\u0000\u00ba\u00be\u0003\u0012\t\u0000\u00bb\u00be\u00032\u0019\u0000"+
		"\u00bc\u00be\u0003<\u001e\u0000\u00bd\u00b7\u0001\u0000\u0000\u0000\u00bd"+
		"\u00b8\u0001\u0000\u0000\u0000\u00bd\u00b9\u0001\u0000\u0000\u0000\u00bd"+
		"\u00ba\u0001\u0000\u0000\u0000\u00bd\u00bb\u0001\u0000\u0000\u0000\u00bd"+
		"\u00bc\u0001\u0000\u0000\u0000\u00be\r\u0001\u0000\u0000\u0000\u00bf\u00c0"+
		"\u0005\u0001\u0000\u0000\u00c0\u00c1\u0005\u0002\u0000\u0000\u00c1\u000f"+
		"\u0001\u0000\u0000\u0000\u00c2\u00c4\u0003J%\u0000\u00c3\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c4\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000"+
		"\u0000\u0000\u00c5\u00c6\u0005\u0015\u0000\u0000\u00c6\u00c8\u0003h4\u0000"+
		"\u00c7\u00c9\u0003Z-\u0000\u00c8\u00c7\u0001\u0000\u0000\u0000\u00c8\u00c9"+
		"\u0001\u0000\u0000\u0000\u00c9\u0011\u0001\u0000\u0000\u0000\u00ca\u00cb"+
		"\u0005\u001c\u0000\u0000\u00cb\u00cc\u0005\u0015\u0000\u0000\u00cc\u00cd"+
		"\u0003h4\u0000\u00cd\u00ce\u0003Z-\u0000\u00ce\u0013\u0001\u0000\u0000"+
		"\u0000\u00cf\u00d1\u0003J%\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d0"+
		"\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0005\u0016\u0000\u0000\u00d3\u00d5\u0003h4\u0000\u00d4\u00d6\u0003"+
		"\u0018\f\u0000\u00d5\u00d4\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d6\u00d8\u0001\u0000\u0000\u0000\u00d7\u00d9\u0003Z-\u0000"+
		"\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000"+
		"\u00d9\u00db\u0001\u0000\u0000\u0000\u00da\u00dc\u0003\u001a\r\u0000\u00db"+
		"\u00da\u0001\u0000\u0000\u0000\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc"+
		"\u0015\u0001\u0000\u0000\u0000\u00dd\u00de\u0005\u001c\u0000\u0000\u00de"+
		"\u00df\u0005\u0016\u0000\u0000\u00df\u00e1\u0003h4\u0000\u00e0\u00e2\u0003"+
		"\u0018\f\u0000\u00e1\u00e0\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000"+
		"\u0000\u0000\u00e2\u00e4\u0001\u0000\u0000\u0000\u00e3\u00e5\u0003Z-\u0000"+
		"\u00e4\u00e3\u0001\u0000\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000\u0000"+
		"\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6\u00e7\u0003\u001c\u000e\u0000"+
		"\u00e7\u00f8\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005\u001c\u0000\u0000"+
		"\u00e9\u00ea\u0005\u0016\u0000\u0000\u00ea\u00ec\u0003h4\u0000\u00eb\u00ed"+
		"\u0003\u0018\f\u0000\u00ec\u00eb\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001"+
		"\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000\u00ee\u00f0\u0003"+
		"Z-\u0000\u00ef\u00f1\u0003\u000e\u0007\u0000\u00f0\u00ef\u0001\u0000\u0000"+
		"\u0000\u00f0\u00f1\u0001\u0000\u0000\u0000\u00f1\u00f8\u0001\u0000\u0000"+
		"\u0000\u00f2\u00f3\u0005\u001c\u0000\u0000\u00f3\u00f4\u0005\u0016\u0000"+
		"\u0000\u00f4\u00f5\u0003h4\u0000\u00f5\u00f6\u0003\u0018\f\u0000\u00f6"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f7\u00dd\u0001\u0000\u0000\u0000\u00f7"+
		"\u00e8\u0001\u0000\u0000\u0000\u00f7\u00f2\u0001\u0000\u0000\u0000\u00f8"+
		"\u0017\u0001\u0000\u0000\u0000\u00f9\u00fa\u0006\f\uffff\uffff\u0000\u00fa"+
		"\u00fc\u0005\u0018\u0000\u0000\u00fb\u00fd\u0005\u0004\u0000\u0000\u00fc"+
		"\u00fb\u0001\u0000\u0000\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000\u00fd"+
		"\u00ff\u0001\u0000\u0000\u0000\u00fe\u0100\u0003t:\u0000\u00ff\u00fe\u0001"+
		"\u0000\u0000\u0000\u0100\u0101\u0001\u0000\u0000\u0000\u0101\u00ff\u0001"+
		"\u0000\u0000\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u0108\u0001"+
		"\u0000\u0000\u0000\u0103\u0104\n\u0001\u0000\u0000\u0104\u0105\u0005\u0004"+
		"\u0000\u0000\u0105\u0107\u0003t:\u0000\u0106\u0103\u0001\u0000\u0000\u0000"+
		"\u0107\u010a\u0001\u0000\u0000\u0000\u0108\u0106\u0001\u0000\u0000\u0000"+
		"\u0108\u0109\u0001\u0000\u0000\u0000\u0109\u0019\u0001\u0000\u0000\u0000"+
		"\u010a\u0108\u0001\u0000\u0000\u0000\u010b\u010f\u0005\u0001\u0000\u0000"+
		"\u010c\u010e\u0003\u001e\u000f\u0000\u010d\u010c\u0001\u0000\u0000\u0000"+
		"\u010e\u0111\u0001\u0000\u0000\u0000\u010f\u010d\u0001\u0000\u0000\u0000"+
		"\u010f\u0110\u0001\u0000\u0000\u0000\u0110\u0112\u0001\u0000\u0000\u0000"+
		"\u0111\u010f\u0001\u0000\u0000\u0000\u0112\u0113\u0005\u0002\u0000\u0000"+
		"\u0113\u001b\u0001\u0000\u0000\u0000\u0114\u0116\u0005\u0001\u0000\u0000"+
		"\u0115\u0117\u0003\u001e\u000f\u0000\u0116\u0115\u0001\u0000\u0000\u0000"+
		"\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000\u0000"+
		"\u0118\u0119\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000\u0000\u0000"+
		"\u011a\u011b\u0005\u0002\u0000\u0000\u011b\u001d\u0001\u0000\u0000\u0000"+
		"\u011c\u011e\u0003J%\u0000\u011d\u011c\u0001\u0000\u0000\u0000\u011d\u011e"+
		"\u0001\u0000\u0000\u0000\u011e\u011f\u0001\u0000\u0000\u0000\u011f\u0121"+
		"\u0003h4\u0000\u0120\u0122\u0003 \u0010\u0000\u0121\u0120\u0001\u0000"+
		"\u0000\u0000\u0121\u0122\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000"+
		"\u0000\u0000\u0123\u0124\u0005\u0003\u0000\u0000\u0124\u0126\u0003r9\u0000"+
		"\u0125\u0127\u0003Z-\u0000\u0126\u0125\u0001\u0000\u0000\u0000\u0126\u0127"+
		"\u0001\u0000\u0000\u0000\u0127\u001f\u0001\u0000\u0000\u0000\u0128\u012a"+
		"\u0005\u0005\u0000\u0000\u0129\u012b\u0003\"\u0011\u0000\u012a\u0129\u0001"+
		"\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000\u0000\u012c\u012a\u0001"+
		"\u0000\u0000\u0000\u012c\u012d\u0001\u0000\u0000\u0000\u012d\u012e\u0001"+
		"\u0000\u0000\u0000\u012e\u012f\u0005\u0006\u0000\u0000\u012f!\u0001\u0000"+
		"\u0000\u0000\u0130\u0132\u0003J%\u0000\u0131\u0130\u0001\u0000\u0000\u0000"+
		"\u0131\u0132\u0001\u0000\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000"+
		"\u0133\u0134\u0003h4\u0000\u0134\u0135\u0005\u0003\u0000\u0000\u0135\u0137"+
		"\u0003r9\u0000\u0136\u0138\u0003p8\u0000\u0137\u0136\u0001\u0000\u0000"+
		"\u0000\u0137\u0138\u0001\u0000\u0000\u0000\u0138\u013a\u0001\u0000\u0000"+
		"\u0000\u0139\u013b\u0003Z-\u0000\u013a\u0139\u0001\u0000\u0000\u0000\u013a"+
		"\u013b\u0001\u0000\u0000\u0000\u013b#\u0001\u0000\u0000\u0000\u013c\u013e"+
		"\u0003J%\u0000\u013d\u013c\u0001\u0000\u0000\u0000\u013d\u013e\u0001\u0000"+
		"\u0000\u0000\u013e\u013f\u0001\u0000\u0000\u0000\u013f\u0140\u0005\u0017"+
		"\u0000\u0000\u0140\u0142\u0003h4\u0000\u0141\u0143\u0003\u0018\f\u0000"+
		"\u0142\u0141\u0001\u0000\u0000\u0000\u0142\u0143\u0001\u0000\u0000\u0000"+
		"\u0143\u0145\u0001\u0000\u0000\u0000\u0144\u0146\u0003Z-\u0000\u0145\u0144"+
		"\u0001\u0000\u0000\u0000\u0145\u0146\u0001\u0000\u0000\u0000\u0146\u0148"+
		"\u0001\u0000\u0000\u0000\u0147\u0149\u0003\u001a\r\u0000\u0148\u0147\u0001"+
		"\u0000\u0000\u0000\u0148\u0149\u0001\u0000\u0000\u0000\u0149%\u0001\u0000"+
		"\u0000\u0000\u014a\u014b\u0005\u001c\u0000\u0000\u014b\u014c\u0005\u0017"+
		"\u0000\u0000\u014c\u014e\u0003h4\u0000\u014d\u014f\u0003\u0018\f\u0000"+
		"\u014e\u014d\u0001\u0000\u0000\u0000\u014e\u014f\u0001\u0000\u0000\u0000"+
		"\u014f\u0151\u0001\u0000\u0000\u0000\u0150\u0152\u0003Z-\u0000\u0151\u0150"+
		"\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0153"+
		"\u0001\u0000\u0000\u0000\u0153\u0154\u0003\u001c\u000e\u0000\u0154\u0165"+
		"\u0001\u0000\u0000\u0000\u0155\u0156\u0005\u001c\u0000\u0000\u0156\u0157"+
		"\u0005\u0017\u0000\u0000\u0157\u0159\u0003h4\u0000\u0158\u015a\u0003\u0018"+
		"\f\u0000\u0159\u0158\u0001\u0000\u0000\u0000\u0159\u015a\u0001\u0000\u0000"+
		"\u0000\u015a\u015b\u0001\u0000\u0000\u0000\u015b\u015d\u0003Z-\u0000\u015c"+
		"\u015e\u0003\u000e\u0007\u0000\u015d\u015c\u0001\u0000\u0000\u0000\u015d"+
		"\u015e\u0001\u0000\u0000\u0000\u015e\u0165\u0001\u0000\u0000\u0000\u015f"+
		"\u0160\u0005\u001c\u0000\u0000\u0160\u0161\u0005\u0017\u0000\u0000\u0161"+
		"\u0162\u0003h4\u0000\u0162\u0163\u0003\u0018\f\u0000\u0163\u0165\u0001"+
		"\u0000\u0000\u0000\u0164\u014a\u0001\u0000\u0000\u0000\u0164\u0155\u0001"+
		"\u0000\u0000\u0000\u0164\u015f\u0001\u0000\u0000\u0000\u0165\'\u0001\u0000"+
		"\u0000\u0000\u0166\u0168\u0003J%\u0000\u0167\u0166\u0001\u0000\u0000\u0000"+
		"\u0167\u0168\u0001\u0000\u0000\u0000\u0168\u0169\u0001\u0000\u0000\u0000"+
		"\u0169\u016a\u0005\u001a\u0000\u0000\u016a\u016c\u0003h4\u0000\u016b\u016d"+
		"\u0003Z-\u0000\u016c\u016b\u0001\u0000\u0000\u0000\u016c\u016d\u0001\u0000"+
		"\u0000\u0000\u016d\u016f\u0001\u0000\u0000\u0000\u016e\u0170\u0003,\u0016"+
		"\u0000\u016f\u016e\u0001\u0000\u0000\u0000\u016f\u0170\u0001\u0000\u0000"+
		"\u0000\u0170)\u0001\u0000\u0000\u0000\u0171\u0172\u0005\u001c\u0000\u0000"+
		"\u0172\u0173\u0005\u001a\u0000\u0000\u0173\u0175\u0003h4\u0000\u0174\u0176"+
		"\u0003Z-\u0000\u0175\u0174\u0001\u0000\u0000\u0000\u0175\u0176\u0001\u0000"+
		"\u0000\u0000\u0176\u0177\u0001\u0000\u0000\u0000\u0177\u0178\u0003,\u0016"+
		"\u0000\u0178\u017f\u0001\u0000\u0000\u0000\u0179\u017a\u0005\u001c\u0000"+
		"\u0000\u017a\u017b\u0005\u001a\u0000\u0000\u017b\u017c\u0003h4\u0000\u017c"+
		"\u017d\u0003Z-\u0000\u017d\u017f\u0001\u0000\u0000\u0000\u017e\u0171\u0001"+
		"\u0000\u0000\u0000\u017e\u0179\u0001\u0000\u0000\u0000\u017f+\u0001\u0000"+
		"\u0000\u0000\u0180\u0181\u0005\u0007\u0000\u0000\u0181\u0182\u0003.\u0017"+
		"\u0000\u0182-\u0001\u0000\u0000\u0000\u0183\u0185\u0006\u0017\uffff\uffff"+
		"\u0000\u0184\u0186\u0005\b\u0000\u0000\u0185\u0184\u0001\u0000\u0000\u0000"+
		"\u0185\u0186\u0001\u0000\u0000\u0000\u0186\u0187\u0001\u0000\u0000\u0000"+
		"\u0187\u0188\u0003t:\u0000\u0188\u018e\u0001\u0000\u0000\u0000\u0189\u018a"+
		"\n\u0001\u0000\u0000\u018a\u018b\u0005\b\u0000\u0000\u018b\u018d\u0003"+
		"t:\u0000\u018c\u0189\u0001\u0000\u0000\u0000\u018d\u0190\u0001\u0000\u0000"+
		"\u0000\u018e\u018c\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000"+
		"\u0000\u018f/\u0001\u0000\u0000\u0000\u0190\u018e\u0001\u0000\u0000\u0000"+
		"\u0191\u0193\u0003J%\u0000\u0192\u0191\u0001\u0000\u0000\u0000\u0192\u0193"+
		"\u0001\u0000\u0000\u0000\u0193\u0194\u0001\u0000\u0000\u0000\u0194\u0195"+
		"\u0005\u0019\u0000\u0000\u0195\u0197\u0003h4\u0000\u0196\u0198\u0003Z"+
		"-\u0000\u0197\u0196\u0001\u0000\u0000\u0000\u0197\u0198\u0001\u0000\u0000"+
		"\u0000\u0198\u019a\u0001\u0000\u0000\u0000\u0199\u019b\u00034\u001a\u0000"+
		"\u019a\u0199\u0001\u0000\u0000\u0000\u019a\u019b\u0001\u0000\u0000\u0000"+
		"\u019b1\u0001\u0000\u0000\u0000\u019c\u019d\u0005\u001c\u0000\u0000\u019d"+
		"\u019e\u0005\u0019\u0000\u0000\u019e\u01a0\u0003h4\u0000\u019f\u01a1\u0003"+
		"Z-\u0000\u01a0\u019f\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000"+
		"\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000\u01a2\u01a3\u00036\u001b\u0000"+
		"\u01a3\u01ac\u0001\u0000\u0000\u0000\u01a4\u01a5\u0005\u001c\u0000\u0000"+
		"\u01a5\u01a6\u0005\u0019\u0000\u0000\u01a6\u01a7\u0003h4\u0000\u01a7\u01a9"+
		"\u0003Z-\u0000\u01a8\u01aa\u0003\u000e\u0007\u0000\u01a9\u01a8\u0001\u0000"+
		"\u0000\u0000\u01a9\u01aa\u0001\u0000\u0000\u0000\u01aa\u01ac\u0001\u0000"+
		"\u0000\u0000\u01ab\u019c\u0001\u0000\u0000\u0000\u01ab\u01a4\u0001\u0000"+
		"\u0000\u0000\u01ac3\u0001\u0000\u0000\u0000\u01ad\u01b1\u0005\u0001\u0000"+
		"\u0000\u01ae\u01b0\u00038\u001c\u0000\u01af\u01ae\u0001\u0000\u0000\u0000"+
		"\u01b0\u01b3\u0001\u0000\u0000\u0000\u01b1\u01af\u0001\u0000\u0000\u0000"+
		"\u01b1\u01b2\u0001\u0000\u0000\u0000\u01b2\u01b4\u0001\u0000\u0000\u0000"+
		"\u01b3\u01b1\u0001\u0000\u0000\u0000\u01b4\u01b5\u0005\u0002\u0000\u0000"+
		"\u01b55\u0001\u0000\u0000\u0000\u01b6\u01b8\u0005\u0001\u0000\u0000\u01b7"+
		"\u01b9\u00038\u001c\u0000\u01b8\u01b7\u0001\u0000\u0000\u0000\u01b9\u01ba"+
		"\u0001\u0000\u0000\u0000\u01ba\u01b8\u0001\u0000\u0000\u0000\u01ba\u01bb"+
		"\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001\u0000\u0000\u0000\u01bc\u01bd"+
		"\u0005\u0002\u0000\u0000\u01bd7\u0001\u0000\u0000\u0000\u01be\u01c0\u0003"+
		"J%\u0000\u01bf\u01be\u0001\u0000\u0000\u0000\u01bf\u01c0\u0001\u0000\u0000"+
		"\u0000\u01c0\u01c1\u0001\u0000\u0000\u0000\u01c1\u01c3\u0003L&\u0000\u01c2"+
		"\u01c4\u0003Z-\u0000\u01c3\u01c2\u0001\u0000\u0000\u0000\u01c3\u01c4\u0001"+
		"\u0000\u0000\u0000\u01c49\u0001\u0000\u0000\u0000\u01c5\u01c7\u0003J%"+
		"\u0000\u01c6\u01c5\u0001\u0000\u0000\u0000\u01c6\u01c7\u0001\u0000\u0000"+
		"\u0000\u01c7\u01c8\u0001\u0000\u0000\u0000\u01c8\u01c9\u0005\u001b\u0000"+
		"\u0000\u01c9\u01cb\u0003h4\u0000\u01ca\u01cc\u0003Z-\u0000\u01cb\u01ca"+
		"\u0001\u0000\u0000\u0000\u01cb\u01cc\u0001\u0000\u0000\u0000\u01cc\u01ce"+
		"\u0001\u0000\u0000\u0000\u01cd\u01cf\u0003>\u001f\u0000\u01ce\u01cd\u0001"+
		"\u0000\u0000\u0000\u01ce\u01cf\u0001\u0000\u0000\u0000\u01cf;\u0001\u0000"+
		"\u0000\u0000\u01d0\u01d1\u0005\u001c\u0000\u0000\u01d1\u01d2\u0005\u001b"+
		"\u0000\u0000\u01d2\u01d4\u0003h4\u0000\u01d3\u01d5\u0003Z-\u0000\u01d4"+
		"\u01d3\u0001\u0000\u0000\u0000\u01d4\u01d5\u0001\u0000\u0000\u0000\u01d5"+
		"\u01d6\u0001\u0000\u0000\u0000\u01d6\u01d7\u0003@ \u0000\u01d7\u01e0\u0001"+
		"\u0000\u0000\u0000\u01d8\u01d9\u0005\u001c\u0000\u0000\u01d9\u01da\u0005"+
		"\u001b\u0000\u0000\u01da\u01db\u0003h4\u0000\u01db\u01dd\u0003Z-\u0000"+
		"\u01dc\u01de\u0003\u000e\u0007\u0000\u01dd\u01dc\u0001\u0000\u0000\u0000"+
		"\u01dd\u01de\u0001\u0000\u0000\u0000\u01de\u01e0\u0001\u0000\u0000\u0000"+
		"\u01df\u01d0\u0001\u0000\u0000\u0000\u01df\u01d8\u0001\u0000\u0000\u0000"+
		"\u01e0=\u0001\u0000\u0000\u0000\u01e1\u01e5\u0005\u0001\u0000\u0000\u01e2"+
		"\u01e4\u0003\"\u0011\u0000\u01e3\u01e2\u0001\u0000\u0000\u0000\u01e4\u01e7"+
		"\u0001\u0000\u0000\u0000\u01e5\u01e3\u0001\u0000\u0000\u0000\u01e5\u01e6"+
		"\u0001\u0000\u0000\u0000\u01e6\u01e8\u0001\u0000\u0000\u0000\u01e7\u01e5"+
		"\u0001\u0000\u0000\u0000\u01e8\u01e9\u0005\u0002\u0000\u0000\u01e9?\u0001"+
		"\u0000\u0000\u0000\u01ea\u01ec\u0005\u0001\u0000\u0000\u01eb\u01ed\u0003"+
		"\"\u0011\u0000\u01ec\u01eb\u0001\u0000\u0000\u0000\u01ed\u01ee\u0001\u0000"+
		"\u0000\u0000\u01ee\u01ec\u0001\u0000\u0000\u0000\u01ee\u01ef\u0001\u0000"+
		"\u0000\u0000\u01ef\u01f0\u0001\u0000\u0000\u0000\u01f0\u01f1\u0005\u0002"+
		"\u0000\u0000\u01f1A\u0001\u0000\u0000\u0000\u01f2\u01f4\u0003J%\u0000"+
		"\u01f3\u01f2\u0001\u0000\u0000\u0000\u01f3\u01f4\u0001\u0000\u0000\u0000"+
		"\u01f4\u01f5\u0001\u0000\u0000\u0000\u01f5\u01f6\u0005\u001d\u0000\u0000"+
		"\u01f6\u01f7\u0005\t\u0000\u0000\u01f7\u01f9\u0003h4\u0000\u01f8\u01fa"+
		"\u0003 \u0010\u0000\u01f9\u01f8\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001"+
		"\u0000\u0000\u0000\u01fa\u01fc\u0001\u0000\u0000\u0000\u01fb\u01fd\u0005"+
		"\u001f\u0000\u0000\u01fc\u01fb\u0001\u0000\u0000\u0000\u01fc\u01fd\u0001"+
		"\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000\u0000\u01fe\u01ff\u0005"+
		"\u001e\u0000\u0000\u01ff\u0200\u0003F#\u0000\u0200C\u0001\u0000\u0000"+
		"\u0000\u0201\u0202\u0003h4\u0000\u0202E\u0001\u0000\u0000\u0000\u0203"+
		"\u0204\u0006#\uffff\uffff\u0000\u0204\u0205\u0003D\"\u0000\u0205\u020b"+
		"\u0001\u0000\u0000\u0000\u0206\u0207\n\u0001\u0000\u0000\u0207\u0208\u0005"+
		"\b\u0000\u0000\u0208\u020a\u0003D\"\u0000\u0209\u0206\u0001\u0000\u0000"+
		"\u0000\u020a\u020d\u0001\u0000\u0000\u0000\u020b\u0209\u0001\u0000\u0000"+
		"\u0000\u020b\u020c\u0001\u0000\u0000\u0000\u020cG\u0001\u0000\u0000\u0000"+
		"\u020d\u020b\u0001\u0000\u0000\u0000\u020e\u020f\u0007\u0000\u0000\u0000"+
		"\u020fI\u0001\u0000\u0000\u0000\u0210\u0211\u0005#\u0000\u0000\u0211K"+
		"\u0001\u0000\u0000\u0000\u0212\u0213\u0003f3\u0000\u0213M\u0001\u0000"+
		"\u0000\u0000\u0214\u0218\u0005\n\u0000\u0000\u0215\u0217\u0003j5\u0000"+
		"\u0216\u0215\u0001\u0000\u0000\u0000\u0217\u021a\u0001\u0000\u0000\u0000"+
		"\u0218\u0216\u0001\u0000\u0000\u0000\u0218\u0219\u0001\u0000\u0000\u0000"+
		"\u0219\u021b\u0001\u0000\u0000\u0000\u021a\u0218\u0001\u0000\u0000\u0000"+
		"\u021b\u021c\u0005\u000b\u0000\u0000\u021cO\u0001\u0000\u0000\u0000\u021d"+
		"\u0221\u0005\n\u0000\u0000\u021e\u0220\u0003l6\u0000\u021f\u021e\u0001"+
		"\u0000\u0000\u0000\u0220\u0223\u0001\u0000\u0000\u0000\u0221\u021f\u0001"+
		"\u0000\u0000\u0000\u0221\u0222\u0001\u0000\u0000\u0000\u0222\u0224\u0001"+
		"\u0000\u0000\u0000\u0223\u0221\u0001\u0000\u0000\u0000\u0224\u0225\u0005"+
		"\u000b\u0000\u0000\u0225Q\u0001\u0000\u0000\u0000\u0226\u022a\u0005\u0001"+
		"\u0000\u0000\u0227\u0229\u0003V+\u0000\u0228\u0227\u0001\u0000\u0000\u0000"+
		"\u0229\u022c\u0001\u0000\u0000\u0000\u022a\u0228\u0001\u0000\u0000\u0000"+
		"\u022a\u022b\u0001\u0000\u0000\u0000\u022b\u022d\u0001\u0000\u0000\u0000"+
		"\u022c\u022a\u0001\u0000\u0000\u0000\u022d\u022e\u0005\u0002\u0000\u0000"+
		"\u022eS\u0001\u0000\u0000\u0000\u022f\u0233\u0005\u0001\u0000\u0000\u0230"+
		"\u0232\u0003X,\u0000\u0231\u0230\u0001\u0000\u0000\u0000\u0232\u0235\u0001"+
		"\u0000\u0000\u0000\u0233\u0231\u0001\u0000\u0000\u0000\u0233\u0234\u0001"+
		"\u0000\u0000\u0000\u0234\u0236\u0001\u0000\u0000\u0000\u0235\u0233\u0001"+
		"\u0000\u0000\u0000\u0236\u0237\u0005\u0002\u0000\u0000\u0237U\u0001\u0000"+
		"\u0000\u0000\u0238\u0239\u0003h4\u0000\u0239\u023a\u0005\u0003\u0000\u0000"+
		"\u023a\u023b\u0003j5\u0000\u023bW\u0001\u0000\u0000\u0000\u023c\u023d"+
		"\u0003h4\u0000\u023d\u023e\u0005\u0003\u0000\u0000\u023e\u023f\u0003l"+
		"6\u0000\u023fY\u0001\u0000\u0000\u0000\u0240\u0242\u0003\\.\u0000\u0241"+
		"\u0240\u0001\u0000\u0000\u0000\u0242\u0243\u0001\u0000\u0000\u0000\u0243"+
		"\u0241\u0001\u0000\u0000\u0000\u0243\u0244\u0001\u0000\u0000\u0000\u0244"+
		"[\u0001\u0000\u0000\u0000\u0245\u0246\u0005\t\u0000\u0000\u0246\u0248"+
		"\u0003h4\u0000\u0247\u0249\u0003^/\u0000\u0248\u0247\u0001\u0000\u0000"+
		"\u0000\u0248\u0249\u0001\u0000\u0000\u0000\u0249]\u0001\u0000\u0000\u0000"+
		"\u024a\u024c\u0005\u0005\u0000\u0000\u024b\u024d\u0003`0\u0000\u024c\u024b"+
		"\u0001\u0000\u0000\u0000\u024d\u024e\u0001\u0000\u0000\u0000\u024e\u024c"+
		"\u0001\u0000\u0000\u0000\u024e\u024f\u0001\u0000\u0000\u0000\u024f\u0250"+
		"\u0001\u0000\u0000\u0000\u0250\u0251\u0005\u0006\u0000\u0000\u0251_\u0001"+
		"\u0000\u0000\u0000\u0252\u0253\u0003h4\u0000\u0253\u0254\u0005\u0003\u0000"+
		"\u0000\u0254\u0255\u0003l6\u0000\u0255a\u0001\u0000\u0000\u0000\u0256"+
		"\u0257\u0007\u0001\u0000\u0000\u0257c\u0001\u0000\u0000\u0000\u0258\u025c"+
		"\u0003b1\u0000\u0259\u025c\u0005\u000e\u0000\u0000\u025a\u025c\u0005\u000f"+
		"\u0000\u0000\u025b\u0258\u0001\u0000\u0000\u0000\u025b\u0259\u0001\u0000"+
		"\u0000\u0000\u025b\u025a\u0001\u0000\u0000\u0000\u025ce\u0001\u0000\u0000"+
		"\u0000\u025d\u0260\u0003b1\u0000\u025e\u0260\u0005\u001e\u0000\u0000\u025f"+
		"\u025d\u0001\u0000\u0000\u0000\u025f\u025e\u0001\u0000\u0000\u0000\u0260"+
		"g\u0001\u0000\u0000\u0000\u0261\u0266\u0003b1\u0000\u0262\u0266\u0005"+
		"\u000e\u0000\u0000\u0263\u0266\u0005\u000f\u0000\u0000\u0264\u0266\u0005"+
		"\u001e\u0000\u0000\u0265\u0261\u0001\u0000\u0000\u0000\u0265\u0262\u0001"+
		"\u0000\u0000\u0000\u0265\u0263\u0001\u0000\u0000\u0000\u0265\u0264\u0001"+
		"\u0000\u0000\u0000\u0266i\u0001\u0000\u0000\u0000\u0267\u0270\u0005#\u0000"+
		"\u0000\u0268\u0270\u0005!\u0000\u0000\u0269\u0270\u0005\"\u0000\u0000"+
		"\u026a\u0270\u0005\u000e\u0000\u0000\u026b\u0270\u0005\u000f\u0000\u0000"+
		"\u026c\u0270\u0003L&\u0000\u026d\u0270\u0003N\'\u0000\u026e\u0270\u0003"+
		"R)\u0000\u026f\u0267\u0001\u0000\u0000\u0000\u026f\u0268\u0001\u0000\u0000"+
		"\u0000\u026f\u0269\u0001\u0000\u0000\u0000\u026f\u026a\u0001\u0000\u0000"+
		"\u0000\u026f\u026b\u0001\u0000\u0000\u0000\u026f\u026c\u0001\u0000\u0000"+
		"\u0000\u026f\u026d\u0001\u0000\u0000\u0000\u026f\u026e\u0001\u0000\u0000"+
		"\u0000\u0270k\u0001\u0000\u0000\u0000\u0271\u027b\u0003n7\u0000\u0272"+
		"\u027b\u0005#\u0000\u0000\u0273\u027b\u0005!\u0000\u0000\u0274\u027b\u0005"+
		"\"\u0000\u0000\u0275\u027b\u0005\u000e\u0000\u0000\u0276\u027b\u0005\u000f"+
		"\u0000\u0000\u0277\u027b\u0003L&\u0000\u0278\u027b\u0003P(\u0000\u0279"+
		"\u027b\u0003T*\u0000\u027a\u0271\u0001\u0000\u0000\u0000\u027a\u0272\u0001"+
		"\u0000\u0000\u0000\u027a\u0273\u0001\u0000\u0000\u0000\u027a\u0274\u0001"+
		"\u0000\u0000\u0000\u027a\u0275\u0001\u0000\u0000\u0000\u027a\u0276\u0001"+
		"\u0000\u0000\u0000\u027a\u0277\u0001\u0000\u0000\u0000\u027a\u0278\u0001"+
		"\u0000\u0000\u0000\u027a\u0279\u0001\u0000\u0000\u0000\u027bm\u0001\u0000"+
		"\u0000\u0000\u027c\u027d\u0005\f\u0000\u0000\u027d\u027e\u0003h4\u0000"+
		"\u027eo\u0001\u0000\u0000\u0000\u027f\u0280\u0005\u0007\u0000\u0000\u0280"+
		"\u0281\u0003j5\u0000\u0281q\u0001\u0000\u0000\u0000\u0282\u0286\u0003"+
		"t:\u0000\u0283\u0286\u0003v;\u0000\u0284\u0286\u0003x<\u0000\u0285\u0282"+
		"\u0001\u0000\u0000\u0000\u0285\u0283\u0001\u0000\u0000\u0000\u0285\u0284"+
		"\u0001\u0000\u0000\u0000\u0286s\u0001\u0000\u0000\u0000\u0287\u0288\u0003"+
		"h4\u0000\u0288u\u0001\u0000\u0000\u0000\u0289\u028a\u0005\n\u0000\u0000"+
		"\u028a\u028b\u0003r9\u0000\u028b\u028c\u0005\u000b\u0000\u0000\u028cw"+
		"\u0001\u0000\u0000\u0000\u028d\u028e\u0003t:\u0000\u028e\u028f\u0005\r"+
		"\u0000\u0000\u028f\u0294\u0001\u0000\u0000\u0000\u0290\u0291\u0003v;\u0000"+
		"\u0291\u0292\u0005\r\u0000\u0000\u0292\u0294\u0001\u0000\u0000\u0000\u0293"+
		"\u028d\u0001\u0000\u0000\u0000\u0293\u0290\u0001\u0000\u0000\u0000\u0294"+
		"y\u0001\u0000\u0000\u0000W}\u0081\u0084\u0088\u008e\u0095\u009b\u00a4"+
		"\u00a6\u00a9\u00b5\u00bd\u00c3\u00c8\u00d0\u00d5\u00d8\u00db\u00e1\u00e4"+
		"\u00ec\u00f0\u00f7\u00fc\u0101\u0108\u010f\u0118\u011d\u0121\u0126\u012c"+
		"\u0131\u0137\u013a\u013d\u0142\u0145\u0148\u014e\u0151\u0159\u015d\u0164"+
		"\u0167\u016c\u016f\u0175\u017e\u0185\u018e\u0192\u0197\u019a\u01a0\u01a9"+
		"\u01ab\u01b1\u01ba\u01bf\u01c3\u01c6\u01cb\u01ce\u01d4\u01dd\u01df\u01e5"+
		"\u01ee\u01f3\u01f9\u01fc\u020b\u0218\u0221\u022a\u0233\u0243\u0248\u024e"+
		"\u025b\u025f\u0265\u026f\u027a\u0285\u0293";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}