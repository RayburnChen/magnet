package org.nfa.athena.algorithm;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class TestBellmanFord {

	@Test
	public void testGraph1() {
		int m = Integer.MAX_VALUE, start = 0;
		int[][] graph = { 
				{ 0, 4, m, 2, m }, 
				{ 4, 0, 4, 1, m }, 
				{ m, 4, 0, 1, 3 }, 
				{ 2, 1, 1, 0, 7 },
				{ m, m, 3, 7, 0 } };
		Assert.assertArrayEquals(new int[] { 0, Integer.MAX_VALUE, 10, 50, 30, 60 }, bellmanFord(graph, start));
	}
	
	private int[] bellmanFord(int[][] graph, int start) {

		return null;
	}

	private class GraphContext {
		private int start;
		private final int[][] graph;
		private final int size;
		private final boolean[] visited;
		private final int[] weights;
		private final String[] routes;

		private GraphContext(int[][] graph, int start) {
			super();
			int n = graph.length;
			this.graph = graph;
			this.start = start;
			this.size = n;
			this.weights = Arrays.copyOf(graph[start], n);
			this.routes = new String[n];
			for (int i = 0; i < n; i++) {
				this.routes[i] = start + "->" + i;
			}
			this.visited = new boolean[n];
			this.visited[start] = true;
		}

	}
	
}
