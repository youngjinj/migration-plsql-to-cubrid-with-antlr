import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import youngjinj.antlr.CaseChangingCharStream;
import youngjinj.antlr.PlSqlLexer;
import youngjinj.antlr.PlSqlParser;
import youngjinj.antlr.PlSqlParser.Assignment_statementContext;
import youngjinj.antlr.PlSqlParser.Bind_variableContext;
import youngjinj.antlr.PlSqlParser.BlockContext;
import youngjinj.antlr.PlSqlParser.BodyContext;
import youngjinj.antlr.PlSqlParser.Case_statementContext;
import youngjinj.antlr.PlSqlParser.Char_set_nameContext;
import youngjinj.antlr.PlSqlParser.Column_aliasContext;
import youngjinj.antlr.PlSqlParser.Continue_statementContext;
import youngjinj.antlr.PlSqlParser.Create_function_bodyContext;
import youngjinj.antlr.PlSqlParser.Cursor_declarationContext;
import youngjinj.antlr.PlSqlParser.Cursor_expressionContext;
import youngjinj.antlr.PlSqlParser.Cursor_manipulation_statementsContext;
import youngjinj.antlr.PlSqlParser.Data_manipulation_language_statementsContext;
import youngjinj.antlr.PlSqlParser.Declare_specContext;
import youngjinj.antlr.PlSqlParser.Default_value_partContext;
import youngjinj.antlr.PlSqlParser.Delete_statementContext;
import youngjinj.antlr.PlSqlParser.Exception_declarationContext;
import youngjinj.antlr.PlSqlParser.Exception_handlerContext;
import youngjinj.antlr.PlSqlParser.Execute_immediateContext;
import youngjinj.antlr.PlSqlParser.Exit_statementContext;
import youngjinj.antlr.PlSqlParser.Explain_statementContext;
import youngjinj.antlr.PlSqlParser.ExpressionContext;
import youngjinj.antlr.PlSqlParser.Fetch_clauseContext;
import youngjinj.antlr.PlSqlParser.For_update_clauseContext;
import youngjinj.antlr.PlSqlParser.Forall_statementContext;
import youngjinj.antlr.PlSqlParser.From_clauseContext;
import youngjinj.antlr.PlSqlParser.Function_bodyContext;
import youngjinj.antlr.PlSqlParser.Function_callContext;
import youngjinj.antlr.PlSqlParser.Function_nameContext;
import youngjinj.antlr.PlSqlParser.Function_specContext;
import youngjinj.antlr.PlSqlParser.Goto_statementContext;
import youngjinj.antlr.PlSqlParser.Group_by_clauseContext;
import youngjinj.antlr.PlSqlParser.Hierarchical_query_clauseContext;
import youngjinj.antlr.PlSqlParser.Id_expressionContext;
import youngjinj.antlr.PlSqlParser.IdentifierContext;
import youngjinj.antlr.PlSqlParser.If_statementContext;
import youngjinj.antlr.PlSqlParser.Insert_statementContext;
import youngjinj.antlr.PlSqlParser.Into_clauseContext;
import youngjinj.antlr.PlSqlParser.Label_declarationContext;
import youngjinj.antlr.PlSqlParser.Label_nameContext;
import youngjinj.antlr.PlSqlParser.Lock_table_statementContext;
import youngjinj.antlr.PlSqlParser.Logical_expressionContext;
import youngjinj.antlr.PlSqlParser.Loop_statementContext;
import youngjinj.antlr.PlSqlParser.Merge_statementContext;
import youngjinj.antlr.PlSqlParser.Model_clauseContext;
import youngjinj.antlr.PlSqlParser.Native_datatype_elementContext;
import youngjinj.antlr.PlSqlParser.Null_statementContext;
import youngjinj.antlr.PlSqlParser.Offset_clauseContext;
import youngjinj.antlr.PlSqlParser.Order_by_clauseContext;
import youngjinj.antlr.PlSqlParser.ParameterContext;
import youngjinj.antlr.PlSqlParser.Parameter_nameContext;
import youngjinj.antlr.PlSqlParser.Pipe_row_statementContext;
import youngjinj.antlr.PlSqlParser.Pragma_declarationContext;
import youngjinj.antlr.PlSqlParser.Precision_partContext;
import youngjinj.antlr.PlSqlParser.Procedure_bodyContext;
import youngjinj.antlr.PlSqlParser.Procedure_callContext;
import youngjinj.antlr.PlSqlParser.Procedure_specContext;
import youngjinj.antlr.PlSqlParser.Query_blockContext;
import youngjinj.antlr.PlSqlParser.Raise_statementContext;
import youngjinj.antlr.PlSqlParser.Return_statementContext;
import youngjinj.antlr.PlSqlParser.Select_list_elementsContext;
import youngjinj.antlr.PlSqlParser.Select_only_statementContext;
import youngjinj.antlr.PlSqlParser.Select_statementContext;
import youngjinj.antlr.PlSqlParser.Selected_listContext;
import youngjinj.antlr.PlSqlParser.Seq_of_declare_specsContext;
import youngjinj.antlr.PlSqlParser.Seq_of_statementsContext;
import youngjinj.antlr.PlSqlParser.Sql_scriptContext;
import youngjinj.antlr.PlSqlParser.Sql_statementContext;
import youngjinj.antlr.PlSqlParser.StatementContext;
import youngjinj.antlr.PlSqlParser.SubqueryContext;
import youngjinj.antlr.PlSqlParser.Subquery_basic_elementsContext;
import youngjinj.antlr.PlSqlParser.Subquery_factoring_clauseContext;
import youngjinj.antlr.PlSqlParser.Subquery_operation_partContext;
import youngjinj.antlr.PlSqlParser.Subtype_declarationContext;
import youngjinj.antlr.PlSqlParser.Tableview_nameContext;
import youngjinj.antlr.PlSqlParser.Transaction_control_statementsContext;
import youngjinj.antlr.PlSqlParser.Type_declarationContext;
import youngjinj.antlr.PlSqlParser.Type_specContext;
import youngjinj.antlr.PlSqlParser.Unit_statementContext;
import youngjinj.antlr.PlSqlParser.Update_statementContext;
import youngjinj.antlr.PlSqlParser.Variable_declarationContext;
import youngjinj.antlr.PlSqlParser.Variable_nameContext;
import youngjinj.antlr.PlSqlParser.Where_clauseContext;

public class MigrationPlsqlToJavaSP {
	private Map<String, String> functionName = null;
	private List<Map<String, String>> parameters = null;
	private String returnValue = null;
	private Map<String, String> returnType = null;
	private List<Map<String, String>> declareSpecs = null;
	private List<Map<String, String>> querys = null;
	private Map<Integer, Map<String, String>> bindVariables = null;
	private List<Map<String, String>> intoVariables = null;
	
	public static void main(String[] args) throws IOException {
		InputStream in = MigrationPlsqlToJavaSP.class.getClassLoader().getResourceAsStream("compute_bonus.sql");
		CharStream s = CharStreams.fromStream(in);
		CaseChangingCharStream upper = new CaseChangingCharStream(s, true);
		
		PlSqlLexer lexer = new PlSqlLexer(upper);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PlSqlParser parser = new PlSqlParser(tokens);
		
		Sql_scriptContext sql_scriptContext = parser.sql_script();
		
		MigrationPlsqlToJavaSP migrationPlsqlToJavaSP = new MigrationPlsqlToJavaSP(sql_scriptContext);
		System.out.println(migrationPlsqlToJavaSP.make());
	}
	
	public MigrationPlsqlToJavaSP(Sql_scriptContext sql_scriptContext) {
		parse(sql_scriptContext);
	}
	
	public String make() {
		STGroup group = new STGroupFile(MigrationPlsqlToJavaSP.class.getClassLoader().getResource("MigrationPlsqlToJavaSP.stg"));
		group.registerRenderer(String.class, new StringRenderer());
		
		ST classSt = group.getInstanceOf("class");
		classSt.add("name", functionName.get("name"));
		
		ST methodSt = group.getInstanceOf("method");
		methodSt.add("name", functionName.get("name"));
		methodSt.add("origName", functionName.get("origName"));
		methodSt.add("returnValue", returnValue);
		methodSt.add("returnType", returnType);
		methodSt.add("parameters", parameters);
		methodSt.add("variables", declareSpecs);
		
		List<String> codeBlocks = new ArrayList<String>();
	
		ST defaultConnectionSt = group.getInstanceOf("defaultConnection");
		
		for (Map<String, String> query : querys) {
			ST executeQuerySt = group.getInstanceOf("executeQuery");
			executeQuerySt.add("query", query);
			executeQuerySt.add("bindVariables", bindVariables.values());
			executeQuerySt.add("intoVariables", intoVariables);
			executeQuerySt.add("codeBlocks", null);
			
			codeBlocks.add(executeQuerySt.render());
		}
		
		defaultConnectionSt.add("codeBlocks", codeBlocks);
		methodSt.add("codeBlocks", defaultConnectionSt.render());
		classSt.add("method", methodSt.render());

		return classSt.render();
	}

	public void parse(Sql_scriptContext sql_scriptContext) {
		querys = new ArrayList<Map<String, String>>();
		
		for (Unit_statementContext unit_statementContext : sql_scriptContext.unit_statement()) {
			Create_function_bodyContext create_function_bodyContext = unit_statementContext.create_function_body();
			
			/* Function_nameContext */
			Function_nameContext function_nameContext = create_function_bodyContext.function_name();
			functionName = new Hashtable<String, String>();
			if (function_nameContext.id_expression() == null) {
				IdentifierContext identifierContext = function_nameContext.identifier();  /* Need to test. */
				functionName.put("name", convertPascalCase(getFullText(identifierContext).replace("\"", "")));
				functionName.put("origName", getFullText(identifierContext).replace("\"", ""));
			} else {
				Id_expressionContext id_expressionContext = function_nameContext.id_expression();
				functionName.put("name", convertPascalCase(getFullText(id_expressionContext).replace("\"", "")));
				functionName.put("origName", getFullText(id_expressionContext).replace("\"", ""));
			}
			
			/* ParameterContext */
			parameters = new ArrayList<Map<String, String>>();
			List<ParameterContext> parameterContextList = create_function_bodyContext.parameter();
			for (ParameterContext parameterContext : parameterContextList) {
				Parameter_nameContext parameter_nameContext = parameterContext.parameter_name();
				Type_specContext childType_specContext = parameterContext.type_spec();
				Default_value_partContext default_value_partContext = parameterContext.default_value_part(); /* Need to test. */
				
				Native_datatype_elementContext native_datatype_elementContext = childType_specContext.datatype().native_datatype_element();
				Precision_partContext precision_partContext = childType_specContext.datatype().precision_part();
				
				Map<String, String> parameterMap = new Hashtable<String, String>();
				parameterMap.put("name", convertCamelCase(getFullText(parameter_nameContext)));
				parameterMap.put("origName", getFullText(parameter_nameContext));
				parameterMap.put("type", getTargetJavaType(getFullText(native_datatype_elementContext)));
				parameterMap.put("class", getTargetClass(getTargetJavaType(getFullText(native_datatype_elementContext))));
				parameterMap.put("origType", getTargetCubridType(getFullText(native_datatype_elementContext)));
				parameterMap.put("precision", getFullText(precision_partContext));
				
				parameters.add(parameterMap);
			}
			
			/* Type_specContext */
			Type_specContext type_specContext = create_function_bodyContext.type_spec();
			Native_datatype_elementContext native_datatype_elementContext = type_specContext.datatype().native_datatype_element();
			Precision_partContext precision_partContext = type_specContext.datatype().precision_part();
			
			returnType = new Hashtable<String, String>();
			returnType.put("type", getTargetJavaType(getFullText(native_datatype_elementContext)));
			returnType.put("origType", getTargetCubridType(getFullText(native_datatype_elementContext)));
			returnType.put("class", getTargetClass(getTargetJavaType(getFullText(native_datatype_elementContext))));
			returnType.put("precision", getFullText(precision_partContext));
			
			/* Seq_of_declare_specsContext */
			declareSpecs = new ArrayList<Map<String, String>>();
			Seq_of_declare_specsContext seq_of_declare_specsContext = create_function_bodyContext.seq_of_declare_specs();
			for (Declare_specContext declare_specContext : seq_of_declare_specsContext.declare_spec()) {
				Pragma_declarationContext pragma_declarationContext = declare_specContext.pragma_declaration(); /* Need to test. */
				Variable_declarationContext variable_declarationContext = declare_specContext.variable_declaration();
				Subtype_declarationContext subtype_declarationContext = declare_specContext.subtype_declaration(); /* Need to test. */
				Cursor_declarationContext cursor_declarationContext = declare_specContext.cursor_declaration(); /* Need to test. */
				Exception_declarationContext exception_declarationContext = declare_specContext.exception_declaration(); /* Need to test. */
				Type_declarationContext type_declarationContext = declare_specContext.type_declaration(); /* Need to test. */
				Procedure_specContext procedure_specContext = declare_specContext.procedure_spec(); /* Need to test. */
				Function_specContext function_specContext = declare_specContext.function_spec(); /* Need to test. */
				Procedure_bodyContext procedure_bodyContext = declare_specContext.procedure_body(); /* Need to test. */
				Function_bodyContext function_bodyContext = declare_specContext.function_body(); /* Need to test. */
				
				/* Variable_declarationContext */
				if (variable_declarationContext == null) { continue; }
				IdentifierContext identifierContext = variable_declarationContext.identifier();
				Type_specContext childType_specContext = variable_declarationContext.type_spec();
				Native_datatype_elementContext childNative_datatype_elementContext = childType_specContext.datatype().native_datatype_element();
				Precision_partContext childPrecision_partContext = childType_specContext.datatype().precision_part();
				Default_value_partContext default_value_partContext = variable_declarationContext.default_value_part();
				
				Map<String, String> variable_declarationMap = new Hashtable<String, String>();
				variable_declarationMap.put("name", convertCamelCase(getFullText(identifierContext)));
				variable_declarationMap.put("origName", getFullText(identifierContext));
				variable_declarationMap.put("type", getTargetJavaType(getFullText(childNative_datatype_elementContext)));
				variable_declarationMap.put("precision", getFullText(precision_partContext));
				variable_declarationMap.put("default", getFullText(default_value_partContext)); /* Need to test. */
				
				declareSpecs.add(variable_declarationMap);
			}
			
			/* BodyContext */
			BodyContext bodyContext = create_function_bodyContext.body();
			Seq_of_statementsContext seq_of_statementsContext = bodyContext.seq_of_statements();
			List<Exception_handlerContext> exception_handlerContextList = bodyContext.exception_handler(); /* Need to test. */
			Label_nameContext label_nameContext = bodyContext.label_name(); /* Need to test. */

			/* Seq_of_statementsContext */
			List<StatementContext> statementContextList = seq_of_statementsContext.statement();
			List<Label_declarationContext> label_declarationContextList = seq_of_statementsContext.label_declaration(); /* Need to test. */
			
			/* StatementContext */
			for (StatementContext statementContext : statementContextList) {
				BodyContext childBodyContext = statementContext.body(); /* Need to test. */
				BlockContext blockContext = statementContext.block(); /* Need to test. */
				Assignment_statementContext assignment_statementContext = statementContext.assignment_statement(); /* Need to test. */
				Continue_statementContext continue_statementContext = statementContext.continue_statement(); /* Need to test. */
				Exit_statementContext exit_statementContext = statementContext.exit_statement(); /* Need to test. */
				Goto_statementContext goto_statementContext = statementContext.goto_statement(); /* Need to test. */
				If_statementContext if_statementContext = statementContext.if_statement(); /* Need to test. */
				Loop_statementContext loop_statementContext = statementContext.loop_statement(); /* Need to test. */
				Forall_statementContext forall_statementContext = statementContext.forall_statement(); /* Need to test. */
				Null_statementContext null_statementContext = statementContext.null_statement(); /* Need to test. */
				Raise_statementContext raise_statementContext = statementContext.raise_statement(); /* Need to test. */
				Return_statementContext return_statementContext = statementContext.return_statement();
				Case_statementContext case_statementContext = statementContext.case_statement(); /* Need to test. */
				Sql_statementContext sql_statementContext = statementContext.sql_statement();
				Function_callContext function_callContext = statementContext.function_call(); /* Need to test. */
				Pipe_row_statementContext pipe_row_statementContext = statementContext.pipe_row_statement(); /* Need to test. */
				Procedure_callContext procedure_callContext = statementContext.procedure_call(); /* Need to test. */
				
				parseSql_statementContext(sql_statementContext);
				
				/* Return_statementContext */
				if (return_statementContext != null) {
					ExpressionContext expressionContext = return_statementContext.expression();
					
					/* ExpressionContext */
					Cursor_expressionContext cursor_expressionContext = expressionContext.cursor_expression(); /* Need to test. */
					Logical_expressionContext logical_expressionContext = expressionContext.logical_expression(); /* Need to test. */
					
					returnValue = getFullText(expressionContext);
					
					for (Map<String, String> parameter : parameters) {
						returnValue = returnValue.replace(parameter.get("origName"), parameter.get("name"));
					}
					
					for (Map<String, String> declareSpec : declareSpecs) {
						returnValue = returnValue.replace(declareSpec.get("origName"), declareSpec.get("name"));
					}
				}
				
				
			}
		}
	}
	
	public void parseSql_statementContext(Sql_statementContext sql_statementContext) {
		/* Sql_statementContext */
		if (sql_statementContext == null) { return; }
		Execute_immediateContext execute_immediateContext = sql_statementContext.execute_immediate(); /* Need to test. */
		Data_manipulation_language_statementsContext data_manipulation_language_statementsContext = sql_statementContext.data_manipulation_language_statements();
		Cursor_manipulation_statementsContext cursor_manipulation_statementsContext = sql_statementContext.cursor_manipulation_statements(); /* Need to test. */
		Transaction_control_statementsContext transaction_control_statementsContext = sql_statementContext.transaction_control_statements(); /* Need to test. */
		
		/* Data_manipulation_language_statementsContext */
		if (data_manipulation_language_statementsContext == null) { return; }
		Merge_statementContext merge_statementContext = data_manipulation_language_statementsContext.merge_statement(); /* Need to test. */
		Lock_table_statementContext lock_table_statementContext = data_manipulation_language_statementsContext.lock_table_statement(); /* Need to test. */
		Select_statementContext select_statementContext = data_manipulation_language_statementsContext.select_statement();
		Update_statementContext update_statementContext = data_manipulation_language_statementsContext.update_statement(); /* Need to test. */
		Delete_statementContext delete_statementContext = data_manipulation_language_statementsContext.delete_statement(); /* Need to test. */
		Insert_statementContext insert_statementContext = data_manipulation_language_statementsContext.insert_statement(); /* Need to test. */
		Explain_statementContext explain_statementContext = data_manipulation_language_statementsContext.explain_statement(); /* Need to test. */
	
		/* Select_statementContext */
		if (select_statementContext == null) { return; }
		Select_only_statementContext select_only_statementContext = select_statementContext.select_only_statement(); 
		List<For_update_clauseContext> for_update_clauseContextList = select_statementContext.for_update_clause(); /* Need to test. */
		List<Order_by_clauseContext> order_by_clauseContextList = select_statementContext.order_by_clause(); /* Need to test. */
		List<Offset_clauseContext> offset_clauseContextList = select_statementContext.offset_clause(); /* Need to test. */
		List<Fetch_clauseContext> fetch_clauseContext = select_statementContext.fetch_clause(); /* Need to test. */
		
		/* Select_only_statementContext */
		if (select_only_statementContext == null) { return; }
		Subquery_factoring_clauseContext subquery_factoring_clauseContext = select_only_statementContext.subquery_factoring_clause(); /* Need to test. */
		SubqueryContext subqueryContext = select_only_statementContext.subquery();
		
		/* SubqueryContext */
		if (subqueryContext == null) { return; }
		Subquery_basic_elementsContext subquery_basic_elementsContext = subqueryContext.subquery_basic_elements();
		List<Subquery_operation_partContext> subquery_operation_partContextList = subqueryContext.subquery_operation_part(); /* Need to test. */
		
		/* Subquery_basic_elementsContext */
		if (subquery_basic_elementsContext == null) { return; }
		Query_blockContext query_blockContext = subquery_basic_elementsContext.query_block();
		SubqueryContext childSubqueryContext = subquery_basic_elementsContext.subquery(); /* Need to test. */

		/* Query_blockContext */
		if (query_blockContext == null) { return; }
		Selected_listContext selected_listContext = query_blockContext.selected_list();
		Into_clauseContext into_clauseContext = query_blockContext.into_clause();
		From_clauseContext from_clauseContext = query_blockContext.from_clause();
		Where_clauseContext where_clauseContext = query_blockContext.where_clause();
		Hierarchical_query_clauseContext hierarchical_query_clauseContext = query_blockContext.hierarchical_query_clause(); /* Need to test. */
		Group_by_clauseContext group_by_clauseContext = query_blockContext.group_by_clause(); /* Need to test. */
		Model_clauseContext model_clauseContext = query_blockContext.model_clause(); /* Need to test. */
		Order_by_clauseContext order_by_clauseContext = query_blockContext.order_by_clause(); /* Need to test. */
		Fetch_clauseContext childFetch_clauseContext = query_blockContext.fetch_clause(); /* Need to test. */
		
		/* Into_clauseContext */
		if (into_clauseContext == null) { return; }
		intoVariables = new ArrayList<Map<String, String>>();
		{
			int i = 0;
			
			for (Variable_nameContext variable_nameContext : into_clauseContext.variable_name()) {
				Char_set_nameContext char_set_nameContext = variable_nameContext.char_set_name(); /* Need to test. */
				List<Id_expressionContext> id_expressionContextList = variable_nameContext.id_expression();
				Bind_variableContext bind_variableContext = variable_nameContext.bind_variable(); /* Need to test. */
				
				/* Id_expressionContext */
				for (Id_expressionContext childId_expressionContext : id_expressionContextList) {
					Map<String, String> intoVariableMap = new Hashtable<String, String>();
					intoVariableMap.put("name", convertCamelCase(getFullText(childId_expressionContext)));
					
					for (Map<String, String> declareSpecMap : declareSpecs) {
						if ((getFullText(childId_expressionContext)).equals(declareSpecMap.get("origName"))) {
							intoVariableMap.put("type", declareSpecMap.get("type"));
						}
					}
					
					intoVariables.add(intoVariableMap);
				}
			}
		}
		
		/* getFullText(Query_blockContext). But exclude Into_clauseContext. */
		StringJoiner queryBlock = new StringJoiner(" ");
		for (int i = 0; i < query_blockContext.getChildCount(); i++) {
			if ((into_clauseContext.INTO()).equals(getFullText(query_blockContext.getChild(i)))) {
				continue;
			}
			
			if (query_blockContext.getChild(i) instanceof Into_clauseContext) {
				continue;
			}
			
			queryBlock.add(getFullText(query_blockContext.getChild(i)));		
		}
		
		bindVariables = new TreeMap<Integer, Map<String, String>>();
		String sql = queryBlock.toString();
		for (Map<String, String> parameter : parameters) {
			int index = 0;
			
			while (true) {
				index = sql.indexOf(parameter.get("origName"), index + 1);
				
				if (index < 0) {
					break;
				}
				
				Map<String, String> bindVariableMap = new Hashtable<String, String>();
				bindVariableMap.put("name", parameter.get("name"));
				bindVariableMap.put("type", parameter.get("type"));
				
				bindVariables.put(index, bindVariableMap);
			}
		}
		
		for (Map<String, String> parameter : parameters) {
			sql = sql.replace(parameter.get("origName"), "?");
		}
		
		Map<String, String> queryBlockMap = new Hashtable<String, String>();
		queryBlockMap.put("sql", sql);
		
		/* Selected_listContext */
		if (selected_listContext == null) { return; }
		List<Select_list_elementsContext> Select_list_elementsContext = selected_listContext.select_list_elements();
		
		/* Select_list_elementsContext */
		for (Select_list_elementsContext select_list_elementsContext : Select_list_elementsContext) {
			Tableview_nameContext tableview_nameContext = select_list_elementsContext.tableview_name();
			ExpressionContext expressionContext = select_list_elementsContext.expression(); /* Need to test. */
			Column_aliasContext column_aliasContext =select_list_elementsContext.column_alias();
			
			queryBlockMap.put("expressionContext", getFullText(getFullText(expressionContext)));
			queryBlockMap.put("column_aliasContext", getFullText(getFullText(column_aliasContext)));
		}
		
		querys.add(queryBlockMap);
	}
	
	public String getTargetCubridType(String type) {
		String targetType = type;

		switch (type.toUpperCase()) {
		case "INT":
		case "BIGINT":
			break;
			
		case "NUMBER":
			targetType = "NUMERIC";
			break;

		case "CHAR":
			break;
			
		case "VARCHAR":
		case "VARCHAR2":
			targetType = "VARCHAR";
			break;

		default:
			break;
		}

		return targetType;
	}
	
	public String getTargetJavaType(String type) {
		String targetType = type;

		switch (type.toUpperCase()) {
		case "SHORT":
		case "INT":
		case "LONG":
			targetType = "long";
			break;
			
		case "NUMBER":
			targetType = "double";
			break;

		case "CHAR":
		case "VARCHAR":
		case "VARCHAR2":
			targetType = "String";
			break;

		default:
			break;
		}

		return targetType;
	}
	
	public String getTargetClass(String type) {
		String targetClass = type;

		switch (type) {
		case "short":
		case "int":
		case "long":
		case "float":
		case "double":
			break;
		
		case "Short":
		case "Integer":
		case "Long":
		case "Float":
		case "Double":
		case "String":
			targetClass = "java.lang." + type;
			break;

		default:
			break;
		}

		return targetClass;
	}
	
	public String convertPascalCase(String name) {
		Matcher match = Pattern.compile("([a-zA-Z])([a-zA-Z0-9]*)").matcher(name);
		
		StringBuilder pascalCaseName = new StringBuilder();
		while (match.find()) {
			pascalCaseName.append(match.group(1).toUpperCase());
			pascalCaseName.append(match.group(2).toLowerCase());
		}
		
		return pascalCaseName.toString();
	}
	
	public String convertCamelCase(String name) {
		String pascalCaseName = convertPascalCase(name);
		String camelCaseName = pascalCaseName.substring(0, 1).toLowerCase() + pascalCaseName.substring(1);
		
		return camelCaseName;
	}
	
	public String getFullText(Object o) {
		if (o == null) {
			return "";
		}
		
		if (o instanceof TerminalNode) {
			return ((TerminalNode) o).getText();
		}
		
		ParserRuleContext ctx = null;
		if (o instanceof ParserRuleContext) {
			ctx = (ParserRuleContext) o;
			
			if (ctx.getChildCount() > 0) {
				StringJoiner sj = new StringJoiner(" ");
				
				for (int i = 0; i < ctx.getChildCount(); i++) {
					sj.add(getFullText(ctx.getChild(i)));
				}
				
				return sj.toString();
			}
			
			return ctx.getText();
		}
		
		return o.toString();
	}
}
