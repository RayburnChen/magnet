package org.nfa.athena.algorithm;

import org.junit.Test;

public class TestDijkstra {
	
//	From 0 to 0 Weight 0 Routes 0
//	From 0 to 1 Weight 3 Routes 0-->3-->1
//	From 0 to 2 Weight 3 Routes 0-->3-->2
//	From 0 to 3 Weight 2 Routes 0-->3
//	From 0 to 4 Weight 6 Routes 0-->3-->2-->4
	
	@Test
	public void testDijkstra() {
		int m = Integer.MAX_VALUE, start = 0;
		int[][] graph = { 
				{ 0, m, 10, m,  30, 100 }, 
				{ m, 5, m,  m,  m,  m }, 
				{ m, m, m,  50, m,  m }, 
				{ m, m, m,  m,  m,  10 }, 
				{ m, m, m,  20, 60, m }, 
				{ m, m, m,  m,  m,  m } };
		int[][] graph2 = { 
				{ 0, 4, m, 2, m }, 
				{ 4, 0, 4, 1, m }, 
				{ m, 4, 0, 1, 3 }, 
				{ 2, 1, 1, 0, 7 },
				{ m, m, 3, 7, 0 } };
		dijkstra(graph2, start);
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
		int[][] graph = c.graph;
		int start = c.start;
		for (int i = 0; i < c.size; i++) {
			int wei = c.weights[start] + graph[start][i];// may overflow
			if (!c.visited[i] && 0 < wei && wei < c.weights[i]) {
				c.weights[i] = wei;
				c.routes[i] = c.routes[start] + "-->" + i;
			}
		}
		int min = Integer.MAX_VALUE, next = start;
		for (int i = 0; i < c.size; i++) {
			if (!c.visited[i] && graph[start][i] < min) {
				min = graph[start][i];// minimum weight from start to i
				next = i;// minimum weight point
			}
		}
		c.visited[next] = true;
		c.start = next;
		if (next == start) {
			return c;
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
			this.visited = new boolean[n];
			this.weights = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };
			this.routes = new String[n];
			this.routes[start] = String.valueOf(start);
			this.visited[start] = true;
			this.weights[start] = 0;
		}

	}

}
