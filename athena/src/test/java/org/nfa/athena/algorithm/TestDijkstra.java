package org.nfa.athena.algorithm;

import java.util.Arrays;

import org.junit.Test;

public class TestDijkstra {
	
//	From 0 to 0 Weight 0 Routes 0->0
//	From 0 to 1 Weight 3 Routes 0->3->1
//	From 0 to 2 Weight 3 Routes 0->3->2
//	From 0 to 3 Weight 2 Routes 0->3
//	From 0 to 4 Weight 6 Routes 0->3->2->4
	
	@Test
	public void testGraph1() {
		int m = Integer.MAX_VALUE, start = 0;
		int[][] graph = { 
				{ 0, 4, m, 2, m }, 
				{ 4, 0, 4, 1, m }, 
				{ m, 4, 0, 1, 3 }, 
				{ 2, 1, 1, 0, 7 },
				{ m, m, 3, 7, 0 } };
		dijkstra(graph, start);
	}
	
//	From 0 to 0 Weight 0 Routes 0->0
//	From 0 to 1 Weight 2147483647 Routes 0->1
//	From 0 to 2 Weight 10 Routes 0->2
//	From 0 to 3 Weight 50 Routes 0->4->3
//	From 0 to 4 Weight 30 Routes 0->4
//	From 0 to 5 Weight 60 Routes 0->4->3->5
	
	@Test
	public void testGraph2() {
		int m = Integer.MAX_VALUE, start = 0;
		int[][] graph = { 
				{ 0, m, 10, m,  30, 100 }, 
				{ m, 0, 5,  m,  m,  m }, 
				{ m, m, 0,  50, m,  m }, 
				{ m, m, m,  0,  m,  10 }, 
				{ m, m, m,  20, 0,  60 }, 
				{ m, m, m,  m,  m,  0 } };
		dijkstra(graph, start);
	}
	
//	From 0 to 0 Weight 0 Routes 0->0
//	From 0 to 1 Weight 1 Routes 0->1
//	From 0 to 2 Weight 8 Routes 0->1->3->2
//	From 0 to 3 Weight 4 Routes 0->1->3
//	From 0 to 4 Weight 13 Routes 0->1->3->2->4
//	From 0 to 5 Weight 17 Routes 0->1->3->2->4->5
	
	@Test
	public void testGraph3() {
		int m = Integer.MAX_VALUE, start = 0;
		int[][] graph = { 
				{ 0, 1, 12, m, m,  m }, 
				{ m, 0, 9,  3, m,  m }, 
				{ m, m, 0,  m, 5,  m }, 
				{ m, m, 4,  0, 13, 15 }, 
				{ m, m, m,  m, 0,  4 }, 
				{ m, m, m,  m, m,  m } };
		dijkstra(graph, start);
	}

	/**
	 * 
	 * @param graph is matrix
	 * @param start is start point
	 * @return
	 */
	private int[] dijkstra(int[][] graph, int start) {
		GraphContext context = nested(new GraphContext(graph, start));
		for (int i = 0; i < context.size; i++) {
			System.out.println("From " + start + " to " + i + " Weight " + context.weights[i] + " Routes " + context.routes[i]);
		}
		return context.weights;
	}

	private GraphContext nested(GraphContext c) {
		int min = -1, next = c.start;
		for (int i = 0; i < c.size; i++) {
			if (!c.visited[i] && (min < 0 || c.weights[i] < min)) {
				min = c.weights[i];// minimum weight unvisited
				next = i;// minimum weight point
			}
		}
		if (c.visited[next]) {
			return c;
		}
		c.visited[next] = true;
		for (int i = 0; i < c.size; i++) {
			int wei = c.weights[next] + c.graph[next][i];// may overflow
			if (!c.visited[i] && 0 < wei && wei < c.weights[i]) {
				c.weights[i] = wei;
				c.routes[i] = c.routes[next] + "->" + i;
			}
		}
		return nested(c);
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
