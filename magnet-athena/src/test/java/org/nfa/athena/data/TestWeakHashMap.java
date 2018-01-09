package org.nfa.athena.data;

import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Test;

public class TestWeakHashMap {

	/*
	 * WeakHashMap实现弱引用，是因为它的Entry<K,V>是继承自WeakReference<K>的
	 * 
	 * 在WeakHashMap$Entry<K,V>的构造函数里面是这样写的：
	 * 
	 * 
	 * Entry(K key, V value, ReferenceQueue<K> queue, int hash, Entry<K,V> next) {
	 * 	super(key, queue); 
	 * 	this.value = value; 
	 * 	this.hash = hash; 
	 * 	this.next = next; 
	 * }
	 * 
	 * 
	 * 请注意它构造父类的语句：super(key, queue);
	 * 传入的是key，因此key才是进行弱引用的，value是直接强引用关联在this.value之中，在System.gc()时，
	 * key被清除了，WeakHashMap本身根据ReferenceQueue中接收到的清除通知来清理value值，
	 * 这个动作实现在expungeStaleEntries()方法之内，在getTable()之中对这个方法进行了调用，
	 * 而WeakHashMap几乎在所有public的方法中都是要调用getTable()的。所以效果是key在GC的时候被清楚，
	 * value在key清除后访问WeakHashMap的时候被清除。
	 * 
	 * WeakHashMap的说明之中也是说“An entry in a WeakHashMap will automatically be removed
	 * when its key is no longer in ordinary
	 * use”。所以WeakHashMap的实现并没有问题，只是人们大多想当然的理解为value会自动清除而已。
	 */
	
	@Test
	public void test1() {
		String a = new String("a");
		String b = new String("b");
		Map<String, String> weakMap = new WeakHashMap<>();
		weakMap.put(a, "A");
		weakMap.put(b, "B");

		// 1
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());

		// 2
		a = null;
		System.gc();// gc clear String a
		System.out.println(weakMap);// weakMap clear entry a
		System.out.println(weakMap.size());

	}

	@Test
	public void test2() {
		String a = new String("a");
		Map<String, String> weakMap = new WeakHashMap<>();
		weakMap.put(a, "A");

		// 1
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());

		processMap(weakMap);

		// 3
		System.gc();// gc clear String b
		System.out.println(weakMap);// weakMap clear entry b
		System.out.println(weakMap.size());

	}

	private void processMap(Map<String, String> weakMap) {
		// 2
		String b = new String("B");
		weakMap.put(b, "B");
		System.gc();// gc clear nothing
		System.out.println(weakMap);// weakMap clear nothing
		System.out.println(weakMap.size());
	}
}
