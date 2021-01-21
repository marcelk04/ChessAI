package com.chess.algorithm.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
	private T data = null;
	private final List<Node<T>> children = new ArrayList<Node<T>>();
	private Node<T> parent = null;
	private int depth, degree;

	public Node() {
		this(null);
	}

	public Node(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return data.toString() + "|Depth:" + depth + "|Degree:" + degree;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof Node))
			return false;

		Node<?> otherNode = (Node<?>) other;

		return otherNode.getData().equals(this.data) && otherNode.getDepth() == this.depth
				&& otherNode.getDegree() == this.degree && otherNode.getParent().equals(this.parent);
	}

	public Node<T> addChild(Node<T> child) {
		child.setParent(this);
		children.add(child);
		degree = children.size();
		return child;
	}

	public void addChildren(List<Node<T>> children) {
		this.children.addAll(children);
		this.children.forEach(child -> child.setParent(this));
		degree = this.children.size();
	}

	public void deleteChildren() {
		children.forEach(child -> child.deleteChildren());
		children.clear();
	}

	private int calculateDepth() {
		Node<T> root = getRoot();
		Node<T> currentParent = this;
		int d = 0;
		while (root != currentParent) {
			d++;
			currentParent = currentParent.getParent();
		}
		return d;
	}

	public void updateDepth() {
		children.forEach(child -> child.updateDepth());
		depth = calculateDepth();
	}

	// ===== Getters ===== \\
	public List<Node<T>> getChildren() {
		return children;
	}

	public T getData() {
		return data;
	}

	public int getDegree() {
		return degree;
	}

	public Node<T> getParent() {
		return parent;
	}

	public int getDepth() {
		return depth;
	}

	public Node<T> getRoot() {
		if (parent == null)
			return this;
		return parent.getRoot();
	}

	// ===== Setters ===== \\
	public void setData(T data) {
		this.data = data;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
		updateDepth();
	}
}