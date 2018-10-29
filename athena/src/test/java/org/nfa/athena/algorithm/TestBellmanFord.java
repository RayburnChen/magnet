package org.nfa.athena.algorithm;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author OwenChen 时间复杂度是O(VE) (V为顶点的个数 E为边的个数)
 *
 */
public class TestBellmanFord {
	
//	From 0 to 0 Weight 0 Routes 0
//	From 0 to 1 Weight 3 Routes 0->3->1
//	From 0 to 2 Weight 3 Routes 0->3->2
//	From 0 to 3 Weight 2 Routes 0->3
//	From 0 to 4 Weight 6 Routes 0->3->2->4

	@Test
	public void testGraph1() {
		int m = Integer.MAX_VALUE / 10, start = 0;
		int[][] graph = { 
				{ 0, 4, m, 2, m }, 
				{ 4, 0, 4, 1, m }, 
				{ m, 4, 0, 1, 3 }, 
				{ 2, 1, 1, 0, 7 },
				{ m, m, 3, 7, 0 } };
		Assert.assertArrayEquals(new int[] { 0, 3, 3, 2, 6 }, bellmanFord(graph, start));
	}
	
//	From 0 to 0 Weight 0 Routes 0
//	From 0 to 1 Weight -1 Routes 0->1
//	From 0 to 2 Weight 2 Routes 0->1->2
//	From 0 to 3 Weight -2 Routes 0->1->4->3
//	From 0 to 4 Weight 1 Routes 0->1->4
	
	@Test
	public void testGraph2() {
		int m = Integer.MAX_VALUE / 10, start = 0;
		int[][] graph = { 
				{ 0, -1, 4, m,  m }, 
				{ m, 0,  3, 2,  2 }, 
				{ m, m,  0, m,  m }, 
				{ m, 1,  5, 0,  m },
				{ m, m,  m, -3, 0 } };
		Assert.assertArrayEquals(new int[] { 0, -1, 2, -2, 1 }, bellmanFord(graph, start));
	}
	
	@Test
	public void testGraph3() {
		int m = Integer.MAX_VALUE / 10, start = 0;
		int[][] graph = { 
				{ 0, -1, 4, m,  m }, 
				{ m, 0,  3, 2,  2 }, 
				{ m, m,  0, m,  m }, 
				{ m, 1,  5, 0,  m },
				{ m, m,  m, -5, 0 } };
		try {
			bellmanFord(graph, start);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	private int[] bellmanFord(int[][] graph, int start) {
		GraphContext context = bellmanFord(new GraphContext(graph, start));
		for (int i = 0; i < context.size; i++) {
			System.out.println(
					"From " + start + " to " + i + " Weight " + context.weights[i] + " Routes " + context.routes[i]);
		}
		return context.weights;
	}

	private GraphContext bellmanFord(GraphContext c) {
		int[] weights = c.weights;
		for (int k = 0; k < c.size - 1; k++) {
			for (int i = 0; i < c.size; i++) {
				for (int j = 0; j < c.size; j++) {
					if (i == j || c.graph[i][j] == Integer.MAX_VALUE / 10) {
						continue;// skip
					}
					// go through times is actually E
					int wei = weights[i] + c.graph[i][j];
					if (wei < weights[j]) {
						weights[j] = wei;
						c.routes[j] = c.routes[i] + "->" + j;
					}
				}
			}
		}
		for (int i = 0; i < c.size; i++) {
			for (int j = 0; j < c.size; j++) {
				if (i == j || c.graph[i][j] == Integer.MAX_VALUE / 10) {
					continue;// skip
				}
				int wei = weights[i] + c.graph[i][j];
				if (wei < weights[j]) {
					System.err.println("存在负权回路");
					throw new RuntimeException("存在负权回路");
				}
			}
		}
		return c;
	}

	private class GraphContext {
		private final int[][] graph;
		private final int size;
		private final int[] weights;
		private final String[] routes;

		private GraphContext(int[][] graph, int start) {
			super();
			int n = graph.length;
			this.graph = graph;
			this.size = n;
			this.weights = new int[n];
			this.routes = new String[n];
			for (int i = 0; i < n; i++) {
				this.weights[i] = Integer.MAX_VALUE / 10;
				this.routes[i] = String.valueOf(start);
			}
			weights[start] = 0;
		}

	}
	
}
