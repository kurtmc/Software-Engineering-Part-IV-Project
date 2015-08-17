/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2015 The JavaParser Team.
 *
 * This file is part of JavaParser.
 * 
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License 
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
 
package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.github.javaparser.PositionUtils.sortByBeginPosition;
import static com.github.javaparser.ast.internal.Utils.isNullOrEmpty;

/**
 * Dumps the AST to formatted log file
 * 
 * @author Kurt McAlpine
 */
public final class LogVisitor implements VoidVisitor<Object> {

    private boolean printComments;

    public LogVisitor() {
        this(true);
    }

    public LogVisitor(boolean printComments) {
        this.printComments = printComments;
    }

	private static class SourcePrinter {

		private int level = 0;

		private boolean indented = false;

		private final StringBuilder buf = new StringBuilder();

		public void indent() {
			level++;
		}

		public void unindent() {
			level--;
		}

		private void makeIndent() {
			for (int i = 0; i < level; i++) {
				buf.append("    ");
			}
		}

		public void print(final String arg) {
			if (!indented) {
				makeIndent();
				indented = true;
			}
			buf.append(arg);
		}

		public void printLn(final String arg) {
			print(arg);
			printLn();
		}

		public void printLn() {
			buf.append(System.getProperty("line.separator"));
			indented = false;
		}

		public String getSource() {
			return buf.toString();
		}

		@Override public String toString() {
			return getSource();
		}
	}

	private final SourcePrinter printer = new SourcePrinter();

	public String getSource() {
		return printer.getSource();
	}

	private void printMembers(final List<BodyDeclaration> members, final Object arg) {
		for (final BodyDeclaration member : members) {
			
			member.accept(this, arg);
			
		}
	}

	private void printMemberAnnotations(final List<AnnotationExpr> annotations, final Object arg) {
		if (!isNullOrEmpty(annotations)) {
			for (final AnnotationExpr a : annotations) {
				a.accept(this, arg);
				
			}
		}
	}

	private void printAnnotations(final List<AnnotationExpr> annotations, final Object arg) {
		if (!isNullOrEmpty(annotations)) {
			for (final AnnotationExpr a : annotations) {
				a.accept(this, arg);
			}
		}
	}

	private void printTypeArgs(final List<Type> args, final Object arg) {
        if (!isNullOrEmpty(args)) {
			for (final Iterator<Type> i = args.iterator(); i.hasNext();) {
				final Type t = i.next();
				t.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
	}

	private void printTypeParameters(final List<TypeParameter> args, final Object arg) {
        if (!isNullOrEmpty(args)) {
			for (final Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
				final TypeParameter t = i.next();
				t.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
	}

	private void printArguments(final List<Expression> args, final Object arg) {
        if (!isNullOrEmpty(args)) {
			for (final Iterator<Expression> i = args.iterator(); i.hasNext();) {
				final Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
	}
	
	@Override public void visit(final CompilationUnit n, final Object arg) {

		if (n.getPackage() != null) {
			n.getPackage().accept(this, arg);
		}

		if (n.getImports() != null) {
			for (final ImportDeclaration i : n.getImports()) {
				i.accept(this, arg);
			}
			
		}

		if (n.getTypes() != null) {
			for (final Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
				i.next().accept(this, arg);
				
				if (i.hasNext()) {
					
				}
			}
		}

        printOrphanCommentsEnding(n);
	}

	@Override public void visit(final PackageDeclaration n, final Object arg) {
		printAnnotations(n.getAnnotations(), arg);
		n.getName().accept(this, arg);
		

        printOrphanCommentsEnding(n);
	}

	@Override public void visit(final NameExpr n, final Object arg) {

        printOrphanCommentsEnding(n);
	}

	@Override public void visit(final QualifiedNameExpr n, final Object arg) {
		n.getQualifier().accept(this, arg);

        printOrphanCommentsEnding(n);
	}

	@Override public void visit(final ImportDeclaration n, final Object arg) {
		if (n.isStatic()) {
		}
		n.getName().accept(this, arg);
		if (n.isAsterisk()) {
		}

        printOrphanCommentsEnding(n);
	}

	@Override public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
		printMemberAnnotations(n.getAnnotations(), arg);

		// Prints MyClass:8374012
		printer.printLn(n.getName() + ":" + n.hashCode());

		printTypeParameters(n.getTypeParameters(), arg);

		if (!isNullOrEmpty(n.getExtends())) {
			for (final Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext();) {
				final ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}

		if (!isNullOrEmpty(n.getImplements())) {
			for (final Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
				final ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}

		
		if (!isNullOrEmpty(n.getMembers())) {
			printMembers(n.getMembers(), arg);
		}

        printOrphanCommentsEnding(n);

		
	}

	@Override public void visit(final EmptyTypeDeclaration n, final Object arg) {
		
		

        printOrphanCommentsEnding(n);
	}

	@Override public void visit(final JavadocComment n, final Object arg) {
	}

	@Override public void visit(final ClassOrInterfaceType n, final Object arg) {
		

		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
			}
		}

		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
		printTypeArgs(n.getTypeArgs(), arg);
	}

	@Override public void visit(final TypeParameter n, final Object arg) {
		
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ann : n.getAnnotations()) {
				ann.accept(this, arg);
			}
		}
		if (n.getTypeBound() != null) {
			for (final Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext();) {
				final ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
	}

	@Override public void visit(final PrimitiveType n, final Object arg) {
		
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
			}
		}
	}

	@Override public void visit(final ReferenceType n, final Object arg) {
		
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		List<List<AnnotationExpr>> arraysAnnotations = n.getArraysAnnotations();
		for (int i = 0; i < n.getArrayCount(); i++) {
			if (arraysAnnotations != null && i < arraysAnnotations.size()) {
				List<AnnotationExpr> annotations = arraysAnnotations.get(i);
				if (annotations != null) {
					for (AnnotationExpr ae : annotations) {
						ae.accept(this, arg);

					}
				}
			}
		}
	}

	@Override public void visit(final WildcardType n, final Object arg) {
		
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
			}
		}
		if (n.getExtends() != null) {
			n.getExtends().accept(this, arg);
		}
		if (n.getSuper() != null) {
			n.getSuper().accept(this, arg);
		}
	}

	@Override public void visit(final UnknownType n, final Object arg) {
		// Nothing to dump
	}

	@Override public void visit(final FieldDeclaration n, final Object arg) {
        printOrphanCommentsBeforeThisChildNode(n);

		
		
		printMemberAnnotations(n.getAnnotations(), arg);
		n.getType().accept(this, arg);

		for (final Iterator<VariableDeclarator> i = n.getVariables().iterator(); i.hasNext();) {
			final VariableDeclarator var = i.next();
			var.accept(this, arg);
			if (i.hasNext()) {
			}
		}

	}

	@Override public void visit(final VariableDeclarator n, final Object arg) {
		
		n.getId().accept(this, arg);
		if (n.getInit() != null) {
			n.getInit().accept(this, arg);
		}
	}

	@Override public void visit(final VariableDeclaratorId n, final Object arg) {
		
		for (int i = 0; i < n.getArrayCount(); i++) {
		}
	}

	@Override public void visit(final ArrayInitializerExpr n, final Object arg) {
		
		if (n.getValues() != null) {

			for (final Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
				final Expression expr = i.next();
				expr.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
	}

	@Override public void visit(final VoidType n, final Object arg) {
		
	}

	@Override public void visit(final ArrayAccessExpr n, final Object arg) {
		
		n.getName().accept(this, arg);
		n.getIndex().accept(this, arg);
	}

	@Override public void visit(final ArrayCreationExpr n, final Object arg) {
		
		n.getType().accept(this, arg);
		List<List<AnnotationExpr>> arraysAnnotations = n.getArraysAnnotations();
		if (n.getDimensions() != null) {
			int j = 0;
			for (final Expression dim : n.getDimensions()) {

				if (arraysAnnotations != null && j < arraysAnnotations.size()) {
					List<AnnotationExpr> annotations = arraysAnnotations.get(j);
					if (annotations != null) {
						for (AnnotationExpr ae : annotations) {
							ae.accept(this, arg);
						}
					}
				}
				dim.accept(this, arg);
				j++;
			}
			for (int i = 0; i < n.getArrayCount(); i++) {
				if (arraysAnnotations != null && i < arraysAnnotations.size()) {

					List<AnnotationExpr> annotations = arraysAnnotations.get(i);
					if (annotations != null) {
						for (AnnotationExpr ae : annotations) {
							ae.accept(this, arg);

						}
					}
				}
			}

		} else {
			for (int i = 0; i < n.getArrayCount(); i++) {
				if (arraysAnnotations != null && i < arraysAnnotations.size()) {
					List<AnnotationExpr> annotations = arraysAnnotations.get(i);
					if (annotations != null) {
						for (AnnotationExpr ae : annotations) {
							ae.accept(this, arg);
						}
					}
				}
			}
			n.getInitializer().accept(this, arg);
		}
	}

	@Override public void visit(final AssignExpr n, final Object arg) {
		
		n.getTarget().accept(this, arg);

		n.getValue().accept(this, arg);
	}

	@Override public void visit(final BinaryExpr n, final Object arg) {
		
		n.getLeft().accept(this, arg);


		n.getRight().accept(this, arg);
	}

	@Override public void visit(final CastExpr n, final Object arg) {
		
		n.getType().accept(this, arg);
		n.getExpr().accept(this, arg);
	}

	@Override public void visit(final ClassExpr n, final Object arg) {
		
		n.getType().accept(this, arg);
	}

	@Override public void visit(final ConditionalExpr n, final Object arg) {
		
		n.getCondition().accept(this, arg);

		n.getThenExpr().accept(this, arg);
		n.getElseExpr().accept(this, arg);
	}

	@Override public void visit(final EnclosedExpr n, final Object arg) {
		
		if (n.getInner() != null) {
		n.getInner().accept(this, arg);
		}
	}

	@Override public void visit(final FieldAccessExpr n, final Object arg) {
		
		n.getScope().accept(this, arg);
	}

	@Override public void visit(final InstanceOfExpr n, final Object arg) {
		
		n.getExpr().accept(this, arg);
		n.getType().accept(this, arg);
	}

	@Override public void visit(final CharLiteralExpr n, final Object arg) {

	}

	@Override public void visit(final DoubleLiteralExpr n, final Object arg) {
		
	}

	@Override public void visit(final IntegerLiteralExpr n, final Object arg) {
		
	}

	@Override public void visit(final LongLiteralExpr n, final Object arg) {
		
	}

	@Override public void visit(final IntegerLiteralMinValueExpr n, final Object arg) {
		
	}

	@Override public void visit(final LongLiteralMinValueExpr n, final Object arg) {
	}

	@Override public void visit(final StringLiteralExpr n, final Object arg) {

	}

	@Override public void visit(final BooleanLiteralExpr n, final Object arg) {

	}

	@Override public void visit(final NullLiteralExpr n, final Object arg) {

	}

	@Override public void visit(final ThisExpr n, final Object arg) {
		
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
	}

	@Override public void visit(final SuperExpr n, final Object arg) {
		
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
	}

	@Override public void visit(final MethodCallExpr n, final Object arg) {
		
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
		printTypeArgs(n.getTypeArgs(), arg);
		printArguments(n.getArgs(), arg);
	}

	@Override public void visit(final ObjectCreationExpr n, final Object arg) {
		
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}


		printTypeArgs(n.getTypeArgs(), arg);
		if (!isNullOrEmpty(n.getTypeArgs())) {
		}

		n.getType().accept(this, arg);

		printArguments(n.getArgs(), arg);

		if (n.getAnonymousClassBody() != null) {

			printMembers(n.getAnonymousClassBody(), arg);

		}
	}

	@Override public void visit(final UnaryExpr n, final Object arg) {
		


		n.getExpr().accept(this, arg);


	}

	@Override public void visit(final ConstructorDeclaration n, final Object arg) {
		
		
		printMemberAnnotations(n.getAnnotations(), arg);

		printTypeParameters(n.getTypeParameters(), arg);
		if (n.getTypeParameters() != null) {
		}

		if (n.getParameters() != null) {
			for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
				final Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}

		if (!isNullOrEmpty(n.getThrows())) {
			for (final Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				final NameExpr name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
		n.getBlock().accept(this, arg);
	}

	@Override public void visit(final MethodDeclaration n, final Object arg) {
        printOrphanCommentsBeforeThisChildNode(n);

		
		
		printMemberAnnotations(n.getAnnotations(), arg);

		printTypeParameters(n.getTypeParameters(), arg);


		n.getType().accept(this, arg);
        // Prints MyMethod:28051435
		printer.printLn(((ClassOrInterfaceDeclaration)n.getParentNode()).getName() + ":" + n.getName() + ":" + n.hashCode());

		if (n.getParameters() != null) {
			for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
				final Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}

		if (!isNullOrEmpty(n.getThrows())) {
			for (final Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				final NameExpr name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
		if (n.getBody() == null) {
		} else {
			n.getBody().accept(this, arg);
		}
	}

	@Override public void visit(final Parameter n, final Object arg) {
		
		printAnnotations(n.getAnnotations(), arg);
		if (n.getType() != null) {
			n.getType().accept(this, arg);
		}
		if (n.isVarArgs()) {
		}
		n.getId().accept(this, arg);
	}
	
    @Override public void visit(MultiTypeParameter n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);

        Iterator<Type> types = n.getTypes().iterator();
        types.next().accept(this, arg);
        while (types.hasNext()) {
        	types.next().accept(this, arg);
        }
        
        n.getId().accept(this, arg);
    }

	@Override public void visit(final ExplicitConstructorInvocationStmt n, final Object arg) {
		
		if (n.isThis()) {
			printTypeArgs(n.getTypeArgs(), arg);

		} else {
			if (n.getExpr() != null) {
				n.getExpr().accept(this, arg);

			}
			printTypeArgs(n.getTypeArgs(), arg);

		}
		printArguments(n.getArgs(), arg);

	}

	@Override public void visit(final VariableDeclarationExpr n, final Object arg) {
		
		printAnnotations(n.getAnnotations(), arg);

		n.getType().accept(this, arg);


		for (final Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
			final VariableDeclarator v = i.next();
			v.accept(this, arg);
			if (i.hasNext()) {

			}
		}
	}

	@Override public void visit(final TypeDeclarationStmt n, final Object arg) {
		
		n.getTypeDeclaration().accept(this, arg);
	}

	@Override public void visit(final AssertStmt n, final Object arg) {
		

		n.getCheck().accept(this, arg);
		if (n.getMessage() != null) {

			n.getMessage().accept(this, arg);
		}

	}

	@Override public void visit(final BlockStmt n, final Object arg) {
        printOrphanCommentsBeforeThisChildNode(n);
		

		if (n.getStmts() != null) {
			
			for (final Statement s : n.getStmts()) {
				s.accept(this, arg);

			}
			
		}


	}

	@Override public void visit(final LabeledStmt n, final Object arg) {
		


		n.getStmt().accept(this, arg);
	}

	@Override public void visit(final EmptyStmt n, final Object arg) {
		

	}

	@Override public void visit(final ExpressionStmt n, final Object arg) {
		printOrphanCommentsBeforeThisChildNode(n);
		
		n.getExpression().accept(this, arg);

	}

	@Override public void visit(final SwitchStmt n, final Object arg) {
		

		n.getSelector().accept(this, arg);

		if (n.getEntries() != null) {
			
			for (final SwitchEntryStmt e : n.getEntries()) {
				e.accept(this, arg);
			}
			
		}


	}

	@Override public void visit(final SwitchEntryStmt n, final Object arg) {
		
		if (n.getLabel() != null) {

			n.getLabel().accept(this, arg);

		} else {

		}

		
		if (n.getStmts() != null) {
			for (final Statement s : n.getStmts()) {
				s.accept(this, arg);

			}
		}
		
	}

	@Override public void visit(final BreakStmt n, final Object arg) {
		

		if (n.getId() != null) {


		}

	}

	@Override public void visit(final ReturnStmt n, final Object arg) {
		

		if (n.getExpr() != null) {

			n.getExpr().accept(this, arg);
		}

	}

	@Override public void visit(final EnumDeclaration n, final Object arg) {
		
		
		printMemberAnnotations(n.getAnnotations(), arg);




		if (n.getImplements() != null) {

			for (final Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
				final ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {

				}
			}
		}


		
		if (n.getEntries() != null) {

			for (final Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
				final EnumConstantDeclaration e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {

				}
			}
		}
		if (n.getMembers() != null) {

			printMembers(n.getMembers(), arg);
		} else {
			if (n.getEntries() != null) {

			}
		}
		

	}

	@Override public void visit(final EnumConstantDeclaration n, final Object arg) {
		
		
		printMemberAnnotations(n.getAnnotations(), arg);


		if (n.getArgs() != null) {
			printArguments(n.getArgs(), arg);
		}

		if (n.getClassBody() != null) {

			
			printMembers(n.getClassBody(), arg);
			

		}
	}

	@Override public void visit(final EmptyMemberDeclaration n, final Object arg) {
		
		

	}

	@Override public void visit(final InitializerDeclaration n, final Object arg) {
		
		
		if (n.isStatic()) {

		}
		n.getBlock().accept(this, arg);
	}

	@Override public void visit(final IfStmt n, final Object arg) {
		

		n.getCondition().accept(this, arg);
		final boolean thenBlock = n.getThenStmt() instanceof BlockStmt;
		if (thenBlock) { // block statement should start on the same line

        }else {

			
		}
		n.getThenStmt().accept(this, arg);
		if (!thenBlock)
			
		if (n.getElseStmt() != null) {
			if (thenBlock) {

            }else{}

			final boolean elseIf = n.getElseStmt() instanceof IfStmt;
			final boolean elseBlock = n.getElseStmt() instanceof BlockStmt;

			n.getElseStmt().accept(this, arg);

		}
	}

	@Override public void visit(final WhileStmt n, final Object arg) {
		

		n.getCondition().accept(this, arg);

		n.getBody().accept(this, arg);
	}

	@Override public void visit(final ContinueStmt n, final Object arg) {
		

		if (n.getId() != null) {


		}

	}

	@Override public void visit(final DoStmt n, final Object arg) {
		

		n.getBody().accept(this, arg);

		n.getCondition().accept(this, arg);

	}

	@Override public void visit(final ForeachStmt n, final Object arg) {
		

		n.getVariable().accept(this, arg);

		n.getIterable().accept(this, arg);

		n.getBody().accept(this, arg);
	}

	@Override public void visit(final ForStmt n, final Object arg) {
		

		if (n.getInit() != null) {
			for (final Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
				final Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {

				}
			}
		}

		if (n.getCompare() != null) {
			n.getCompare().accept(this, arg);
		}

		if (n.getUpdate() != null) {
			for (final Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
				final Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {

				}
			}
		}

		n.getBody().accept(this, arg);
	}

	@Override public void visit(final ThrowStmt n, final Object arg) {
		

		n.getExpr().accept(this, arg);

	}

	@Override public void visit(final SynchronizedStmt n, final Object arg) {
		

		n.getExpr().accept(this, arg);

		n.getBlock().accept(this, arg);
	}

	@Override public void visit(final TryStmt n, final Object arg) {
		

		if (!n.getResources().isEmpty()) {

			Iterator<VariableDeclarationExpr> resources = n.getResources().iterator();
			boolean first = true;
			while (resources.hasNext()) {
				visit(resources.next(), arg);
				if (resources.hasNext()) {


					if (first) {
						
					}
				}
				first = false;
			}
			if (n.getResources().size() > 1) {
				
			}

		}
		n.getTryBlock().accept(this, arg);
		if (n.getCatchs() != null) {
			for (final CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {

			n.getFinallyBlock().accept(this, arg);
		}
	}

	@Override public void visit(final CatchClause n, final Object arg) {
		

		n.getExcept().accept(this, arg);

		n.getCatchBlock().accept(this, arg);

	}

	@Override public void visit(final AnnotationDeclaration n, final Object arg) {
		
		
		printMemberAnnotations(n.getAnnotations(), arg);




		
		if (n.getMembers() != null) {
			printMembers(n.getMembers(), arg);
		}
		

	}

	@Override public void visit(final AnnotationMemberDeclaration n, final Object arg) {
		
		
		printMemberAnnotations(n.getAnnotations(), arg);

		n.getType().accept(this, arg);



		if (n.getDefaultValue() != null) {

			n.getDefaultValue().accept(this, arg);
		}

	}

	@Override public void visit(final MarkerAnnotationExpr n, final Object arg) {
		

		n.getName().accept(this, arg);
	}

	@Override public void visit(final SingleMemberAnnotationExpr n, final Object arg) {
		

		n.getName().accept(this, arg);

		n.getMemberValue().accept(this, arg);

	}

	@Override public void visit(final NormalAnnotationExpr n, final Object arg) {
		

		n.getName().accept(this, arg);

		if (n.getPairs() != null) {
			for (final Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext();) {
				final MemberValuePair m = i.next();
				m.accept(this, arg);
				if (i.hasNext()) {

				}
			}
		}

	}

	@Override public void visit(final MemberValuePair n, final Object arg) {
		


		n.getValue().accept(this, arg);
	}

	@Override public void visit(final LineComment n, final Object arg) {
		if (!this.printComments) {
            return;
        }

		String tmp = n.getContent();
		tmp = tmp.replace('\r', ' ');
		tmp = tmp.replace('\n', ' ');

	}

	@Override public void visit(final BlockComment n, final Object arg) {
        if (!this.printComments) {
            return;
        }
	}

	@Override
	public void visit(LambdaExpr n, Object arg) {
        

        List<Parameter> parameters = n.getParameters();
		boolean printPar = false;
		printPar = n.isParametersEnclosed();

		if (printPar) {
		}
		if (parameters != null) {
			for (Iterator<Parameter> i = parameters.iterator(); i.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
				}
			}
		}
		if (printPar) {
		}

		Statement body = n.getBody();
		if (body instanceof ExpressionStmt) {
			// Print the expression directly
			((ExpressionStmt) body).getExpression().accept(this, arg);
		} else {
			body.accept(this, arg);
		}
	}


    @Override
    public void visit(MethodReferenceExpr n, Object arg) {
        
        Expression scope = n.getScope();
        String identifier = n.getIdentifier();
        if (scope != null) {
            n.getScope().accept(this, arg);
        }


        if (n.getTypeParameters() != null) {
            for (Iterator<TypeParameter> i = n.getTypeParameters().iterator(); i
                    .hasNext();) {
                TypeParameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                }
            }
        }
        if (identifier != null) {
        }

    }

    @Override
    public void visit(TypeExpr n, Object arg) {
        
        if (n.getType() != null) {
            n.getType().accept(this, arg);
        }
    }

    private void printOrphanCommentsBeforeThisChildNode(final Node node){
        if (node instanceof Comment) return;

        Node parent = node.getParentNode();
        if (parent==null) return;
        List<Node> everything = new LinkedList<Node>();
        everything.addAll(parent.getChildrenNodes());
        sortByBeginPosition(everything);
        int positionOfTheChild = -1;
        for (int i=0;i<everything.size();i++){
            if (everything.get(i)==node) positionOfTheChild=i;
        }
        if (positionOfTheChild==-1) throw new RuntimeException("My index not found!!! "+node);
        int positionOfPreviousChild = -1;
        for (int i=positionOfTheChild-1;i>=0 && positionOfPreviousChild==-1;i--){
            if (!(everything.get(i) instanceof Comment)) positionOfPreviousChild = i;
        }
        for (int i=positionOfPreviousChild+1;i<positionOfTheChild;i++){
            Node nodeToPrint = everything.get(i);
            if (!(nodeToPrint instanceof Comment)) throw new RuntimeException("Expected comment, instead "+nodeToPrint.getClass()+". Position of previous child: "+positionOfPreviousChild+", position of child "+positionOfTheChild);
            nodeToPrint.accept(this,null);
        }
    }


    private void printOrphanCommentsEnding(final Node node){
        List<Node> everything = new LinkedList<Node>();
        everything.addAll(node.getChildrenNodes());
        sortByBeginPosition(everything);
        if (everything.size()==0) return;

        int commentsAtEnd = 0;
        boolean findingComments = true;
        while (findingComments&&commentsAtEnd<everything.size()){
            Node last = everything.get(everything.size()-1-commentsAtEnd);
            findingComments = (last instanceof Comment);
            if (findingComments) commentsAtEnd++;
        }
        for (int i=0;i<commentsAtEnd;i++){
            everything.get(everything.size()-commentsAtEnd+i).accept(this,null);
        }
    }
}
