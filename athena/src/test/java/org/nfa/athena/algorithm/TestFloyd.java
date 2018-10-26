package org.nfa.athena.algorithm;

import org.junit.Test;

public class TestFloyd {

	@Test
	public void testGraph1() {
		int m = Integer.MAX_VALUE / 2;
		int[][] graph = { 
				{ 0, 4, m, 2, m }, 
				{ 4, 0, 4, 1, m }, 
				{ m, 4, 0, 1, 3 }, 
				{ 2, 1, 1, 0, 7 }, 
				{ m, m, 3, 7, 0 } };
		floyd(graph);
	}
	
	@Test
	public void testGraph2() {
		int m = Integer.MAX_VALUE / 2;
		int[][] graph = { 
				{ 0, m, 10, m,  30, 100 }, 
				{ m, 0, 5,  m,  m,  m }, 
				{ m, m, 0,  50, m,  m }, 
				{ m, m, m,  0,  m,  10 }, 
				{ m, m, m,  20, 0,  60 }, 
				{ m, m, m,  m,  m,  0 } };
		floyd(graph);
	}
	
	@Test
	public void testGraph3() {
		int m = Integer.MAX_VALUE / 2;
		int[][] graph = { 
				{ 0, 3, 8,  m, -4 }, 
				{ m, 0, m,  1, 7 }, 
				{ m, 4, 0,  m, m }, 
				{ 2, m, -5, 0, m }, 
				{ m, m, m,  6, 0 } };
		floyd(graph);
	}

	private int[][] floyd(int[][] graph) {
		int n = graph.length, weight;
		String[][] path = path(n);
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if ((weight = graph[i][k] + graph[k][j]) < graph[i][j]) {
						graph[i][j] = weight;
						path[i][j] = path[i][k] + path[k][j].substring(1);
					}
				}
			}
		}
		printGraph(graph, path);
		return graph;
	}

	private void printGraph(int[][] graph, String[][] path) {
		int n = graph.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (Integer.MAX_VALUE != graph[i][j]) {
					System.out.println("From " + i + " to " + j + " Weight " + graph[i][j] + " Routes " + path[i][j]);
				}
			}
		}
	}

	private String[][] path(int n) {
		String[][] path = new String[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				path[i][j] = i + "->" + j;
			}
		}
		return path;
	}

}
