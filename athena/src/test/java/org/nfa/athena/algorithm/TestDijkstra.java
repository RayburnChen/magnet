package org.nfa.athena.algorithm;

import org.junit.Test;

public class TestDijkstra {

	@Test
	public void testDijkstra() {
		int m = Integer.MAX_VALUE - 1, start = 0;
		int[][] graph = { 
				{ 0, 4, m, 2, m }, 
				{ 4, 0, 4, 1, m }, 
				{ m, 4, 0, 1, 3 }, 
				{ 2, 1, 1, 0, 7 },
				{ m, m, 3, 7, 0 } };
		int[] weights = dijkstra(graph, start);
		for (int i = 0; i < weights.length; i++) {
			System.out.println("Weight from " + start + " to " + i + " is " + weights[i]);
		}
	}

	/**
	 * 
	 * @param graph is matrix
	 * @param start is start point
	 * @return
	 */
	private int[] dijkstra(int[][] graph, int start) {
		int n = graph.length;
		int[] weights = new int[n];// result
		String[] routes = new String[n];
		for (int i = 0; i < n; i++) {
			routes[i] = new String(start + "-->" + i);
			weights[i] = Integer.MAX_VALUE;
		}
		boolean[] visited = new boolean[n];
		visited[start] = true;
		weights[start] = 0;
		nested(graph, visited, weights, routes, start);
		for (int i = 0; i < n; i++) {
			System.out.println("从" + start + "出发到" + i + "的最短路径为：" + routes[i]);
		}
		return weights;
	}

	private void nested(int[][] graph, boolean[] visited, int[] weights, String[] routes, int start) {
		int n = graph.length;
		for (int i = 0; i < n; i++) {
			if (!visited[i] && weights[start] < weights[i] && graph[start][i] < weights[i]
					&& (weights[start] + graph[start][i]) < weights[i]) {
				weights[i] = weights[start] + graph[start][i];
				routes[i] = routes[start] + "-->" + i;
			}
		}
		int min = Integer.MAX_VALUE, poi = start;
		for (int i = 0; i < n; i++) {
			if (!visited[i] && graph[start][i] < min) {
				min = graph[start][i];// minimum weight from start to i
				poi = i;// minimum weight point
			}
		}
		visited[poi] = true;
		if (poi == start) {
			return;
		}
		nested(graph, visited, weights, routes, poi);
	}

}
